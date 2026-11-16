
package Game.Levels;

import Game.codeTP3.TP3GameManager;
import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuRendering.ThreeDimentional.Mesh;
import KiryuEngine.KiryuRendering.ThreeDimentional.MeshRenderer;
import processing.core.PApplet;

public class Level_3 {

    PApplet test;
    Mesh testMesh;
    MeshRenderer meshRenderer;

    TP3GameManager gameManager;

    public Level_3(PApplet app) {
        app.lights();
        test = app;
        meshRenderer = new MeshRenderer(app);
        testMesh = new Mesh("Game/data/models/cube/cube.obj");
        meshRenderer.addMesh(testMesh);

        gameManager = new TP3GameManager(app);
    }

    public void update(float deltaTime) {
        gameManager.update(deltaTime);
    }

    public void draw() {
        test.background(0);

        /*
        test.camera(800, 500, 0, 800, 500, 0, 0, 1, 0);
        test.lights();
        Quaternion quat = new Quaternion();
        quat.rotate(new Vector3(5,0,0));
        meshRenderer.drawMesh(
            testMesh,
            new Vector3(800,500,-100),
            quat
        );

         */


        //
        gameManager.draw();
    }
}
