package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuMath.Vector3;
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
     * Bras de levier (du centre de masse au point d'application de la force).
     * Sert au calcul du torque.
     */
    private final Vector3 leverArm = new Vector3();
    /**
     * Force exercée sur le point de contact par l'objet avec lequel on est entré en collision.
     * Sert au calcul du torque.
     */
    private final Vector3 impulseForce = new Vector3();

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

        // Calcul du bras de levier (le centre de masse soustrait au point de contact)
        this.leverArm
                .add(this.contactPoint)
                .sub(owner.getRigidBody().getCenterOfMassWorld());
        // Récupère la force exercée par l'autre objet au moment de la collision.
        this.impulseForce.set(this.getOther().getRigidBody().getAccumulatedForce());
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
     * Renvoie le bras de levier.
     * @return Vecteur, du centre de masse au point de contact.
     */
    public Vector3 getLeverArm() {
        return leverArm.copy();
    }

    /**
     * Renvoie la force exercée par l'autre objet sur le point de contact.
     * @return Vecteur.
     */
    public Vector3 getImpulseForce() {
        return impulseForce.copy();
    }
    /**
     * @param normalImpulse <font color="orange">{@code double}</font>
     * - set's the strength of the collision
     */
    public void setNormalImpulse(double normalImpulse) {
        this.normalImpulse = normalImpulse;
    }
}
