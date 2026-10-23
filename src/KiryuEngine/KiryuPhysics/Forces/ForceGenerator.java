package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.RigidBody;

public interface ForceGenerator {
    void applyForce(RigidBody rigidBody);
}