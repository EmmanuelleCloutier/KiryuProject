package Game.code.bloc;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

public class Block {

    public static final float SIZE = 75;

    private Vector3 position;

    public Block(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void draw(PApplet sketch) {

        sketch.rectMode(PApplet.CENTER);

        sketch.fill(180);
        sketch.stroke(255);

        sketch.rect(
            (float) position.x,
            (float) position.y,
            SIZE,
            SIZE
        );
    }
}