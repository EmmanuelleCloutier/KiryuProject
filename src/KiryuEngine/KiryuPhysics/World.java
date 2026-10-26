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

        if (first.getCollider().isDeactivated() ||
            second.getCollider().isDeactivated()){
          continue;
        }

        double distSq = first.getPosition().distanceSquared(second.getPosition());
        double sizeSum = first.getCollider().getSize() + second.getCollider().getSize();
        double sizeSumSq = sizeSum * sizeSum;

        if ( distSq <= sizeSumSq){
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

    if (rigidBody.getContacts().isEmpty()) return;

    for (Contact contact: rigidBody.getContacts()
         ) {

      Vector3 correctedPosition = new Vector3();
      Vector3 correctedVelocity = new Vector3();

      double minimumDistance = rigidBody.getCollider().getSize()/2 + contact.getOther().getSize()/2;

      Vector3 difference = new Vector3(rigidBody.getPosition()).sub(
          contact.getOther()
              .getRigidBody()
              .getPosition()
      );


      double distance = difference.getLength();


      if (distance >= minimumDistance) {
        continue;
      }


      Vector3 normal;

      if (distance > 0.00000001f) {
        normal = difference.scale(1.0 / distance);
      } else {
        normal = new Vector3(0, 0, 0);
      }

      correctedPosition.add(
          new Vector3(rigidBody.getPosition())
              .add(new Vector3(normal).scale(minimumDistance))
      );

      rigidBody.setPosition(correctedPosition);

      Vector3 velocity = rigidBody.getLinearVelocity();

      double velocityAlongNormal =
          velocity.x * normal.x +
              velocity.y * normal.y;


      if (velocityAlongNormal < 0) {

        correctedVelocity.set(
            new Vector3(velocity)
                .sub(new Vector3(normal).scale(velocityAlongNormal))
        );

        rigidBody.setLinearVelocity(
            correctedVelocity
        );
      }

    }


    rigidBody.getContacts().clear();

  }

}


