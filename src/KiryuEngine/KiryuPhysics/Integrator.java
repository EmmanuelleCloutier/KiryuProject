package KiryuEngine.KiryuPhysics;

public class Integrator {

    /**
     * Integrates a rigidbody using Euler integration.
     *
     * @param rigidbody rigidbody to integrate
     * @param deltaTime time between frames
     */
    public static void integrate(
        RigidBody rigidbody,
        float deltaTime
    ) {

        // v1 = ((damping ^ dt) * v0) + (a * dt)

        Vector3 newVelocity =
            rigidbody
                .getLinearVelocity()
                .scale(
                    Math.pow(
                        rigidbody.getDamping(),
                        deltaTime
                    )
                )
                .add(
                    rigidbody
                        .getAccumulatedForce()
                        .scale(deltaTime)
                );


        rigidbody.setLinearVelocity(
            newVelocity
        );

        Vector3 newDirection = Vector3.getVectorNormal(
            rigidbody.getLinearVelocity()
        );

        rigidbody.setDirection(newDirection);

        // p1 = p0 + (v1 * dt)

        Vector3 newPosition =
            rigidbody
                .getPosition()
                .add(
                    newVelocity.scale(deltaTime)
                );


        rigidbody.setPosition(
            newPosition
        );


    }
}