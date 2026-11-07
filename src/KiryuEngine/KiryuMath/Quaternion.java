
package KiryuEngine.KiryuMath;

public class Quaternion {

    public float w;
    public float x;
    public float y;
    public float z;

    private static final float EPSILON = 0.000001f;

    //identity quaternion
    public Quaternion() {
        this(1, 0, 0, 0);
    }

    //constructor
    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //identity rotation
    public static Quaternion identity() {
        return new Quaternion(1, 0, 0, 0);
    }

    //magnitude, get quaternion lenght
    public float magnitude() {
        return (float) Math.sqrt(
            w * w + x * x + y * y + z * z
        );
    }

    //normalize
    public Quaternion normalize() {
        float length = magnitude();

        if (length < EPSILON) {
            return identity();
        }

        return new Quaternion(
            w / length,
            x / length,
            y / length,
            z / length
        );
    }

    //conjugate 
    public Quaternion conjugate() {
        return new Quaternion(w, -x, -y, -z);
    }

    //inverse
    public Quaternion inverse() {
        float lengthSquared =
            w * w + x * x + y * y + z * z;

        if (lengthSquared < EPSILON) {
            throw new ArithmeticException(
                "Cannot invert a zero quaternion"
            );
        }

        Quaternion c = conjugate();

        return new Quaternion(
            c.w / lengthSquared,
            c.x / lengthSquared,
            c.y / lengthSquared,
            c.z / lengthSquared
        );
    }

    //multiplication
    public Quaternion multiply(Quaternion q) {
        return new Quaternion(
            w * q.w - x * q.x - y * q.y - z * q.z,
            w * q.x + x * q.w + y * q.z - z * q.y,
            w * q.y - x * q.z + y * q.w + z * q.x,
            w * q.z + x * q.y - y * q.x + z * q.w
        );
    }

    //2D rotation
    public static Quaternion fromAngle2D(float degrees) {
        float halfAngle =
            (float) Math.toRadians(degrees) / 2f;

        return new Quaternion(
            (float) Math.cos(halfAngle),
            0,
            0,
            (float) Math.sin(halfAngle)
        );
    }

    //3D rotation from axis
    public static Quaternion fromAxisAngle(
        Vector3 axis,
        float degrees
    ) {
        float length = (float) Math.sqrt(
            axis.x * axis.x +
            axis.y * axis.y +
            axis.z * axis.z
        );

        if (length < EPSILON) {
            throw new IllegalArgumentException(
                "Rotation axis cannot be zero"
            );
        }

        float halfAngle =
            (float) Math.toRadians(degrees) / 2f;

        float sin = (float) Math.sin(halfAngle);

        return new Quaternion(
            (float) Math.cos(halfAngle),
            (float) (axis.x / length * sin),
            (float) (axis.y / length * sin),
            (float) (axis.z / length * sin)
        );
    }

    //rotate a 3D with quaternion
    public Vector3 rotate(Vector3 vector) {
        Quaternion rotation = normalize();

        Quaternion point = new Quaternion(
            0,
            (float) vector.x,
            (float) vector.y,
            (float) vector.z
        );

        Quaternion result = rotation
            .multiply(point)
            .multiply(rotation.conjugate());

        return new Vector3(
            result.x,
            result.y,
            result.z
        );
    }

    //2D rotation with z axis
    public Vector3 rotate2D(Vector3 vector) {
        return rotate(new Vector3(
            vector.x,
            vector.y,
            0
        ));
    }

    @Override
    public String toString() {
        return "Quaternion(" +
            w + ", " +
            x + ", " +
            y + ", " +
            z + ")";
    }

    //negate
    public Quaternion negate() {
        return new Quaternion(
            -w,
            -x,
            -y,
            -z
        );
    }

    
    //soustraction with quarternions
    public Quaternion subtract(Quaternion q) {
        return new Quaternion(
            w - q.w,
            x - q.x,
            y - q.y,
            z - q.z
        );
    }

    
    //produit scalaire between two quaternions
    public float dot(Quaternion q) {
        return w * q.w +
               x * q.x +
               y * q.y +
               z * q.z;
    }

    
    //exponentiation quaternions
    public Quaternion exp() {
        float vectorLength = (float) Math.sqrt(
            x * x + y * y + z * z
        );

        float expW = (float) Math.exp(w);

        float cos = (float) Math.cos(vectorLength);

        float scale;

        if (vectorLength < EPSILON) {
            scale = 1f;
        } else {
            scale = (float) Math.sin(vectorLength)
                    / vectorLength;
        }

        return new Quaternion(
            expW * cos,
            expW * scale * x,
            expW * scale * y,
            expW * scale * z
        );
    }

    
    //SLERP (spherical linear interpolation)
    public static Quaternion slerp(
        Quaternion start,
        Quaternion end,
        float t
    ) {
        //clamp interpolation factor
        t = Math.max(0f, Math.min(1f, t));

        Quaternion q1 = start.normalize();
        Quaternion q2 = end.normalize();

        float dot = q1.dot(q2);

        //use shortest rotation path
        if (dot < 0f) {
            q2 = q2.negate();
            dot = -dot;
        }

        //clamp produit scalaire
        dot = Math.max(-1f, Math.min(1f, dot));

        //linear interpolation for close rotations
        if (dot > 0.9995f) {
            Quaternion result = new Quaternion(
                q1.w + t * (q2.w - q1.w),
                q1.x + t * (q2.x - q1.x),
                q1.y + t * (q2.y - q1.y),
                q1.z + t * (q2.z - q1.z)
            );

            return result.normalize();
        }

        //interpolation spherical
        float theta = (float) Math.acos(dot);
        float sinTheta = (float) Math.sin(theta);

        float weight1 = (float) Math.sin((1f - t) * theta)
                        / sinTheta;

        float weight2 = (float) Math.sin(t * theta)
                        / sinTheta;

        return new Quaternion(
            weight1 * q1.w + weight2 * q2.w,
            weight1 * q1.x + weight2 * q2.x,
            weight1 * q1.y + weight2 * q2.y,
            weight1 * q1.z + weight2 * q2.z
        ).normalize();
    }
}
