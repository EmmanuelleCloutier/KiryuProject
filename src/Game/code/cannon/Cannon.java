package Game.code.cannon;

import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuPhysics.Force;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Sprite;
import processing.core.PApplet;
import processing.core.PVector;

public class Cannon {

  private Sprite sprite;
  private PApplet sketch;
  private Vector3 position;
  private TrajectoryRenderer trajectoryRenderer;
  private Vector3 projectileStartPosition;
  private Vector3 launchVelocity;
  private ProjectileType selectedProjectileType = ProjectileType.BALL;



  public Cannon(PApplet sketch, Vector3 position){
    this.sketch = sketch;
    this.sprite = new Sprite(
        "Game/data/cannon.png",
        50,
        100
    );
    this.position = position;
    this.trajectoryRenderer = new TrajectoryRenderer(sketch);
    this.projectileStartPosition = position;
    this.launchVelocity = Vector3.ZERO_VECTOR;
  }

  public void shoot(){
    //TODO add vfx
  }


  public void draw(){

    this.sketch.pushMatrix();
    this.sketch.translate((float)this.position.x, (float)this.position.y);

    PVector direction = new PVector(
        this.sketch.mouseX - (float)position.x,
        this.sketch.mouseY - (float)position.y
    );

    float angle = direction.heading() - this.sketch.HALF_PI;
    this.sketch.rotate(angle);

    this.sketch.rectMode(this.sketch.CENTER);
    this.sketch.fill(50);
    this.sketch.rect(0, 0, 50, 100);

    PVector topOffset = new PVector(0, 65);
    topOffset.rotate(angle);

    this.sketch.popMatrix();

    projectileStartPosition = new Vector3(
        topOffset.x + position.x,
        topOffset.y + position.y,
        0
    );

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
