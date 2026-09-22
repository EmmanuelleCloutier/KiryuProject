package Game.code.bloc;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Animation;
import KiryuEngine.KiryuRendering.Sprite;
import processing.core.PApplet;

public class Block {

    public static final float SIZE = 75;

    //position du centre du bloc dans le jeu
    private final Vector3 position;

    private final Sprite sprite;

    private final Animation destroyAnimation;

    public Block(PApplet sketch, Vector3 position) {
        this.position = position;
        this.sprite = new Sprite(
            "Game/data/brick_tillable.png",
            SIZE,
            SIZE
        );

        this.destroyAnimation = new Animation(
            "Game/data/destroy.gif",
            50,
            50,
            false
        );
        destroyAnimation.loadRenderableImage(sketch,"explosion.gif");

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