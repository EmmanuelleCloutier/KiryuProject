package Game.codeTP3;

import KiryuEngine.KiryuMath.Vector3;
import processing.core.PApplet;

public class Terrain {

    int width; //Largeur du terrain
    float x; //Position sur l'axe x du terrain
    float y; //Position en hauteur du terrain


    public Terrain(int width, float x, float y) {
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public void draw_terrain(PApplet sketch) {

        sketch.stroke(255);

        sketch.translate(this.x, this.y, 0);
        sketch.fill(255);
        sketch.box(10, 10, 10);

        sketch.translate(0, 0, -200);
        sketch.fill(205,0,0);
        sketch.box(width, 1, 400);

        sketch.translate(0, 0, -300);
        sketch.fill(205,75,0);
        sketch.box(width, 1, 200);

        sketch.translate(0, 0, -150);
        sketch.fill(205,205,0);
        sketch.box(width, 1, 100);

        sketch.translate(0, 0, -75);
        sketch.fill(50,205,50);
        sketch.box(width, 1, 50);

    }

    /**
     * Renvoi si le projectile a touché ou dépassé le sol
     * @param pos : la position du projectile
     * @return
     */
    public boolean checkOnGround(Vector3 pos) {
        return pos.y > this.y;

    }


    /**
     * retourne si le projectile est à l'intérieur de l'écran
     * @param pos : la position du projectile
     * @return
     */
    public boolean checkOutside(Vector3 pos) {

        //largeur position en posX > this.x + width/2 et posX < this.x + width/2
        //profondeur position en posZ > 0 et posZ < 750
        return (pos.x > this.x - (this.width/2)) && (pos.x < (this.x + (this.width/2)))
                && (pos.z < 0) &&  (pos.z > -750);
    }

    /**
     * renvoi le nombre de point en fonction de la distance à l'origine en profondeur
     * @param pos
     * @return
     */
    public int computeScore(Vector3 pos) {
        //Si le projectile sort en hauteur (hauteur < 0) mais reste dans les limites du terrain on incrémente le score
        //Sinon on a 0 point
        return 0;
    }



}
