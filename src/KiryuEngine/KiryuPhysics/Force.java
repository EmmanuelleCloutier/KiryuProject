package KiryuEngine.KiryuPhysics;

public class Force {

    public static final Vector3 GRAVITY =
        new Vector3(0, 200, 0);

    public static final Vector3 INFINITE =
        new Vector3(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);


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

        /*
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
        */

        // Simplification => fonctionne pareil que plus haut.
        // Preuve : a = F x 1/m
        //          a = g x m x 1/m
        //          a = g x 1
        //          a = g
        particle.setAcceleration(GRAVITY);
    }
}