
package Game.Levels;

import Game.codeTP3.TP3GameManager;
import processing.core.PApplet;

public class Level_3 {

    private final TP3GameManager tp3GameManager;

    public Level_3(PApplet app) {
        tp3GameManager = new TP3GameManager(app);
    }

    public void update(float deltaTime) {
        tp3GameManager.update(deltaTime);
    }

    public void draw() {
        tp3GameManager.draw();
    }
}
