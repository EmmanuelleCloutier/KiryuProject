package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import processing.core.PImage;
import static processing.awt.ShimAWT.loadImage;
import static processing.core.PConstants.CENTER;

public class Sprite implements Renderable{

  private final String spritePath;
  private PImage sprite = null;

  public Sprite(String spritePath){
    this.spritePath = spritePath;
  }

  public String getSpritePath() {
    return spritePath;
  }

  @Override
  public void draw(PApplet sketch,Vector3 position) {
    sketch.imageMode(CENTER);
    sketch.image(sprite,(float)position.x,(float)position.x);
  }


  @Override
  public void loadRenderableImage(PApplet sketch) {
    this.sprite = loadImage(sketch, this.spritePath);
  }
}
