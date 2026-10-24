package KiryuEngine.KiryuPhysics.Collision;

import KiryuEngine.KiryuPhysics.RigidBody;

public class BoxCollider extends Collider{

  private double size;

  public BoxCollider(double size, RigidBody rigidbody) {
    this.size = size;
    this.rigidBody = rigidbody;
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
  public double getSizeSquared() {
    return size * size;
  }

}
