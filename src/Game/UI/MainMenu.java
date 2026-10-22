
package Game.UI;

import processing.core.PApplet;

public class MainMenu {

    private final PApplet app;

    public MainMenu(PApplet app) {
        this.app = app;
    }

    public void draw() {
        app.background(35, 40, 55);

        app.textAlign(PApplet.CENTER, PApplet.CENTER);

        app.fill(255);
        app.textSize(60);
        app.text("KIRYU ENGINE", app.width / 2f, app.height * 0.25f);

        drawButton("LEVEL 1 - PROJECTILES", app.height * 0.45f);
        drawButton("LEVEL 2 - SHEEP", app.height * 0.55f);
        drawButton("EXIT", app.height * 0.65f);
    }

    private void drawButton(String text, float y) {
        float x = app.width / 2f;
        float w = 350;
        float h = 60;

        app.rectMode(PApplet.CENTER);
        app.fill(80, 110, 150);
        app.rect(x, y, w, h, 10);

        app.fill(255);
        app.textSize(22);
        app.text(text, x, y);
    }

    public int mousePressed(int mouseX, int mouseY) {

        if (isInsideButton(mouseX, mouseY, app.height * 0.45f)) {
            return 1;
        }

        if (isInsideButton(mouseX, mouseY, app.height * 0.55f)) {
            return 2;
        }

        if (isInsideButton(mouseX, mouseY, app.height * 0.65f)) {
            return 3;
        }

        return 0;
    }

    private boolean isInsideButton(int mx, int my, float y) {
        float x = app.width / 2f;

        return mx >= x - 175 &&
               mx <= x + 175 &&
               my >= y - 30 &&
               my <= y + 30;
    }
}
