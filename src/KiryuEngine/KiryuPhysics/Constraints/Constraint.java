package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.Contact;

public abstract class Constraint {

    protected Collider first;
    protected Collider second;

    protected float maxDistance;

    public Constraint(Collider first, Collider second, float maxDistance) {
        this.first = first;
        this.second = second;
        this.maxDistance = maxDistance;
    }

    public abstract boolean isConstrained();

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
