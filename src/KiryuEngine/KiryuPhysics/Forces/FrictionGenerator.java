package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Collision.Contact;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;


public class FrictionGenerator implements ForceGenerator {

    /**
     * add the generator's gravity force to the RigidBody's
     * accumulated forces
     * @param rigidBody <font color="yellow">{@link RigidBody}</font>
     * - the RigidBody we apply the generator's gravity force to
     */
    public void applyForce(RigidBody rigidBody) {

    }

    public void applyForce(RigidBody bodyA, RigidBody bodyB, Contact contact){

        double normalImpulse = contact.getNormalImpulse();
        double inverseMassSum = bodyA.getInverseMass() +
            bodyB.getInverseMass();
        Vector3 normal = contact.getNormal();

        // Relative velocity: B relative to A.
        Vector3 relativeVelocity =
            new Vector3(bodyB.getLinearVelocity())
                .sub(bodyA.getLinearVelocity());

        // Remove the normal component to obtain tangential velocity.
        // this is the velocity tangent to the contact point
        double velocityAlongNormal =
            relativeVelocity.dotProduct(normal);

        Vector3 tangent =
            new Vector3(relativeVelocity)
                .sub(new Vector3(normal).scale(velocityAlongNormal));

        double tangentLength = tangent.getLength();

        if (tangentLength < 1e-8 || normalImpulse <= 0.0) {
            return;
        }

        tangent.scale(1.0 / tangentLength);

        //calculate the impulse needed to stop the sliding motion
        double tangentImpulseMagnitude =
            -relativeVelocity.dotProduct(tangent) / inverseMassSum;

        //combine friction coefficients
        double staticFriction = Math.sqrt(
            bodyA.getStaticFrictionCoefficient()
                * bodyB.getStaticFrictionCoefficient()
        );

        double kineticFriction = Math.sqrt(
            bodyA.getKineticFrictionCoefficient()
                * bodyB.getKineticFrictionCoefficient()
        );

        //the impulse needed to break over static friction
        double maxStaticImpulse = staticFriction * normalImpulse;

        //if the tangent velocity is greater than the static friction
        if (Math.abs(tangentImpulseMagnitude) > maxStaticImpulse) {
            // this means the bodies are sliding:
            // clamp to the kinetic friction.
            tangentImpulseMagnitude =
                // we get the direction on the velocity and inverse it
                // either 1, 0 or -1, then we
                -Math.signum(relativeVelocity.dotProduct(tangent))
                    * kineticFriction * normalImpulse;
        }

        Vector3 frictionImpulse =
            new Vector3(tangent).scale(tangentImpulseMagnitude);


        if (!bodyA.isStatic()) {
            bodyA.setLinearVelocity(
                new Vector3(bodyA.getLinearVelocity())
                    .sub(new Vector3(frictionImpulse)
                        .scale(bodyA.getInverseMass()))
            );
        }

        if (!bodyB.isStatic()) {
            bodyB.setLinearVelocity(
                new Vector3(bodyB.getLinearVelocity())
                    .add(new Vector3(frictionImpulse)
                        .scale(bodyB.getInverseMass()))
            );
        }
    }
}
