package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;
import org.jetbrains.annotations.NotNull;


public class Contact {

    /**
     * The collider of the other RigidBody the owner of the contact
     * is colliding with
     */
    private final Collider other;
    /**
     * the contact point's normal
     */
    private final Vector3 normal = new Vector3();
    /**
     * the position where the two RigidBody collided
     */
    private final Vector3 contactPoint = new Vector3();
    /**
     * the strength at which the two RigidBody Collided
     */
    private double normalImpulse;
    /**
     * the distance between the two RigidBody at impact
     */
    private double distance;

    /**
     * constructor
     * @param owner <font color="yellow">{@link Collider}</font>
     * - The owner of the contact
     * @param other <font color="yellow">{@link Collider}</font>
     * - the collider the owner collided with
     */
    public Contact(@NotNull Collider owner,@NotNull Collider other) {

        this.other = other;

        Vector3 difference = new Vector3(other.getRigidBody().getPosition())
            .sub(owner.getRigidBody().getPosition());

        this.distance = difference.getLength();

        // Avoid an undefined collision normal.
        if (this.distance < 1e-8) {
            this.normal.set(0,1,0);
        }

        this.normal.set(difference);
        this.normal.scale(1.0/this.distance);

        this.contactPoint.set(
            new Vector3(owner.getRigidBody().getPosition())
                .add(new Vector3(normal).scale(owner.getSize()))
        );
    }
    /**
     * @return <font color="yellow">{@link Collider}</font>
     * - the collider with whom the owner collided with
     */
    public Collider getOther() {
        return other;
    }
    /**
     * @return <font color="yellow">{@link Vector3}</font>
     * - the position where the collision occurred
     */
    public Vector3 getContactPoint() {
        return contactPoint;
    }
    /**
     * @return <font color="yellow">{@link Vector3}</font>
     * - the normal vector of the contact
     */
    public Vector3 getNormal() {
        return normal;
    }
    /**
     * @return <font color="orange">{@code double}</font>
     * - the strength of the collision
     */
    public double getNormalImpulse() {
        return normalImpulse;
    }
    /**
     * @return <font color="orange">{@code double}</font>
     * - the distance between the two collider
     */
    public double getDistance() {
        return distance;
    }
    /**
     * @param normalImpulse <font color="orange">{@code double}</font>
     * - set's the strength of the collision
     */
    public void setNormalImpulse(double normalImpulse) {
        this.normalImpulse = normalImpulse;
    }
}
