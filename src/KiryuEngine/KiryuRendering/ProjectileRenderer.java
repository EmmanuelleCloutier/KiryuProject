package KiryuEngine.KiryuRendering;

import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * this is basically a sprite renderer but with some logic specific for projectiles
 */
public class ProjectileRenderer extends SpriteRenderer{

  /**
   * @param sketch processing applet, calls super since we rely on the parent class
   */
  public ProjectileRenderer(PApplet sketch) {
    super(sketch);
  }

  /**
   * @param spritePositions a map of the type of projectiles and all the positions
   * where that projectile needs to be drawn
   */
  public void drawProjectileSprites(HashMap<ProjectileType, ArrayList<Vector3>> spritePositions) {

    for (var entry : spritePositions.entrySet()) {

      ProjectileType type = entry.getKey();
      ArrayList<Vector3> positions = entry.getValue();

      for (Vector3 position : positions) {
        this.drawSprite(type.sprite.getSpritePath(), position);
      }
    }
  }
}
