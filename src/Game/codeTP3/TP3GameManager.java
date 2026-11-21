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

    //Mesh
    MeshRenderer meshRenderer;
    Mesh [] meshs = new Mesh[3];
    Projectile3DType [] projectilesType = new Projectile3DType[3];
    int currentMesh = 0;


    private final Vector3 projectileOrigin; //Position initiale du projectile

    private Vector3 startShootPosition;

    private Terrain terrain;
    private Projectile3D projectile = null; //projectile lancé
    private int score = 0;

    public TP3GameManager(PApplet sketch) {
        this.sketch = sketch;

        this.terrain = new Terrain(
            400,
            sketch.width * 0.35f,
            sketch.height/2
        );

        world = new World();
        world.isGravityAllowed(true);

        meshRenderer = new MeshRenderer(this.sketch);

        this.projectileOrigin = new Vector3(sketch.width*0.20f,sketch.height/2f, 0);

        setupMesh();

        //shoot();


    }

    public void setupMesh(){

        meshs[0] = new Mesh("src/Game/data/models/basketball/Basketball.obj");
        meshRenderer.addMesh(meshs[0]);
        projectilesType[0] = Projectile3DType.BALL;


        meshs[1] = new Mesh("src/Game/data/models/cannonball/cannonball.obj");
        meshRenderer.addMesh(meshs[1]);
        projectilesType[1] = Projectile3DType.CANNONBALL;

        meshs[2] = new Mesh("src/Game/data/models/laser/laser.obj");
        meshRenderer.addMesh(meshs[2]);
        projectilesType[2] = Projectile3DType.BAR;

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

        //Tracer la visé
        if (this.startShootPosition != null) {
            sketch.line((float)this.startShootPosition.x, (float)this.startShootPosition.y, sketch.mouseX, sketch.mouseY);
        }


        sketch.hint(sketch.ENABLE_DEPTH_TEST);
    }


    //Input

    public void startShoot() {
        this.startShootPosition = new  Vector3(sketch.mouseX, sketch.mouseY, 0);
    }

    //Shoot the projectiles
    public void shoot() {

        if (this.projectile != null) {
            this.projectile.removeProjectile(this.world);
        }

        if (this.startShootPosition != null) {

            //Initial Rotation
            Quaternion quat = Quaternion.fromAxisAngle(
                    Vector3.UP_VECTOR.copy(),
                    0
            );

            this.projectile = new Projectile3D(this.world,
                    this.projectileOrigin,
                    quat,
                    Projectile3DType.BALL,
                    meshs[currentMesh]
            );

            //Calcul de la trajectoire
            Vector3 endShootPosition = new Vector3(sketch.mouseX, sketch.mouseY, 0);
            this.projectile.addForce(this.startShootPosition.sub(endShootPosition).scale(1000));

            this.startShootPosition = null;

        }

    }

    public void switchProjectile() {
        currentMesh = (currentMesh + 1)%3;

    }


    float rotation = 0;
    public void testdraw(){



        rotation+=1;
        Quaternion quat = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            rotation
        );


        meshRenderer.drawMesh(
                meshs[currentMesh],
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
