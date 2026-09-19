package KiryuEngine.KiryuPhysics;

public class Force {

    private static final Vector3 GRAVITY =
        new Vector3(0, 200, 0);


    /**
     * Applies gravity to a particle.
     *
     * F = m * g
     * a = F / m
     *
     * @param particle particle affected by gravity
     */
    public static void applyGravity(
        Particle particle
    ) {

        // F = m * g
        Vector3 gravityForce =
            GRAVITY.scale(
                particle.getMass()
            );


        // a = F * inverseMass
        Vector3 acceleration =
            gravityForce.scale(
                particle.getInverseMass()
            );


        particle.setAcceleration(
            acceleration
        );
    }
}