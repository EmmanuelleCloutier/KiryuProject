package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Collision.Contact;
import KiryuEngine.KiryuPhysics.Constraints.Constraint;
import KiryuEngine.KiryuPhysics.Forces.ForceGenerator;

import java.util.ArrayList;

public class World {

  private final ArrayList<RigidBody> rigidBodies;
  private ArrayList<String> resultingCollisions;
  private ArrayList<String> resultingConstraints;
  private final ArrayList<ForceGenerator> forceGenerators;
  private final ArrayList<Constraint> constraints;


  public World(){
    this.rigidBodies = new ArrayList<>();
    this.forceGenerators = new ArrayList<>();
    constraints = new ArrayList<>();
  }

  public World(ArrayList<ForceGenerator> forceGenerators){
    this.rigidBodies = new ArrayList<>();
    this.forceGenerators = forceGenerators;
    this.constraints = new ArrayList<>();
  }

  public void UpdatePhysics(float deltaTime){

    for (RigidBody rb : rigidBodies) {

      accumulateForces(rb);

    }

    for (Constraint cons: constraints) {
      if(cons.isConstrained()){
        applyConstraints(deltaTime,cons);
      }
    }

    checkCollisions();
    resolveCollisions();

    ResolvePhysics(deltaTime);
  }

  private void accumulateForces(RigidBody rigidBody){

    Vector3 resultingForce = new Vector3();
    resultingForce.set(rigidBody.getForce());

    // this is partially wrong since there might be friction applied
    // to unwanted rigid-bodies

//    for (ForceGenerator fg: forceGenerators
//         ) {
//      fg.applyForce(resultingForce);
//    }

    rigidBody.setAccumulatedForce(resultingForce);
  }

  private void resolveCollisions(){}

  private void applyConstraints(float deltaTime, Constraint constraint){

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
