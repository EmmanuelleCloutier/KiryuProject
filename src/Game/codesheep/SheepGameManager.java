
package Game.codesheep;

import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;

import KiryuEngine.KiryuPhysics.Constraints.Spring;
import KiryuEngine.KiryuPhysics.Vector3;

import java.util.ArrayList;
import java.util.Random;

public class SheepGameManager {

    private final PApplet sketch;
    private final World world;
    private final SpriteRenderer spriteRenderer;

    private final ArrayList<Sheep> sheepList;
    private final Random random;
    private final Dog dog;
    private final ArrayList<Spring> springs;
    private float impulseTimer = 0f;

    private static final double LOST_DISTANCE = 180.0;
    private static final double REJOIN_DISTANCE = 100.0;

    private static final int INITIAL_SHEEP_COUNT = 50;
    private static final float SHEEP_SPACING = 75f;

    public SheepGameManager(PApplet sketch) {
        this.sketch = sketch;

        world = new World();
        spriteRenderer = new SpriteRenderer(sketch);
        springs = new ArrayList<>();

        sheepList = new ArrayList<>();
        random = new Random();
        dog = new Dog(sketch);

        generateInitialHerd();
        createSheepSprings();
    }

    
    private static class SheepConnection {

        Sheep first;
        Sheep second;
        Spring spring;

        SheepConnection(Sheep first, Sheep second, Spring spring) {
            this.first = first;
            this.second = second;
            this.spring = spring;
        }
    }

    private final ArrayList<SheepConnection> connections =
        new ArrayList<>();


    private void generateInitialHerd() {

        int columns = 5;
        int rows = 4;

        float centerX = sketch.width / 2f;
        float centerY = sketch.height / 2f;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                float x = centerX
                    + (col - (columns - 1) / 2f) * SHEEP_SPACING;

                float y = centerY
                    + (row - (rows - 1) / 2f) * SHEEP_SPACING;

                // Small random variation for a natural herd.
                x += random.nextFloat() * 10f - 5f;
                y += random.nextFloat() * 10f - 5f;

                Sheep sheep = new Sheep(world, x, y);
                sheepList.add(sheep);
            }
        }
    }

    
    private void updateLostSheep() {

        double centerX = 0;
        double centerY = 0;
        int activeCount = 0;

        // Calculate the center of the active herd.
        for (Sheep sheep : sheepList) {

            if (!sheep.isLost()) {
                centerX += sheep.getPosition().x;
                centerY += sheep.getPosition().y;
                activeCount++;
            }
        }

        if (activeCount == 0) {
            return;
        }

        centerX /= activeCount;
        centerY /= activeCount;

        for (Sheep sheep : sheepList) {

            double dx = sheep.getPosition().x - centerX;
            double dy = sheep.getPosition().y - centerY;

            double distance = Math.sqrt(dx * dx + dy * dy);

            if (!sheep.isLost() && distance > LOST_DISTANCE) {
                sheep.setLost(true);
            }
            else if (sheep.isLost() && distance < REJOIN_DISTANCE) {
                sheep.setLost(false);
            }
        }
    }


    
    private void createSheepSprings() {

        int columns = 5;
        int rows = 4;

        float restLength = SHEEP_SPACING;

        double stiffness = 12.0;
        double damping = 5.0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                int index = row * columns + col;

                Sheep current = sheepList.get(index);

                // Connect to the sheep on the right.
                if (col < columns - 1) {

                    Sheep right = sheepList.get(index + 1);

                   
                    Spring spring = new Spring(
                        current.getParticle().getCollider(),
                        right.getParticle().getCollider(),
                        restLength,
                        stiffness,
                        damping
                    );

                    connections.add(
                        new SheepConnection(current, right, spring)
                    );

                }

                // Connect to the sheep below.
                if (row < rows - 1) {

                    Sheep below =
                        sheepList.get(index + columns);

                    springs.add(new Spring(
                        current.getParticle().getCollider(),
                        below.getParticle().getCollider(),
                        restLength,
                        stiffness,
                        damping
                    ));
                }
            }
        }
    }

    public void update(float deltaTime) {

        // Update dog position.
        dog.update(deltaTime);

        
        impulseTimer += deltaTime;

        // Apply random impulses every 0.4 seconds.
        if (impulseTimer >= 0.4f) {

            impulseTimer = 0f;

            // Move a few sheep at a time.
            for (int i = 0; i < 4; i++) {

                Sheep sheep = sheepList.get(
                    random.nextInt(sheepList.size())
                );

                sheep.applyRandomImpulse(random);
            }
        }


        // 1. Apply dog repulsion.
        // This also resets the previous frame's acceleration.
        for (Sheep sheep : sheepList) {
            sheep.applyDogRepulsion(dog.getPosition());
        }

       updateLostSheep();

        for (SheepConnection connection : connections) {

            if (!connection.first.isLost()
                    && !connection.second.isLost()) {

                connection.spring.applyForce();
            }
        }

        // 3. Update physics.
        world.UpdatePhysics(deltaTime);

        // 4. Slow down the sheep.
        double damping = Math.exp(-2.0 * deltaTime);

        for (Sheep sheep : sheepList) {
            sheep.getParticle().setLinearVelocity(
                sheep.getParticle()
                    .getLinearVelocity()
                    .scale(damping)
            );
        }
    }



    public void draw() {

    sketch.background(110, 155, 95);

    // Draw the herd.
    for (Sheep sheep : sheepList) {
        sheep.draw(spriteRenderer);
    }

    // Draw the dog above the sheep.
    dog.draw(spriteRenderer);
}

    public ArrayList<Sheep> getSheepList() {
        return sheepList;
    }

    public World getWorld() {
        return world;
    }
}
