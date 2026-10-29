package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.Contact;
import KiryuEngine.KiryuPhysics.Constraints.Constraint;
import KiryuEngine.KiryuPhysics.Forces.FrictionGenerator;
import KiryuEngine.KiryuPhysics.Forces.GravityGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class manages physics applied to any RigidBodies
 * as of right now it also manages collision detection
 */
public class World {

  /**
   * The rigidBodies that are active in the world
   */
  private final ArrayList<RigidBody> rigidBodies = new ArrayList<>();
  /**
   *  Force generator that applies a gravity force to rigidBodies
   */
  private final GravityGenerator gravityGenerator = new GravityGenerator();
  /**
   * force generator that applies friction to RigidBodies
   */
  private final FrictionGenerator frictionGenerator = new FrictionGenerator();
  /**
   * list of constraints affecting rigidBodies
   */
  private final ArrayList<Constraint> constraints = new ArrayList<>();
  /**
   * Whether the world should have gravity
   */
  private boolean allowGravity = true;

  /**
   * Constructor
   */
  public World(){}

  /*
  * GETTERS
  */
  //region

  /**
   * @return <font color="orange">{@code boolean}</font>
   * - whether the gravity is on or off
   */
  public boolean isGravityAllowed() {
    return allowGravity;
  }

  //endregion

  /*
   * SETTER
   */
  //region

  /**
   *
   * @param bAllowGravity <font color="orange">{@code boolean}</font>
   * - set's if the gravity should be on or off
   */
  public void isGravityAllowed(boolean bAllowGravity) {
    this.allowGravity = bAllowGravity;
  }

  /**
   * toggle's the world's gravity state
   */
  public void toggleGravity(){
    this.allowGravity = !allowGravity;
  }

//endregion

  /**
   * update the physics of this world in order:
   * Rigidbody integration,
   * Collision Check,
   * Solve constraints,
   * Solve collisions
   * @param deltaTime <font color="orange">{@code double}</font>
   * - time elapsed between each frames
   */
  public void updatePhysics(float deltaTime){

    updateRigidBodies(deltaTime);

    checkCollisions();

    updateConstraints();

    resolvePhysics();
  }
  /**
   * resolve the last step of the physics engine
   * and also cleanup any rigidBody that should no longer
   * be active in the world
   */
  private void resolvePhysics(){

    for (RigidBody rb : rigidBodies
         ) {
      if (rb.isStatic()) continue;
      if (rb.getContacts().isEmpty())continue;

      resolveCollisions(rb);

      //after resolving collisions we clear all collision
      //detected this frame
      rb.clearContacts();
    }

    //remove rigidBodies that should not be active anymore
    rigidBodies.removeIf(RigidBody::isMarkedForDestruction);
  }

  /*
   * RIGIDBODY MANAGEMENT
   */
  //region
  /**
   * Adds a rigid body to this world.
   * @param rigidBody <font color="yellow">{@link RigidBody}</font>
   * - the RigidBody to add
   * @return <font color="orange">{@code boolean}</font>
   * - Whether we added the RigidBody successfully
   */
  public boolean addRigidBody(@NotNull RigidBody rigidBody) {

    if (rigidBodies.contains(rigidBody)) {
      return false;
    }

    return rigidBodies.add(rigidBody);
  }
  /**
   * Removes a rigid body and its associated constraints.
   * @param rigidBody <font color="yellow">{@link RigidBody}</font>
   * - The RigidBody to remove
   * @return <font color="orange">{@code boolean}</font>
   * - Whether we removed the RigidBody successfully
   */
  public boolean removeRigidBody(RigidBody rigidBody){
    if (!rigidBodies.remove(rigidBody)) {
      return false;
    }

    removeConstraint(rigidBody.getCollider());

    return true;
  }
  /**
   * applies forces to each rigidbody in this world
   * and integrates them using Euler's integration
   */
  public void updateRigidBodies(float deltaTime){
    if (rigidBodies.isEmpty()) return;

    for (RigidBody rb : rigidBodies) {

      //we don't need to apply forces to a static RigidBody
      if (rb.isStatic()) {
        continue;
      }

      if (allowGravity) {
        gravityGenerator.applyForce(rb);
      }

      //if a RigidBody is colliding, we apply friction for every
      //collision it has
      if (rb.isColliding() && !rb.getContacts().isEmpty()) {
        for (Contact contact : rb.getContacts()) {
          frictionGenerator.applyForce(rb);
        }
      }
      // integrate all forces applied to the RigidBody
      Integrator.integrate(rb, deltaTime);
    }
  }
  //endregion

  /*
   * CONSTRAINTS MANAGEMENT
   */
  //region
  /**
   * Adds a constraint to this world.
   * @param constraint <font color="yellow">{@link Constraint}</font>
   * - the constraint to add
   * @return <font color="orange">{@code boolean}</font>
   * - Whether we added the constraint successfully
   */
  public boolean addConstraint(@NotNull Constraint constraint) {

    if (constraints.contains(constraint)) {
      return false;
    }

    return constraints.add(constraint);
  }
  /**
   * removes a constraint from this world.
   * @param constraint <font color="yellow">{@link Constraint}</font>
   * - the constraint to remove
   * @return <font color="orange">{@code boolean}</font>
   * - Whether we removed the constraint successfully
   */
  public boolean removeConstraint(Constraint constraint){
    return constraints.remove(constraint);
  }
  /**
   * removes all constraints associated with a collider from this world.
   * @param collider <font color="yellow">{@link Collider}</font>
   * - the collider we should remove constraints for
   * @return <font color="orange">{@code boolean}</font>
   * - Whether we removed the constraint successfully
   */
  public boolean removeConstraint(Collider collider){

    return constraints.removeIf(constraint ->
        constraint.getFirst() == collider ||
            constraint.getSecond() == collider
    );
  }
  /**
   * Applies active constraints to the RigidBodies it is
   * linked to
   */
  private void updateConstraints() {
    for (Constraint constraint : constraints) {
      RigidBody first = constraint.getFirst().getRigidBody();
      RigidBody second = constraint.getSecond().getRigidBody();

      //if both are static there's no need to apply constraints
      if (first.isStatic() && second.isStatic()) {
        continue;
      }
      // apply the constraint if the conditions are met
      if (constraint.isConstrained()) {
        constraint.applyConstraint();
      }
    }

    //clean up constraints of ineligible RigidBodies
    constraints.removeIf(constraint ->
        constraint.getFirst().getRigidBody().isStatic()
            && constraint.getSecond().getRigidBody().isStatic()
    );
  }
  //endregion

  /*
   * COLLISION MANAGEMENT
   */
  //region
  /**
   * Simple Collision detection
   */
  public void checkCollisions(){

    // right now this O(n^2), which is kinda bad
    // we need to change this for a quad tree at some point

    for (int i = 0; i < rigidBodies.size() - 1 ; i++) {
      for (int j = i + 1; j < rigidBodies.size(); j++) {
        RigidBody first = rigidBodies.get(i);
        RigidBody second = rigidBodies.get(j);

        // Static bodies cannot move, so they don't need to collide with each other.
        if (first.isStatic() && second.isStatic()) {
          continue;
        }
        //if either collider are deactivated, don't check collisions
        if (first.getCollider().isDeactivated()
            || second.getCollider().isDeactivated()) {
          continue;
        }

        double distanceSquared =
            first.getPosition().distanceSquared(second.getPosition());

        double radiusSum =
            first.getCollider().getSize()
                + second.getCollider().getSize();

        if (distanceSquared <= radiusSum * radiusSum) {

          // Store the contact on each non-static body.
          if (!first.isStatic()) {
            first.addContact(
                new Contact(first.getCollider(), second.getCollider())
            );
          }

          if (!second.isStatic()) {
            second.addContact(
                new Contact(second.getCollider(), first.getCollider())
            );
          }
        }
      }
    }
  }
  /**
   * resolve collisions for every contact the rigidBody has
   * @param rigidBody <font color="yellow">{@link RigidBody}</font>
   * - the RigidBody that has collisions
   */
  private void resolveCollisions(RigidBody rigidBody){

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

      rigidBody.onCollision(contact.getOther().getRigidBody());

      Vector3 normal;
      if (distance < 0.00000001f) {
        continue;
      }

      normal = new Vector3(difference).scale(1.0 / distance);

      // Calculate penetration depth
      double penetration = minimumDistance - distance;

      // Accumulate positional correction using a slop threshold
      if (penetration > slop) {
        Vector3 correction = new Vector3(normal).scale((penetration - slop) * percent);
        totalPositionCorrection.add(correction);
        collisionCount++;
      }

      // Accumulate velocity adjustments against the accumulated frame velocity
      double velocityAlongNormal = velocity.dotProduct(normal);

      if (velocityAlongNormal < 0) {
        velocity.sub(new Vector3(normal).scale(velocityAlongNormal));
      }
    }

    // Apply the averaged/blended changes once at the end of the frame
    if (collisionCount > 0) {
      Vector3 finalPosition = new Vector3(rigidBody.getPosition()).add(totalPositionCorrection);
      rigidBody.setPosition(finalPosition);
    }

    rigidBody.setLinearVelocity(velocity);
  }
//endregion

}


