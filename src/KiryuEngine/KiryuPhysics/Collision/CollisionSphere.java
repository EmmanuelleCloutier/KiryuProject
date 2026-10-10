package KiryuEngine.KiryuPhysics.Collision;

public class CollisionSphere extends Collider {

    private double rayon;

    public CollisionSphere(double rayon) {
        this.rayon = rayon;
    }

    public double getRayon() {
        return rayon;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }
}
