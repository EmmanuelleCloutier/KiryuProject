
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

public class SheepPen {

    private final PApplet sketch;

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public SheepPen(
        PApplet sketch,
        float x,
        float y,
        float width,
        float height
    ) {
        this.sketch = sketch;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean overlapsCircle(Vector3 center, double radius) {

    double closestX = Math.max(
        x,
        Math.min(center.x, x + width)
    );

    double closestY = Math.max(
        y,
        Math.min(center.y, y + height)
    );

    double dx = center.x - closestX;
    double dy = center.y - closestY;

    return dx * dx + dy * dy < radius * radius;
}

    public boolean contains(Vector3 position) {

        return position.x >= x
            && position.x <= x + width
            && position.y >= y
            && position.y <= y + height;
    }

    public void draw() {

        sketch.pushStyle();

        sketch.fill(90, 180, 100, 100);
        sketch.stroke(60, 110, 60);
        sketch.strokeWeight(4);

        sketch.rect(x, y, width, height);

        sketch.popStyle();
    }
}
