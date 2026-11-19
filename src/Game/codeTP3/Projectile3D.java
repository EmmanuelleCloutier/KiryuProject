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
        double mass,
        Vector3 pos,
        Collider collider,
        Mesh mesh
    ) {

        this.rigidBody = new RigidBody(mass,
                pos,
                Vector3.ZERO_VECTOR.copy(),
                collider);

        world.addRigidBody(this.rigidBody);

        this.mesh = mesh;
    }

    public Vector3 getPos() {
        return this.rigidBody.getPosition();
    }

    public void removeProjectile(World world) {
        world.removeRigidBody(this.rigidBody);
    }

    public void drawProjectile(MeshRenderer meshRenderer){
        meshRenderer.drawMesh(
            this.mesh,
            this.rigidBody.getPosition(),
            this.rigidBody.getQRotation()
        );
    }
}
