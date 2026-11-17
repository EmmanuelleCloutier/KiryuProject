package Game.codeTP3;

import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.ThreeDimentional.Mesh;
import KiryuEngine.KiryuRendering.ThreeDimentional.MeshRenderer;
import processing.core.PApplet;


public class TP3GameManager {

    private final PApplet sketch;
    private final World world;


    MeshRenderer meshRenderer;


    private final Vector3 projectileOrigin = Vector3.ZERO_VECTOR.copy(); //Position initiale du projectile

    private Terrain terrain;
    private Projectile3D projectile = null; //projectile lancé
    private int score = 0;

    public TP3GameManager(PApplet sketch) {
        this.sketch = sketch;

        this.terrain = new Terrain(
            400,
            sketch.width/2,
            sketch.height * 0.75f
        );

        world = new World();
        world.isGravityAllowed(true);

        meshRenderer = new MeshRenderer(this.sketch);

        testsetup();

    }


    public void update(float deltaTime) {

        world.updatePhysics(deltaTime);

        //Si le projectile sort du terrain on calcul le score avec la distance
        if (this.projectile != null) {
            if (terrain.checkOutside(new Vector3(0,0,0))) {
                double distance = this.projectile.getPos().distance(this.projectileOrigin);
                score += Math.round(distance);
                this.projectile.removeProjectile(this.world);
                this.projectile = null;
            }
        }


    }


    public void draw() {

        sketch.background(125);
        sketch.lights();

        sketch.camera(
            sketch.width/2,
            sketch.mouseY,
            1000,
            sketch.width/2,
            sketch.height/2,
            0,
            0,
            1,
            0
        );

        testdraw();






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

    Mesh cannonball;
    Mesh basketball;
    Mesh laser;
    public void testsetup(){
        cannonball = new Mesh("src/Game/data/models/cannonball/cannonball.obj");
        meshRenderer.addMesh(cannonball);

        basketball = new Mesh("src/Game/data/models/basketball/Basketball.obj");
        meshRenderer.addMesh(basketball);

        laser = new Mesh("src/Game/data/models/laser/laser.obj");
        meshRenderer.addMesh(laser);

    }

    float rotation = 0;
    public void testdraw(){



        rotation+=1;
        Quaternion quat = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            rotation
        );

        meshRenderer.drawMesh(
            cannonball,
            new Vector3(sketch.width/2f,sketch.height/2f,100),
            quat
        );

        meshRenderer.drawMesh(
            basketball,
            new Vector3(sketch.width/2f - 125,sketch.height/2f,100),
            quat
        );


        meshRenderer.drawMesh(
            laser,
            new Vector3(sketch.width/2f + 125,sketch.height/2f,100),
            quat
        );

    }

}
