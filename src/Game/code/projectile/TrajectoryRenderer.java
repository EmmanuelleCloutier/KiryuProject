package Game.code.projectile;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

public class TrajectoryRenderer {

    private final PApplet sketch;

    public TrajectoryRenderer(PApplet sketch) {
        this.sketch = sketch;
    }

    //affiche la position prédite du projectile en utiilisant position, velocity et acceleration
    public void drawTrajectory(
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

            //dessiner un point de la trajectoire prédéfinie
            sketch.circle(
                (float)x,
                (float)y,
                8
            );
        }
    }
}