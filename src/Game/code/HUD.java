package Game.code;

import Game.code.projectile.ProjectileType;
import processing.core.PApplet;
import processing.core.PImage;

public class HUD {

  //image pour le bas
  private final PImage ballIcon;
  private final PImage cannonBallIcon;
  private final PImage laserIcon;
  private final PImage fireBallIcon;
  private final PImage duckIcon;

  //types de projectiles actuellement actif dans les main des joueurs
  private ProjectileType selectedProjectile = ProjectileType.BALL;


  private final PApplet sketch;

  public HUD(PApplet sketch){
    this.sketch = sketch;
    ballIcon = sketch.loadImage("Game/data/ball.png");
    cannonBallIcon = sketch.loadImage("Game/data/cannonball.png");
    laserIcon = sketch.loadImage("Game/data/laser.png");
    fireBallIcon = sketch.loadImage("Game/data/fireball.png");
    duckIcon = sketch.loadImage("Game/data/duck.png");
  }

  public void draw(){

    //windows en bas
    float hudHeight = 170;
    float hudY = this.sketch.height - hudHeight;

    // Separation line
    this.sketch.stroke(255);
    this.sketch.strokeWeight(3);
    this.sketch.line(0, hudY, this.sketch.width, hudY);

    //separe lecran en 4 partie pour avoir une partie egale en bas
    float slotWidth = this.sketch.width / 5.0f;

    float iconWidth = 75.f;
    float iconHeight = 56.f;
    float iconY = hudY + 50f;

    // Center images
    this.sketch.imageMode(this.sketch.CENTER);

    this.sketch.fill(255);
    this.sketch.circle(slotWidth * (1.5f + (0.5f * this.selectedProjectile.ordinal())), iconY, iconWidth);

    this.sketch.image(
        this.ballIcon,
        slotWidth * 1.5f,
        iconY,
        iconWidth,
        iconHeight
    );

    this.sketch.image(
        this.cannonBallIcon,
        slotWidth * 2f,
        iconY,
        iconWidth,
        iconHeight
    );

    this.sketch.image(
        this.laserIcon,
        slotWidth * 2.5f,
        iconY,
        iconWidth,
        iconHeight
    );

    this.sketch.image(
        this.fireBallIcon,
        slotWidth * 3f,
        iconY,
        iconWidth,
        iconHeight
    );

    this.sketch.image(
        this.duckIcon,
        slotWidth * 3.5f,
        iconY,
        iconWidth,
        iconHeight
    );

    // Numbers
    this.sketch.fill(255);
    this.sketch.textAlign(this.sketch.CENTER, this.sketch.CENTER);
    this.sketch.textSize(24);

    this.sketch.text("1", slotWidth * 1.35f, hudY + 15);
    this.sketch.text("2", slotWidth * 1.85f, hudY + 15);
    this.sketch.text("3", slotWidth * 2.35f, hudY + 15);
    this.sketch.text("4", slotWidth * 2.85f, hudY + 15);
    this.sketch.text("5", slotWidth * 3.35f, hudY + 15);

  }

  public ProjectileType getSelectedProjectile() {
    return selectedProjectile;
  }

  public void setSelectedProjectile(ProjectileType selectedProjectile) { this.selectedProjectile = selectedProjectile; }


}
