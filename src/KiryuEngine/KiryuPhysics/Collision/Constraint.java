package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;

public abstract class Constraint {

    protected Particle p1;
    protected Particle p2;

    protected float maxDistance;

    public Constraint(Particle p1, Particle p2, float maxDistance) {
        this.p1 = p1;
        this.p2 = p2;
        this.maxDistance = maxDistance;
    }

    public abstract boolean isColliding ();

    public Contact getContact() {
        return new Contact(p1, p2);
    }

    public Particle getParticle1() {
        return p1;
    }

    public Particle getParticle21() {
        return p2;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

}
