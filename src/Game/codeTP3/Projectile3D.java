package Game.codeTP3;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.ThreeDimentional.Mesh;
import KiryuEngine.KiryuRendering.ThreeDimentional.MeshRenderer;

public class Projectile3D {

    private final Mesh mesh;
    private final RigidBody rigidBody;

    public Projectile3D(
        World world,
        Vector3 pos,
        Projectile3DType ptype,
        Mesh mesh
    ) {

        this.rigidBody = new RigidBody(ptype.mass,
                pos,
                Vector3.ZERO_VECTOR.copy(),
                ptype.collider);

        world.addRigidBody(this.rigidBody);

        this.mesh = mesh;
    }

    public Vector3 getPos() {
        return this.rigidBody.getPosition();
    }

    public void addForce(Vector3 force) {
        rigidBody.getAccumulatedForce().add(force);
    }

    public void removeProjectile(World world) {
        world.removeRigidBody(this.rigidBody);
    }

    public void drawProjectile(MeshRenderer meshRenderer) {

        meshRenderer.drawMesh(
                this.mesh,
                this.rigidBody.getPosition(),
                this.rigidBody.getQRotation()
        );
    }
}
