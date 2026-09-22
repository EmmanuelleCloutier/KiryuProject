package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;


/**
 * basic interface of what a basic renderable objet needs to implement
 */
public interface Renderable {

  void draw(PApplet sketch,Vector3 position);

  void draw(PApplet sketch,Vector3 position, Vector3 direction);

  void draw(PApplet sketch,Vector3 position, float angle);

  void draw(PApplet sketch, Vector3 position, float width, float height);

  void draw(PApplet sketch, Vector3 position,Vector3 direction, float width, float height);

  void loadRenderableImage(PApplet sketch);

  void loadRenderableImage(PApplet sketch, String filename);

}
