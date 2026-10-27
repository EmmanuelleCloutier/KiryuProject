package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public class BoxCollider extends Collider{

  private double size;

  public BoxCollider(double size) {
    this.size = size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  @Override
  public void onCollision(Collider other) {
    this.getRigidBody().onCollision();
  }

  @Override
  public double getSize() { return size; }

  @Override
  public void linkRigidBody(RigidBody rigidbody) {
    if (this.rigidBody != null) return;

    this.rigidBody = rigidbody;
  }

}
