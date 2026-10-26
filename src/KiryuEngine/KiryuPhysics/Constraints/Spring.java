
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
        //spring continuously apply force between connected bodies
        return true;
    }

    @Override
    public void applyConstraint() {

        RigidBody bodyA = first.getRigidBody();
        RigidBody bodyB = second.getRigidBody();

        //calculate the distance between the two connected bodies
        Vector3 difference =
            new Vector3(bodyB.getPosition()).sub(bodyA.getPosition());

        double distance = difference.getLength();

        //avoid division by zero when both bodies share a position
        if (distance < 0.0001) {
            return;
        }

        Vector3 direction =
            new Vector3(difference).scale(1.0 / distance);

        // Hooke's law : displacement from the spring resting lenght
        double displacement = distance - maxDistance;

        //calculate the relative velocity between both bodie 
        Vector3 relativeVelocity =
            new Vector3(bodyB.getLinearVelocity())
                .sub(bodyA.getLinearVelocity());

        //project the relative velocity onto the spring direction
        double velocityAlongSpring =
            relativeVelocity.x * direction.x
            + relativeVelocity.y * direction.y
            + relativeVelocity.z * direction.z;

        //combine spring stiffness and damping forces
        double forceMagnitude =
            stiffness * displacement
            + damping * velocityAlongSpring;

        Vector3 springForce =
            new Vector3(direction).scale(forceMagnitude);

        
        //convert the spring into acceleration
        Vector3 accelerationA =
            new Vector3(springForce).scale(bodyA.getInverseMass());

        Vector3 accelerationB =
            new Vector3(springForce).scale(-bodyB.getInverseMass());

        //apply equal and opposite contributions to the both bodies

        bodyA.getAccumulatedForce().add(accelerationA);
        bodyB.getAccumulatedForce().add(accelerationB);

    }
}
