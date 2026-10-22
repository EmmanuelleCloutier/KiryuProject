
package Game.Levels;

import Game.codesheep.Sheep;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.SpriteRenderer;

import processing.core.PApplet;

public class Level_2 {

    private final PApplet app;
    private final World world;
    private final SpriteRenderer spriteRenderer;

    private final Sheep sheep;

    public Level_2(PApplet app) {

        this.app = app;

        // Independent physics world for Game 2.
        world = new World();

        // Shared rendering system.
        spriteRenderer = new SpriteRenderer(app);

        // Create one sheep at the center of the screen.
        sheep = new Sheep(
            world,
            app.width / 2.0,
            app.height / 2.0
        );
    }

    public void update(float deltaTime) {

        // No physics simulation yet.
        // We will activate it when movement is implemented.
    }

    public void draw() {

        // Grass background.
        app.background(110, 155, 95);

        // Render the sheep.
        sheep.draw(spriteRenderer);
    }
}
