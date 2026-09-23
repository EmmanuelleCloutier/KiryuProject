package Game.code.bloc;

import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;

import java.util.ArrayList;


public class BlockRenderer extends SpriteRenderer {
  /**
   * @param sketch the processing applet is saved here so we can pass it to the
   *               sprites later when we need to load or draw them
   */

  public BlockRenderer(PApplet sketch) {
    super(sketch);
  }
  

  public void drawBlockSprites(ArrayList<Block> blocks){

    for (Block block : blocks
         ) {
      this.drawSprite(block.getSprite(),block.getPosition());
    }
  }
}
