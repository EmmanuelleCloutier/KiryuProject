package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Force;
import KiryuEngine.KiryuPhysics.Integrator;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * this class is the system used in the "game" it creates the bridge between the data
 * and the rendering system
 */
public class ProjectileSystem {


  private final ArrayList<Particle> projectiles;
  private final ArrayList<ProjectileType> projectileTypes;


  private final ProjectileRenderer projectileRenderer;

  /**
   *
   * @param projectileRenderer the projectile renderer
   */
  public ProjectileSystem(ProjectileRenderer projectileRenderer){
    this.projectileRenderer = projectileRenderer;
    this.projectiles = new ArrayList<>();
    this.projectileTypes = new ArrayList<>();
  }

  /**
   * just a basic movement method, this will be change later after we
   * created the euler integration
   */
  public void update(float deltaTime) {
    if (this.projectiles.isEmpty()) return;

    for (int i = this.projectiles.size() - 1; i >= 0; i--) {

      Particle particle = this.projectiles.get(i);

      if (this.projectileTypes.get(i) != ProjectileType.LASER){
        //appliquer la gravite a la particule
        Force.applyGravity(particle);
      }

      //mettre a jouer la vitesse et la position de la particule
      Integrator.integrate(
          particle,
          deltaTime
      );
    }
  }

  /**
   * this is where the map of different type of projectiles are mapped to all the
   * positions that projectile needs to be drawn
   */
  public void drawProjectiles(){

    if (this.projectiles.isEmpty()) return;

    ArrayList<Vector3>positions = new ArrayList<>();
    ArrayList<Vector3>directions = new ArrayList<>();


    for (int i = 0; i < this.projectiles.size(); i++){

      positions.add(i,projectiles.get(i).getPosition());
      directions.add(i,projectiles.get(i).getDirection());
    }

    this.projectileRenderer.drawProjectileSprites(projectileTypes,positions,directions);

  }

  /**
   * @param type the type of projectile to instantiate
   * @return true if the data was all added with no error (safety measure)
   */
  public boolean addProjectile(ProjectileType type) {

    return (
        this.projectiles.add(new Particle(type.mass)) &&
            this.projectileTypes.add(type)
    );
  }

  public boolean addProjectileAtPosition(ProjectileType type,Vector3 position, Vector3 velocity) {

    return (this.projectiles.add(
        new Particle(
            type.mass,
            position,
            velocity)
    )
        && this.projectileTypes.add(type)
    );
  }

  /**
   * @param particle remove a projectile
   * @return true if the data was all removed with no error (safety measure)
   */
  public boolean removeProjectile(Particle particle) {
    if (this.projectiles.isEmpty()) return false;

    int index = this.projectiles.indexOf(particle);
    return (
        this.projectiles.remove(index) != null
            && this.projectileTypes.remove(index) != null
    );

  }

  /**
   * this function may not be used very often since velocity must be calculated
   * changing it by hand might cause undefined behaviours
   *
   * @param index the index of the projectile,
   * @param newVelocity the new velocity of the projectile (velocity is not speed)
   * @return true if the data was modified successfully (safety measure)
   */
  public boolean setProjectileVelocity(int index, Vector3 newVelocity){
    if (index > projectiles.size()) return false;

    this.projectiles.get(index).setLinearVelocity(newVelocity);
    return true;
  }

  /**
   * @param index the index of the projectile, it can be any index in any list
   *    * in projectileData, since each index matches in all arrays.
   * @param newDirection the new direction of the projectile
   * @return true if the data was modified successfully (safety measure)
   */
  public boolean setProjectileDirection(int index, Vector3 newDirection){
    if (index > projectiles.size()) return false;

    this.projectiles.get(index).setDirection(newDirection);
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
    if (index > projectiles.size())return false;

    this.projectiles.get(index).setPosition(newPosition);
    return true;
  }

  /**
   * @param index the index of the projectile that we want to get its velocity
   * @return the projectile's velocity as a Vector3
   */
  public Vector3 getProjectileVelocity(int index){
    return this.projectiles.get(index).getLinearVelocity();
  }

  /**
   * @param index the index of the projectile that we want to get its position
   * @return the projectile's position as a Vector3
   */
  public Vector3 getProjectilePosition(int index){
    return this.projectiles.get(index).getPosition();
  }

  /**
   * @param index the index of the projectile that we want to get its direction
   * @return the projectile's direction as a Vector3
   */
  public Vector3 getProjectileDirection(int index){
    return this.projectiles.get(index).getDirection();
  }

  public ArrayList<Particle> getProjectiles() {
    return projectiles;
  }

  public ProjectileType getProjectileType(Particle particle){
    int index  = projectiles.indexOf(particle);
    return projectileTypes.get(index);
  }

}
