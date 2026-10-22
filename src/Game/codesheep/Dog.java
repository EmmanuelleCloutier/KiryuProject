
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;

public class Dog {

    private final PApplet sketch;
    private Vector3 position;

    private static final String SPRITE_PATH =
        "src/Game/data/chien.png";

    private static final float SIZE = 65f;

    public Dog(PApplet sketch) {
        this.sketch = sketch;

        //default position
        position = new Vector3(
            sketch.width / 2.0,
            sketch.height / 2.0 + 250,
            0
        );
    }

    public void update(float deltaTime) {

        //follow mouse
        position = new Vector3(
            sketch.mouseX,
            sketch.mouseY,
            0
        );
    }

    public void draw(SpriteRenderer renderer) {
        renderer.drawSprite(
            SPRITE_PATH,
            position,
            SIZE,
            SIZE
        );
    }

    public Vector3 getPosition() {
        return position;
    }
}
