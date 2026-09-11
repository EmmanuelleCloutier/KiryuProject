package Game.code;

import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.ProjectileRenderer;

public class Projectile {

  private Vector3 position;
  private final double velocity;
  private final double mass;
  private final ProjectileRenderer renderer;
  private final ProjectileType type;

  public Projectile(ProjectileType type, Vector3 position){
    this.position = position;
    this.type = type;
    this.velocity = type.getVelocity();
    this.mass = type.getMass();
    this.renderer = type.getRenderer();
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  public double getVelocity() {
    return velocity;
  }

  public ProjectileRenderer getRenderer() {
    return renderer;
  }

  public ProjectileType getType() {
    return type;
  }

  public double getMass() {
    return mass;
  }
}
