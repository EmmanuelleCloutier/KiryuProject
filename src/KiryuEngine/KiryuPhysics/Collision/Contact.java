package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.Vector3;



public class Contact {

    Collider other;
    Vector3 normal = new Vector3();
    Vector3 contactPoint = new Vector3();

    public Contact(Collider self, Collider other) {

        if (self == null || other == null) return;

        this.other = other;


        this.normal.set(
            new Vector3(this.other.getRigidBody().getPosition())
            .sub(
                self.getRigidBody().getPosition()
            ).normalize()
        );

        this.contactPoint.set(new Vector3(self.getRigidBody().getPosition())
            .add(
                new Vector3(this.normal).scale(self.getSize())
            )
        );

    }

    public Collider getOther() {
        return other;
    }

    public Vector3 getContactPoint() {
        return contactPoint;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public boolean isContactAtRest(float deltaTime) {

//        Vector3 gravity = GRAVITY;
//
//        gravity = gravity.scale(deltaTime);
//
//        return Math.abs(gravity.dotProduct(this.normal)) >
//                Math.abs(this.particle.getLinearVelocity().dotProduct(this.normal));
     return false;
    }
}
