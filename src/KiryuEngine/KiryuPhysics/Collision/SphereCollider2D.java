package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public class SphereCollider2D extends Collider {

    private double rayon;

    public SphereCollider2D(double rayon) {
        this.rayon = rayon;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }

    @Override
    public double getSize() { return rayon; }

    @Override
    public void linkRigidBody(RigidBody rigidbody) {
        if (this.rigidBody != null) return;

        this.rigidBody = rigidbody;
    }

}
