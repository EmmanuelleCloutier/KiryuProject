package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public class SphereCollider extends Collider {

    private double rayon;

    public SphereCollider(double rayon) {
        this.rayon = rayon;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }

    @Override
    public void onCollision(Collider other) {
        this.getRigidBody().onCollision();
    }

    @Override
    public double getSize() { return rayon; }

    @Override
    public void linkRigidBody(RigidBody rigidbody) {
        if (this.rigidBody != null) return;

        this.rigidBody = rigidbody;
    }

}
