package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Collision.SphereCollider;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuPhysics.World;

public class Projectile {

  private final RigidBody rigidBody;


  public Projectile(World world, ProjectileType type){
    this(
        world,
        type,
        Vector3.ZERO_VECTOR.copy(),
        Vector3.ZERO_VECTOR.copy()
    );
  }

  public Projectile(
      World world,
      ProjectileType type,
      Vector3 position,
      Vector3 velocity
  ){
    this.rigidBody = new RigidBody(
        type.mass,
        position,
        velocity,
        new SphereCollider(50.f)
    );

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

}
