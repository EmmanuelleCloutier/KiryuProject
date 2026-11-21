package Game.codeTP3;

import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.Collision.BoxCollider2D;
import KiryuEngine.KiryuPhysics.Collision.SphereCollider2D;
import KiryuEngine.KiryuPhysics.World;
import KiryuEngine.KiryuRendering.ThreeDimentional.Mesh;
import KiryuEngine.KiryuRendering.ThreeDimentional.MeshRenderer;
import processing.core.PApplet;
import processing.core.PConstants;


public class TP3GameManager {

    private final PApplet sketch;
    private final World world;

    //Mesh
    MeshRenderer meshRenderer;
    Mesh [] meshs = new Mesh[3];
    int currentMesh = 0;


    private final Vector3 projectileOrigin; //Position initiale du projectile

    private Terrain terrain;
    private Projectile3D projectile = null; //projectile lancé
    private int score = 0;

    public TP3GameManager(PApplet sketch) {
        this.sketch = sketch;

        this.terrain = new Terrain(
            400,
            sketch.width * 0.25f,
            sketch.height/2
        );

        world = new World();
        world.isGravityAllowed(true);

        meshRenderer = new MeshRenderer(this.sketch);

        this.projectileOrigin = new Vector3(sketch.width*0.10f,sketch.height/2f, 0);

        setupMesh();

        //shoot();


    }

    public void setupMesh(){

        meshs[0] = new Mesh("src/Game/data/models/basketball/Basketball.obj");
        meshRenderer.addMesh(meshs[0]);

        meshs[1] = new Mesh("src/Game/data/models/cannonball/cannonball.obj");
        meshRenderer.addMesh(meshs[1]);

        meshs[2] = new Mesh("src/Game/data/models/laser/laser.obj");
        meshRenderer.addMesh(meshs[2]);

    }

//Update

    public void update(float deltaTime) {

        world.updatePhysics(deltaTime);

        //Si le projectile sort du terrain on calcul le score avec la distance

        if (this.projectile != null) {
            if (terrain.checkOnGround(this.projectile.getPos())) {
                //double distance = this.projectile.getPos().distance(this.projectileOrigin);

                //score += Math.round(distance);
                this.projectile.removeProjectile(this.world);
                this.projectile = null;
            }
        }



    }

//draw

    public void draw() {

        sketch.background(125);
        sketch.lights();

        sketch.camera(
            sketch.width/2,
                sketch.height/2,
            1000,
            sketch.width/2,
            sketch.height/2,
            0,
            0,
            1,
            0
        );



        //draw le terrain
        terrain.draw_terrain(sketch);

        //draw the projectile
        if (this.projectile != null) {
            this.projectile.drawProjectile(this.meshRenderer);
        } else {
            testdraw();
        }

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


    //Input

    //Shoot the projectiles
    public void shoot() {

        if (this.projectile != null) {
            this.projectile.removeProjectile(this.world);
        }

        this.projectile = new Projectile3D(this.world,
                this.projectileOrigin,
                Projectile3DType.BALL,
                meshs[currentMesh]);
        this.projectile.addForce(new Vector3(10000,0,0));

    }


    float rotation = 0;
    public void testdraw(){



        rotation+=1;
        Quaternion quat = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            rotation
        );


        meshRenderer.drawMesh(
                meshs[0],
            this.projectileOrigin,//new Vector3(sketch.width/2f + 100,sketch.height/2f,0),
            quat
        );


        /**
        meshRenderer.drawMesh(
            meshs[currentMesh],
            new Vector3(sketch.width/2f - 125,sketch.height/2f,100),
            quat
        );


        meshRenderer.drawMesh(
            meshs[2],
            new Vector3(sketch.width/2f + 125,sketch.height/2f,100),
            quat
        );*/

    }

}
