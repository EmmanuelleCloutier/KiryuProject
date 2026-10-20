package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Vector3;

import static KiryuEngine.KiryuPhysics.Forces.Force.GRAVITY;

public class FrictionGenerator implements ForceGenerator {

    public double staticFrictionCoefficient;
    public double kineticFrictionCoefficient;

    public FrictionGenerator(double staticFrictionCoefficient, double cineticFrictionCoefficient) {
        this.staticFrictionCoefficient = staticFrictionCoefficient;
        this.kineticFrictionCoefficient = cineticFrictionCoefficient;
    }

    public void applyForce(Vector3 applyForceTo) {
        if (applyForceTo.equals(Vector3.ZERO_VECTOR))
            applyStaticFriction(applyForceTo);
        else applyKineticFriction(applyForceTo);
    }

    private void applyStaticFriction(Vector3 applyForceTo) {
        //Formule utilisée : F = u * N, avec N = m x g => a = u x g
        applyForceTo.add(GRAVITY.scale(staticFrictionCoefficient));
    }

    private void applyKineticFriction(Vector3 applyForceTo) {
            applyForceTo.reverse().scale(kineticFrictionCoefficient);
    }
}
