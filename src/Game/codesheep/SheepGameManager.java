
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


    private static final double HERD_CONNECTION_DISTANCE = 105.0;

    private static final int INITIAL_SHEEP_COUNT = 50;
    private static final float SHEEP_SPACING = 75f;

    private final ArrayList<SheepObstacle> obstacles =
    new ArrayList<>();

    private static final int ROCK_COUNT = 12;
    private static final int BUSH_COUNT = 18;
    
    private final SheepPen sheepPen;
    private int safeSheepCount = 0;

    public SheepGameManager(PApplet sketch) {
        this.sketch = sketch;

        world = new World();
        spriteRenderer = new SpriteRenderer(sketch);

        sheepList = new ArrayList<>();
        random = new Random();
        dog = new Dog(sketch);

        //create the sheep pen 
        sheepPen = new SheepPen(
            sketch,
            70f,
            sketch.height - 330f,
            450f,
            290f
        );

        generateInitialHerd();
        createSheepSprings();
        generateObstacles();
        
    }

    
  
    private void updateSheepPen() {

        for (Sheep sheep : sheepList) {

            if (sheep.isSafe()) {
                continue;
            }

            if (sheepPen.contains(sheep.getPosition())) {

                //assign unique resting space
                Vector3 safePosition =
                    sheepPen.getSafePosition(safeSheepCount);

                sheep.setSafe(true);
                sheep.setLost(false);

                //move the sheep to its resting position
                sheep.getParticle().setPosition(safePosition);

                //stop movement 
                sheep.getParticle().setLinearVelocity(
                    new Vector3(0, 0, 0)
                );

                sheep.getParticle().setAcceleration(
                    new Vector3(0, 0, 0)
                );

                safeSheepCount++;
            }
        }
    }


        
    private void generateObstacles() {

        int total = ROCK_COUNT + BUSH_COUNT;

        for (int i = 0; i < total; i++) {

            SheepObstacle.Type type = i < ROCK_COUNT
                ? SheepObstacle.Type.ROCK
                : SheepObstacle.Type.BUSH;

            boolean placed = false;

            //limits placement 
            for (int attempt = 0; attempt < 200 && !placed; attempt++) {

                double margin = 60.0;

                double x = margin
                    + random.nextDouble() * (sketch.width - margin * 2);

                double y = margin
                    + random.nextDouble() * (sketch.height - margin * 2);

                SheepObstacle candidate =
                    new SheepObstacle(type, x, y);

                boolean valid = true;

                //no obstacles outside the sheep pen
                if (sheepPen.overlapsCircle(
                        candidate.getPosition(),
                        candidate.getRadius() + 20.0)) {

                    valid = false;
                }

                //avoid spawning obstacles on the inital herd
                for (Sheep sheep : sheepList) {

                    double distance = candidate.getPosition()
                        .sub(sheep.getPosition())
                        .getLength();

                    if (distance < candidate.getRadius() + 65.0) {
                        valid = false;
                        break;
                    }
                }

                //prevent obstacles from overlapping 
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

                //add obstacle only when position is valid
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
        double bottomMargin = 55.0;

        if (y > sketch.height - bottomMargin) {
            y = sketch.height - bottomMargin;

            if (vy > 0) {
                vy = 0;
            }
        }

        //apply the corrected position and velocity 
        sheep.getParticle().setPosition(
            new Vector3(x, y, position.z)
        );

        sheep.getParticle().setLinearVelocity(
            new Vector3(vx, vy, velocity.z)
        );
    }

    //sotre the two sheep connected by spring 
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

                //variation randomness for curve sheep blob
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

        //find the groups of sheep connected by proximity
        for (int i = 0; i < count; i++) {

            if (visited[i] || sheepList.get(i).isSafe()) {
                continue;
            }

            ArrayList<Integer> group = new ArrayList<>();
            ArrayList<Integer> queue = new ArrayList<>();

            queue.add(i);
            visited[i] = true;

            //explore nearby sheep using BFS (breadth first search)
            for (int q = 0; q < queue.size(); q++) {

                int currentIndex = queue.get(q);
                group.add(currentIndex);

                Sheep current = sheepList.get(currentIndex);

                for (int j = 0; j < count; j++) {

                    if (visited[j] || sheepList.get(j).isSafe()) {
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

            //the largest connected group is the herd
            if (group.size() > mainHerd.size()) {
                mainHerd = group;
            }
        }

        boolean[] belongsToMainHerd = new boolean[count];

        for (int index : mainHerd) {
            belongsToMainHerd[index] = true;
        }

        //mark sheep outisde of the main herd as lost
        for (int i = 0; i < count; i++) {

            Sheep sheep = sheepList.get(i);

            if (!sheep.isSafe()) {
                sheep.setLost(!belongsToMainHerd[i]);
            }
        }
    }
    
    private void createSheepSprings() {

        int columns = 5;
        int rows = 10;

        float restLength = SHEEP_SPACING;

        double stiffness = 4.0;
        double damping = 2.0;

        //connect neighboring sheep to create the blob structure
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

        dog.update(deltaTime);

        impulseTimer += deltaTime;

        //randonm pulse 
        if (impulseTimer >= 0.4f) {

            impulseTimer = 0f;

            //select a few sheep to move randomly 
            for (int i = 0; i < 4; i++) {

               Sheep sheep = sheepList.get(
                    random.nextInt(sheepList.size())
                );

                if (!sheep.isSafe()) {
                    sheep.applyRandomImpulse(random);
                }
            }
        }

        //apply dog repulsion and reset acceleration when out of range
        for (Sheep sheep : sheepList) {
            sheep.applyDogRepulsion(dog.getPosition());
        }

       updateLostSheep(); //identrify sheep that have separated from the main herd

       //apply spring force only between active sheep in the main herd
        for (SheepConnection connection : connections) {

            if (!connection.first.isLost()
                    && !connection.second.isLost()
                    && !connection.first.isSafe()
                    && !connection.second.isSafe()) {

                connection.spring.applyForce();
            }
        }

        //update particule positions and velocities through the physic engine 
        world.UpdatePhysics(deltaTime);

        //resolve collision between sheep and environmental obstacles
        for (SheepObstacle obstacle : obstacles) {

            for (Sheep sheep : sheepList) {

                obstacle.resolveCollision(sheep);
            }
        }

        updateSheepPen();

        // Keep sheep inside the window.
        for (Sheep sheep : sheepList) {
            resolveWindowCollision(sheep);
        }

        //keep rescued sheep stationary
        for (Sheep sheep : sheepList) {

            if (sheep.isSafe()) {

                sheep.getParticle().setAcceleration(
                    new Vector3(0, 0, 0)
                );

                sheep.getParticle().setLinearVelocity(
                    new Vector3(0, 0, 0)
                );
            }
        }
    }
   

    public void draw() {

        sketch.background(110, 155, 95);

        sheepPen.draw();

        for (SheepObstacle obstacle : obstacles) { //draw obstacle
            obstacle.draw(spriteRenderer);
        }

        for (Sheep sheep : sheepList) { //draw sheep
            sheep.draw(spriteRenderer);
        }

        //draw dog
        dog.draw(spriteRenderer);

        //how much rescue sheep 
        sketch.pushStyle();

        sketch.fill(255);
        sketch.textSize(24);

        sketch.text(
            "Sheep Safe: " + safeSheepCount
                + " / " + sheepList.size(),
            20,
            35
        );

        sketch.popStyle();
    }

    public ArrayList<Sheep> getSheepList() {
        return sheepList;
    }

    public World getWorld() {
        return world;
    }

    
}
