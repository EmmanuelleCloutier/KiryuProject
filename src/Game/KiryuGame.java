package Game;

import Game.code.projectile.ProjectileData;
import Game.code.projectile.ProjectileSystem;
import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.ProjectileRenderer;
import processing.core.PApplet;


public class KiryuGame extends PApplet {

  ProjectileSystem projectileSystem;


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

    frameRate(120);

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
    }

  }

  @Override
  public void draw() {

    background(100);

    projectileSystem.drawProjectiles();


    projectileSystem.updatePosition();
  }




}
