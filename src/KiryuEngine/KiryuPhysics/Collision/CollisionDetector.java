package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class CollisionDetector {



    public static boolean isOverlapping(Particle p1, Particle p2) {

        double distance = p1.getPosition().distance(p2.getPosition());
        return distance <= p1.getCollider().getSize() + p2.getCollider().getSize();

    }
}
