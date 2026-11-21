package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.Contact;

public abstract class Constraint {

    protected Collider first;
    protected Collider second;

    protected float maxDistance;

    protected double distance;
    protected Vector3 normal;

    public Constraint(Collider first, Collider second, float maxDistance) {
        this.first = first;
        this.second = second;
        this.maxDistance = maxDistance;
    }

    public abstract boolean isConstrained();

    public abstract void applyConstraint();

    public Contact getContact() {
        return new Contact(first, second);
    }

    public Collider getFirst() {
        return first;
    }

    public Collider getSecond() {
        return second;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

}
