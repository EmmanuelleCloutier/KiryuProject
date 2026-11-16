package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuMath.Matrix3;
import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;

public class Integrator {

    private static void integrate_rotation(RigidBody rigidBody, float deltaTime) {

        //Mise à jour du Quaternion décrivant la rotation
        Vector3 av = rigidBody.getAngularVelocity();

        Quaternion newRQuaternion =
                new Quaternion(0, (float) av.x, (float) av.y, (float) av.z)
                        .multiply(rigidBody.getQRotation());

        newRQuaternion.x = rigidBody.getQRotation().x + (newRQuaternion.x * 0.5f * deltaTime);
        newRQuaternion.y = rigidBody.getQRotation().y + (newRQuaternion.y * 0.5f * deltaTime);
        newRQuaternion.z = rigidBody.getQRotation().z + (newRQuaternion.z * 0.5f * deltaTime);

        //Mise à jour de la Matrice associée
        Matrix3 newRMatrix = new Matrix3();
        double [] elems = {
                1 - 2*((newRQuaternion.y*newRQuaternion.y) + (newRQuaternion.z*newRQuaternion.z)),
                2*((newRQuaternion.x*newRQuaternion.y) + (newRQuaternion.z*newRQuaternion.w)),
                2*((newRQuaternion.x*newRQuaternion.y) - (newRQuaternion.y*newRQuaternion.w)),
                2*((newRQuaternion.x*newRQuaternion.y) - (newRQuaternion.z*newRQuaternion.w)),
                1 - 2*((newRQuaternion.x*newRQuaternion.x) + (newRQuaternion.z*newRQuaternion.z)),
                2*((newRQuaternion.y*newRQuaternion.z) + (newRQuaternion.x*newRQuaternion.w)),
                2*((newRQuaternion.x*newRQuaternion.z) + (newRQuaternion.y*newRQuaternion.w)),
                2*((newRQuaternion.y*newRQuaternion.z) - (newRQuaternion.x*newRQuaternion.w)),
                1 - 2*((newRQuaternion.x*newRQuaternion.x) + (newRQuaternion.y*newRQuaternion.y))
        };

        newRMatrix.setElements(elems);

    }

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
}