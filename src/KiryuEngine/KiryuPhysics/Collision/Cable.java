package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;

public class Cable extends Constraint {

    public Cable(Particle p1, Particle p2, float maxDistance) {
        super(p1, p2, maxDistance);
    }

    @Override
    public boolean isColliding() {
        return p1.getPosition().distance(p2.getPosition()) > maxDistance;
    }
}
