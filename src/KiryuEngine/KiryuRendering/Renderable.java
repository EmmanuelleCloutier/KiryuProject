package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

public interface Renderable {

  void draw(PApplet sketch,Vector3 position);

  void loadRenderableImage(PApplet sketch);



}
