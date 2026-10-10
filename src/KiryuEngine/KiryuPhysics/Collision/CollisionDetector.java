package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;

public class CollisionDetector {


    //Renvoi true si une collision à lieu et false sinon et ajoute le contact associé au collider de la particule
    public static boolean isOverlapping(Particle p1, Particle p2) {

        double distance = p1.getPosition().distance(p2.getPosition());

        if (p1.getCollider() instanceof CollisionSphere cs1) {
            if (p2.getCollider() instanceof CollisionSphere cs2) {

                if (distance < cs1.getRayon() + cs2.getRayon()) {
                    return true;
                }
            }
        }

        return false;

    }
}
