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

    resolveCollisions();

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
        frictionGenerator.applyForce(rb);
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
   *
   */
  private void resolveCollisions(){

    for (int i = 0; i < rigidBodies.size() - 1; i++) {
      for (int j = i + 1; j < rigidBodies.size(); j++) {
        RigidBody bodyA = rigidBodies.get(i);
        RigidBody bodyB = rigidBodies.get(j);

        Contact contact = findContactBetween(bodyA, bodyB);

        if (contact == null) {
          continue;
        }

        resolveContact(bodyA, bodyB, contact);
      }
    }

    for (RigidBody body : rigidBodies) {
      body.clearContacts();
    }
  }


  /**
   *
   * @param owner
   * @param other
   * @return
   */
  private Contact findContact(RigidBody owner, RigidBody other) {
    for (Contact contact : owner.getContacts()) {
      if (contact.getOther() == other.getCollider()) {
        return contact;
      }
    }

    return null;
  }

  /**
   *
   * @param bodyA
   * @param bodyB
   * @return
   */
  private Contact findContactBetween(RigidBody bodyA, RigidBody bodyB) {
    Contact contact = findContact(bodyA, bodyB);

    return contact != null
        ? contact
        : findContact(bodyB, bodyA);
  }


  /**
   *
   * @param bodyA
   * @param bodyB
   * @param contact
   */
  private void resolveContact(
      RigidBody bodyA,
      RigidBody bodyB,
      Contact contact
  ) {
    double inverseMassA = bodyA.getInverseMass();
    double inverseMassB = bodyB.getInverseMass();
    double distance = contact.getDistance();
    Vector3 normal = contact.getNormal();

    double inverseMassSum = inverseMassA + inverseMassB;

    if (inverseMassSum <= 0.0) {
      return;
    }

    double minimumDistance =
        bodyA.getCollider().getSize()
            + bodyB.getCollider().getSize();

    double penetration = minimumDistance - distance;

    /*
    Baumgarte stabilization parameters
    to stop jittering on collisions
     */
    double slop = 0.1; //minimum penetration allowed so collisions are less strict
    double percent = 0.8; //penetration % allowed

    if (penetration > slop) {
      double correctionMagnitude =
          (penetration - slop) * percent / inverseMassSum;

      Vector3 correction =
          new Vector3(normal).scale(correctionMagnitude);

      //correct positions if the RigidBodies aren't static
      if (!bodyA.isStatic()) {
        bodyA.setPosition(
            new Vector3(bodyA.getPosition())
                .sub(new Vector3(correction).scale(inverseMassA))
        );
      }

      if (!bodyB.isStatic()) {
        bodyB.setPosition(
            new Vector3(bodyB.getPosition())
                .add(new Vector3(correction).scale(inverseMassB))
        );
      }
    }

    // Relative velocity: B relative to A.
    Vector3 relativeVelocity =
        new Vector3(bodyB.getLinearVelocity())
            .sub(bodyA.getLinearVelocity());

    double velocityAlongNormal =
        relativeVelocity.dotProduct(normal);

    // the rigid bodies are separating:
    // no normal or friction impulse is needed.
    if (velocityAlongNormal >= 0.0) {
      contact.setNormalImpulse(0.0);
      return;
    }

    // Start with zero restitution. We can add restitution later.
    // 0 = inelastic , 1 = perfectly elastic
    double restitution = 0.0;

    //-(1.0 + restitution) * velocityAlongNormal
    // the relative separation is proportional to the relative approach
    double normalImpulseMagnitude =
        -(1.0 + restitution) * velocityAlongNormal
            / inverseMassSum;

    Vector3 normalImpulse =
        new Vector3(normal).scale(normalImpulseMagnitude);

    // Apply normal impulse if the rigid bodies aren't static
    if (!bodyA.isStatic()) {
      bodyA.setLinearVelocity(
          new Vector3(bodyA.getLinearVelocity())
              .sub(new Vector3(normalImpulse).scale(inverseMassA))
      );
    }

    if (!bodyB.isStatic()) {
      bodyB.setLinearVelocity(
          new Vector3(bodyB.getLinearVelocity())
              .add(new Vector3(normalImpulse).scale(inverseMassB))
      );
    }

    contact.setNormalImpulse(normalImpulseMagnitude);

    //apply friction
    frictionGenerator.applyForce(bodyA,bodyB,contact);

    // Notify both bodies of the collision.
    bodyA.onCollision(bodyB);
    bodyB.onCollision(bodyA);
  }
//endregion

}


