package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.RigidBody;

public class Rod extends Constraint {

    public Rod(Collider first, Collider second, float maxDistance) {
        super(first, second, maxDistance);
    }

    @Override
    public boolean isConstrained() {
        Vector3 delta = new Vector3(second.getRigidBody().getPosition())
            .sub(first.getRigidBody().getPosition());

        this.distance = delta.getLength();
        if (distance == 0.0) {
            return false;
        }

        this.normal = new Vector3(delta)
            .scale(1.0 / distance);

        return  Math.abs(this.distance - this.maxDistance) > 1e-08;
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

        Vector3 correction = new Vector3(normal)
            .scale(Math.abs(this.distance - this.maxDistance) / inverseMassSum);

        bodyA.getPosition().add(
            new Vector3(correction).scale(bodyA.getInverseMass())
        );

        bodyB.getPosition().sub(
            new Vector3(correction).scale(bodyB.getInverseMass())
        );

        Vector3 relativeVelocity =
            new Vector3(bodyB.getLinearVelocity())
                .sub(bodyA.getLinearVelocity());

        double separatingVelocity  =
            relativeVelocity.dotProduct(this.normal);

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
    
}
