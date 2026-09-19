package Game;

import Game.code.projectile.ProjectileData;
import Game.code.projectile.ProjectileSystem;
import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuCore.KiryuTime;
import KiryuEngine.KiryuPhysics.Vector3;
import Game.code.projectile.ProjectileRenderer;
import Game.code.projectile.TrajectoryRenderer;
import processing.core.PImage;
import java.util.ArrayList;
import KiryuEngine.KiryuPhysics.Force;
import KiryuEngine.KiryuPhysics.Integrator;
import KiryuEngine.KiryuPhysics.Particle;
import processing.core.PApplet;


public class KiryuGame extends PApplet {

  ProjectileSystem projectileSystem;
  ProjectileType selectedProjectile = ProjectileType.BALL;
  TrajectoryRenderer trajectoryRenderer;

  ArrayList<Particle> particles = new ArrayList<>();

  boolean isAiming = false;

  Vector3 projectileStartPosition;
  Vector3 launchVelocity;

  PImage ballIcon;
  PImage cannonBallIcon;
  PImage laserIcon;
  PImage fireBallIcon;

  public static void main(String[] args) {
    // Tells Processing to run this specific class
    PApplet.main("Game.KiryuGame");
  }

  @Override
  public void keyPressed() {

      if (key == '1') {
          selectedProjectile = ProjectileType.BALL;
      }

      if (key == '2') {
          selectedProjectile = ProjectileType.CANNON_BALL;
      }

      if (key == '3') {
          selectedProjectile = ProjectileType.LASER;
      }

      if (key == '4') {
          selectedProjectile = ProjectileType.FIRE_BALL;
      }
  }

  @Override
  public void settings() {
    size(1200, 800);
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

    ballIcon = loadImage("Game/data/ball.png");
    cannonBallIcon = loadImage("Game/data/cannonball.png");
    laserIcon = loadImage("Game/data/laser.png");
    fireBallIcon = loadImage("Game/data/fireball.png");

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

  public void drawProjectileHUD() {

      float hudHeight = 170;
      float hudY = height - hudHeight;

      // HUD background
      noStroke();
      fill(50);
      rect(0, hudY, width, hudHeight);

      // Separation line
      stroke(255);
      strokeWeight(3);
      line(0, hudY, width, hudY);

      float slotWidth = width / 4.0f;

      float iconSize = 80;
      float iconY = hudY + 60;

      // Center images
      imageMode(CENTER);

      image(
          ballIcon,
          slotWidth * 0.5f,
          iconY,
          iconSize,
          iconSize
      );

      image(
          cannonBallIcon,
          slotWidth * 1.5f,
          iconY,
          iconSize,
          iconSize
      );

      image(
          laserIcon,
          slotWidth * 2.5f,
          iconY,
          iconSize,
          iconSize
      );

      image(
          fireBallIcon,
          slotWidth * 3.5f,
          iconY,
          iconSize,
          iconSize
      );

      // Numbers
      fill(255);
      textAlign(CENTER, CENTER);
      textSize(24);

      text("1", slotWidth * 0.5f, hudY + 130);
      text("2", slotWidth * 1.5f, hudY + 130);
      text("3", slotWidth * 2.5f, hudY + 130);
      text("4", slotWidth * 3.5f, hudY + 130);
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

      // Draw the projectile
    projectileSystem.drawProjectiles();


    // While the player is aiming
    if (isAiming) {

        Vector3 mousePosition = new Vector3(
            mouseX,
            mouseY,
            0
        );

        launchVelocity = mousePosition
            .sub(projectileStartPosition)
            .scale(2.0);

        Vector3 gravity = new Vector3(
            0,
            200,
            0
        );

        trajectoryRenderer.drawTrajectory(
            projectileStartPosition,
            launchVelocity,
            gravity
        );

      
    }


  float gameBottom = height - 170;

  for (int i = particles.size() - 1; i >= 0; i--) {

      Particle particle = particles.get(i);

      Force.applyGravity(particle);

      Integrator.integrate(
          particle,
          dt
      );

      Vector3 position = particle.getPosition();

      // Destroy projectile when it leaves the game area
      if (
          position.x < 0 ||
          position.x > width ||
          position.y < 0 ||
          position.y > gameBottom
      ) {
          particles.remove(i);
          projectileSystem.removeProjectile(i);

          continue;
      }

      projectileSystem.setProjectilePosition(
          i,
          position
      );
  }

 drawProjectileHUD();
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
              selectedProjectile,
              new Vector3(projectileStartPosition),
              new Vector3(0, 0, 0)
          );

          isAiming = true;
      }
  }

  @Override
  public void mouseReleased() {

      if (isAiming) {

        Particle newParticle = new Particle(
            selectedProjectile.mass,
            projectileStartPosition,
            launchVelocity
        );
        
          particles.add(newParticle);

          isAiming = false;
      }
  }

}
