
package Game.codesheep;

import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.SpriteRenderer;

public class SheepObstacle {

    public enum Type {
        ROCK,
        BUSH
    }

    private final Type type;
    private final RigidBody rigidBody;

    private static final String ROCK_SPRITE =
        "Game/data/rocher.png";

    private static final String BUSH_SPRITE =
        "Game/data/buisson.png";

    public SheepObstacle(Type type, double x, double y) {

        this.type = type;
        Vector3 position = new Vector3(x, y, 0);

        //use a different collision radius for each obstacle type
        double radius = type == Type.ROCK ? 38f : 32f;

        this.rigidBody = new RigidBody(100,radius);
        this.rigidBody.setPosition(position);
        this.rigidBody.setIsStatic(true);

    }

    public Vector3 getPosition() {
        return this.rigidBody.getPosition();
    }

    public double getRadius() {
        return this.rigidBody.getCollider().getSize();
    }

    public void draw(SpriteRenderer renderer) {

        //select the sprite and size based on the obstacle type
        String spritePath = type == Type.ROCK
            ? ROCK_SPRITE
            : BUSH_SPRITE;

        float size = type == Type.ROCK ? 85f : 75f;

        renderer.drawSprite(
            spritePath,
            this.getPosition(),
            size,
            size
        );
    }
}
