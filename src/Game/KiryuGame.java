package Game;

import Game.code.projectile.ProjectileData;
import Game.code.projectile.ProjectileSystem;
import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.ProjectileRenderer;
import KiryuEngine.KiryuRendering.Sprite;
import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public class KiryuGame extends PApplet {

  SpriteRenderer spriteRenderer;
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
    projectileSystem = new ProjectileSystem(
        new ProjectileData(),new ProjectileRenderer(this)
    );

    projectileSystem.addProjectile(ProjectileType.BALL,
        new Vector3(200,200,0),
        new Vector3(0,0,0));

    projectileSystem.setupProjectileRenderer();


  }

  @Override
  public void draw() {
    projectileSystem.drawProjectiles();
  }

}
