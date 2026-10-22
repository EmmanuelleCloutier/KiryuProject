
package Game.Levels;

import Game.code.GameManager;
import KiryuEngine.KiryuPhysics.World;
import processing.core.PApplet;

public class Level_1 {

    private final GameManager gameManager;
    private final World world;

    public Level_1(PApplet app) {
        world = new World();
        gameManager = new GameManager(app, world);
    }

    public void update(float deltaTime) {
        gameManager.update(deltaTime);
    }

    public void draw() {
        gameManager.draw();
    }

    public void keyPressed(char key) {
        gameManager.setProjectileType(key);
    }

    public void mousePressed() {
        gameManager.shoot();
    }
}
