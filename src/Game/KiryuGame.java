package Game;

import Game.code.GameManager;
import KiryuEngine.KiryuCore.KiryuTime;
import processing.core.PApplet;
import java.awt.Dimension;
import java.awt.Toolkit;


public class KiryuGame extends PApplet {

  GameManager gameManager;


  public static void main(String[] args) {
    // Tells Processing to run this specific class
    PApplet.main("Game.KiryuGame");
  }

  //pour switch between les images
  @Override
  public void keyPressed() {
    gameManager.setProjectileType(key);
  }

  @Override
  public void settings() {
    //size(1920, 1080); //changement de la windows pour qu'on voit actuellement les choix des projectiles

    // Récupération de la taille de l'écran
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    size(width, height);
  }

  @Override
  public void setup() {
    frameRate(100000);
    loadImage("Game/data/background.png");

    //delta time
    KiryuTime.init();

    gameManager = new GameManager(this);
  }


  @Override
  public void draw() {
    KiryuTime.update();


    //UPDATE GAME LOOP ORDER
    //GET INPUTS
    //UPDATE PHYSICS
    //DRAW


    gameManager.update(KiryuTime.getDeltaTime());

    gameManager.draw();

//
//    // While the player is aiming
//    if (isAiming) {
//
//      //recupere la position actuelle de la mouse
//        Vector3 mousePosition = new Vector3(
//            mouseX,
//            mouseY,
//            0
//        );
//
//        //calcule la vitesse de lancement selon la distance, entre la position de depart et la sourie
//        launchVelocity = mousePosition
//            .sub(projectileStartPosition)
//            .scale(2.0);
//
//
//        trajectoryRenderer.drawCurvedTrajectory(
//            projectileStartPosition,
//            launchVelocity,
//            Force.GRAVITY
//        );
//
//
//    }

}


  //quand le player clique ca fait aparaitre un projectile sur le mouse position
  @Override
  public void mousePressed() {

    gameManager.shoot();
//
//    //commence une nouvelle visee seulement si le joueur est pas en train de viser
//      if (!isAiming) {
//          projectileStartPosition = new Vector3(
//              mouseX,
//              mouseY,
//              0
//          );
//
//
//          isAiming = true;
//      }
  }

  //se fait call quand on relache la mouse
  @Override
  public void mouseReleased() {

//      if (isAiming) {
//
//        gameManager.projectileSystem.addProjectileAtPosition(
//            gameManager.headUpDisplay.getSelectedProjectile(),
//            projectileStartPosition,
//            launchVelocity
//        );
//          isAiming = false;
//      }
  }

}
