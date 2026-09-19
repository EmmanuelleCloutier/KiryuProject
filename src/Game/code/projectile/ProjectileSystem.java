package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Vector3;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * this class is the system used in the "game" it creates the bridge between the data
 * and the rendering system
 */
public class ProjectileSystem {

  private final ProjectileData projectileData;
  private final ProjectileRenderer projectileRenderer;

  /**
   *
   * @param projectileData the projectiles' data that the system will use
   * @param projectileRenderer the projectile renderer
   */
  public ProjectileSystem(ProjectileData projectileData, ProjectileRenderer projectileRenderer){
    this.projectileData = projectileData;
    this.projectileRenderer = projectileRenderer;
  }

  /**
   * just a basic movement method, this will be change later after we
   * created the euler integration
   */
  public void updatePosition(){

    for (int i = 0 ; i< this.projectileData.positions.size(); i++)
    {
      Vector3 currentPosition = this.projectileData.positions.get(i);
      Vector3 direction = this.projectileData.directions.get(i);
      double speed = this.projectileData.types.get(i).speed;

     this.projectileData.velocities.get(i)
          .set(direction.scale(speed));

      Vector3 newPosition = currentPosition
          .add(this.projectileData.velocities.get(i));


      this.projectileData.positions.get(i).set(newPosition);
    }
  }

  /**
   * this is where the map of different type of projectiles are mapped to all the
   * positions that projectile needs to be drawn
   */
  public void drawProjectiles(){

    HashMap<ProjectileType,ArrayList<Vector3>> spritePositions = new HashMap<>();

    for (int i = 0; i < this.projectileData.types.size(); i++){

      spritePositions
          .computeIfAbsent(this.projectileData.types.get(i), k -> new ArrayList<>())
          .add(this.projectileData.positions.get(i));
    }

    this.projectileRenderer.drawProjectileSprites(spritePositions);

  }

  /**
   * @param type the type of projectile to instantiate
   * @param pos the position where it should spawn
   * @param dir the direction where the projectile should be going
   * @return true if the data was all added with no error (safety measure)
   */
  public boolean addProjectile(ProjectileType type, Vector3 pos, Vector3 dir) {
    return this.projectileData.addProjectile(type, pos, dir);
  }

  /**
   * @param index remove a projectile at a specific index
   * @return true if the data was all removed with no error (safety measure)
   */
  public boolean removeProjectile(int index) {
    return this.projectileData.removeProjectile(index);
  }

  /**
   * this function may not be used very often since velocity must be calculated
   * changing it by hand might cause undefined behaviours
   *
   * @param index the index of the projectile, it can be any index in any list
   * in projectileData, since each index matches in all arrays.
   * @param newVelocity the new velocity of the projectile (velocity is not speed)
   * @return true if the data was modified successfully (safety measure)
   */
  public boolean setProjectileVelocity(int index, Vector3 newVelocity){
    return this.projectileData.setProjectileVelocity(index,newVelocity);
  }

  /**
   * @param index the index of the projectile, it can be any index in any list
   *    * in projectileData, since each index matches in all arrays.
   * @param newDirection the new direction of the projectile
   * @return true if the data was modified successfully (safety measure)
   */
  public boolean setProjectileDirection(int index, Vector3 newDirection){
    return this.projectileData.setProjectileDirection(index,newDirection);
  }

  /**
   * this function may not be used very often since positions must be calculated
   * changing it by hand might cause undefined behaviours
   *
   * @param index the index of the projectile, it can be any index in any list
   * in projectileData, since each index matches in all arrays.
   * @param newPosition the new direction of the projectile
   * @return true if the data was modified successfully (safety measure)
   */
  public boolean setProjectilePosition(int index, Vector3 newPosition){
    return this.projectileData.setProjectilePosition(index,newPosition);
  }

  /**
   * @param index the index of the projectile that we want to get its velocity
   * @return the projectile's velocity as a Vector3
   */
  public Vector3 getProjectileVelocity(int index){
    return this.projectileData.getProjectileVelocity(index);
  }

  /**
   * @param index the index of the projectile that we want to get its position
   * @return the projectile's position as a Vector3
   */
  public Vector3 getProjectilePosition(int index){
    return this.projectileData.getProjectilePosition(index);
  }

  /**
   * @param index the index of the projectile that we want to get its direction
   * @return the projectile's direction as a Vector3
   */
  public Vector3 getProjectileDirection(int index){
    return this.projectileData.getProjectileDirection(index);
  }

  /**
   * @return the number of projectile present in the scene
   */
  public int getProjectileCount(){
    return this.projectileData.projectileCount;
  }
}
