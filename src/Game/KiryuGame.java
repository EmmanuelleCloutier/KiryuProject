
package Game;

import Game.Levels.Level_1;
import Game.Levels.Level_2;
import Game.UI.MainMenu;

import KiryuEngine.KiryuCore.KiryuTime;
import processing.core.PApplet;

import java.awt.Dimension;
import java.awt.Toolkit;

public class KiryuGame extends PApplet {

    private enum GameState {
        MENU,
        LEVEL_1,
        LEVEL_2
    }

    private GameState currentState = GameState.MENU;

    private MainMenu mainMenu;
    private Level_1 level1;
    private Level_2 level2;

    public static void main(String[] args) {
        PApplet.main("Game.KiryuGame");
    }

    @Override
    public void settings() {
        Dimension screenSize =
            Toolkit.getDefaultToolkit().getScreenSize();

        size(screenSize.width, screenSize.height);
    }

    @Override
    public void setup() {
        frameRate(60);

        KiryuTime.init();

        mainMenu = new MainMenu(this);
    }

    @Override
    public void draw() {
        KiryuTime.update();

        float deltaTime = KiryuTime.getDeltaTime();

        switch (currentState) {

            case MENU:
                mainMenu.draw();
                break;

            case LEVEL_1:
                level1.update(deltaTime);
                level1.draw();
                break;

            case LEVEL_2:
                level2.update(deltaTime);
                level2.draw();
                break;
        }
    }

    @Override
    public void mousePressed() {

        switch (currentState) {

            case MENU:
                int choice = mainMenu.mousePressed(mouseX, mouseY);

                if (choice == 1) {
                    level1 = new Level_1(this);
                    currentState = GameState.LEVEL_1;
                }

                else if (choice == 2) {
                    level2 = new Level_2(this);
                    currentState = GameState.LEVEL_2;
                }

                else if (choice == 3) {
                    exit();
                }
                break;

            case LEVEL_1:
                level1.mousePressed();
                break;

            case LEVEL_2:
                // Future mouse inputs
                break;
        }
    }

    @Override
    public void keyPressed() {

        // ESC pour revenir au menu
        if (key == ESC) {
            key = 0;
            currentState = GameState.MENU;
            return;
        }

        switch (currentState) {

            case LEVEL_1:
                level1.keyPressed(key);
                break;

            case LEVEL_2:
                // Future keyboard inputs
                break;

            default:
                break;
        }
    }
}
