package Game.codeTP3;

import KiryuEngine.KiryuMath.Quaternion;
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
        Quaternion rotation,
        Projectile3DType ptype,
        Mesh mesh
    ) {

        this.rigidBody = new RigidBody(ptype.mass,
                pos,
                Vector3.ZERO_VECTOR.copy(),
                ptype.collider,
                ptype.inertiaMatrix,
                rotation);

        world.addRigidBody(this.rigidBody);

        this.mesh = mesh;
    }

    public Vector3 getPos() {
        return this.rigidBody.getPosition();
    }

    public void hit(Vector3 contact, Vector3 force) {
        addForce(force);
        this.rigidBody.addTorque(contact, force);
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
