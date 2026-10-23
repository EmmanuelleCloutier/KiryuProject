
package Game.Levels;

import Game.codesheep.SheepGameManager;
import processing.core.PApplet;

public class Level_2 {

    private final SheepGameManager sheepGameManager;

    public Level_2(PApplet app) {
        sheepGameManager = new SheepGameManager(app);
    }

    public void update(float deltaTime) {
        sheepGameManager.update(deltaTime);
    }

    public void draw() {
        sheepGameManager.draw();
    }
}
