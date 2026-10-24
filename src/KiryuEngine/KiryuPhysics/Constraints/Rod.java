package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Vector3;

public class Rod extends Constraint {

    public Rod(Collider first, Collider second, float maxDistance) {
        super(first, second, maxDistance);
    }

    @Override
    public boolean isConstrained() {
        return first.getRigidBody()
            .getPosition()
            .distance(
                second.getRigidBody().getPosition()
            ) != maxDistance;
    }

    @Override
    public void applyConstraint() {

    }


}
