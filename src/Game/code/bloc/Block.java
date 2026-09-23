package Game.code.bloc;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Animation;
import KiryuEngine.KiryuRendering.Sprite;
import processing.core.PApplet;

public class Block {

    public static final float SIZE = 75;

    private final Vector3 position;

    private final Sprite sprite;
    private final Animation destroyAnimation;

    public Block(
        Vector3 position,
        Sprite sprite,
        Animation destroyAnimation
    ) {
        this.position = position;
        this.sprite = sprite;
        this.destroyAnimation = destroyAnimation;
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