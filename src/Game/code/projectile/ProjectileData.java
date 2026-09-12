package Game.code.projectile;

import java.util.ArrayList;
import KiryuEngine.KiryuPhysics.Vector3;

public class ProjectileData {

  ArrayList<Vector3> positions;
  ArrayList<Vector3> velocities;
  ArrayList<Vector3> directions;
  ArrayList<ProjectileType> types;

  public ProjectileData(){
    positions = new ArrayList<Vector3>();
    velocities = new ArrayList<Vector3>();
    directions = new ArrayList<Vector3>();
    types = new ArrayList<ProjectileType>();
  }

  public boolean addProjectile(ProjectileType type, Vector3 pos, Vector3 dir){
    return types.add(type) &&
           velocities.add(Vector3.ZERO_VECTOR) &&
           positions.add(pos) &&
           directions.add(dir);
  }

  public boolean removeProjectile(int index){
    if (index > types.size()) return false;

    return (types.remove(index) != null &&
            positions.remove(index) != null &&
            velocities.remove(index) != null &&
            directions.remove(index) != null);
  }

  public boolean setProjectileVelocity(int index, Vector3 newVelocity){
    if (index >  this.velocities.size()) return false;
    this.velocities.get(index).set(newVelocity);
    return true;
  }

  public boolean setProjectileDirection(int index, Vector3 newDirection){
    if (index >  this.directions.size()) return false;
    this.directions.get(index).set(newDirection);
    return true;
  }

  public boolean setProjectilePosition(int index, Vector3 newPosition){
    if (index >  this.positions.size()) return false;
    this.positions.get(index).set(newPosition);
    return true;
  }

  public Vector3 getProjectileVelocity(int index){
    if (index > this.velocities.size()) return Vector3.ZERO_VECTOR;
    return this.velocities.get(index);
  }

  public Vector3 getProjectilePosition(int index){
    if (index > this.positions.size()) return Vector3.ZERO_VECTOR;
    return this.positions.get(index);
  }

  public Vector3 getProjectileDirection(int index){
    if (index > this.directions.size()) return Vector3.ZERO_VECTOR;
    return this.directions.get(index);
  }



}
