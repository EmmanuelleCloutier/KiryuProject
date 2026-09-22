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
  public float height;
  public float width;

  /**
   * Constructor
   * @param spritePath the project path of the image that the sprite is using
   */
  public Sprite(
      String spritePath,
      float height,
      float width
  ) {

    this.spritePath = spritePath;
    this.height = height;
    this.width = width;
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
  public void draw(
      PApplet sketch,
      Vector3 position
  ) {
    sketch.imageMode(CENTER);
    sketch.image(sprite,
        (float)position.x,
        (float)position.y,
        this.width,
        this.height
    );
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      Vector3 direction
  ) {

    sketch.pushMatrix();
    sketch.translate((float)position.x,(float)position.y);
    float angle = PApplet.atan2((float)direction.y,(float)direction.x);
    sketch.rotate(angle);
    sketch.imageMode(CENTER);
    sketch.image(sprite,
        0,
        0,
        this.width,
        this.height
    );
    sketch.popMatrix();
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      float angle
  ) {
    sketch.pushMatrix();
    sketch.translate((float)position.x,(float)position.y);
    sketch.rotate(angle);
    sketch.imageMode(CENTER);
    sketch.image(sprite,
        0,
        0,
        this.width,
        this.height
    );
    sketch.popMatrix();
  }


  //new draw pour mettre les sprite plus petit
  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      float width,
      float height
  ) {
      sketch.imageMode(CENTER);
      sketch.image(
          sprite,
          (float)position.x,
          (float)position.y,
          width,
          height
      );
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      Vector3 direction,
      float width,
      float height
  ) {

    sketch.pushMatrix();
    sketch.translate((float)position.x,(float)position.y);
    float angle = PApplet.atan2((float)direction.x,(float)direction.y) - sketch.HALF_PI;
    sketch.rotate(angle);
    sketch.imageMode(CENTER);
    sketch.image(sprite,
        (float)position.x,
        (float)position.y,
        width,
        height
    );
    sketch.popMatrix();
  }

  /**
   * @param sketch the processing applet keeps in memory the image, so we can
   * reuse it later
   */
  @Override
  public void loadRenderableImage(PApplet sketch) {
    this.sprite = loadImage(sketch, this.spritePath);
  }

  @Override
  public void loadRenderableImage(PApplet sketch, String filename) {

  }
}
