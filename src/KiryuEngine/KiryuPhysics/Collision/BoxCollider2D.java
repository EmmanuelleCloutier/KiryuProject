package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public class BoxCollider2D extends Collider {

  private double size;

  public BoxCollider2D(double size) {
    this.size = size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  @Override
  public double getSize() { return size; }

  @Override
  public void linkRigidBody(RigidBody rigidbody) {
    if (this.rigidBody != null) return;

    this.rigidBody = rigidbody;
  }

}
