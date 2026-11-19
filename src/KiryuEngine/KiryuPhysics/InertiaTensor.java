package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuMath.Matrix3;

public class InertiaTensor {


    /**
     *  J = (2/5) * M * R² * I3
      * @param rayon
     * @param masse
     * @return le tenseur d'inertie associé à la sphere
     */
    public static Matrix3 getSphereInertiaTensor(double rayon, double masse) {
        double jsphere = 0.4 * masse * (rayon * rayon);

        Matrix3 J = Matrix3.getIdentity();
        J.multiplyScalar(jsphere);

        return J;
    }

    /**
     * J = (1/12) * M * (largeur² + longueur²) * I3
     * @param largeur
     * @param longueur
     * @param masse
     * @return le tenseur d'inertie associé au parallelepipede
     */
    public static Matrix3 getParallelepipedeInertiaTensor(double largeur, double longueur, double masse) {
        double jpar = (1/.12) * masse * (largeur * largeur + longueur * longueur);

        Matrix3 J = Matrix3.getIdentity();
        J.multiplyScalar(jpar);

        return J;
    }

    /**
     * Iz = (1/2) * M * R²
     * Ix = Iy = (M * R²)/4 + (M * h²)/12
     * @param rayon
     * @param hauteur
     * @param masse
     * @return le tenseur d'inertie associé au cylindre
     */
    public static Matrix3 getCylinderInertiaTensor(double rayon, double hauteur, double masse) {

        double iz = 0.5 * masse * (rayon * rayon);
        double ixy = ((masse * rayon * rayon)/.4) + ((masse * hauteur * hauteur)/.12);

        Matrix3 J = new Matrix3(
                new double[]{ixy, 0, 0,
                            0, ixy, 0,
                            0, 0, iz}
        );

        return J;
    }

}
