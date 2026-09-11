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

  /**
   * all of these parameters are determined by the default subclass of this enum
   * please refer to that when you want to tweak some values
   * @param velocity the speed at which the type should travel
   * @param mass the mass the type should have
   * @param renderer the renderer used by this type
   */
  ProjectileType(double velocity, double mass, ProjectileRenderer renderer){
    this.velocity = velocity;
    this.mass = mass;
    this.renderer = renderer;
  }

  /**
   *
   * @return this type's default velocity
   */
  public double getVelocity() {
    return velocity;
  }

  /**
   *
   * @return this type's default mass
   */
  public double getMass() {
    return mass;
  }

  /**
   *
   * @return this type's default renderer
   */
  public ProjectileRenderer getRenderer() {
    return renderer;
  }

  /**
   * this class contains all the default setting for every type of
   * projectiles.
   */
  static class Defaults {

    /**
     * BALL DEFAULTS
     */
    public static final double DEFAULT_BALL_VELOCITY = 10.f;
    public static final double DEFAULT_BALL_MASS = 2.f;

    /**
     * CANNON BALL DEFAULTS
     */
    public static final double DEFAULT_CANNONBALL_VELOCITY = 10.f;
    public static final double DEFAULT_CANNONBALL_MASS = 10.f;

    /**
     * LASER DEFAULTS
     */
    public static final double DEFAULT_LASER_VELOCITY = 50.f;
    public static final double DEFAULT_LASER_MASS = 0.f;

    /**
     * FIREBALL DEFAULTS
     */
    public static final double DEFAULT_FIREBALL_VELOCITY = 10.f;
    public static final double DEFAULT_FIREBALL_MASS = 5.f;
  }

}
