package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

import java.util.Vector;

public class FrictionGenerator implements ForceGenerator {
    public static final Vector3 GRAVITY =
            new Vector3(0, 200, 0);

    public double staticFrictionCoefficient;
    public double cineticFrictionCoefficient;

    public FrictionGenerator(double staticFrictionCoefficient, double cineticFrictionCoefficient) {
        this.staticFrictionCoefficient = staticFrictionCoefficient;
        this.cineticFrictionCoefficient = cineticFrictionCoefficient;
    }

    public void applyForce(Particle particle) {
        if (particle.getLinearVelocity().equals(Vector3.ZERO_VECTOR))
            applyStaticFriction(particle);
        else applyCineticFriction(particle);
    }

    private void applyStaticFriction(Particle particle) {
        //Formule utilisée : F = u * N, avec N = m x g => a = u x g
        particle.setAcceleration(
                GRAVITY.scale(staticFrictionCoefficient)
        );
    }

    private void applyCineticFriction(Particle particle) {
        particle.setAcceleration(
                particle.getLinearVelocity().reverse().scale(cineticFrictionCoefficient)
        );
    }
}
