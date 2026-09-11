package KiryuEngine.KiryuPhysics;

import static java.lang.Math.sqrt;

public class Vector3 {
    public static final Vector3 NULL_VECTOR = new Vector3();

    double x;
    double y;
    double z;

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(Vector3 vecteur) {
        this.x = vecteur.x;
        this.y = vecteur.y;
        this.z = vecteur.z;
    }

    /**
     * Remplace les coordonnées du vecteur.
     * @param x Abscisse.
     * @param y Ordonnée.
     * @param z Cote.
     * @return Soi.
     */
    public Vector3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public boolean equals(Vector3 other) {
        return
                this.x == other.x &&
                this.y == other.y &&
                this.z == other.z;
    }
    public boolean equals(double x, double y, double z) {
        return
                this.x == x &&
                this.y == y &&
                this.z == z;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    /**
     * Calcule la norme (ou longueur) du vecteur.
     * @return Norme <code>L = sqrt(x² + y² + z²)</code>
     */
    public double getNorme() {
        double norme = sqrt(
                Math.pow(x, 2) +
                Math.pow(y, 2) +
                Math.pow(z, 2)
        );
        return norme;
    }

    /**
     * Calcule une version normalisée du vecteur.
     * @return Nouveau vecteur, de longueur 1.
     */
    public Vector3 normalize() {
        double norme = getNorme();
        if (norme == 0)
            return new Vector3(this);

        double x = this.x / norme;
        double y = this.y / norme;
        double z = this.z / norme;

        return new Vector3(x, y, z);
    }

    /**
     * Multiplie un vecteur par un scalaire.
     * @param val Coefficient à appliquer au vecteur.
     * @return Nouveau vecteur.
     */
    public Vector3 mult(double val) {
        double x = val * this.x;
        double y = val * this.y;
        double z = val * this.z;
        return new Vector3(x, y, z);
    }

    /**
     * Additionne les deux vecteurs.
     * @param other Second vecteur.
     * @return Vecteur <code>v = a + b</code>, nouveau vecteur.
     */
    public Vector3 add(Vector3 other) {
        double x = this.x + other.x;
        double y = this.y + other.y;
        double z = this.z + other.z;
        return new Vector3(x, y, z);
    }

    /**
     * Calcule le produit scalaire des deux vecteurs.
     * @param other Second vecteur.
     * @return Produit <code>S = xa*xb + ya*yb + za*zb</code>
     */
    public double produitScalaire(Vector3 other) {
        double produit =
                (this.x * other.x) +
                (this.y * other.y) +
                (this.z * other.z);
        return produit;
    }

    /**
     * Calcule le produit vectoriel des deux vecteurs.
     * @param other Second vecteur.
     * @return Nouveau vecteur.
     */
    public Vector3 produitVectoriel(Vector3 other) {
        double x = this.y*other.z - this.z*other.y;
        double y = this.z*other.x - this.x*other.z;
        double z = this.x*other.y - this.y*other.x;
        return new Vector3(x, y, z);
    }

    /**
     * Renvoie si les deux vecteurs sont orthogonaux/perpendiculaires.
     * @param other Second vecteur.
     * @return Booléen. <i>"Les deux vecteurs sont orthogonaux."</i>
     */
    public boolean isOrthogonal(Vector3 other) {
        return this.produitScalaire(other) == 0;
    }

    /**
     * Renvoie si les deux vecteurs sont colinéaires/parallèles.
     * @param other Second vecteur.
     * @return Booléen. <i>"Les deux vecteurs sont colinéaires."</i>
     */
    public boolean isColineaire(Vector3 other) {
        return this.produitVectoriel(other).equals(Vector3.NULL_VECTOR);
    }
}
