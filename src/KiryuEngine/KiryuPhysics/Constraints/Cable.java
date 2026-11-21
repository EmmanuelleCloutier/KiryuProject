package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.RigidBody;

public class Cable extends Constraint {
    private double distance;
    private Vector3 normal;

    public Cable(Collider first, Collider second, float maxDistance) {
        super(first, second, maxDistance);
    }

    @Override
    public boolean isConstrained() {
        Vector3 delta = new Vector3(second.getRigidBody().getPosition())
            .sub(first.getRigidBody().getPosition());

        this.distance = delta.getLength();

        this.normal = new Vector3(delta)
            .scale(1.0 / distance);

        return  this.distance > this.maxDistance;
    }

    @Override
    public void applyConstraint() {
        RigidBody bodyA = first.getRigidBody();
        RigidBody bodyB = second.getRigidBody();
        if (bodyA == null || bodyB == null) {
            return;
        }

        double inverseMassSum =
            bodyA.getInverseMass() +
                bodyB.getInverseMass();

        if (inverseMassSum == 0.0) {
            return;
        }

        Vector3 radiusA = new Vector3(bodyA.getCenterOfMass());

        Vector3 radiusB = new Vector3(bodyB.getCenterOfMass());

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

        double separatingVelocity  =
            relativeVelocity.dotProduct(normal);

        if (separatingVelocity  >= 0.0) {
            // Bodies are already separating
            double impulseMagnitude =
                separatingVelocity / inverseMassSum;

            Vector3 impulse = new Vector3(normal)
                .scale(impulseMagnitude);

            bodyA.getLinearVelocity().add(
                new Vector3(impulse).scale(bodyA.getInverseMass())
            );

            bodyB.getLinearVelocity().sub(
                new Vector3(impulse).scale(bodyB.getInverseMass())
            );
        }

        double error = this.distance - this.maxDistance;

        Vector3 correction = new Vector3(normal)
            .scale(error / inverseMassSum);

        bodyA.getPosition().add(
            new Vector3(correction).scale(bodyA.getInverseMass())
        );

        bodyB.getPosition().sub(
            new Vector3(correction).scale(bodyB.getInverseMass())
        );

    }
}
