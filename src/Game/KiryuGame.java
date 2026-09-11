package Game;

import Game.code.Projectile;
import Game.code.ProjectileType;
import processing.core.PApplet;


public class KiryuGame extends PApplet {

  public static void main(String[] args) {
    // Tells Processing to run this specific class
    PApplet.main("Game.KiryuGame");
  }

  @Override
  public void settings() {
    size(800, 600);
  }

  @Override
  public void setup() {
    background(20, 20, 20);
  }

  @Override
  public void draw() {
    fill(0, 150, 255);
    ellipse(mouseX, mouseY, 50, 50);
  }
}
