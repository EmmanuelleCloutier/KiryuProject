package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Vector3;
import org.jetbrains.annotations.NotNull;


public class Contact {

    private final Collider other;
    private final Vector3 normal = new Vector3();
    private final Vector3 contactPoint = new Vector3();

    private double normalImpulse;

    private double distance;

    public Contact(@NotNull Collider self,@NotNull Collider other) {

        this.other = other;

        Vector3 difference = new Vector3(other.getRigidBody().getPosition())
            .sub(self.getRigidBody().getPosition());

        this.distance = difference.getLength();

        // Avoid an undefined collision normal.
        if (this.distance < 1e-8) {
            this.normal.set(0,1,0);
        }

        this.normal.set(difference);
        this.normal.scale(1.0/this.distance);

        this.contactPoint.set(
            new Vector3(self.getRigidBody().getPosition())
                .add(new Vector3(normal).scale(self.getSize()))
        );
    }

    public Collider getOther() {
        return other;
    }

    public Vector3 getContactPoint() {
        return contactPoint;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public double getNormalImpulse() {
        return normalImpulse;
    }

    public double getDistance() {
        return distance;
    }


    public void setNormalImpulse(double normalImpulse) {
        this.normalImpulse = normalImpulse;
    }
}
