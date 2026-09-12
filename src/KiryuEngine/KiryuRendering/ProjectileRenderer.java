package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

import java.util.HashMap;

public class ProjectileRenderer extends SpriteRenderer{

  public ProjectileRenderer(PApplet sketch) {
    super(sketch);
  }

  public void drawSprite(String key, Vector3 position){
    this.getSprites(key).draw(this.getSketch(),position);
  }
}
