package KiryuEngine.KiryuPhysics.Forces;

import KiryuEngine.KiryuPhysics.Collision.Contact;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuMath.Vector3;


public class FrictionGenerator implements ForceGenerator {

    public void applyForce(RigidBody rigidBody) {

    }

    /**
     * calculates and applies friction force to a pair of RigidBody
     * @param bodyA <font color="yellow">{@link RigidBody}</font>
     * - first RigidBody of the pair
     * @param bodyB <font color="yellow">{@link RigidBody}</font>
     * - second RigidBody of the pair
     * @param contact <font color="yellow">{@link Contact}</font>
     * - the contact between the two RigidBodies
     */
    public void applyForce(
        RigidBody bodyA,
        RigidBody bodyB,
        Contact contact
    ){

        double normalImpulse = contact.getNormalImpulse();

        double inverseMassSum = bodyA.getInverseMass() +
            bodyB.getInverseMass();

        Vector3 normal = contact.getNormal();

        Vector3 radiusA = new Vector3(contact.getContactPoint())
            .sub(bodyA.getCenterOfMassWorld());

        Vector3 radiusB = new Vector3(contact.getContactPoint())
            .sub(bodyB.getCenterOfMassWorld());

        // Relative velocity: B relative to A.
        Vector3 velocityA = new Vector3(bodyA.getLinearVelocity())
            .add(
                new Vector3(bodyA.getAngularVelocity())
                    .crossProduct(radiusA)
            );

        Vector3 velocityB = new Vector3(bodyB.getLinearVelocity())
            .add(
                new Vector3(bodyB.getAngularVelocity())
                    .crossProduct(radiusB)
            );

        Vector3 relativeVelocity = new Vector3(velocityB).sub(velocityA);

        // Remove the normal component to obtain tangential velocity.
        // this is the velocity tangent to the contact point
        double velocityAlongNormal =
            relativeVelocity.dotProduct(normal);

        if (velocityAlongNormal >= 0.0) {
            return; // Bodies are already separating
        }

        Vector3 tangent =
            new Vector3(relativeVelocity)
                .sub(new Vector3(normal).scale(velocityAlongNormal));

        double tangentLength = tangent.getLength();

        if (tangentLength < 1e-8 || normalImpulse <= 0.0) {
            return;
        }

        tangent.scale(1.0 / tangentLength);


        // Angular contribution.
        Vector3 angularA = bodyA.getInverseInertiaTensor()
            .multiplyVector(new Vector3(radiusA).crossProduct(tangent));

        Vector3 angularB = bodyB.getInverseInertiaTensor()
            .multiplyVector(new Vector3(radiusB).crossProduct(tangent));

        double angularEffect =
            angularA.crossProduct(radiusA).dotProduct(tangent)
                + angularB.crossProduct(radiusB).dotProduct(tangent);

        double effectiveInverseMass = inverseMassSum + angularEffect;

        if (effectiveInverseMass <= 0.0) {
            return;
        }

        double frictionImpulseMagnitude =
            -relativeVelocity.dotProduct(tangent) / effectiveInverseMass;

        // Average friction coefficients.
        double staticFriction = Math.sqrt(
            bodyA.getStaticFrictionCoefficient()
                * bodyB.getStaticFrictionCoefficient()
        );

        double kineticFriction = Math.sqrt(
            bodyA.getKineticFrictionCoefficient()
                * bodyB.getKineticFrictionCoefficient()
        );


        double maxStaticFriction = normalImpulse * staticFriction;

        Vector3 frictionImpulse;

        if (Math.abs(frictionImpulseMagnitude) <= maxStaticFriction) {

            // Static friction.
            frictionImpulse = new Vector3(tangent)
                .scale(frictionImpulseMagnitude);

        } else {

            // Kinetic friction.
            frictionImpulse = new Vector3(tangent)
                .scale(-normalImpulse * kineticFriction);
        }


        if (!bodyA.isStatic()) {

            bodyA.setLinearVelocity(
                new Vector3(bodyA.getLinearVelocity())
                    .sub(new Vector3(frictionImpulse).scale(bodyA.getInverseMass()))
            );

            Vector3 angularImpulseA = new Vector3(radiusA)
                .crossProduct(frictionImpulse)
                .scale(-1.0);

            bodyA.setAngularVelocity(
                new Vector3(bodyA.getAngularVelocity())
                    .add(bodyA.getInverseInertiaTensor()
                        .multiplyVector(angularImpulseA))
            );
        }

        if (!bodyB.isStatic()) {

            bodyB.setLinearVelocity(
                new Vector3(bodyB.getLinearVelocity())
                    .add(new Vector3(frictionImpulse).scale(bodyB.getInverseMass()))
            );

            Vector3 angularImpulseB = new Vector3(radiusB)
                .crossProduct(frictionImpulse);

            bodyB.setAngularVelocity(
                new Vector3(bodyB.getAngularVelocity())
                    .add(bodyB.getInverseInertiaTensor()
                        .multiplyVector(angularImpulseB))
            );
        }
    }
}
