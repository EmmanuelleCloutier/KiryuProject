package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Collision.SphereCollider;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuPhysics.World;

public class Projectile {

  private final RigidBody rigidBody;

  private final ProjectileType type;

  private final ProjectileSystem projectileSystem;

  private boolean destroyed = false;

  public Projectile(World world,ProjectileSystem projectileSystem, ProjectileType type){
    this(
        world,
        projectileSystem,
        type,
        Vector3.ZERO_VECTOR.copy(),
        Vector3.ZERO_VECTOR.copy()
    );
  }

  public Projectile(
      World world,
      ProjectileSystem projectileSystem,
      ProjectileType type,
      Vector3 position,
      Vector3 velocity
  ){
    this.projectileSystem = projectileSystem;
    this.type = type;

    this.rigidBody = new RigidBody(
        type.mass,
        position,
        velocity,
        new SphereCollider(50.f)
    );
    this.rigidBody.setCollisionListener(this::onCollision);

    if (type == ProjectileType.LASER){
      this.rigidBody.setAffectedByGravity(false);
    }

    world.addRigidBody(this.rigidBody);

  }


  public Vector3 getPosition(){
    return this.rigidBody.getPosition();
  }

  public Vector3 getDirection(){
    return this.rigidBody.getDirection();
  }

  public Vector3 getLinearVelocity(){
    return this.rigidBody.getLinearVelocity();
  }

  public ProjectileType getType() {
    return type;
  }

  public RigidBody getRigidBody() {
    return rigidBody;
  }


  private void onCollision(RigidBody other) {
    if (!destroyed) {
      destroyed = true;
      other.onCollision(this.rigidBody);
      this.rigidBody.setMarkForDestruction(true);
    }
  }

  public boolean isDestroyed() {
    return destroyed;
  }

}
