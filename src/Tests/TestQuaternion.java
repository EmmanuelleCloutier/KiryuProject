
package Tests;

import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;

public class TestQuaternion {

    private static final float EPSILON = 0.0001f;

    private static String errorMessage(String test) {
        return "Erreur Quaternion - " + test;
    }

    private static boolean equals(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }

    private static boolean equals(Quaternion a, Quaternion b) {
        return equals(a.w, b.w) &&
               equals(a.x, b.x) &&
               equals(a.y, b.y) &&
               equals(a.z, b.z);
    }

    public static void main(String[] args) {

        System.out.println("TEST Quaternion...");

        Quaternion a = new Quaternion(1, 2, 3, 4);
        Quaternion b = new Quaternion(2, 1, 0, 1);

        //identity
        assert equals(new Quaternion(), Quaternion.identity())
            : errorMessage("identity");

        //magnitude 
        assert equals(a.magnitude(), (float) Math.sqrt(30))
            : errorMessage("magnitude");

        //normalize
        assert equals(a.normalize().magnitude(), 1f)
            : errorMessage("normalize");

        //negation
        assert equals(a.negate(), new Quaternion(-1, -2, -3, -4))
            : errorMessage("negate");

        //conjugate
        assert equals(a.conjugate(), new Quaternion(1, -2, -3, -4))
            : errorMessage("conjugate");

        //inverse
        assert equals(a.multiply(a.inverse()), Quaternion.identity())
            : errorMessage("inverse");

        //multiplication
        assert equals(a.multiply(Quaternion.identity()), a)
            : errorMessage("multiply");

        //subtraction
        assert equals(a.subtract(b), new Quaternion(-1, 1, 3, 3))
            : errorMessage("subtract");

        //produit scalaire
        assert equals(a.dot(b), 8f)
            : errorMessage("dot");

        //exponential
        assert equals(new Quaternion(0, 0, 0, 0).exp(),
                      Quaternion.identity())
            : errorMessage("exp");

        //2D rotation
        Quaternion rotation2D = Quaternion.fromAngle2D(90);
        Vector3 result2D = rotation2D.rotate2D(new Vector3(1, 0, 0));

        assert equals((float) result2D.x, 0f) &&
               equals((float) result2D.y, 1f)
            : errorMessage("rotation 2D");

        //3D rotation
        Quaternion rotation3D = Quaternion.fromAxisAngle(
            new Vector3(0, 1, 0), 90
        );

        Vector3 result3D = rotation3D.rotate(new Vector3(1, 0, 0));

        assert equals((float) result3D.x, 0f) &&
               equals((float) result3D.z, -1f)
            : errorMessage("rotation 3D");

        //SLERP
        Quaternion start = Quaternion.fromAngle2D(0);
        Quaternion end = Quaternion.fromAngle2D(90);
        Quaternion middle = Quaternion.slerp(start, end, 0.5f);

        Vector3 result = middle.rotate2D(new Vector3(1, 0, 0));

        assert equals((float) result.x, 0.7071068f) &&
               equals((float) result.y, 0.7071068f)
            : errorMessage("SLERP");

        System.out.println("TEST Quaternion termine : tous les tests passent !");
    }
}
