package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class Contact {

    Particle particle;
    Vector3 normale;
    Vector3 contactPoint;

    public Contact(Particle particle, Particle particle2) {

        this.particle = particle;

        this.normale = particle.getPosition().sub(particle2.getPosition());
        this.contactPoint = particle.getPosition().add(
                this.normale.normalize().scale(particle.getCollider().getSize()));

    }

    public Particle getParticle() {
        return particle;
    }

    public Vector3 getContactPoint() {
        return contactPoint;
    }

    public Vector3 getNormale() {
        return normale;
    }

    public static boolean isContactAtRest(float deltaTime) {

        return false;
    }
}
