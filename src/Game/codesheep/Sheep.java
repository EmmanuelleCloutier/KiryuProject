
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Collision.SphereCollider2D;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;
import java.util.Random;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.SpriteRenderer;

public class Sheep {

    private final RigidBody rigidBody;

    private static final String SPRITE_PATH =
        "Game/data/mouton.png";

    private static final float SIZE = 55;
    private static final double mass = 1.f;

    //track si le mouton est lost ou est dans le pen 
    private boolean isSafe = false;
    private boolean isLost = false;

    private double detectionRadius = 225.0;
    private double repulsionStrength = 200.0;


    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        this.isSafe = safe;
    }

    public Sheep(World world, double x, double y) {

        this.rigidBody = new RigidBody(
            mass,
            new Vector3(x, y, 0),
            Vector3.ZERO_VECTOR.copy(),
            new SphereCollider2D(SIZE/2)
        );

        world.addRigidBody(this.rigidBody);
    }

    public boolean isLost() {
    return isLost;
    }

    public void setLost(boolean lost) {
        this.isLost = lost;
    }

    public RigidBody getRigidBody() {
        return this.rigidBody;
    }

    public Vector3 getPosition() {
        return this.rigidBody.getPosition();
    }
    
    public void applyRandomImpulse(Random random) {

        if (isSafe) { //on retire les impulse le sheep est safe
            return;
        }   

        //generate a random direction and impulse 
        double angle = random.nextDouble() * Math.PI * 2.0;
        double strength = 20f + random.nextDouble() * 35.0;

        Vector3 impulseDirection = new Vector3(
            Math.cos(angle),
            Math.sin(angle),
            0
        );

        this.rigidBody.addImpulse(impulseDirection,strength);


    }
    
    public void applyDogRepulsion(Vector3 dogPosition) {

        if (isSafe) {
           this.rigidBody.isStatic(true);
            return;
        }


        Vector3 direction =
            new Vector3(this.rigidBody.getPosition()).sub(dogPosition);

        double distance = direction.getLength();

        if (distance > 0 && distance < detectionRadius) {

            //increase repulsion as the dog gets closer
            double intensity = 1.0 - (distance / detectionRadius);

            double strength = (1.0f / distance) * (repulsionStrength * intensity);

            this.rigidBody.addImpulse(direction,strength);

        }
    }

    public void draw(SpriteRenderer renderer) {
        renderer.drawSprite(
            SPRITE_PATH,
            rigidBody.getPosition(),
            SIZE,
            SIZE
        );
    }
}
