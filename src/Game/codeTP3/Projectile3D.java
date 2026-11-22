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
    private Vector3 referencePoint; // point utilisé pour dessiner la trajectoire du point de contact

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
        addForce(force.scale(400));
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

    /**
     * Initialise le point de référence.
     * @param worldPosition Coordonnées globales du point de référence
     */
    public void setReferencePoint(Vector3 worldPosition) {
        referencePoint = new Vector3(worldPosition).sub(getPos());

        referencePoint = rigidBody.getQRotation().inverse().rotate(referencePoint);
    }

    /**
     * Renvoie la position globale du point de référence selon la position et la rotation du projectile.
     * @return Coordonnées du point de référence.
     */
    public Vector3 getReferencePoint() {
        return new Vector3(getPos()).add(rigidBody.getQRotation().rotate(referencePoint));
    }
}
