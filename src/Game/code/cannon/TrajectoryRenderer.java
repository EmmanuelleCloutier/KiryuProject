package Game.code.cannon;

import KiryuEngine.KiryuPhysics.Force;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Sprite;
import KiryuEngine.KiryuRendering.SpriteRenderer;
import processing.core.PApplet;

public class TrajectoryRenderer extends SpriteRenderer {

    private static final float SIZE = 8;

    private final Sprite sprite;
    public TrajectoryRenderer(PApplet sketch) {
        super(sketch);
        this.sprite = new Sprite(
            "Game/data/trajectoryball",
            SIZE,
            SIZE
        );
    }

    //affiche la position prédite du projectile en utiilisant position, velocity et acceleration
    public void drawCurvedTrajectory(
        Vector3 startPosition,
        Vector3 velocity,
        Vector3 acceleration
    ) {

        //interval entre chaque point
        float timeStep = 0.15f;

        //nombre de point pour display la trajectoire 
        int pointCount = 30;

    
        for (int i = 0; i < pointCount; i++) {

            //nombre de point utilisé pour display la trajectoire
            float t = i * timeStep;

            //calculer le x prédit 
            double x =
                startPosition.x
                + velocity.x * t
                + 0.5 * acceleration.x * t * t;

            //calculer le y prédit
            double y =
                startPosition.y
                + velocity.y * t
                + 0.5 * acceleration.y * t * t;

            Vector3 position = new Vector3(x,y,0);

            //dessiner un point de la trajectoire prédéfinie
            this.getSketch().fill(100,100,255);
            this.getSketch().circle(
                (float)x,
                (float)y,
                8
            );

            // commented for now since we down have a sprite yet
            //this.drawSprite(sprite,position);
        }
    }

    public void drawStraightTrajectory(
        Vector3 startPosition,
        Vector3 velocity
    ) {

        //interval entre chaque point
        float timeStep = 0.15f;

        //nombre de point pour display la trajectoire
        int pointCount = 30;


        for (int i = 0; i < pointCount; i++) {

            //nombre de point utilisé pour display la trajectoire
            float t = i * timeStep;

            //calculer le x prédit
            double x =
                startPosition.x
                    + velocity.x * t
                    + 0.5 * 1 * t * t;

            //calculer le y prédit
            double y =
                startPosition.y
                    + velocity.y * t;

            Vector3 position = new Vector3(x,y,0);

            //dessiner un point de la trajectoire prédéfinie
            this.getSketch().circle(
                (float)x,
                (float)y,
                8
            );

            // commented for now since we down have a sprite yet
            //this.drawSprite(sprite,position);
        }
    }


    public Sprite getSprite() {
        return sprite;
    }
}