package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public abstract class Collider {

    protected RigidBody rigidBody = null;

    public abstract void onCollision(Collider other);

    public abstract double getSize();

    public abstract double getSizeSquared();

    public RigidBody getRigidBody(){
        return this.rigidBody;
    }

}
