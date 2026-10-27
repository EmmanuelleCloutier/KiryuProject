package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;

import static KiryuEngine.KiryuPhysics.Forces.Force.GRAVITY;

public class GravityGenerator implements ForceGenerator {

    public void applyForce(RigidBody rigidBody) {
        if (rigidBody.isStatic()) {
            return; // Static body
        }

        Vector3 gravityForce = new Vector3(GRAVITY)
            .scale(rigidBody.getMass());

        rigidBody.getAccumulatedForce().add(gravityForce);
    }

}
