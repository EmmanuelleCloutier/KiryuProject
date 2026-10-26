package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;


public class FrictionGenerator implements ForceGenerator {

    public FrictionGenerator() {

    }

    public void applyForce(RigidBody rigidBody) {
        Vector3 force = rigidBody.getAccumulatedForce();
        if (rigidBody.getLinearVelocity().equals(Vector3.ZERO_VECTOR))
            applyStaticFriction(force, rigidBody.getStaticFrictionCoefficient());
        else applyKineticFriction(force, rigidBody.getKineticFrictionCoefficient());

        rigidBody.getAccumulatedForce().set(force);
    }

    private void applyStaticFriction(
        Vector3 applyForceTo,
        double staticFrictionCoefficient
    ) {
        //Formule utilisée : F = u * N, avec N = m x g => a = u x g
        applyForceTo.add(
            new Vector3(applyForceTo)
                .reverse()
                .scale(staticFrictionCoefficient)
        );
    }

    private void applyKineticFriction(
        Vector3 applyForceTo,
        double kineticFrictionCoefficient
    ) {
        applyForceTo.add(
            new Vector3(applyForceTo)
                .reverse()
                .scale(kineticFrictionCoefficient)
        );
    }
}
