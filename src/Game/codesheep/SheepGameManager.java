
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

    private final ArrayList<SheepObstacle> obstacles =
    new ArrayList<>();

    private static final int ROCK_COUNT = 12;
    private static final int BUSH_COUNT = 18;

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
        generateObstacles();
    }

        
    private void generateObstacles() {

        int total = ROCK_COUNT + BUSH_COUNT;

        for (int i = 0; i < total; i++) {

            SheepObstacle.Type type = i < ROCK_COUNT
                ? SheepObstacle.Type.ROCK
                : SheepObstacle.Type.BUSH;

            boolean placed = false;

            // Limit attempts to avoid infinite loops.
            for (int attempt = 0; attempt < 200 && !placed; attempt++) {

                double margin = 60.0;

                double x = margin
                    + random.nextDouble() * (sketch.width - margin * 2);

                double y = margin
                    + random.nextDouble() * (sketch.height - margin * 2);

                SheepObstacle candidate =
                    new SheepObstacle(type, x, y);

                boolean valid = true;

                // Avoid spawning on sheep.
                for (Sheep sheep : sheepList) {

                    double distance = candidate.getPosition()
                        .sub(sheep.getPosition())
                        .getLength();

                    if (distance < candidate.getRadius() + 65.0) {
                        valid = false;
                        break;
                    }
                }

                // Avoid overlapping other obstacles.
                if (valid) {

                    for (SheepObstacle obstacle : obstacles) {

                        double distance = candidate.getPosition()
                            .sub(obstacle.getPosition())
                            .getLength();

                        double minimumDistance =
                            candidate.getRadius()
                            + obstacle.getRadius()
                            + 35.0;

                        if (distance < minimumDistance) {
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid) {
                    obstacles.add(candidate);
                    placed = true;
                }
            }
        }
    }


    private void resolveWindowCollision(Sheep sheep) {

        double radius = 27.5; // Sheep sprite size / 2

        Vector3 position = sheep.getPosition();
        Vector3 velocity = sheep.getParticle().getLinearVelocity();

        double x = position.x;
        double y = position.y;

        double vx = velocity.x;
        double vy = velocity.y;

        // Left boundary.
        if (x < radius) {
            x = radius;
            if (vx < 0) vx = 0;
        }

        // Right boundary.
        if (x > sketch.width - radius) {
            x = sketch.width - radius;
            if (vx > 0) vx = 0;
        }

        // Top boundary.
        if (y < radius) {
            y = radius;
            if (vy < 0) vy = 0;
        }

        // Bottom boundary.
        if (y > sketch.height - radius) {
            y = sketch.height - radius;
            if (vy > 0) vy = 0;
        }

        sheep.getParticle().setPosition(
            new Vector3(x, y, position.z)
        );

        sheep.getParticle().setLinearVelocity(
            new Vector3(vx, vy, velocity.z)
        );
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

        // Resolve sheep-obstacle collisions.
        for (SheepObstacle obstacle : obstacles) {

            for (Sheep sheep : sheepList) {

                obstacle.resolveCollision(sheep);
            }
        }

        // Keep sheep inside the window.
        for (Sheep sheep : sheepList) {
            resolveWindowCollision(sheep);
        }

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

        // Draw obstacles.
        for (SheepObstacle obstacle : obstacles) {
            obstacle.draw(spriteRenderer);
        }

        // Draw sheep.
        for (Sheep sheep : sheepList) {
            sheep.draw(spriteRenderer);
        }

        // Draw dog.
        dog.draw(spriteRenderer);

        // HUD.
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
