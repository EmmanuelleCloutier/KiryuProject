package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;


public class GravityGenerator implements ForceGenerator {
    /**
     * the force applied by the generator by default it is (0,200,0)
     */
    private Vector3 gravity = new Vector3(0,200,0);
    /**
     * @return <font color="yellow">{@link Vector3}</font>
     * - the generator's gravity vector
     */
    public Vector3 getGravity() {
        return gravity;
    }
    /**
     * @param direction <font color="yellow">{@link Vector3}</font>
     * - the direction of the new gravity. it must be unit vector and
     * have only 1 parameter set. we do not allow angular gravity
     * @param force <font color="orange">{@code double}</font>
     * - the force this new gravity will apply
     */
    public void setGravity(Vector3 direction, double force) {
        if (direction.getLength() != 1.0f) return;
        if (direction.dotProduct(direction) != 1.0f)return;

        this.gravity = new Vector3(direction).scale(force);
    }
    /**
     * add the generator's gravity force to the RigidBody's
     * accumulated forces
     * @param rigidBody <font color="yellow">{@link RigidBody}</font>
     * - the RigidBody we apply the generator's gravity force to
     */
    public void applyForce(RigidBody rigidBody) {

        Vector3 gravityForce = new Vector3(this.gravity)
            .scale(rigidBody.getMass());

        rigidBody.getAccumulatedForce().add(gravityForce);
    }
}
