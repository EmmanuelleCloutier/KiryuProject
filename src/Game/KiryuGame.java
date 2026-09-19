package Game;

import Game.code.projectile.ProjectileData;
import Game.code.projectile.ProjectileSystem;
import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuCore.KiryuTime;
import KiryuEngine.KiryuPhysics.Vector3;
import Game.code.projectile.ProjectileRenderer;
import Game.code.projectile.TrajectoryRenderer;
import processing.core.PApplet;


public class KiryuGame extends PApplet {

  ProjectileSystem projectileSystem;

  TrajectoryRenderer trajectoryRenderer;
  boolean isAiming = false;
  Vector3 projectileStartPosition;


  public static void main(String[] args) {
    // Tells Processing to run this specific class
    PApplet.main("Game.KiryuGame");
  }

  @Override
  public void settings() {
    size(1400, 1400);
  }

  @Override
  public void setup() {

    KiryuTime.init();
    frameRate(120);

    //test uno projecto
    projectileSystem = new ProjectileSystem(
      new ProjectileData(),
      new ProjectileRenderer(this)
    );  

    trajectoryRenderer = new TrajectoryRenderer(this);

    /* TEMPORAIREMENT DISABLE
    projectileSystem = new ProjectileSystem(
        new ProjectileData(),new ProjectileRenderer(this)
    );

    projectileSystem.addProjectile(ProjectileType.BALL,
        new Vector3(200,200,0),
        new Vector3(0,0,0));



    projectileSystem.addProjectile(ProjectileType.CANNON_BALL,
        new Vector3(500,800,0),
        new Vector3(0,0,0));


    projectileSystem.addProjectile(ProjectileType.LASER,
        new Vector3(600,200,0),
        new Vector3(0,0,0));


    for (int i = 0; i < projectileSystem.getProjectileCount(); i++) {
      projectileSystem.setProjectileDirection(i,new Vector3(1,0,0));
    }*/

  }

  @Override
  public void draw() {
    KiryuTime.update();
    float dt = KiryuTime.getDeltaTime();

    background(100);

    /* TEMPORAIREMENT DISABLE
    projectileSystem.drawProjectiles();
    projectileSystem.updatePosition();
    */

        projectileSystem.drawProjectiles();

    if (isAiming) {

        Vector3 mousePosition = new Vector3(
            mouseX,
            mouseY,
            0
        );

        Vector3 velocity = mousePosition
            .sub(projectileStartPosition)
            .scale(2.0);

        Vector3 gravity = new Vector3(
            0,
            200,
            0
        );

        trajectoryRenderer.drawTrajectory(
            projectileStartPosition,
            velocity,
            gravity
        );
    }

    // PAS ENCORE
    // projectileSystem.updatePosition();
  }


    //quand le player clique ca fait aparaitre un projectile sur le mouse position
    @Override
    public void mousePressed() {

        if (!isAiming) {

          projectileStartPosition = new Vector3(
              mouseX,
              mouseY,
              0
          );

          projectileSystem.addProjectile(
              ProjectileType.BALL,
              projectileStartPosition,
              new Vector3(0, 0, 0)
          );

          isAiming = true;
      }
    }


}
