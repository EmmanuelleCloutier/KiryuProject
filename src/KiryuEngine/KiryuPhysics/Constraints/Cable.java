package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuPhysics.Collision.Collider;

public class Cable extends Constraint {

    public Cable(Collider first, Collider second, float maxDistance) {
        super(first, second, maxDistance);
    }

    @Override
    public boolean isConstrained() {
        return first.getRigidBody()
            .getPosition()
            .distance(
                second.getRigidBody().getPosition()
            ) > maxDistance;
    }
}
