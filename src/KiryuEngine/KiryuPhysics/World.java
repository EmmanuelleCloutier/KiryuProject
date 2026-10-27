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

    Vector3 totalPositionCorrection = new Vector3(0, 0, 0);
    Vector3 velocity = new Vector3(rigidBody.getLinearVelocity());
    int collisionCount = 0;

    // Tuning parameters to stop jitter
    double percent = 0.2; // Positional correction percentage (Baumgarte Stabilization)
    double slop = 0.1;    // Penetration allowance to prevent micro-oscillations

    for (Contact contact : rigidBody.getContacts()) {
      double minimumDistance = rigidBody.getCollider().getSize() + contact.getOther().getSize();

      Vector3 difference = new Vector3(rigidBody.getPosition()).sub(
          contact.getOther().getRigidBody().getPosition()
      );

      double distance = difference.getLength();

      if (distance >= minimumDistance) {
        continue;
      }

      Vector3 normal;
      if (distance < 0.00000001f) {
        continue;
      }

      normal = new Vector3(difference).scale(1.0 / distance).normalize();

      // 1. Calculate penetration depth
      double penetration = minimumDistance - distance;

      // 2. Accumulate positional correction using a slop threshold
      if (penetration > slop) {
        Vector3 correction = new Vector3(normal).scale((penetration - slop) * percent);
        totalPositionCorrection.add(correction);
        collisionCount++;
      }

      // 3. Accumulate velocity adjustments against the original/accumulated frame velocity
      double velocityAlongNormal = velocity.dotProduct(normal);

      if (velocityAlongNormal < 0) {
        velocity.sub(new Vector3(normal).scale(velocityAlongNormal));
      }
    }

    // 4. Apply the averaged/blended changes once at the end of the frame
    if (collisionCount > 0) {
      Vector3 finalPosition = new Vector3(rigidBody.getPosition()).add(totalPositionCorrection);
      rigidBody.setPosition(finalPosition);
    }

    rigidBody.setLinearVelocity(velocity);
    rigidBody.getContacts().clear();
  }

}


