package Game.code.projectile;

import java.util.ArrayList;
import KiryuEngine.KiryuPhysics.Vector3;


/**
 * this class follows a data oriented design making calculating physic object
 * easier and faster because we reduce the number of cache misses when doing
 * large quantity of operations
 */
@Deprecated
public class ProjectileData {

  ArrayList<Vector3> positions;
  ArrayList<Vector3> velocities;
  ArrayList<Vector3> directions;
  ArrayList<ProjectileType> types;
  public int projectileCount = 0;

  public ProjectileData(){
    positions = new ArrayList<>();
    velocities = new ArrayList<>();
    directions = new ArrayList<>();
    types = new ArrayList<>();
  }

  /**
   * @param type the type of projectile to instantiate
   * @param pos the position where it should spawn
   * @param dir the direction where the projectile should be going
   * @return true if the data was all added with no error (safety measure)
   */
  public boolean addProjectile(ProjectileType type, Vector3 pos, Vector3 dir){
    projectileCount++;
    return types.add(type) &&
           velocities.add(new Vector3()) &&
           positions.add(pos) &&
           directions.add(dir);
  }

  /**
   * @param index remove a projectile at a specific index
   * @return true if the data was all removed with no error (safety measure)
   */
  public boolean removeProjectile(int index){
    if (index > types.size()) return false;

    projectileCount--;
    return (types.remove(index) != null &&
            positions.remove(index) != null &&
            velocities.remove(index) != null &&
            directions.remove(index) != null);
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
    if (index >  this.velocities.size()) return false;
    this.velocities.get(index).set(newVelocity);
    return true;
  }

  /**
   * @param index the index of the projectile, it can be any index in any list
   *    * in projectileData, since each index matches in all arrays.
   * @param newDirection the new direction of the projectile
   * @return true if the data was modified successfully (safety measure)
   */
  public boolean setProjectileDirection(int index, Vector3 newDirection){
    if (index >  this.directions.size()) return false;
    this.directions.get(index).set(newDirection);
    return true;
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
    if (index >  this.positions.size()) return false;
    this.positions.get(index).set(newPosition);
    return true;
  }

  /**
   * @param index the index of the projectile that we want to get its velocity
   * @return the projectile's velocity as a Vector3
   */
  public Vector3 getProjectileVelocity(int index){
    if (index > this.velocities.size()) return Vector3.ZERO_VECTOR;
    return this.velocities.get(index);
  }

  /**
   * @param index the index of the projectile that we want to get its position
   * @return the projectile's position as a Vector3
   */
  public Vector3 getProjectilePosition(int index){
    if (index > this.positions.size()) return Vector3.ZERO_VECTOR;
    return this.positions.get(index);
  }

  /**
   * @param index the index of the projectile that we want to get its direction
   * @return the projectile's direction as a Vector3
   */
  public Vector3 getProjectileDirection(int index){
    if (index > this.directions.size()) return Vector3.ZERO_VECTOR;
    return this.directions.get(index);
  }
}
