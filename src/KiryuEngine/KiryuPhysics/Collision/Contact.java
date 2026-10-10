package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class Contact {

    Particle particle1;
    Particle particle2;
    Vector3 normale;
    Vector3 contactPoint;

    public Contact(Particle particle1, Particle particle2, Vector3 normale) {

        this.particle1 = particle1;
        this.particle2 = particle2;
        this.normale = normale;
        this.contactPoint = particle1.getPosition().add(particle1.getAcceleration().mul(this.normale.normalize()));

    }

    public Particle getParticle1() {
        return particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }

    public Vector3 getContactPoint() {
        return contactPoint;
    }

    public Vector3 getNormale() {
        return normale;
    }
}
