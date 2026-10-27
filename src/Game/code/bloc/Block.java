package Game.code.bloc;
import KiryuEngine.KiryuPhysics.Collision.BoxCollider;
import KiryuEngine.KiryuPhysics.RigidBody;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.Animation;
import KiryuEngine.KiryuRendering.Sprite;

public class Block {

    public static final float SIZE = 75;
    private static final double mass = 1;

    private final RigidBody rigidBody;

    private final Vector3 position;

    private final Sprite sprite;
    private final Animation destroyAnimation;

    public Block(
        World world,
        Vector3 position,
        Sprite sprite,
        Animation destroyAnimation
    ) {
        this.position = position;
        this.sprite = sprite;
        this.destroyAnimation = destroyAnimation;
        this.rigidBody = new RigidBody(
            mass,
            position,
            Vector3.ZERO_VECTOR.copy(),
            new BoxCollider(SIZE)
        );
        this.rigidBody.setIsStatic(true);
        world.addRigidBody(this.rigidBody);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Animation getDestroyAnimation() {
        return destroyAnimation;
    }

}