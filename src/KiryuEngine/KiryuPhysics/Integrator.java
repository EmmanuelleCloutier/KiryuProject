package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuMath.Matrix3;
import KiryuEngine.KiryuMath.Matrix4;
import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;

public class Integrator {


    /**
     * Integrates a rigidbody using Euler integration.
     *
     * @param rigidBody rigidbody to integrate
     * @param deltaTime time between frames
     */
    public static void integrate(
        RigidBody rigidBody,
        float deltaTime
    ) {
      integrate_linear(rigidBody,deltaTime);
      integrate_rotation(rigidBody,deltaTime);
    }

    private static void integrate_linear(
        RigidBody rigidBody,
        float deltaTime
    ){

      // v1 = ((damping ^ dt) * v0) + (a * dt)

      Vector3 newVelocity = new Vector3(rigidBody.getLinearVelocity())
          .scale(
              Math.pow(
                  1.f,
                  deltaTime
              )
          )
          .add(new Vector3(rigidBody.getAccumulatedForce())
              .scale(rigidBody.getInverseMass() * deltaTime)
          );


      rigidBody.setLinearVelocity(
          newVelocity
      );

      // Update direction if the body is moving.
      if (newVelocity.getLengthSquared() > 0.0f) {
        rigidBody.setDirection(
            Vector3.getVectorNormal(newVelocity)
        );
      }

      // p1 = p0 + (v1 * dt)

      Vector3 newPosition = new Vector3(rigidBody.getPosition())
          .add(
              new Vector3(newVelocity).scale(deltaTime)
          );

      rigidBody.setPosition(
          newPosition
      );

      rigidBody.getAccumulatedForce().clear();
    }


    private static void integrate_rotation(
        RigidBody rigidBody,
        float deltaTime
    ) {

      //Calcul de l'accélération angulaire

      Vector3 angularAcceleration = rigidBody.getTorque().multiplyMatrix(rigidBody.getInverseInertiaTensor());
      rigidBody.resetTorque();

      //Mise à jour de la vélocité angulaire

      Vector3 av = rigidBody.getAngularVelocity().add(angularAcceleration.scale(deltaTime));
      rigidBody.setAngularVelocity(av);

      //Mise à jour du Quaternion décrivant la rotation


      Quaternion newRQuaternion =
          new Quaternion(0, (float) av.x, (float) av.y, (float) av.z)
              .multiply(rigidBody.getQRotation());

      newRQuaternion.w = rigidBody.getQRotation().w + (newRQuaternion.w * 0.5f * deltaTime);
      newRQuaternion.x = rigidBody.getQRotation().x + (newRQuaternion.x * 0.5f * deltaTime);
      newRQuaternion.y = rigidBody.getQRotation().y + (newRQuaternion.y * 0.5f * deltaTime);
      newRQuaternion.z = rigidBody.getQRotation().z + (newRQuaternion.z * 0.5f * deltaTime);

      rigidBody.setQRotation(newRQuaternion);

      //Mise à jour de la Matrice associée
      Matrix4 rotationMatrix  = new Matrix4().fromQuaternion(newRQuaternion);
      Matrix3 newRMatrix = rotationMatrix.getMatrix3();
      rigidBody.setMRotation(newRMatrix);

      //Mise à jour de la matrice inverse d'inertie
      Matrix3 inverseR = newRMatrix.getInverse();
      Matrix3 inverseInertiaTensor = new Matrix3(newRMatrix);
      inverseInertiaTensor.multiplyMatrix(rigidBody.getInverseInertiaTensor());
      if (inverseR!=null) {
        inverseInertiaTensor.multiplyMatrix(inverseR);
      }
      rigidBody.setInverseInertiaTensor(inverseInertiaTensor);
  }


}
