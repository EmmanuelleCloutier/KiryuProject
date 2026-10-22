
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.SpriteRenderer;

public class SheepObstacle {

    public enum Type {
        ROCK,
        BUSH
    }

    private final Type type;
    private final Vector3 position;
    private final double radius;

    private static final String ROCK_SPRITE =
        "Game/data/rocher.png";

    private static final String BUSH_SPRITE =
        "Game/data/buisson.png";

    public SheepObstacle(Type type, double x, double y) {

        this.type = type;
        this.position = new Vector3(x, y, 0);

        // Collision radius depends on obstacle type.
        this.radius = type == Type.ROCK ? 38.0 : 32.0;
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public void resolveCollision(Sheep sheep) {

        Vector3 sheepPosition = sheep.getPosition();

        Vector3 difference = sheepPosition.sub(position);
        double distance = difference.getLength();

        // Sheep sprite is 55 pixels wide.
        double sheepRadius = 27.5;
        double minimumDistance = radius + sheepRadius;

        if (distance >= minimumDistance) {
            return;
        }

        // Avoid division by zero.
        Vector3 normal;

        if (distance > 0.0001) {
            normal = difference.scale(1.0 / distance);
        } else {
            normal = new Vector3(1, 0, 0);
        }

        // Push the sheep outside the obstacle.
        Vector3 correctedPosition = position.add(
            normal.scale(minimumDistance)
        );

        sheep.getParticle().setPosition(correctedPosition);

        // Remove velocity going into the obstacle.
        Vector3 velocity = sheep.getParticle().getLinearVelocity();

        double velocityAlongNormal =
            velocity.x * normal.x +
            velocity.y * normal.y;

        if (velocityAlongNormal < 0) {

            Vector3 correctedVelocity = velocity.sub(
                normal.scale(velocityAlongNormal)
            );

            sheep.getParticle().setLinearVelocity(
                correctedVelocity
            );
        }
    }

    public void draw(SpriteRenderer renderer) {

        String spritePath = type == Type.ROCK
            ? ROCK_SPRITE
            : BUSH_SPRITE;

        float size = type == Type.ROCK ? 85f : 75f;

        renderer.drawSprite(
            spritePath,
            position,
            size,
            size
        );
    }
}
