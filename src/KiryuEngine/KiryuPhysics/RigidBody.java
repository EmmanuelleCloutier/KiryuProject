package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.CollisionSphere;

public class RigidBody {

  private Collider collider;

  private final double mass;
  private final double inverseMass;
  private final double size;

  private final double damping = 1.f;

  private Vector3 force;
  private Vector3 accumulatedForce;
  private Vector3 position;
  private Vector3 linearVelocity;
  private Vector3 direction;

  public RigidBody(double mass, double size) {
    this(
        mass,
        size,
        new Vector3(),
        new Vector3()
    );
}

  /* 
  public RigidBody(
      double mass,
      double size
  ) {
    this.mass = mass;
    this.inverseMass = 1/mass;
    this.size = size;
  }*/


  public RigidBody(
      double mass,
      double size, Vector3 position,
      Vector3 linearVelocity
  ) {

    this.mass = mass;
    this.size = size;
    this.inverseMass = 1 / this.mass;

    this.force = new Vector3();
    this.position = new Vector3(position);
    this.linearVelocity = new Vector3(linearVelocity);
    this.collider = new CollisionSphere(this.size,this);
  }

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
   * @param force sets the RigidBody's acceleration
   */
  public void setForce(Vector3 force) {
    this.force = force;
  }


  /**
   * @return RigidBody's accumulatedForce
   */
  public Vector3 getAccumulatedForce() {
    return accumulatedForce;
  }


  /**
   * @param force sets the RigidBody's accumulatedForce
   */
  public void setAccumulatedForce(Vector3 force) {
    this.accumulatedForce = force;
  }



  /**
   * @return RigidBody's position
   */
  public Vector3 getPosition() {
    return position;
  }


  /**
   * @param position sets the RigidBody's position
   */
  public void setPosition(Vector3 position) {
    this.position = position;
  }


  /**
   * @return RigidBody's linear velocity
   */
  public Vector3 getLinearVelocity() {
    return linearVelocity;
  }


  /**
   * @param linearVelocity sets the RigidBody's velocity
   */
  public void setLinearVelocity(Vector3 linearVelocity) {
    this.linearVelocity = linearVelocity;
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

  public void setDirection(Vector3 direction) {
    this.direction = direction;
  }

  public Collider getCollider() {
    return collider;
  }



  public void onCollision(){}

}
