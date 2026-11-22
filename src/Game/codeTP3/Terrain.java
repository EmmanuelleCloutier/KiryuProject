package Game.codeTP3;

import KiryuEngine.KiryuMath.Vector3;
import processing.core.PApplet;
import processing.core.PConstants;

public class Terrain {

    int height; //Largeur du terrain
    float x; //Position sur l'axe x du terrain
    float y; //Position en hauteur du terrain
    int scale = 325;
    float z = -100;

    public Terrain(int height, float x, float y) {
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void draw_terrain(PApplet sketch) {

        sketch.pushMatrix();

        sketch.stroke(255);

        sketch.translate(this.x, this.y, this.z);
        sketch.box(10, 10, 10);

        sketch.translate(scale/2, 0, 0);
        sketch.fill(205,0,0);
        sketch.box(scale, height, 0);

        sketch.translate(3*scale/4, 0, 0);
        sketch.fill(205,75,0);
        sketch.box(scale/2, height, 0);

        sketch.translate(3*scale/8, 0, 0);
        sketch.fill(205,205,0);
        sketch.box(scale/4, height, 0);

        sketch.translate(3*scale/16, 0, 0);
        sketch.fill(50,205,50);
        sketch.box(scale/8, height, 0);

        /**
        sketch.translate(this.x, this.y, 0);
        sketch.rotateZ(PConstants.PI/2);
        sketch.rotateX(PConstants.PI/2);
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

         */
        sketch.popMatrix();

    }

    /**
     * Renvoi si le projectile a touché ou dépassé le sol
     * @param pos : la position du projectile
     * @return
     */
    public boolean checkOnGround(Vector3 pos) {
        return pos.y > this.y+(this.height/2);

    }

    /**
     * retourne si le projectile est à l'intérieur du terrain (il n'a pas dépassé le fond du terrain
     * @param pos : la position du projectile
     * @return
     */
    public boolean checkOutside(Vector3 pos) {
        return pos.x < this.x + ((15*scale)/8.f);
    }

    /**
     * renvoi le nombre de point en fonction de la distance à l'origine en profondeur
     * @param pos
     * @return
     */
    public int computeScore(Vector3 pos) {

        if (!checkOutside(pos) || pos.x < this.x) {
            return 0;
        } else if (pos.x < this.x + scale) {
            return 1;
        } else if (pos.x < this.x + (scale*3)/2) {
            return 2;
        } else if (pos.x < this.x + (scale*7)/4) {
            return 3;
        } else {
            return 4;
        }

    }



}
