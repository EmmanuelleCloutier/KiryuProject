package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Constraints.Constraint;
import org.jetbrains.annotations.NotNull;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.Contact;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The rigid body class is used to simulate physics applied to
 * objects in our physics world.
 */

public class RigidBody {

  /**
   * the collider can be either a sphere, a box, a capsule, etc
   */
  private final Collider collider;
  /**
   * mass of the rigidBody, it can never be 0
   */
  private double mass = 1.0f;
  /**
   * inverse mass is simply 1/mass,
   * hence why mass can never be 0
   */
  private final double inverseMass;
  /**
   * Friction coefficient used when a non-moving body
   * gets forces applied to it
   */
  private double staticFrictionCoefficient = 0.5f;
  /**
   * Friction coefficient used on a moving body
   */
  private double kineticFrictionCoefficient = 0.5f;
  /**
   * determines whether the body is static(immovable) or not
   */
  private boolean isStatic = false;
  /**
   * determines whether gravity should affect the body
   */
  private boolean isAffectedByGravity = true;
  /**
   * marks the body for destruction, allowing the physics
   * world to clean up bodies that should not be simulated anymore
   */
  private boolean isMarkedForDestruction = false;
  /**
   * force acts as the acceleration of the body
   * it never changes unless specifically told to.
   * example, this could be useful for a rocket type projectile
   * that accelerate due to its propulsion system
   */
  private Vector3 force;
  /**
   * the sum of all other forces applied to the body:
   * gravity, friction, constraints, etc
   */
  private Vector3 accumulatedForce;
  /**
   * the position in the physics world of the body
   */
  private Vector3 position;
  /**
   * Vector describing the velocity of the body
   * linearVelocity.length() is the actual speed
   */
  private Vector3 linearVelocity;
  /**
   * the direction the body is going towards
   * it is the equivalent of linearVelocity.normalize()
   */
  private Vector3 direction;
  /**
   * list of contacts of the body. A contact registers,
   * in the event of a collision; the other body, the point of contact
   * and the normal from the contact point to this body
   */
  private final ArrayList<Contact> contacts;
  /**
   * Consumer is an interface that just execute an operation on this body
   * in our games we bind it to RigidBody::onCollision. Hence, when onCollision
   * is called on this body we can "capture" the call and execute gameplay code
   * needed for collision interaction in the game
   */
  private Consumer<RigidBody> collisionListener;
  /**
   * simple constructor that instantiate a body with position 0
   * and velocity 0
   * it calls the full parameter constructor
   * @param mass - the mass of the rigid body
   * @param collider - the collider of the rigid body
   */
  public RigidBody(double mass, Collider collider) {
    this(
        mass,
        new Vector3(),
        new Vector3(),
        collider
    );
  }
  /**
   * full parameter constructor
   * @param mass - the mass of the rigid body
   * @param position - the position the rigid body starts at
   * @param linearVelocity - the velocity the rigid body starts with
   * @param collider - the collider of the rigid body
   */
  public RigidBody(
      double mass,
      @NotNull Vector3 position,
      @NotNull Vector3 linearVelocity,
      @NotNull Collider collider
  ) {

    this.mass = mass;
    this.inverseMass = 1 / this.mass;

    this.force = Vector3.ZERO_VECTOR.copy();
    this.position = new Vector3(position);
    this.linearVelocity = new Vector3(linearVelocity);
    this.accumulatedForce = Vector3.ZERO_VECTOR.copy();
    this.contacts = new ArrayList<>();
    this.collider = collider;
    //the collider needs this link to the rigidbody for gameplay purposes
    collider.linkRigidBody(this);
  }
  /*
    GETTERS
   */
//region
  /**
   * @return <font color="orange">{@code double}</font>
   * - the mass of the RigidBody
   */
  public double getMass() {
    return mass;
  }
  /**
   * @return <font color="orange">{@code double}</font>
   * - 1 / mass of the RigidBody
   */
  public double getInverseMass() {
    return inverseMass;
  }
  /**
   * @return <font color="yellow">{@link Vector3}</font>
   * - RigidBody's force
   */
  public Vector3 getForce() {
    return force;
  }
  /**
   * @return <font color="yellow">{@link Vector3}</font>
   * - RigidBody's accumulatedForce
   */
  public Vector3 getAccumulatedForce() {
    return accumulatedForce;
  }
  /**
   * @return <font color="yellow">{@link Vector3}</font>
   * - RigidBody's position
   */
  public Vector3 getPosition() {
    return position;
  }
  /**
   * @return <font color="yellow">{@link Vector3}</font>
   * - RigidBody's linear velocity
   */
  public Vector3 getLinearVelocity() {
    return linearVelocity;
  }
  /**
   * @return <font color="yellow">{@link Vector3}</font>
   * - RigidBody's direction
   */
  public Vector3 getDirection() {
    return direction;
  }
  /**
   * @return <font color="yellow">{@link Collider}</font>
   * - RigidBody's Collider
   */
  public Collider getCollider() {
    return collider;
  }
  /**
   * @return <font color="orange">{@code double}</font>
   * - RigidBody's static friction coefficient
   */
  public double getStaticFrictionCoefficient() {
    return staticFrictionCoefficient;
  }
  /**
   * @return <font color="orange">{@code double}</font>
   * - RigidBody's kinetic friction coefficient
   */
  public double getKineticFrictionCoefficient() {
    return kineticFrictionCoefficient;
  }
  /**
   * @return <font color="orange">{@code boolean}</font>
   * - Whether the rigidBody is static or not
   */
  public boolean isStatic() {
    return isStatic;
  }
  /**
   *  @return <span style="color: orange;">ArrayList</span>&lt;<span style="color: yellow;">{@link Contact}</span>&gt;
   * - List of contacts affecting this RigidBody
   */
  public ArrayList<Contact> getContacts() {
    return contacts;
  }
  /**
   * @return <font color="orange">{@code boolean}</font>
   * - Whether the rigidBody is affected by gravity or not
   */
  public boolean isAffectedByGravity() {
    return isAffectedByGravity;
  }
  /**
   * @return <font color="orange">{@code boolean}</font>
   * - Whether the rigidBody is marked for destruction or not
   */
  public boolean isMarkedForDestruction() {
    return isMarkedForDestruction;
  }

  //endregion

  /*
    SETTERS
   */
//region
  /**
   * @param force <font color="yellow">{@link Vector3}</font>
   * - sets the RigidBody's force
   */
  public void setForce(@NotNull Vector3 force) {
    this.force = force;
  }
  /**
   * @param force <font color="yellow">{@link Vector3}</font>
   * - sets the RigidBody's accumulated force
   */
  public void setAccumulatedForce(@NotNull Vector3 force) {
    this.accumulatedForce = force;
  }
  /**
   * @param position <font color="yellow">{@link Vector3}</font>
   * - sets the RigidBody's position
   */
  public void setPosition(@NotNull Vector3 position) {
    this.position = position;
  }
  /**
   * @param linearVelocity <font color="yellow">{@link Vector3}</font>
   * - sets the RigidBody's linear velocity
   */
  public void setLinearVelocity(@NotNull Vector3 linearVelocity) {
    this.linearVelocity = linearVelocity;
  }
  /**
   * @param direction <font color="yellow">{@link Vector3}</font>
   * - sets the RigidBody's direction
   */
  public void setDirection(@NotNull Vector3 direction) {
    this.direction = direction;
  }
  /**
   * @param bIsStatic <font color="orange">{@code boolean}</font>
   * - sets whether the RigidBody is static or not
   */
  public void isStatic(boolean bIsStatic) {
    this.isStatic = bIsStatic;
  }
  /**
   * Toggle's the RigidBody static state
   */
  public void toggleIsStatic(){
    this.isStatic = !this.isStatic;
  }
  /**
   * @param bAffectedByGravity <font color="orange">{@code boolean}</font>
   * - sets whether the RigidBody is affected by gravity or not
   */
  public void isAffectedByGravity(boolean bAffectedByGravity) {
    this.isAffectedByGravity = bAffectedByGravity;
  }
  /**
   * Toggle's the RigidBody gravity state
   */
  public void toggleIsAffectedByGravity(){
    this.isAffectedByGravity = !this.isAffectedByGravity;
  }
  /**
   * @param markedForDestruction <font color="orange">{@code boolean}</font>
   * - sets whether the RigidBody is marked for destruction or not
   */
  public void markForDestruction(boolean markedForDestruction) {
    this.isMarkedForDestruction = markedForDestruction;
  }
  /**
   * marks the RigidBody for destruction
   */
  public void markForDestruction() {
    this.isMarkedForDestruction = true;
  }

  //endregion

  /**
   * adds a new contact to the RigidBody's list
   * @param contact <font color="yellow">{@link Contact}</font>
   * - the contact to add
   * @return <font color="orange">{@code boolean}</font> - (optional)
   * - Whether we added the contact successfully or not
   */
  public boolean addContact(@NotNull Contact contact){
    return this.contacts.add(contact);
  }
  /**
   * Clears the contact list of the RigidBody
   */
  public void clearContacts(){
    this.contacts.clear();
  }
  /**
   *
   * @param addedForce <font color="yellow">{@link Vector3}</font>
   * - add this force to the rigidBody's current force
   */
  public void addForce(@NotNull Vector3 addedForce){
    this.force.add(addedForce);
  }
  /**
   * Add a normalized {@link Vector3} scaled by a strength factor
   * to the RigidBody's linear velocity
   * @param impulseDirection <font color="yellow">{@link Vector3}</font>
   * - direction of the impulse
   * @param impulseStrength <font color="orange">{@code double}</font>
   * - strength of the impulse
   */
  public void addImpulse(@NotNull Vector3 impulseDirection, double impulseStrength){

    this.linearVelocity.add(
        impulseDirection.scale(impulseStrength)
            .scale(this.inverseMass)
    );

  }
  /**
   * Add an impulse to the RigidBody's linear velocity along its
   * direction
   * @param impulseStrength <font color="orange">{@code double}</font>
   * - strength of the impulse
   */
  public void addImpulse(double impulseStrength){

    Vector3 impulse = new Vector3(
        this.direction.x * impulseStrength,
        this.direction.y * impulseStrength,
        this.direction.z * impulseStrength
    );

    this.linearVelocity.add(impulse.scale(this.inverseMass));

  }
  /**
   * @return <font color="orange">{@code boolean}</font>
   * - Whether the rigidBody is colliding or not
   */
  public boolean isColliding(){
    return !contacts.isEmpty();
  }
  /**
   * Sets the RigidBody's collision listener
   * example : this.rigidBody.setCollisionListener(this::onCollision);
   * in this example the gameplay object as a onCollision()
   * when its rigidbody calls onCollision it will then call the
   * object's onCollision
   * @param listener <span style="color: orange;">Consumer</span>&lt;<span style="color: yellow;">{@link RigidBody}</span>&gt
   */
  public void setCollisionListener(Consumer<RigidBody> listener) {
    this.collisionListener = listener;
  }
  /**
   * paired with setCollisionListener, this is the function that calls
   * the gameplay object's onCollision()
   * @param other <font color="yellow">{@link RigidBody}</font>
   * - the RigidBody this RigidBody collided with
   */
  public void onCollision(RigidBody other) {
    if (collisionListener != null) {
      collisionListener.accept(other);
    }
  }


}