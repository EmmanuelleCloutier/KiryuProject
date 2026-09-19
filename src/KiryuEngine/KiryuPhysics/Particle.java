package KiryuEngine.KiryuPhysics;

public class Particle {

  private final double mass;

  private final double inverseMass;
  private Vector3 acceleration = Vector3.UNIT_VECTOR;
  private Vector3 position;
  private Vector3 linearVelocity;
  private final double damping = 1.f;

  Particle(double mass){
    this.mass = mass;
    this.inverseMass = 1/this.mass;
  };

  /**
   * @return the mass of the particle
   */
  public double getMass() {
    return mass;
  }

  /**
   * @return 1 / mass of the particle, used for physics, precalculated for shortcut
   */
  public double getInverseMass() {
    return inverseMass;
  }

  /**
   * @return particle's acceleration
   */
  public Vector3 getAcceleration() {
    return acceleration;
  }

  /**
   * @param acceleration sets the particle's acceleration
   */
  public void setAcceleration(Vector3 acceleration) {
    this.acceleration = acceleration;
  }

  /**
   * @return particle's position
   */
  public Vector3 getPosition() {
    return position;
  }

  /**
   * @return particle's velocity along planes
   */
  public Vector3 getLinearVelocity() {
    return linearVelocity;
  }

  /**
   * @return friction coefficient
   */
  public double getDamping() {
    return damping;
  }

  /**
   * implementation of Euler's integration
   * @param deltaTime time between each frames
   */
  public void integrate(float deltaTime){

    //new velocity is equals to:
    // ((damping ^ deltaTime) * currentVelocity) + (acceleration * deltaTime)
    // v1 = ((d^dt) * v0) + (dt * a)
    this.linearVelocity = this.linearVelocity.scale(Math.pow(this.damping,deltaTime))
        .add(this.acceleration.scale(deltaTime));

    //new position equals to:
    // position + (current velocity * deltaTime)
    // p1 = p0 + (v1 * dt)
    this.position = this.position.add(this.linearVelocity.scale(deltaTime));
  }

}
