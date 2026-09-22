package Game.code.cannon;

import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuPhysics.Force;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Animation;
import KiryuEngine.KiryuRendering.Sprite;
import processing.core.PApplet;
import processing.core.PVector;

public class Cannon {

  private final Sprite sprite;
  private final Animation blastAnimation;
  private final PApplet sketch;
  private final Vector3 position;
  private final TrajectoryRenderer trajectoryRenderer;
  private Vector3 projectileStartPosition;
  private Vector3 launchVelocity;
  private ProjectileType selectedProjectileType = ProjectileType.BALL;



  public Cannon(PApplet sketch, Vector3 position){
    this.sketch = sketch;
    this.sprite = new Sprite(
        "Game/data/cannon.png",
        200,
        200
    );
    this.sprite.loadRenderableImage(sketch);

    this.blastAnimation = new Animation(
        "Game/data/explosion.gif",
        50,
        50,
        false
    );
    blastAnimation.loadRenderableImage(sketch,"explosion.gif");

    this.position = position;
    this.trajectoryRenderer = new TrajectoryRenderer(sketch);
    this.projectileStartPosition = position;
    this.launchVelocity = Vector3.ZERO_VECTOR;

  }

  public void shoot(){
    blastAnimation.playAnimation();
  }


  public void draw(){

    PVector direction = new PVector(
        this.sketch.mouseX - (float)position.x,
        this.sketch.mouseY - (float)position.y
    );

    float angle = direction.heading() + this.sketch.HALF_PI/3;

    this.sprite.draw(this.sketch,this.position,angle);

    PVector topOffset = new PVector(100, -60);
    topOffset.rotate(angle);

    projectileStartPosition = new Vector3(
        topOffset.x + position.x,
        topOffset.y + position.y,
        0
    );

    this.blastAnimation.draw(this.sketch,projectileStartPosition);

    launchVelocity = new Vector3(this.sketch.mouseX,this.sketch.mouseY,0)
        .sub(projectileStartPosition)
        .scale(selectedProjectileType.speed);


    if (selectedProjectileType == ProjectileType.LASER){
      trajectoryRenderer.drawStraightTrajectory(
          projectileStartPosition,
          launchVelocity
      );
    }
    else {
      trajectoryRenderer.drawCurvedTrajectory(
          projectileStartPosition,
          launchVelocity,
          Force.GRAVITY
      );
    }
  }

  public void setSelectedProjectileType(ProjectileType selectedProjectileType) {
    this.selectedProjectileType = selectedProjectileType;
  }

  public Vector3 getProjectileStartPosition() {
    return projectileStartPosition;
  }

  public Vector3 getLaunchVelocity() {
    return launchVelocity;
  }

}
