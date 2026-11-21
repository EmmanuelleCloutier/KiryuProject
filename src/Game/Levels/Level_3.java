
package Game.Levels;

import Game.codeTP3.TP3GameManager;
import processing.core.PApplet;

public class Level_3 {

    PApplet sketch;


    TP3GameManager gameManager;

    public Level_3(PApplet app) {
        sketch = app;
        gameManager = new TP3GameManager(app);
    }

    public void update(float deltaTime) {
        gameManager.update(deltaTime);
    }

    public void draw() {
        gameManager.draw();
    }

    public void keyPressed(char key) {

    }

    public void mousePressed() {
        gameManager.shoot();
    }
}
