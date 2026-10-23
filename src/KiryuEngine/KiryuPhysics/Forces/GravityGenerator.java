package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.RigidBody;

import static KiryuEngine.KiryuPhysics.Forces.Force.GRAVITY;

public class GravityGenerator implements ForceGenerator {

    public void applyForce(RigidBody rigidBody) {
        rigidBody.getAccumulatedForce().add(GRAVITY);
    }

}
