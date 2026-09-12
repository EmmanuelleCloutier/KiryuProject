package Game.code.projectile;

import KiryuEngine.KiryuRendering.Sprite;

import static Game.code.projectile.ProjectileType.Default.*;


public enum ProjectileType {

  BALL(DEFAULT_BALL_MASS, DEFAULT_BALL_SPEED,new Sprite(DEFAULT_BALL_SPRITE_PATH)),
  CANNON_BALL(DEFAULT_CANNONBALL_MASS, DEFAULT_CANNONBALL_SPEED,new Sprite(DEFAULT_CANNONBALL_SPRITE_PATH)),
  LASER(DEFAULT_LASER_MASS, DEFAULT_LASER_SPEED,new Sprite(DEFAULT_LASER_SPRITE_PATH)),
  FIRE_BALL(DEFAULT_FIREBALL_MASS, DEFAULT_FIREBALL_SPEED,new Sprite(DEFAULT_FIREBALL_SPRITE_PATH));

  public final double mass;
  public final double speed;
  public final Sprite sprite;

  ProjectileType(double mass, double speed,Sprite sprite) {
    this.mass = mass;
    this.speed = speed;
    this.sprite = sprite;
  }

  /**
   * this subclass contains all the default setting for every type of
   * projectiles.
   */
  static class Default{

    /**
     * BALL DEFAULTS
     */
    public static final double DEFAULT_BALL_SPEED = 10.f;
    public static final double DEFAULT_BALL_MASS = 2.f;
    public static final String DEFAULT_BALL_SPRITE_PATH = "Game/data/ball.png";


    /**
     * CANNON BALL DEFAULTS
     */
    public static final double DEFAULT_CANNONBALL_SPEED = 10.f;
    public static final double DEFAULT_CANNONBALL_MASS = 10.f;
    public static final String DEFAULT_CANNONBALL_SPRITE_PATH = "Game/data/cannonball.png";

    /**
     * LASER DEFAULTS
     */
    public static final double DEFAULT_LASER_SPEED = 50.f;
    public static final double DEFAULT_LASER_MASS = 0.1f;
    public static final String DEFAULT_LASER_SPRITE_PATH = "Game/data/laser.png";

    /**
     * FIREBALL DEFAULTS
     */
    public static final double DEFAULT_FIREBALL_SPEED= 10.f;
    public static final double DEFAULT_FIREBALL_MASS = 5.f;
    public static final String DEFAULT_FIREBALL_SPRITE_PATH = "Game/data/fireball.png";


  }



}


