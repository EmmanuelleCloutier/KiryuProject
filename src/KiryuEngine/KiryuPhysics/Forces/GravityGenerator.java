package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Vector3;

import static KiryuEngine.KiryuPhysics.Forces.Force.GRAVITY;

public class GravityGenerator implements ForceGenerator {

    public void applyForce(Vector3 applyForceTo) {
        applyForceTo.add(GRAVITY);
    }
}
