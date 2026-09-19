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
import Game.code.bloc.BlockManager;
import KiryuEngine.KiryuPhysics.Particle;
import processing.core.PApplet;


public class KiryuGame extends PApplet {

  ProjectileSystem projectileSystem; //system qui gere les donnees et l'affichage des projectiles 
  BlockManager blockManager; //manager qui gere la creation et l'affichage des bloccs 

  //types de projectiles actuellement actif dans les main des joueurs 
  ProjectileType selectedProjectile = ProjectileType.BALL;
  TrajectoryRenderer trajectoryRenderer;  //renderer utilise pour afficher trajectoire du projectile

  //la liste des particules utilises pour stimuler la physique des projectiles 
  ArrayList<Particle> particles = new ArrayList<>();

  boolean isAiming = false;

  //vitesse 
  Vector3 projectileStartPosition;
  Vector3 launchVelocity;

  //image pour le bas
  PImage ballIcon;
  PImage cannonBallIcon;
  PImage laserIcon;
  PImage fireBallIcon;

  public static void main(String[] args) {
    // Tells Processing to run this specific class
    PApplet.main("Game.KiryuGame");
  }

  //pour switch between les images
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
    size(1200, 800); //changement de la windows pour qu'on voit actuellement les choix des projectiles 
  }

  @Override
  public void setup() {

    //delta time
    KiryuTime.init();
    frameRate(120);

    //cree le system de projectile 
    projectileSystem = new ProjectileSystem(
      new ProjectileData(),
      new ProjectileRenderer(this)
    );  

    //cree le rendere de la trajectoire 
    trajectoryRenderer = new TrajectoryRenderer(this);

    //les images en bas
    ballIcon = loadImage("Game/data/ball.png");
    cannonBallIcon = loadImage("Game/data/cannonball.png");
    laserIcon = loadImage("Game/data/laser.png");
    fireBallIcon = loadImage("Game/data/fireball.png");

    //construction du manager de blocs
    blockManager = new BlockManager();

    float gameBottom = height - 170;

    //generation de structure 
    blockManager.generateRandomStructure(
        width * 0.75f,
        gameBottom
    );

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

    //windwos en bas 
      float hudHeight = 170;
      float hudY = height - hudHeight;

      // Separation line
      stroke(255);
      strokeWeight(3);
      line(0, hudY, width, hudY);

    //separe lecran en 4 partie pour avoir une partie egale en bas
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

    blockManager.draw(this);

    /* TEMPORAIREMENT DISABLE
    projectileSystem.drawProjectiles();
    projectileSystem.updatePosition();
    */

      // Draw the projectile
    projectileSystem.drawProjectiles();


    // While the player is aiming
    if (isAiming) {

      //recupere la position actuelle de la mouse
        Vector3 mousePosition = new Vector3(
            mouseX,
            mouseY,
            0
        );

        //calcule la vitesse de lancement selon la distance, entre la position de depart et la sourie 
        launchVelocity = mousePosition
            .sub(projectileStartPosition)
            .scale(2.0);

        //acceleration de la gravité utilisee pour preduire la trajectoire
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

    //appliquer la gravite a la particule 
    Force.applyGravity(particle);

    //mettre a jouer la vitesse et la position de la particule 
    Integrator.integrate(
        particle,
        dt
    );

    //recupere la nouvelle position calculee
      Vector3 position = particle.getPosition();


      //verifie si le projectile touch bloc
      boolean hitBlock =
          blockManager.checkProjectileCollision(
              position
          );

      // If a block was hit, destroy the projectile too
      if (hitBlock) {

          particles.remove(i);
          projectileSystem.removeProjectile(i);

          continue;
      }


      //verification si le projectile est out
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

    //commence une nouvelle visee seulement si le joueur est pas en train de viser
      if (!isAiming) {
          projectileStartPosition = new Vector3(
              mouseX,
              mouseY,
              0
          );

          //ajoute le projectile au projectile system
          projectileSystem.addProjectile(
              selectedProjectile, //type actif
              new Vector3(projectileStartPosition), //position de depart
              new Vector3(0, 0, 0)
          );

          isAiming = true;
      }
  }

  //se fait call quand on relache la mouse
  @Override
  public void mouseReleased() {

      if (isAiming) {

        //cree une particule physique pour le projectile 
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
