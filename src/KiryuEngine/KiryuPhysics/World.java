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


  public World(){
    this.rigidBodies = new ArrayList<>();
    this.constraints = new ArrayList<>();
    this.frictionGenerator = new FrictionGenerator();
    this.gravityGenerator = new GravityGenerator();
  }

  public void UpdatePhysics(float deltaTime){

    for (RigidBody rb : rigidBodies) {

      gravityGenerator.applyForce(rb);

      if (rb.isColliding()){
        for (Contact contact: rb.getContacts()
             ) {
          frictionGenerator.applyForce(rb);
        }
      }
    }

    for (Constraint cons: constraints) {
      if(cons.isConstrained()){
        applyConstraints(cons);
      }
    }

    checkCollisions();
    resolveCollisions();

    ResolvePhysics(deltaTime);
  }

  private void resolveCollisions(){}

  private void applyConstraints(Constraint constraint){

  }

  private void ResolvePhysics(float deltaTime){

    for (RigidBody rb : rigidBodies
         ) {
      Integrator.integrate(rb,deltaTime);
    }

  }

  public boolean addRigidBody(RigidBody rigidBody){
    return rigidBodies.add(rigidBody);
  }

  public boolean removeRigidBody(RigidBody rigidBody){
    return rigidBodies.remove(rigidBody);
  }



  public void checkCollisions(){

    // right now this O(n^2), which is kinda bad
    // we need to change this for a quad tree at some point

    for (int i = 0; i < rigidBodies.size() - 1 ; i++) {
      for (int j = i + 1; j < rigidBodies.size(); j++) {

        RigidBody first = rigidBodies.get(i);
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
}
