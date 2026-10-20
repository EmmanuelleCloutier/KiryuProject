package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Forces.ForceGenerator;

import java.util.ArrayList;

public class World {

  private final ArrayList<RigidBody> rigidBodies;
  private ArrayList<String> resultingCollisions;
  private ArrayList<String> resultingConstraints;
  private final ArrayList<ForceGenerator> forceGenerators;


  public World(){
    this.rigidBodies = new ArrayList<>();
    this.forceGenerators = new ArrayList<>();
  }

  public World(ArrayList<ForceGenerator> forceGenerators){
    this.rigidBodies = new ArrayList<>();
    this.forceGenerators = forceGenerators;
  }

  public void UpdatePhysics(float deltaTime){

    for (RigidBody rigidBody : rigidBodies) {

      accumulateForces(rigidBody);

    }

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

  private void applyCollisions(float deltaTime, RigidBody rigidBody){}

  private void applyConstraints(float deltaTime, RigidBody rigidBody){}

  private void ResolvePhysics(float deltaTime){

    for (RigidBody rigidbody : rigidBodies
         ) {
      Integrator.integrate(rigidbody,deltaTime);
    }

  }

  public boolean addRigidBody(RigidBody rigidBody){
    return rigidBodies.add(rigidBody);
  }

  public boolean removeRigidBody(RigidBody rigidBody){
    return rigidBodies.remove(rigidBody);
  }



}
