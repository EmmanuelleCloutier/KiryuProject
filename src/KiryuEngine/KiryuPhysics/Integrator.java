package KiryuEngine.KiryuPhysics;

public class Integrator {

    /**
     * Integrates a particle using Euler integration.
     *
     * @param particle particle to integrate
     * @param deltaTime time between frames
     */
    public static void integrate(
        Particle particle,
        float deltaTime
    ) {

        // v1 = ((damping ^ dt) * v0) + (a * dt)

        Vector3 newVelocity =
            particle
                .getLinearVelocity()
                .scale(
                    Math.pow(
                        particle.getDamping(),
                        deltaTime
                    )
                )
                .add(
                    particle
                        .getAcceleration()
                        .scale(deltaTime)
                );


        particle.setLinearVelocity(
            newVelocity
        );

        Vector3 newDirection = Vector3.getVectorNormal(particle.getLinearVelocity());
        particle.setDirection(newDirection);

        // p1 = p0 + (v1 * dt)

        Vector3 newPosition =
            particle
                .getPosition()
                .add(
                    newVelocity.scale(deltaTime)
                );


        particle.setPosition(
            newPosition
        );


    }
}