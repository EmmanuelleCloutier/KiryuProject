package KiryuEngine.KiryuPhysics;

import static java.lang.Math.sqrt;

public class Vector3 {
    public static final Vector3 NULL_VECTOR = new Vector3();
    public static final Vector3 ZERO_VECTOR = new Vector3(0,0,0);
    public static final Vector3 UP_VECTOR = new Vector3(0,1,0);
    public static final Vector3 DOWN_VECTOR = new Vector3(0,-1,0);
    public static final Vector3 RIGHT_VECTOR = new Vector3(1,0,0);
    public static final Vector3 LEFT_VECTOR = new Vector3(-1,0,0);
    public static final Vector3 FORWARD_VECTOR = new Vector3(0,0,1);
    public static final Vector3 BACKWARD_VECTOR = new Vector3(0,0,-1);


    private static final float epsilon = 0.00001f;

    public double x;
    public double y;
    public double z;

    public Vector3() {}

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
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Remplace les coordonnées du vecteur.
     * @param other Second vecteur.
     */
    public void set(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    /**
     *
     * @param other Second vecteur.
     * @return retourne si les vecteur sont egaux
     */
    public boolean equals(Vector3 other) {
        return Math.abs(this.x - other.x) < epsilon &&
            Math.abs(this.y - other.y) < epsilon &&
            Math.abs(this.z - other.z) < epsilon;
    }

    /**
     * @param x Abscisse.
     * @param y Ordonnée.
     * @param z Cote.
     * @return retourne si le vecteur est egale au coordonnes fournis
     */
    public boolean equals(double x, double y, double z) {
        return Math.abs(this.x - x) < epsilon &&
            Math.abs(this.y - y) < epsilon &&
            Math.abs(this.z - z) < epsilon;
    }

    /**
     *
     * @return retourne une string formattee des info du vecteur
     * avec une precision de 3 decimales
     */
    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f)", x, y, z);
    }

    /**
     * Calcule la norme (ou longueur) du vecteur.
     * @return Norme <code>L = sqrt(x² + y² + z²)</code>
     */
    public double getLength() {
        return sqrt(x*x + y*y + z*z);
    }

    /**
     * Calcule la norme (ou longueur) du vecteur au carre.
     * @return Norme <code>L² = (x² + y² + z²)</code>
     */
    public double getLengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     *
     * @param other second vecteur
     * @return la distance au carre entre les deux vecteurs
     */
    public double distanceSquared(Vector3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     *
     * @param other second vecteur
     * @return la distance reel entre les deux vecteurs
     */
    public double distance(Vector3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }


    /**
     * Calcule une version normalisée du vecteur.
     * @return Nouveau vecteur, de longueur 1.
     */
    public Vector3 normalize() {


        //les multiplications sont plus rapides que les divisions
        //j'ai optimiser un peu le calcul de la normalisation afin
        //d'avoir le moins de division possible sachant qu'on risque
        //de beaucoup utiliser cette fonction

        double lenSq = getLengthSquared();

        if (lenSq == 0.0f) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            return this;
        }

        double invLen = 1.0f / Math.sqrt(lenSq);
        this.x *= invLen;
        this.y *= invLen;
        this.z *= invLen;

        return this;
    }

    /**
     * Multiplie un vecteur par un scalaire.
     * @param val Coefficient à appliquer au vecteur.
     * @return Nouveau vecteur.
     */
    public Vector3 scale(double val) {
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
     * Soustrait les deux vecteurs.
     * @param other Second vecteur.
     * @return Vecteur <code>v = a - b</code>, nouveau vecteur.
     */
    public Vector3 sub(Vector3 other) {
        double x = this.x - other.x;
        double y = this.y - other.y;
        double z = this.z - other.z;
        return new Vector3(x, y, z);
    }


    /**
     * multiplie les deux vecteurs.
     * @param other Second vecteur.
     * @return Vecteur <code>v = a * b</code>, nouveau vecteur.
     */
    public Vector3 mul(Vector3 other) {
        double x = this.x * other.x;
        double y = this.y * other.y;
        double z = this.z * other.z;
        return new Vector3(x, y, z);
    }


    /**
     * Calcule le produit scalaire des deux vecteurs.
     * @param other Second vecteur.
     * @return Produit <code>S = xa*xb + ya*yb + za*zb</code>
     */
    public double dotProduct(Vector3 other) {
        return (this.x * other.x) +
                (this.y * other.y) +
                (this.z * other.z);
    }

    /**
     * Calcule le produit vectoriel des deux vecteurs.
     * @param other Second vecteur.
     * @return Nouveau vecteur.
     */
    public Vector3 crossProduct(Vector3 other) {
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
        return this.dotProduct(other) == 0;
    }

    /**
     * Renvoie si les deux vecteurs sont colinéaires/parallèles.
     * @param other Second vecteur.
     * @return Booléen. <i>"Les deux vecteurs sont colinéaires."</i>
     */
    public boolean isCollinear(Vector3 other) {
        return this.crossProduct(other).equals(Vector3.ZERO_VECTOR);
    }
}
