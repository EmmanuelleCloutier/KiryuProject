
package Game.Levels;

import processing.core.PApplet;

public class Level_2 {

    private final PApplet app;

    public Level_2(PApplet app) {
        this.app = app;
    }

    public void update(float deltaTime) {
        // Future logique des moutons
    }

    public void draw() {
        app.background(100, 180, 100);

        app.fill(255);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(50);

        app.text(
            "SHEEP GAME - COMING SOON",
            app.width / 2f,
            app.height / 2f
        );
    }
}
