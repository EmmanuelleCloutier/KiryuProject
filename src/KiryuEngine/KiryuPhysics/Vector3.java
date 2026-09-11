package KiryuEngine.KiryuPhysics;

import static java.lang.Math.sqrt;

public class Vector3 {
    double x;
    double y;
    double z;

    Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * @return Vecteur de longueur 1.
     */
    public Vector3 normalize() {
        double norme = getNorme();
        double x = this.x / norme;
        double y = this.y / norme;
        double z = this.z / norme;

        return new Vector3(x, y, z);
    }

    /**
     * Additionne deux vecteurs.
     * @param a Premier vecteur à additionner.
     * @param b Second vecteur à additionner.
     * @return Vecteur <code>v = a + b</code>
     */
    public static Vector3 add(Vector3 a, Vector3 b) {
        double x = a.x + b.x;
        double y = a.y + b.y;
        double z = a.z + b.z;
        return new Vector3(x, y, z);
    }

    /**
     * Calcule le produit scalaire de deux vecteurs.
     * @param a Premier vecteur.
     * @param b Second vecteur.
     * @return Produit <code>S = xa*xb + ya*yb + za*zb</code>
     */
    public static double produitScalaire(Vector3 a, Vector3 b) {
        double produit =
                (a.x * b.x) +
                (a.y * b.y) +
                (a.z * b.z);
        return produit;
    }

    /**
     * Calcule le produit vectoriel de deux vecteurs.
     * @param a Premier vecteur.
     * @param b Second vecteur.
     * @return Produit vectoriel.
     */
    public static Vector3 produitVectoriel(Vector3 a, Vector3 b) {
        double x = a.y*b.z - a.z*b.y;
        double y = a.z*b.x - a.x*b.z;
        double z = a.x*b.y - a.y*b.x;
        return new Vector3(x, y, z);
    }
}
