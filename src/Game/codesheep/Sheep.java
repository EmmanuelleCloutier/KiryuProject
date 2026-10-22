
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.SpriteRenderer;

public class Sheep {

    private final Particle particle;

    private static final String SPRITE_PATH =
        "Game/data/mouton.png";

    private static final float SIZE = 55;

    public Sheep(World world, double x, double y) {

        // Each sheep is represented by one physical particle.
        particle = new Particle(
            world,
            1.0,
            SIZE / 2.0,
            new Vector3(x, y, 0),
            new Vector3(0, 0, 0)
        );

        // No movement or gravity for the initial test.
        particle.setAcceleration(new Vector3(0, 0, 0));
    }

    public Particle getParticle() {
        return particle;
    }

    public Vector3 getPosition() {
        return particle.getPosition();
    }

    public void testMethod() {
    System.out.println("TEST SHEEP");
    }
    
    public void applyDogRepulsion(Vector3 dogPosition) {

        double detectionRadius = 150.0;
        double repulsionStrength = 600.0;

        Vector3 direction = particle.getPosition().sub(dogPosition);

        double distance = direction.getLength();

        if (distance > 0 && distance < detectionRadius) {

            // Stronger repulsion when the dog is closer.
            double intensity = 1.0 - (distance / detectionRadius);

            Vector3 acceleration = direction
                .scale(1.0 / distance)
                .scale(repulsionStrength * intensity);

            particle.setAcceleration(acceleration);

        } else {
            particle.setAcceleration(new Vector3(0, 0, 0));
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
