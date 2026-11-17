package Game.codeTP3;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.World;
import processing.core.PApplet;


public class TP3GameManager {

    private final PApplet sketch;
    private final World world;


    private final Vector3 projectileOrigine = Vector3.ZERO_VECTOR.copy(); //Position initiale du projectile

    private Terrain terrain;
    private Projectile3D projectile = null; //projectile lancé
    private int score = 0;

    public TP3GameManager(PApplet sketch) {
        this.sketch = sketch;

        this.terrain = new Terrain(400, sketch.width/2,sketch.height * 0.75f);

        world = new World();
        world.isGravityAllowed(true);
    }


    public void update(float deltaTime) {

        world.updatePhysics(deltaTime);

        //Si le projectile sort du terrain on calcul le score avec la distance
        if (this.projectile != null) {
            if (terrain.checkOutside(new Vector3(0,0,0))) {
                double distance = this.projectile.getPos().distance(this.projectileOrigine);
                score += Math.round(distance);
                this.projectile.removeProjectile(this.world);
                this.projectile = null;
            }
        }


    }

    public void draw() {

        sketch.background(0);

        sketch.camera(sketch.width/2, sketch.mouseY, (sketch.height/2) / sketch.tan(sketch.PI/6), sketch.width/2, sketch.height/2, 0, 0, 1, 0);

        //draw le terrain
        terrain.draw_terrain(sketch);

        //draw the projectile

        //draw the ui
        this.drawUI();


    }

    public void drawUI() {

        sketch.hint(sketch.DISABLE_DEPTH_TEST);
        sketch.camera();
        sketch.ortho();

        //Draw Score
        String scoreText = "Score: " + score;

        sketch.textSize(32);
        sketch.fill(255);
        sketch.text(scoreText, sketch.width/2, 32);




        sketch.hint(sketch.ENABLE_DEPTH_TEST);
    }

}
