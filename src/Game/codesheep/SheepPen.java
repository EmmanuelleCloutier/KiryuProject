
package Game.codesheep;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

public class SheepPen {

    private final PApplet sketch;

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public SheepPen(PApplet sketch, float x, float y,
                    float width, float height) {

        this.sketch = sketch;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Vector3 position) {

        return position.x >= x
            && position.x <= x + width
            && position.y >= y
            && position.y <= y + height;
    }

    public boolean overlapsCircle(Vector3 center, double radius) {

        double closestX = Math.max(x,
            Math.min(center.x, x + width));

        double closestY = Math.max(y,
            Math.min(center.y, y + height));

        double dx = center.x - closestX;
        double dy = center.y - closestY;

        return dx * dx + dy * dy <= radius * radius;
    }

    public Vector3 getSafePosition(int index) {

    // Number of sheep per row.
    int columns = 7;

    // Spacing between sheep.
    float spacing = 55f;

    int column = index % columns;
    int row = index / columns;

    // Center the formation inside the pen.
    float centerX = x + width / 2f;
    float centerY = y + height / 2f;

    float startX = centerX - (columns - 1) * spacing / 2f;
    float startY = centerY - (7 - 1) * spacing / 2f;

    return new Vector3(
        startX + column * spacing,
        startY + row * spacing,
        0
    );
}

    public void draw() {

        sketch.pushStyle();

        // Force rectangle coordinates to use top-left corner.
        sketch.rectMode(PApplet.CORNER);

        // Enclosure ground.
        sketch.noStroke();
        sketch.fill(175, 190, 115);
        sketch.rect(x, y, width, height);

        // Fence.
        sketch.noFill();
        sketch.stroke(115, 75, 40);
        sketch.strokeWeight(9);

        // Left fence.
        sketch.line(x, y, x, y + height);

        // Bottom fence.
        sketch.line(x, y + height, x + width, y + height);

        // Right fence.
        sketch.line(x + width, y, x + width, y + height);

        // Top fence with an entrance gap.
        float entranceWidth = 110f;
        float entranceStart = x + (width - entranceWidth) / 2f;

        sketch.line(x, y, entranceStart, y);
        sketch.line(
            entranceStart + entranceWidth,
            y,
            x + width,
            y
        );

        sketch.popStyle();
    }
}
