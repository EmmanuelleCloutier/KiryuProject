
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
    //private final ArrayList<Spring> springs;
    private float impulseTimer = 0f;
    private int lostSheepCount = 0;

    private static final double HERD_CONNECTION_DISTANCE = 105.0;

    private static final int INITIAL_SHEEP_COUNT = 50;
    private static final float SHEEP_SPACING = 75f;

    public SheepGameManager(PApplet sketch) {
        this.sketch = sketch;

        world = new World();
        spriteRenderer = new SpriteRenderer(sketch);
        //springs = new ArrayList<>();

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
        int rows = 10;

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

        int count = sheepList.size();

        boolean[] visited = new boolean[count];

        ArrayList<Integer> mainHerd = new ArrayList<>();

        // Find all groups of nearby sheep.
        for (int i = 0; i < count; i++) {

            if (visited[i]) {
                continue;
            }

            ArrayList<Integer> group = new ArrayList<>();
            ArrayList<Integer> queue = new ArrayList<>();

            queue.add(i);
            visited[i] = true;

            // Explore all sheep connected by proximity.
            for (int q = 0; q < queue.size(); q++) {

                int currentIndex = queue.get(q);
                group.add(currentIndex);

                Sheep current = sheepList.get(currentIndex);

                for (int j = 0; j < count; j++) {

                    if (visited[j]) {
                        continue;
                    }

                    Sheep other = sheepList.get(j);

                    double distance = current.getPosition()
                        .sub(other.getPosition())
                        .getLength();

                    if (distance <= HERD_CONNECTION_DISTANCE) {

                        visited[j] = true;
                        queue.add(j);
                    }
                }
            }

            // Keep the largest connected group.
            if (group.size() > mainHerd.size()) {
                mainHerd = group;
            }
        }

        // Mark sheep outside the main herd as lost.
        boolean[] belongsToMainHerd = new boolean[count];

        for (int index : mainHerd) {
            belongsToMainHerd[index] = true;
        }

        for (int i = 0; i < count; i++) {

            Sheep sheep = sheepList.get(i);

            sheep.setLost(!belongsToMainHerd[i]);
        }

        lostSheepCount = count - mainHerd.size();
    }



    
    private void createSheepSprings() {

        int columns = 5;
        int rows = 10;

        float restLength = SHEEP_SPACING;

        double stiffness = 4.0;
        double damping = 2.0;

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

                    Sheep below = sheepList.get(index + columns);

                    Spring spring = new Spring(
                        current.getParticle().getCollider(),
                        below.getParticle().getCollider(),
                        restLength,
                        stiffness,
                        damping
                    );

                    connections.add(
                        new SheepConnection(current, below, spring)
                    );
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

        // Display lost sheep counter.
        sketch.fill(255);
        sketch.textSize(22);

        sketch.text(
            "Lost sheep: " + lostSheepCount,
            20,
            35
        );
    }


    public ArrayList<Sheep> getSheepList() {
        return sheepList;
    }

    public World getWorld() {
        return world;
    }

    
}
