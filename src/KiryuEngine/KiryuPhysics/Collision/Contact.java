package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Forces.GravityGenerator;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class Contact {

    Particle particle;
    Vector3 normale;
    Vector3 contactPoint;

    public Contact(Particle particle, Particle particle2) {

        this.particle = particle;

        this.normale = particle2.getPosition().sub(particle.getPosition());
        this.contactPoint = particle.getPosition().add(
                Vector3.getVectorNormal(this.normale).scale(particle.getCollider().getSize()));

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

    public boolean isContactAtRest(float deltaTime) {

        Vector3 gravity = GravityGenerator.GRAVITY;

        gravity = gravity.scale(deltaTime);
        
        return Math.abs(gravity.dotProduct(this.normale)) >
                Math.abs(this.particle.getLinearVelocity().dotProduct(this.normale));
    }
}
