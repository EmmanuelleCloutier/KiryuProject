package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.SphereCollider;
import KiryuEngine.KiryuPhysics.Collision.Contact;

import java.util.ArrayList;

public class RigidBody {

  private Collider collider;

  private final double mass;
  private final double inverseMass;
  private final double size;

  private double damping = 1.f;
  private double staticFrictionCoefficient = 0.5f;
  private double kineticFrictionCoefficient = 0.5f;

  private boolean bIsStatic = false;

  private Vector3 force;
  private Vector3 accumulatedForce;
  private Vector3 position;
  private Vector3 linearVelocity;
  private Vector3 direction;

  private ArrayList<Contact> contacts;

  public RigidBody(double mass, double size) {
    this(
        mass,
        size,
        new Vector3(),
        new Vector3()
    );
  }

  public RigidBody(
      double mass,
      double size,
      Vector3 position,
      Vector3 linearVelocity
  ) {

    this.mass = mass;
    this.size = size;
    this.inverseMass = 1 / this.mass;

    this.force = new Vector3();
    this.position = new Vector3(position);
    this.linearVelocity = new Vector3(linearVelocity);
    this.collider = new SphereCollider(this.size,this);
    this.accumulatedForce = new Vector3();
    this.contacts = new ArrayList<>();
  }

  /**
   * GETTERS
   */
//region
  /**
   * @return the mass of the RigidBody
   */
  public double getMass() {
    return mass;
  }


  /**
   * @return 1 / mass of the RigidBody
   */
  public double getInverseMass() {
    return inverseMass;
  }

  /**
   * @return RigidBody's force
   */
  public Vector3 getForce() {
    return force;
  }

  /**
   * @return RigidBody's accumulatedForce
   */
  public Vector3 getAccumulatedForce() {
    return accumulatedForce;
  }

  /**
   * @return RigidBody's position
   */
  public Vector3 getPosition() {
    return position;
  }

  /**
   * @return RigidBody's linear velocity
   */
  public Vector3 getLinearVelocity() {
    return linearVelocity;
  }

  /**
   * @return damping coefficient
   */
  public double getDamping() {
    return damping;
  }

  public Vector3 getDirection() {
    return direction;
  }

  public Collider getCollider() {
    return collider;
  }

  public double getStaticFrictionCoefficient() {
    return staticFrictionCoefficient;
  }

  public double getKineticFrictionCoefficient() {
    return kineticFrictionCoefficient;
  }

  public boolean isStatic() {
    return bIsStatic;
  }

  //endregion

  /**
   * SETTERS
   */
//region
  /**
   * @param force sets the RigidBody's force
   */
  public void setForce(Vector3 force) {
    this.force = force;
  }

  /**
   * @param force sets the RigidBody's accumulatedForce
   */
  public void setAccumulatedForce(Vector3 force) {
    this.accumulatedForce = force;
  }

  /**
   * @param position sets the RigidBody's position
   */
  public void setPosition(Vector3 position) {
    this.position = position;
  }

  /**
   * @param linearVelocity sets the RigidBody's velocity
   */
  public void setLinearVelocity(Vector3 linearVelocity) {
    this.linearVelocity = linearVelocity;
  }


  public void setDirection(Vector3 direction) {
    this.direction = direction;
  }

  public void setIsStatic(boolean bIsStatic) {
    this.bIsStatic = bIsStatic;
  }

  //endregion


  public void onCollision(){}

  public ArrayList<Contact> getContacts() {
    return contacts;
  }

  public boolean addContact(Contact contact){
    return this.contacts.add(contact);
  }

  public void clearContacts(){
    this.contacts.clear();
  }

  /**
   *
   * @param addedForce add this force to the rigidBody's current force
   */
  public void addForce(Vector3 addedForce){
    this.force.add(addedForce);
  }


  public void addImpulse(Vector3 impulseDirection, double impulseStrength){

    this.force.add(impulseDirection.scale(impulseStrength));

  }

  public void addImpulse(double impulseStrength){

    Vector3 impulse = new Vector3(
        this.direction.x * impulseStrength,
        this.direction.y * impulseStrength,
        this.direction.z * impulseStrength
    );

    this.force.add(impulse);

  }

  public boolean isColliding(){
    return !contacts.isEmpty();
  }

}
