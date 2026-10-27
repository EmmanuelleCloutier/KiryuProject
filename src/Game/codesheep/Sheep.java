
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;
import java.util.Random;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.SpriteRenderer;

public class Sheep {

    private final Particle particle;

    private static final String SPRITE_PATH =
        "Game/data/mouton.png";

    private static final float SIZE = 55;

    //track si le mouton est lost ou est dans le pen 
    private boolean isSafe = false;
    private boolean isLost = false;

    private double detectionRadius = 300.0;
    private double repulsionStrength = 600.0;


    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        this.isSafe = safe;
    }

    public Sheep(World world, double x, double y) {

        //track whether the sheep
        particle = new Particle(
            world,
            1.0,
            SIZE / 2.0,
            new Vector3(x, y, 0),
            new Vector3(0, 0, 0)
        );

        world.addRigidBody(this.particle.getRigidbody());
    }

    public boolean isLost() {
    return isLost;
    }

    public void setLost(boolean lost) {
        this.isLost = lost;
    }

    public Particle getParticle() {
        return particle;
    }

    public Vector3 getPosition() {
        return particle.getPosition();
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

        particle.getRigidbody().addImpulse(impulseDirection,strength);

    }
    
    public void applyDogRepulsion(Vector3 dogPosition) {

        if (isSafe) {
            particle.getRigidbody().setIsStatic(true);
            return;
        }


        Vector3 direction = new Vector3(particle.getPosition()).sub(dogPosition);

        double distance = direction.getLength();

        if (distance > 0 && distance < detectionRadius) {

            //increase repulsion as the dog gets closer
            double intensity = 1.0 - (distance / detectionRadius);

            double strength = (1.0f / distance) * (repulsionStrength * intensity);

            particle.getRigidbody().addImpulse(direction,strength);

        }
    }

    public void draw(SpriteRenderer renderer) {
        renderer.drawSprite(
            SPRITE_PATH,
            particle.getPosition(),
            SIZE,
            SIZE
        );
    }
}
