package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class GravityGenerator implements ForceGenerator {
    public static final Vector3 GRAVITY =
            new Vector3(0, 200, 0);

    public void applyForce(Particle particle) {
        particle.setAcceleration(GRAVITY);
    }
}
