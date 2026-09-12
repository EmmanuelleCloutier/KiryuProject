package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.ProjectileRenderer;

public class ProjectileSystem {

  private final ProjectileData projectileData;
  private final ProjectileRenderer projectileRenderer;

  public ProjectileSystem(ProjectileData projectileData, ProjectileRenderer projectileRenderer){
    this.projectileData = projectileData;
    this.projectileRenderer = projectileRenderer;
  }

  public void updatePosition(float deltaTime){

    for (int i = 0 ; i< this.projectileData.positions.size(); i++)
    {
      Vector3 currentPosition = this.projectileData.positions.get(i);
      Vector3 direction = this.projectileData.directions.get(i);
      ProjectileType type = this.projectileData.types.get(i);

     this.projectileData.velocities.get(i)
          .set(direction.scale(type.speed *deltaTime));

      Vector3 newPosition = currentPosition
          .add(this.projectileData.velocities.get(i));

      this.projectileData.positions.get(i).set(newPosition);
    }
  }

  public void drawProjectiles(){
    for (int i = 0; i < this.projectileData.positions.size(); i++){
      this.projectileRenderer.drawSprite(
          this.projectileData.types.get(i).sprite.getSpritePath(),
          this.projectileData.positions.get(i)
      );
    }
  }

  public void setupProjectileRenderer(){
    for (ProjectileType type : this.projectileData.types
         ) {
      this.projectileRenderer.addSprite(type.sprite);
    }

    this.projectileRenderer.loadSprites();
  }


  public boolean addProjectile(ProjectileType type, Vector3 pos, Vector3 dir) {
    return this.projectileData.addProjectile(type, pos, dir);
  }

  public boolean removeProjectile(int index) {
    return this.projectileData.removeProjectile(index);
  }

  public boolean setProjectileVelocity(int index, Vector3 newVelocity){
    return this.projectileData.setProjectileVelocity(index,newVelocity);
  }

  public boolean setProjectileDirection(int index, Vector3 newDirection){
    return this.projectileData.setProjectileDirection(index,newDirection);
  }

  public boolean setProjectilePosition(int index, Vector3 newPosition){
    return this.projectileData.setProjectilePosition(index,newPosition);
  }

  public Vector3 getProjectileVelocity(int index){
    return this.projectileData.getProjectileVelocity(index);
  }

  public Vector3 getProjectilePosition(int index){
    return this.projectileData.getProjectilePosition(index);
  }

  public Vector3 getProjectileDirection(int index){
    return this.projectileData.getProjectileDirection(index);
  }





}
