package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class CollisionDetector {



    public static Contact isOverlapping(Particle p1, Particle p2) {

        double distance = p1.getPosition().distance(p2.getPosition());

        if (p1.getCollider() instanceof CollisionSphere cs1) {
            if (p2.getCollider() instanceof CollisionSphere cs2) {
                     if (distance <= cs1.getRayon() + cs2.getRayon()) {
                         Vector3 normale = p1.getPosition().sub(p2.getPosition());
                         return new Contact(p1, p2, normale);
                     }
            }
        }


        return null;

    }

    public static boolean isContactAtRest(Particle p1, Particle p2, float deltaTime) {
        return false;
    }
}
