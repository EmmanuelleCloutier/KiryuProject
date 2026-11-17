package Game.codeTP3;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.World;

public class Projectile3D {

    private RigidBody rigidBody;

    public Projectile3D(World world, double mass, Vector3 pos, Collider collider) {

        this.rigidBody = new RigidBody(mass,
                pos,
                Vector3.ZERO_VECTOR.copy(),
                collider);

        world.addRigidBody(this.rigidBody);

    }

    public Vector3 getPos() {
        return this.rigidBody.getPosition();
    }

    public void removeProjectile(World world) {
        world.removeRigidBody(this.rigidBody);
    }

}
