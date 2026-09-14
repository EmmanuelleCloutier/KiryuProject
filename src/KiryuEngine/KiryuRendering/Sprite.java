package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import processing.core.PImage;
import static processing.awt.ShimAWT.loadImage;
import static processing.core.PConstants.CENTER;


/**
 * This class is the foundation of rendering an image in the processing applet.
 * Processing can load images in setup as well as in runtime.
 * However, we must balance how many images we load in runtime since it is
 * an expensive operation, that's why the SpriteRenderer exists
 */

public class Sprite implements Renderable{

  private final String spritePath;
  private PImage sprite = null;

  /**
   * Constructor
   * @param spritePath the project path of the image that the sprite is using
   */
  public Sprite(String spritePath){
    this.spritePath = spritePath;
  }

  /**
   * @return the path in the project where the image is stored
   */
  public String getSpritePath() {
    return spritePath;
  }

  /**
   * @param sketch the processing applet accesses the surface where the sprite will be drawn
   * @param position the position where the sprite will be drawn
   */
  @Override
  public void draw(PApplet sketch,Vector3 position) {
    sketch.imageMode(CENTER);
    sketch.image(sprite,(float)position.x,(float)position.y);
  }

  /**
   * @param sketch the processing applet keeps in memory the image, so we can
   * reuse it later
   */
  @Override
  public void loadRenderableImage(PApplet sketch) {
    this.sprite = loadImage(sketch, this.spritePath);
  }
}
