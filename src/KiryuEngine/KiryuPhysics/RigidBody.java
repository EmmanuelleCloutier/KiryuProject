package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuMath.Matrix3;
import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;
import org.jetbrains.annotations.NotNull;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.Contact;
import org.jetbrains.annotations.Nullable;

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

  // ------------- ROTATION ATTRIBUTES --------------- //

  /**
   * Indicate if the rigid body can rotate
   */
  private boolean isRotating = false;
  /**
   * Vector describing the torque
   */
  private Vector3 torque;
  /**
   * Vector describing the angular velocity of the body
   */
  private Vector3 angularVelocity;
  /**
   * The Quaternion that describe the current rotation
   */
  private Quaternion qRotation;
  /**
   * The Matrix that describe the current rotation
   */
  private Matrix3 mRotation;
  /**
   * The Matrix that represent the initial inertia tensor of the mass center
   */
  private Matrix3 inertiaTensor;
  /**
   * The Matrix that represent the inverse of the inertia tensor
   */
  private Matrix3 inverseInertiaTensor;
  /**
   * indicates the center of mass of the rigidbody
   * this is a position relative to the rigidbody's position
   */
  private Vector3 centerOfMass;

  // ------------- METHODE --------------- //

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
    //if mass is either negative or too near of 0 set it to 1
    if (mass<1e-8){
      mass = 1.f;
    }
    this.mass = mass;
    this.inverseMass = 1 / this.mass;

    this.force = Vector3.ZERO_VECTOR.copy();
    this.position = new Vector3(position);
    this.linearVelocity = new Vector3(linearVelocity);
    this.accumulatedForce = Vector3.ZERO_VECTOR.copy();
    this.contacts = new ArrayList<>();
    this.collider = collider;
    this.torque = Vector3.ZERO_VECTOR.copy();
    this.qRotation = new Quaternion();
    this.mRotation = new Matrix3();
    this.inverseInertiaTensor = new Matrix3();
    this.inertiaTensor = new Matrix3();
    this.angularVelocity = Vector3.ZERO_VECTOR.copy();
    this.centerOfMass = Vector3.ZERO_VECTOR.copy();
    //the collider needs this link to the rigidbody for gameplay purposes
    collider.linkRigidBody(this);
  }

  /**
   * full parameter constructor
   * @param mass - the mass of the rigid body
   * @param position - the position the rigid body starts at
   * @param linearVelocity - the velocity the rigid body starts with
   * @param collider - the collider of the rigid body
   * @param inertiaTensor - the inertia tensor associated with the shape of the mesh
   * @param qRotation - the rotation the rigid body start with
   */
  public RigidBody(double mass,
                   @NotNull Vector3 position,
                   @NotNull Vector3 linearVelocity,
                   @NotNull Collider collider,
                   @NotNull Matrix3 inertiaTensor,
                   @NotNull Quaternion qRotation
                   ) {

    this(mass, position, linearVelocity, collider);
    this.isRotating = true;
    this.inertiaTensor = inertiaTensor;
    this.torque = Vector3.ZERO_VECTOR.copy();
    this.qRotation = qRotation;

    //Initialisation de J'
    double x2 = this.position.x * this.position.x;
    double y2 = this.position.y * this.position.y;
    double z2 = this.position.z * this.position.z;
    Matrix3 translation = new Matrix3( new double[]{
            y2+z2, -this.position.x*this.position.y, -this.position.x*this.position.z,
            -this.position.x*this.position.y, x2+z2, -this.position.y*this.position.z,
            -this.position.x*this.position.z, -this.position.y*this.position.z, x2+y2
    });
    translation.multiplyScalar(this.mass);
    translation.addMatrix(this.inertiaTensor);

    //Inverse de J'
    this.inverseInertiaTensor = translation.getInverse();
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
  /**
   * Get the quaternion for the rotation
   * @return @return <font color="yellow">{@link Quaternion}</font>
   */
  public Quaternion getQRotation() {
    return this.qRotation;
  }
  /**
   * get the rotation matrix
   * @return @return <font color="yellow">{@link Matrix3}</font>
   */
  public Matrix3 getMRotation() {
    return mRotation;
  }
  /**
   * get the angular velocity of the body
   * @return @return <font color="yellow">{@link Vector3}</font>
   */
  public Vector3 getAngularVelocity() {
    return angularVelocity;
  }
  /**
   * get the torque
   * @return @return <font color="yellow">{@link Vector3}</font>
   */
  public Vector3 getTorque() { return torque; }

  /**
   * get the inverse of the inertia tensor matrix
   * @return @return <font color="yellow">{@link Matrix3}</font>
   */
  public Matrix3 getInverseInertiaTensor() { return inverseInertiaTensor; }
  /**
   * get the relative position of the center of mass
   * @return @return @return <font color="yellow">{@link Vector3}</font>
   */
  public Vector3 getCenterOfMass() {
    return centerOfMass;
  }
  /**
   * get the world space coordinates of the center of mass
   * @return @return @return <font color="yellow">{@link Vector3}</font>
   */
  public Vector3 getCenterOfMassWorld() {
    return new Vector3(this.position).add(this.centerOfMass);
  }

  public Vector3 getVelocityAtPoint(Vector3 r) {
    return new Vector3(linearVelocity)
        .add(new Vector3(angularVelocity).crossProduct(r));
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
  /**
   * Set the quaternion for the rotation
   * @param qRotation @return <font color="yellow">{@link Quaternion}</font>
   */
  public void setQRotation(@NotNull Quaternion qRotation) {
    this.qRotation = qRotation;
  }
  /**
   * Set the rotation Matrix
   * @param mRotation @return <font color="yellow">{@link Matrix3}</font>
   */
  public void setMRotation(@NotNull Matrix3 mRotation) {
    this.mRotation = mRotation;
  }
  /**
   * set the angular velocity of the body
   * @param angularVelocity @return <font color="yellow">{@link Vector3}</font>
   */
  public void setAngularVelocity(@NotNull Vector3 angularVelocity) {
    this.angularVelocity = angularVelocity;
  }

  /**
   * set this inverse of the inertia tensor
   * @param inverseInertiaTensor @return <font color="yellow">{@link Matrix3}</font>
   */
  public void setInverseInertiaTensor(@NotNull Matrix3 inverseInertiaTensor) { this.inverseInertiaTensor = inverseInertiaTensor; }
  /**
   * set the center of mass of the rigid body
   * @param centerOfMass <font color="yellow">{@link Vector3}</font>
   */
  public void setCenterOfMass(@NotNull Vector3 centerOfMass) {
    this.centerOfMass = centerOfMass;
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
    // Calcule la nouvelle torque, pour déterminer la possibilité de faire tourner l'objet suite à la collision.
    //
    Vector3 newTorque = contact.getImpulseForce().mul(contact.getLeverArm());
    this.torque.add(newTorque);
    return this.contacts.add(contact);
  }
  /**
   * Clears the contact list of the RigidBody
   */
  public void clearContacts(){
    this.contacts.clear();
    this.torque = Vector3.ZERO_VECTOR.copy();
  }
  /**
   * Find a contact with another RigidBody in the list of contacts
   * @param other <font color="yellow">{@link RigidBody}</font>
   * - the other RigidBody that is supposed to be colliding
   * @return <font color="yellow">{@link Contact}</font>
   * - contact found with the other RigidBody
   */
  public @Nullable Contact findContact(@NotNull RigidBody other) {
    if (this.contacts.isEmpty())return null;

    for (Contact contact : this.getContacts()) {
      if (contact.getOther() == other.getCollider()) {
        return contact;
      }
    }

    return null;
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