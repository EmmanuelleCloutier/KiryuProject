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

    private final BlockManager blockManager;

    private final RigidBody rigidBody;

    private boolean destroyed = false;

    private final Sprite sprite;
    private final Animation destroyAnimation;

    public Block(
        World world,
        BlockManager blockManager,
        Vector3 position,
        Sprite sprite,
        Animation destroyAnimation
    ) {
        this.blockManager = blockManager;
        this.sprite = sprite;
        this.destroyAnimation = destroyAnimation;
        this.rigidBody = new RigidBody(
            mass,
            position,
            Vector3.ZERO_VECTOR.copy(),
            new BoxCollider(25)
        );
        this.rigidBody.setCollisionListener(this::onCollision);
        this.rigidBody.setIsStatic(true);
        this.rigidBody.setPosition(new Vector3(position.x,position.y,0));
        world.addRigidBody(this.rigidBody);
    }

    public Vector3 getPosition() {
        return rigidBody.getPosition();
    }

    public Sprite getSprite() {
        return sprite;
    }


    public RigidBody getRigidBody() {
        return rigidBody;
    }


    public Animation getDestroyAnimation() {
        return destroyAnimation;
    }

    private void onCollision(RigidBody other) {

        if (!destroyed) {
            destroyed = true;
            this.rigidBody.setMarkForDestruction(true);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

}