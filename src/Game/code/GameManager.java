package Game.code;

import Game.code.bloc.BlockManager;
import Game.code.cannon.Cannon;
import Game.code.projectile.ProjectileRenderer;
import Game.code.projectile.ProjectileSystem;
import Game.code.projectile.ProjectileType;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import processing.core.PImage;

public class GameManager {

  private final PApplet sketch;

  public BlockManager blockManager;
  public ProjectileSystem projectileSystem;
  public HUD headUpDisplay;
  public Cannon cannon;

  private final float gameBottom;


  public GameManager(PApplet sketch){

    this.sketch = sketch;
    gameBottom = sketch.height - 170;

    //cree le system de projectile
    this.projectileSystem = new ProjectileSystem(
        new ProjectileRenderer(sketch)
    );

    //construction du manager de blocs
    this.blockManager = new BlockManager(sketch);


    //generation de structure
    blockManager.generateRandomStructure(
        sketch.width * 0.75f,
        gameBottom
    );

    headUpDisplay = new HUD(sketch);

    cannon = new Cannon(sketch,new Vector3(70,gameBottom - 60,0));
  }

  public void update(float deltaTime){

    this.projectileSystem.update(deltaTime);
    this.headUpDisplay.setDelta(deltaTime);
    detectCollisions();
  }

  public void draw(){
    this.sketch.background(100);
    this.blockManager.drawBlocks();
    this.headUpDisplay.draw();
    this.cannon.draw();
    this.projectileSystem.drawProjectiles();
  }

  private void detectCollisions(){
    if (this.projectileSystem.getProjectiles().isEmpty()) return;

    for (int i = this.projectileSystem.getProjectiles().size() - 1; i >= 0; --i) {
      Particle particle = this.projectileSystem.getProjectiles().get(i);

      Vector3 position = particle.getPosition();
      ProjectileType type = projectileSystem.getProjectileType(particle);

      boolean hitBlock =
          blockManager.checkProjectileCollision(
              particle,
              type.sprite.width,
              type.sprite.height
          );


      if (hitBlock) {
        projectileSystem.removeProjectile(particle);
        continue;
      }


      //verification si le projectile est out
      if (
          position.x < 0 ||
              position.x > this.sketch.width ||
              position.y < 0 ||
              position.y > gameBottom
      ) {

        projectileSystem.removeProjectile(particle);

      }
    }
  }

  public void setProjectileType(char key){
    if (key == '1') {
      this.headUpDisplay.setSelectedProjectile(ProjectileType.BALL);
      this.cannon.setSelectedProjectileType(ProjectileType.BALL);
    }

    if (key == '2') {
      this.headUpDisplay.setSelectedProjectile(ProjectileType.CANNON_BALL);
      this.cannon.setSelectedProjectileType(ProjectileType.CANNON_BALL);
    }

    if (key == '3') {
      this.headUpDisplay.setSelectedProjectile(ProjectileType.LASER);
      this.cannon.setSelectedProjectileType(ProjectileType.LASER);
    }

    if (key == '4') {
      this.headUpDisplay.setSelectedProjectile(ProjectileType.FIRE_BALL);
      this.cannon.setSelectedProjectileType(ProjectileType.FIRE_BALL);
    }

    if (key == '5'){
      this.headUpDisplay.setSelectedProjectile(ProjectileType.DUCK);
      this.cannon.setSelectedProjectileType(ProjectileType.DUCK);
    }
  }

  public void shoot(){
    cannon.shoot();
    this.projectileSystem.addProjectileAtPosition(
        this.headUpDisplay.getSelectedProjectile(),
        cannon.getProjectileStartPosition(),
        cannon.getLaunchVelocity()
    );
  }
}
