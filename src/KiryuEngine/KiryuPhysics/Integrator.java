package KiryuEngine.KiryuPhysics;

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

//      System.out.println(
//          "INTEGRATE " + rigidBody
//              + " | Force: " + rigidBody.getAccumulatedForce()
//              + " | Velocity: " + rigidBody.getLinearVelocity()
//      );


        // v1 = ((damping ^ dt) * v0) + (a * dt)

      Vector3 newVelocity = new Vector3(rigidBody.getLinearVelocity())
          .scale(
              Math.pow(
                  rigidBody.getDamping(),
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