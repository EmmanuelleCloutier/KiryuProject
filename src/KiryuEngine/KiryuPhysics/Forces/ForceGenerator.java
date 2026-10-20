package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Vector3;

public interface ForceGenerator {
    void applyForce(Vector3 applyForceTo);
}