package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import java.util.HashMap;


/**
 * The SpriteRenderer job is to keep track of loaded sprites and calls a draw call
 * on the sprites that needs to be rendered
 */
public class SpriteRenderer {
  private final PApplet sketch;

  private final HashMap<String,Sprite> loadedSprites;

  /**
   * @param sketch the processing applet is saved here so we can pass it to the
   * sprites later when we need to load or draw them
   */
  public SpriteRenderer(PApplet sketch){
    this.sketch = sketch;
    this.loadedSprites = new HashMap<>();
  }

  /**
   * @param sprite the sprite to be added to the loaded list
   */
  public void addSprite(Sprite sprite){
    sprite.loadRenderableImage(sketch);
    loadedSprites.put(sprite.getSpritePath(), sprite);
  }

  /**
   * @param sprite the sprite that needs to be removed from the loaded list
   */
  public void removeSprite(Sprite sprite){
    loadedSprites.remove(sprite.getSpritePath());
  }

  /**
   * @return the list of loaded sprites
   */
  public HashMap<String,Sprite> getLoadedSprites() {
    return loadedSprites;
  }

  /**
   * @param spritePath the loaded sprites list is a hashmap using the image directory
   * path as the key
   * @param position the position where the sprite will be drawn
   */
  public void drawSprite(String spritePath,Vector3 position){
    // if the sprites doesn't exist in the list we simply add it
    // this is done at runtime and done once per sprite
    // if we have 10k particles using 1 sprite, the sprite is loaded once
    // but if we have 10k particles with 100 different sprites that needs
    // to be loaded in the same frame, that is where we might encounter lag
    if(!loadedSprites.containsKey(spritePath)){
      Sprite sprite = new Sprite(spritePath);
      addSprite(sprite);
    }

    loadedSprites.get(spritePath).draw(sketch,position);
  }

  /**
   * @return returns the reference to the processing applet
   */
  public PApplet getSketch() {
    return sketch;
  }

}
