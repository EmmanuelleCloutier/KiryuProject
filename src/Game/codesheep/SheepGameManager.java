
package Game.codesheep;

import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class SheepGameManager {

    private final PApplet sketch;
    private final World world;
    private final SpriteRenderer spriteRenderer;

    private final ArrayList<Sheep> sheepList;
    private final Random random;

    private static final int INITIAL_SHEEP_COUNT = 20;
    private static final float SHEEP_SPACING = 75f;

    public SheepGameManager(PApplet sketch) {
        this.sketch = sketch;

        world = new World();
        spriteRenderer = new SpriteRenderer(sketch);

        sheepList = new ArrayList<>();
        random = new Random();

        generateInitialHerd();
    }

    private void generateInitialHerd() {

        int columns = 5;
        int rows = 4;

        float centerX = sketch.width / 2f;
        float centerY = sketch.height / 2f;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                float x = centerX
                    + (col - (columns - 1) / 2f) * SHEEP_SPACING;

                float y = centerY
                    + (row - (rows - 1) / 2f) * SHEEP_SPACING;

                // Small random variation for a natural herd.
                x += random.nextFloat() * 10f - 5f;
                y += random.nextFloat() * 10f - 5f;

                Sheep sheep = new Sheep(world, x, y);
                sheepList.add(sheep);
            }
        }
    }

    public void update(float deltaTime) {
        // Physics and herd movement will be added later.
    }

    public void draw() {
        sketch.background(110, 155, 95);

        for (Sheep sheep : sheepList) {
            sheep.draw(spriteRenderer);
        }
    }

    public ArrayList<Sheep> getSheepList() {
        return sheepList;
    }

    public World getWorld() {
        return world;
    }
}
