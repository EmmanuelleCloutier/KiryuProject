package Game.codeTP3;

import KiryuEngine.KiryuMath.Matrix3;
import KiryuEngine.KiryuPhysics.Collision.BoxCollider2D;
import KiryuEngine.KiryuPhysics.Collision.Collider;
import KiryuEngine.KiryuPhysics.Collision.SphereCollider2D;
import KiryuEngine.KiryuPhysics.InertiaTensor;

public enum Projectile3DType {

    BALL(Default.DEFAULT_BALL_MASS,
            Default.DEFAULT_BALL_COLLIDER,
            Default.DEFAULT_BALL_INERTIAMATRIX),
    CANNONBALL(Default.DEFAULT_CANONBALL_MASS,
            Default.DEFAULT_CANONBALL_COLLIDER,
            Default.DEFAULT_CANONBALL_INERTIAMATRIX),
    BAR(Default.DEFAULT_BAR_MASS,
            Default.DEFAULT_BAR_COLLIDER,
            Default.DEFAULT_BAR_INERTIAMATRIX);


    public final double mass;
    public final Collider collider;
    public final Matrix3 inertiaMatrix;

    Projectile3DType(double mass, Collider collider, Matrix3 inertiaMatrix) {
        this.mass = mass;
        this.collider = collider;
        this.inertiaMatrix = inertiaMatrix;
    }

    public class Default{

        public static double DEFAULT_BALL_MASS = 5;
        public static Collider DEFAULT_BALL_COLLIDER = new SphereCollider2D(100);
        public static Matrix3 DEFAULT_BALL_INERTIAMATRIX = InertiaTensor.getSphereInertiaTensor(100, DEFAULT_BALL_MASS);

        public static double DEFAULT_CANONBALL_MASS = 5;
        public static Collider DEFAULT_CANONBALL_COLLIDER = new SphereCollider2D(100);
        public static Matrix3 DEFAULT_CANONBALL_INERTIAMATRIX = InertiaTensor.getSphereInertiaTensor(100, DEFAULT_CANONBALL_MASS);


        public static double DEFAULT_BAR_MASS = 5;
        public static Collider DEFAULT_BAR_COLLIDER = new BoxCollider2D(100);
        public static Matrix3 DEFAULT_BAR_INERTIAMATRIX = InertiaTensor.getParallelepipedeInertiaTensor(10, 100, DEFAULT_BAR_MASS);



    }


}
