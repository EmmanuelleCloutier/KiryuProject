package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Collision.Contact;
import KiryuEngine.KiryuPhysics.Constraints.Constraint;
import KiryuEngine.KiryuPhysics.Forces.FrictionGenerator;
import KiryuEngine.KiryuPhysics.Forces.GravityGenerator;

import java.util.ArrayList;

public class World {

  private final ArrayList<RigidBody> rigidBodies;
  private final GravityGenerator gravityGenerator;
  private final FrictionGenerator frictionGenerator;
  private final ArrayList<Constraint> constraints;
  private final ArrayList<Constraint> constraintsToRemove;


  private boolean bAllowGravity = true;


  public boolean isGravityAllowed() {
    return bAllowGravity;
  }

  public void setGravity(boolean bAllowGravity) {
    this.bAllowGravity = bAllowGravity;
  }


  public World(){
    this.rigidBodies = new ArrayList<>();
    this.constraints = new ArrayList<>();
    this.constraintsToRemove = new ArrayList<>();
    this.frictionGenerator = new FrictionGenerator();
    this.gravityGenerator = new GravityGenerator();
  }

  public void UpdatePhysics(float deltaTime){

    if (rigidBodies.isEmpty()) return;

    for (RigidBody rb : rigidBodies) {

      if (rb.isStatic()) continue;

      if (bAllowGravity) {
        gravityGenerator.applyForce(rb);
      }

      if (rb.isColliding()){
        if (rb.getContacts().isEmpty()) continue;

        for (Contact contact: rb.getContacts()
             ) {
          frictionGenerator.applyForce(rb);
        }
      }
    }

    if (!constraints.isEmpty()) {
      for (Constraint cons : constraints) {

        if(cons.getFirst().getRigidBody().isStatic() &&
            cons.getSecond().getRigidBody().isStatic()){
            constraintsToRemove.add(cons);
            continue;
        }

        if (cons.isConstrained()) {
          cons.applyConstraint();
        }
      }
    }

    checkCollisions();

    ResolvePhysics(deltaTime);
  }



  private void ResolvePhysics(float deltaTime){

    for (RigidBody rb : rigidBodies
         ) {
      if (rb.isStatic()) continue;

      Integrator.integrate(rb,deltaTime);
      resolveCollisions(rb);
    }

    for (Constraint cons:constraintsToRemove
         ) {
      constraints.remove(cons);
    }
    constraintsToRemove.clear();

  }


  public boolean addRigidBody(RigidBody rigidBody){
    return rigidBodies.add(rigidBody);
  }

  public boolean removeRigidBody(RigidBody rigidBody){
    return rigidBodies.remove(rigidBody);
  }

  public boolean addConstraint(Constraint constraint){
    return constraints.add(constraint);
  }

  public boolean removeConstraint(Constraint constraint){
    return constraints.remove(constraint);
  }


  public void checkCollisions(){

    // right now this O(n^2), which is kinda bad
    // we need to change this for a quad tree at some point

    for (int i = 0; i < rigidBodies.size() - 1 ; i++) {
      for (int j = i + 1; j < rigidBodies.size(); j++) {

        RigidBody first = rigidBodies.get(i);
        // don't need to register contacts for static rigidBodies
        if (first.isStatic()) continue;

        // but we can have contacts with static rigidBodies
        RigidBody second = rigidBodies.get(j);

        if (first.getPosition().distanceSquared(second.getPosition()) <=
            first.getCollider().getSizeSquared()
        + second.getCollider().getSizeSquared()
        ){
          first.addContact(
              new Contact(
                  first.getCollider(),
                  second.getCollider()
              )
          );
        }
      }
    }
  }

  private void resolveCollisions(RigidBody rigidBody){

//    if (rigidBody.getContacts().isEmpty()) return;
//
//
//    Vector3 position = rigidBody.getPosition();
//    Vector3 velocity = rigidBody.getLinearVelocity();
//
//    Vector3 correctedPosition = new Vector3(position);
//    Vector3 correctedVelocity = new Vector3(velocity);
//
//
//    double colliderSize = rigidBody.getCollider().getSize();
//
//    for (Contact contact: rigidBody.getContacts()
//         ) {
//
//      double minimumDistance = colliderSize + contact.getOther().getSize();
//
//      Vector3 difference = new Vector3(correctedPosition).sub(
//          contact.getOther()
//              .getRigidBody()
//              .getPosition()
//      );
//
//      double distance = difference.getLength();
//
//      if (distance >= minimumDistance) {
//        continue;
//      }
//
//      Vector3 normal = new Vector3(difference);
//
//      if (distance > 0.0001f) {
//        normal.scale(1.0 / distance);
//      } else {
//        normal.set(1, 0, 0);
//      }
//
//      double velocityAlongNormal =
//          velocity.x * normal.x +
//              velocity.y * normal.y;
//
//
//      if (velocityAlongNormal < 0) {
//
//        correctedVelocity.sub(
//            new Vector3(normal).scale(velocityAlongNormal)
//        );
//      }
//
//      correctedPosition.add(
//          new Vector3(normal).scale(minimumDistance)
//      );
//
//    }
//
//    rigidBody.setPosition(correctedPosition);
//
//    rigidBody.setLinearVelocity(
//        correctedVelocity
//    );
//
//    rigidBody.getContacts().clear();

  }

}


