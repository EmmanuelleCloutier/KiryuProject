
package KiryuEngine.KiryuPhysics.Constraints;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;

public class Spring extends Constraint {

    private final double stiffness;
    private final double damping;

    public Spring(
        Collider first,
        Collider second,
        float restLength,
        double stiffness,
        double damping
    ) {
        super(first, second, restLength);

        this.stiffness = stiffness;
        this.damping = damping;
    }

    @Override
    public boolean isConstrained() {
        // Springs are evaluated continuously.
        return true;
    }

    public void applyForce() {

        RigidBody bodyA = first.getRigidBody();
        RigidBody bodyB = second.getRigidBody();

        Vector3 difference =
            bodyB.getPosition().sub(bodyA.getPosition());

        double distance = difference.getLength();

        if (distance < 0.0001) {
            return;
        }

        Vector3 direction =
            difference.scale(1.0 / distance);

        // Hooke's law.
        double displacement = distance - maxDistance;

        // Relative velocity along the spring.
        Vector3 relativeVelocity =
            bodyB.getLinearVelocity()
                .sub(bodyA.getLinearVelocity());

        double velocityAlongSpring =
            relativeVelocity.x * direction.x
            + relativeVelocity.y * direction.y
            + relativeVelocity.z * direction.z;

        double forceMagnitude =
            stiffness * displacement
            + damping * velocityAlongSpring;

        Vector3 springForce =
            direction.scale(forceMagnitude);

        
        // Convert spring forces to accelerations.
        Vector3 accelerationA =
            springForce.scale(bodyA.getInverseMass());

        Vector3 accelerationB =
            springForce.scale(-bodyB.getInverseMass());

        // Add spring acceleration to existing acceleration.
        bodyA.setForce(
            bodyA.getForce().add(accelerationA)
        );

        bodyB.setForce(
            bodyB.getForce().add(accelerationB)
        );

    }
}
