package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import java.util.HashMap;

public class SpriteRenderer {
  private final PApplet sketch;

  private final HashMap<String,Sprite> sprites;

  public SpriteRenderer(PApplet sketch){
    this.sketch = sketch;
    this.sprites = new HashMap<String,Sprite>();
  }

  public void addSprite(Sprite sprite){
    if(!sprites.containsKey(sprite.getSpritePath())){
      sprites.put(sprite.getSpritePath(), sprite);
    }
  }

  public void removeSprite(Sprite sprite){
    sprites.remove(sprite.getSpritePath());
  }

  public Sprite getSprites(String key) {
    return sprites.get(key);
  }

  public void loadSprites(){
    sprites.forEach((key, value) -> {
      ((Sprite) value).loadRenderableImage(this.sketch);
    });
  }

  public void drawSprites(Vector3 position){
    sprites.forEach((key,value)->{
      ((Sprite)value).draw(sketch,position);
    });
  }

  public PApplet getSketch() {
    return sketch;
  }

}
