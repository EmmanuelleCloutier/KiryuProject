package Game.code.projectile;

import KiryuEngine.KiryuRendering.Sprite;

import static Game.code.projectile.ProjectileType.Default.*;

/**
 * the types of projectile that can be shot by the cannon
 */
public enum ProjectileType {

  BALL(DEFAULT_BALL_MASS,
      DEFAULT_BALL_SPEED,
      new Sprite(
          DEFAULT_BALL_SPRITE_PATH,
          DEFAULT_BALL_SPRITE_HEIGHT,
          DEFAULT_BALL_SPRITE_WIDTH
      )
  ),
  CANNON_BALL(DEFAULT_CANNONBALL_MASS,
      DEFAULT_CANNONBALL_SPEED,
      new Sprite(
          DEFAULT_CANNONBALL_SPRITE_PATH,
          DEFAULT_CANNONBALL_SPRITE_HEIGHT,
          DEFAULT_CANNONBALL_SPRITE_WIDTH
      )
  ),
  LASER(DEFAULT_LASER_MASS,
      DEFAULT_LASER_SPEED,
      new Sprite(
          DEFAULT_LASER_SPRITE_PATH,
          DEFAULT_LASER_SPRITE_HEIGHT,
          DEFAULT_LASER_SPRITE_WIDTH
      )
  ),
  FIRE_BALL(DEFAULT_FIREBALL_MASS,
      DEFAULT_FIREBALL_SPEED,
      new Sprite(DEFAULT_FIREBALL_SPRITE_PATH,
          DEFAULT_FIREBALL_SPRITE_HEIGHT,
          DEFAULT_FIREBALL_SPRITE_WIDTH
      )
  ),
  DUCK(DEFAULT_DUCK_MASS,
      DEFAULT_DUCK_SPEED,
      new Sprite(DEFAULT_DUCK_SPRITE_PATH,
          DEFAULT_DUCK_SPRITE_HEIGHT,
          DEFAULT_DUCK_SPRITE_WIDTH
      )
  );

  public final double mass;
  public final double speed;
  public final Sprite sprite;



  ProjectileType(double mass, double speed, Sprite sprite) {
    this.mass = mass;
    this.speed = speed;
    this.sprite = sprite;
  }

  /**
   * this subclass contains all the default setting for every type of
   * projectiles.
   * created because the enum need to be declared first, and declaring those
   * settings after, caused a forward declaration error
   */
  static class Default{

    /**
     * BALL DEFAULTS
     */
    public static final double DEFAULT_BALL_SPEED = 5.f;
    public static final double DEFAULT_BALL_MASS = 2.f;
    public static final float DEFAULT_BALL_SPRITE_HEIGHT = 50.f;
    public static final float DEFAULT_BALL_SPRITE_WIDTH = 60.f;
    public static final String DEFAULT_BALL_SPRITE_PATH = "Game/data/ball.png";


    /**
     * CANNON BALL DEFAULTS
     */
    public static final double DEFAULT_CANNONBALL_SPEED = 5.f;
    public static final double DEFAULT_CANNONBALL_MASS = 10.f;
    public static final float DEFAULT_CANNONBALL_SPRITE_HEIGHT = 50.f;
    public static final float DEFAULT_CANNONBALL_SPRITE_WIDTH = 60.f;
    public static final String DEFAULT_CANNONBALL_SPRITE_PATH = "Game/data/cannonball.png";

    /**
     * LASER DEFAULTS
     */
    public static final double DEFAULT_LASER_SPEED = 15.f;
    public static final double DEFAULT_LASER_MASS = 0.1f;
    public static final float DEFAULT_LASER_SPRITE_HEIGHT = 50.f;
    public static final float DEFAULT_LASER_SPRITE_WIDTH = 60.f;
    public static final String DEFAULT_LASER_SPRITE_PATH = "Game/data/laser.png";

    /**
     * FIREBALL DEFAULTS
     */
    public static final double DEFAULT_FIREBALL_SPEED= 5.f;
    public static final double DEFAULT_FIREBALL_MASS = 5.f;
    public static final float DEFAULT_FIREBALL_SPRITE_HEIGHT = 50.f;
    public static final float DEFAULT_FIREBALL_SPRITE_WIDTH = 60.f;
    public static final String DEFAULT_FIREBALL_SPRITE_PATH = "Game/data/fireball.png";

    /**
     * DUCK DEFAULTS
     */
    public static final double DEFAULT_DUCK_SPEED= 5.f;
    public static final double DEFAULT_DUCK_MASS = 5.f;
    public static final float DEFAULT_DUCK_SPRITE_HEIGHT = 50.f;
    public static final float DEFAULT_DUCK_SPRITE_WIDTH = 60.f;
    public static final String DEFAULT_DUCK_SPRITE_PATH = "Game/data/duck.png";


  }
}