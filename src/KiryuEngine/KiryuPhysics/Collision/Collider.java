package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public abstract class Collider {

    protected RigidBody rigidBody = null;

    protected boolean bIsDeactivated = false;


    public abstract double getSize();

    public abstract void linkRigidBody(RigidBody rigidbody);

    public RigidBody getRigidBody(){
        return this.rigidBody;
    }

    public boolean isDeactivated() {
        return bIsDeactivated;
    }

    public void setDeactivated(boolean bIsDeactivated) {
        this.bIsDeactivated = bIsDeactivated;
    }


}
