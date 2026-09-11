package Game.code;


import KiryuEngine.KiryuRendering.ProjectileRenderer;

import static Game.code.ProjectileType.Defaults.*;

public enum ProjectileType {
  Ball(DEFAULT_BALL_VELOCITY,DEFAULT_BALL_MASS, new ProjectileRenderer()),
  CannonBall(DEFAULT_CANNONBALL_VELOCITY,DEFAULT_CANNONBALL_MASS, new ProjectileRenderer()),
  Laser(DEFAULT_LASER_VELOCITY,DEFAULT_LASER_MASS, new ProjectileRenderer()),
  FireBall(DEFAULT_FIREBALL_VELOCITY,DEFAULT_FIREBALL_MASS, new ProjectileRenderer());

  private final double velocity;
  private final double mass;
  private final ProjectileRenderer renderer;

  ProjectileType(double velocity, double mass, ProjectileRenderer renderer){
    this.velocity = velocity;
    this.mass = mass;
    this.renderer = renderer;
  }

  public double getVelocity() {
    return velocity;
  }

  public double getMass() {
    return mass;
  }

  public ProjectileRenderer getRenderer() {
    return renderer;
  }


  static class Defaults {

    public static final double DEFAULT_BALL_VELOCITY = 10.f;
    public static final double DEFAULT_BALL_MASS = 2.f;

    public static final double DEFAULT_CANNONBALL_VELOCITY = 10.f;
    public static final double DEFAULT_CANNONBALL_MASS = 10.f;

    public static final double DEFAULT_LASER_VELOCITY = 50.f;
    public static final double DEFAULT_LASER_MASS = 0.f;

    public static final double DEFAULT_FIREBALL_VELOCITY = 10.f;
    public static final double DEFAULT_FIREBALL_MASS = 5.f;
  }

}
