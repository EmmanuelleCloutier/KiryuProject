package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class Contact {

    Vector3 contactPoint;
    Particle particle;
    Vector3 normale;

    public Contact(Particle particle, Vector3 contactPoint, Vector3 normale) {

        this.particle = particle;
        this.contactPoint = contactPoint;
        this.normale = normale;

    }

}
