package Game.code;

import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.ProjectileRenderer;

public class Projectile {

  private Vector3 position;
  private Vector3 direction;
  private Vector3 velocity;
  private final double speed;
  private final double mass;
  private final ProjectileRenderer renderer;
  private final ProjectileType type;

  /**
   *
   * @param type type of projectile to instantiate
   * @param position starting position of the projectile
   */
  public Projectile(ProjectileType type, Vector3 position){
    this.position = position;
    this.type = type;
    this.speed = type.getVelocity();
    this.mass = type.getMass();
    this.renderer = type.getRenderer();
  }

  /**
   *
   * @param deltaTime time elapsed to create a frame
   */
  public void updateMovement(float deltaTime){
    this.velocity = this.direction.mult(this.speed * deltaTime);
    this.position.add(this.velocity);
  }

  /**
   *
   * @return current position of this projectile
   */
  public Vector3 getPosition() {
    return position;
  }

  /**
   *
   * @param position sets the new position of this projectile
   */
  public void setPosition(Vector3 position) {
    this.position = position;
  }

  /**
   *
   * @return the current direction of this projectile
   */
  public Vector3 getDirection() {
    return direction;
  }

  /**
   *
   * @param direction sets the new direction of this projectile
   */
  public void setDirection(Vector3 direction) {
    this.direction = direction;
  }


  /**
   *
   * @return current speed of this projectile
   */
  public double getSpeed() {
    return speed;
  }

  /**
   *
   * @return current renderer of this projectile
   */
  public ProjectileRenderer getRenderer() {
    return renderer;
  }

  /**
   *
   * @return current type of this projectile
   */
  public ProjectileType getType() {
    return type;
  }

  /**
   *
   * @return current mass of this projectile
   */
  public double getMass() {
    return mass;
  }

  /**
   *
   * @return current velocity of this projectile
   * mind you, this is a vector, so ultimately this is direction * speed
   */
  public Vector3 getVelocity() {
    return velocity;
  }

  /**
   *
   * @return a formatted version of the projectile's information
   */
  public String ToString(){

    return String.format("projectile info: \n" +
        "type: %s" +
        "position: %s" +
        "velocity: %f" +
        "mass: %f" +
        "direction: %s",
        this.type.toString(),
        this.getPosition().toString(),
        this.getSpeed(),
        this.getMass(),
        this.getDirection().toString());
  }
}
