package Tests;

import processing.core.PApplet;

public class KiryuTests extends PApplet {

  public static void main(String[] args) {
    System.out.println("MARMITE");
    // Tells Processing to run this specific class
    //PApplet.main("Tests.KiryuTests");
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