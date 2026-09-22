package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Sprite;
import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * this is basically a sprite renderer but with some logic specific for projectiles
 */
public class ProjectileRenderer extends SpriteRenderer {

  /**
   * @param sketch processing applet, calls super since we rely on the parent class
   */
  public ProjectileRenderer(PApplet sketch) {
    super(sketch);
  }


  public void drawProjectileSprites(
      ArrayList<ProjectileType> types,
      ArrayList<Vector3> positions,
      ArrayList<Vector3> directions
  ) {

    if (types.isEmpty()||positions.isEmpty() || directions.isEmpty()) return;


    for (int i = 0; i < types.size(); i++) {
      Sprite sprite = types.get(i).sprite;
      Vector3 position = positions.get(i);
      Vector3 direction = directions.get(i);

      drawSprite(sprite,position,direction);
    }


  }
}
