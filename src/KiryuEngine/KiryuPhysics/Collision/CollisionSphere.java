package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public class CollisionSphere extends Collider {

    private double rayon;

    public CollisionSphere(double rayon, RigidBody rigidbody) {
        this.rayon = rayon;
        this.rigidBody = rigidbody;
    }

    public double getRayon() {
        return rayon;
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
    public double getSizeSquared() {
        return rayon * rayon;
    }


}
