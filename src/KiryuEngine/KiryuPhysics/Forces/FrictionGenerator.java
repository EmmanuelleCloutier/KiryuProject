package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Vector3;

import static KiryuEngine.KiryuPhysics.Forces.Force.GRAVITY;

public class FrictionGenerator implements ForceGenerator {

    public double staticFrictionCoefficient;
    public double kineticFrictionCoefficient;

    public FrictionGenerator(double staticFrictionCoefficient, double kineticFrictionCoefficient) {
        this.staticFrictionCoefficient = staticFrictionCoefficient;
        this.kineticFrictionCoefficient = kineticFrictionCoefficient;
    }

    public void applyForce(Vector3 applyForceTo) {
        if (applyForceTo.equals(Vector3.ZERO_VECTOR))
            applyStaticFriction(applyForceTo);
        else applyKineticFriction(applyForceTo);
    }

    private void applyStaticFriction(Vector3 applyForceTo) {
        //Formule utilisée : F = u * N, avec N = m x g => a = u x g
        applyForceTo.add(
            GRAVITY.scale(staticFrictionCoefficient)
        );
    }

    private void applyKineticFriction(Vector3 applyForceTo) {
        applyForceTo.add(
            applyForceTo
                .reverse()
                .scale(kineticFrictionCoefficient)
        );
    }
}
