package Game.codeTP3;

import KiryuEngine.KiryuMath.Vector3;
import KiryuEngine.KiryuPhysics.World;
import processing.core.PApplet;


public class TP3GameManager {

    private final PApplet sketch;
    private final World world;


    private final Vector3 projectileOrigine = Vector3.ZERO_VECTOR.copy(); //Position initiale du projectile

    private Projectile3D projectile = null; //projectile lancé
    private int score = 0;

    public TP3GameManager(PApplet sketch) {
        this.sketch = sketch;

        world = new World();
        world.isGravityAllowed(true);
    }


    public void update(float deltaTime) {

        world.updatePhysics(deltaTime);

        if (this.projectile != null) {
            if (checkOutside()) {
                double distance = this.projectile.getPos().distance(this.projectileOrigine);
                score += Math.round(distance);
                this.projectile.removeProjectile(this.world);
                this.projectile = null;
            }
        }


    }

    public void draw() {

        sketch.background(0, 0, 0);
        //draw the projectile

        //draw the score


    }

    //retourne si le projectile est à l'extérieur de l'écran
    public boolean checkOutside() {
        //TODO
        return false;
    }

}
