
package Tests;


import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuaternionTest {

    private static final float EPSILON = 1e-5f;

    private Quaternion quaternion;

    @BeforeEach
    void setUp() {
        quaternion = new Quaternion(1, 2, 3, 4);
    }

    // --------------------------------------------------
    // CONSTRUCTORS
    // --------------------------------------------------

    @Test
    void defaultConstructorCreatesIdentityQuaternion() {
        Quaternion result = new Quaternion();

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void parameterizedConstructorSetsComponents() {
        Quaternion result = new Quaternion(1, 2, 3, 4);

        assertQuaternionEquals(1, 2, 3, 4, result);
    }

    // --------------------------------------------------
    // IDENTITY
    // --------------------------------------------------

    @Test
    void identityReturnsIdentityQuaternion() {
        Quaternion result = Quaternion.identity();

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void identityReturnsNewInstance() {
        Quaternion first = Quaternion.identity();
        Quaternion second = Quaternion.identity();

        assertNotSame(first, second);
    }

    // --------------------------------------------------
    // MAGNITUDE
    // --------------------------------------------------

    @Test
    void magnitudeReturnsCorrectValue() {
        assertEquals(Math.sqrt(30), quaternion.magnitude(), EPSILON);
    }

    @Test
    void identityHasMagnitudeOne() {
        assertEquals(1, new Quaternion().magnitude(), EPSILON);
    }

    @Test
    void zeroQuaternionHasMagnitudeZero() {
        Quaternion zero = new Quaternion(0, 0, 0, 0);

        assertEquals(0, zero.magnitude(), EPSILON);
    }

    // --------------------------------------------------
    // NORMALIZATION
    // --------------------------------------------------

    @Test
    void normalizeReturnsUnitQuaternion() {
        Quaternion result = quaternion.normalize();

        assertEquals(1, result.magnitude(), EPSILON);
    }

    @Test
    void normalizeDoesNotModifyOriginalQuaternion() {
        Quaternion original = new Quaternion(1, 2, 3, 4);

        original.normalize();

        assertQuaternionEquals(1, 2, 3, 4, quaternion);
    }

    @Test
    void normalizeReturnsNewQuaternion() {
        Quaternion result = quaternion.normalize();

        assertNotSame(quaternion, result);
    }

    @Test
    void normalizeZeroQuaternionReturnsIdentity() {
        Quaternion zero = new Quaternion(0, 0, 0, 0);

        Quaternion result = zero.normalize();

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void normalizeIdentityReturnsIdentity() {
        Quaternion result = Quaternion.identity().normalize();

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    // --------------------------------------------------
    // CONJUGATE
    // --------------------------------------------------

    @Test
    void conjugateNegatesVectorComponents() {
        Quaternion result = quaternion.conjugate();

        assertQuaternionEquals(1, -2, -3, -4, result);
    }

    @Test
    void conjugateDoesNotModifyOriginalQuaternion() {
        quaternion.conjugate();

        assertQuaternionEquals(1, 2, 3, 4, quaternion);
    }

    @Test
    void conjugateReturnsNewQuaternion() {
        Quaternion result = quaternion.conjugate();

        assertNotSame(quaternion, result);
    }

    @Test
    void conjugateTwiceReturnsOriginalQuaternion() {
        Quaternion result = quaternion.conjugate().conjugate();

        assertQuaternionEquals(quaternion, result);
    }

    // --------------------------------------------------
    // INVERSE
    // --------------------------------------------------

    @Test
    void inverseReturnsCorrectValue() {
        Quaternion result = quaternion.inverse();

        assertQuaternionEquals(
            1f / 30f,
            -2f / 30f,
            -3f / 30f,
            -4f / 30f,
            result
        );
    }

    @Test
    void inverseMultipliedByOriginalReturnsIdentity() {
        Quaternion result = quaternion.multiply(quaternion.inverse());

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void originalMultipliedByInverseReturnsIdentity() {
        Quaternion result = quaternion.inverse().multiply(quaternion);

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void inverseOfIdentityIsIdentity() {
        Quaternion result = Quaternion.identity().inverse();

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void inverseOfZeroQuaternionThrowsException() {
        Quaternion zero = new Quaternion(0, 0, 0, 0);

        assertThrows(
            ArithmeticException.class,
            zero::inverse
        );
    }

    // --------------------------------------------------
    // MULTIPLICATION
    // --------------------------------------------------

    @Test
    void multiplyReturnsCorrectValue() {
        Quaternion first = new Quaternion(1, 2, 3, 4);
        Quaternion second = new Quaternion(5, 6, 7, 8);

        Quaternion result = first.multiply(second);

        assertQuaternionEquals(-60, 12, 30, 24, result);
    }

    @Test
    void multiplyByIdentityReturnsOriginalValue() {
        Quaternion result = quaternion.multiply(Quaternion.identity());

        assertQuaternionEquals(quaternion, result);
    }

    @Test
    void identityMultipliedByQuaternionReturnsOriginalValue() {
        Quaternion result = Quaternion.identity().multiply(quaternion);

        assertQuaternionEquals(quaternion, result);
    }

    @Test
    void multiplyDoesNotModifyOriginalQuaternions() {
        Quaternion first = new Quaternion(1, 2, 3, 4);
        Quaternion second = new Quaternion(5, 6, 7, 8);

        first.multiply(second);

        assertQuaternionEquals(1, 2, 3, 4, first);
        assertQuaternionEquals(5, 6, 7, 8, second);
    }

    @Test
    void multiplicationIsNotCommutative() {
        Quaternion first = new Quaternion(1, 2, 3, 4);
        Quaternion second = new Quaternion(5, 6, 7, 8);

        Quaternion ab = first.multiply(second);
        Quaternion ba = second.multiply(first);

        assertFalse(quaternionEquals(ab, ba));
    }

    // --------------------------------------------------
    // 2D ROTATION
    // --------------------------------------------------

    @Test
    void fromAngle2DZeroDegreesReturnsIdentity() {
        Quaternion result = Quaternion.fromAngle2D(0);

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void fromAngle2D90DegreesReturnsCorrectQuaternion() {
        Quaternion result = Quaternion.fromAngle2D(90);

        float halfAngle = (float) Math.toRadians(45);

        assertQuaternionEquals(
            (float) Math.cos(halfAngle),
            0,
            0,
            (float) Math.sin(halfAngle),
            result
        );
    }

    @Test
    void fromAngle2D180DegreesReturnsCorrectQuaternion() {
        Quaternion result = Quaternion.fromAngle2D(180);

        assertEquals(0, result.w, EPSILON);
        assertEquals(0, result.x, EPSILON);
        assertEquals(0, result.y, EPSILON);
        assertEquals(1, result.z, EPSILON);
    }

    // --------------------------------------------------
    // AXIS ANGLE
    // --------------------------------------------------

    @Test
    void fromAxisAngleZeroDegreesReturnsIdentity() {
        Quaternion result = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            0
        );

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void fromAxisAngle90DegreesAroundZ() {
        Quaternion result = Quaternion.fromAxisAngle(
            Vector3.FORWARD_VECTOR.copy(),
            90
        );

        float halfAngle = (float) Math.toRadians(45);

        assertQuaternionEquals(
            (float) Math.cos(halfAngle),
            0,
            0,
            (float) Math.sin(halfAngle),
            result
        );
    }

    @Test
    void fromAxisAngleNormalizesAxis() {
        Quaternion first = Quaternion.fromAxisAngle(
            new Vector3(0, 0, 1),
            90
        );

        Quaternion second = Quaternion.fromAxisAngle(
            new Vector3(0, 0, 10),
            90
        );

        assertQuaternionEquals(first, second);
    }

    @Test
    void fromAxisAngleRejectsZeroAxis() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Quaternion.fromAxisAngle(
                Vector3.ZERO_VECTOR.copy(),
                90
            )
        );
    }

    @Test
    void fromAxisAngleReturnsUnitQuaternion() {
        Quaternion result = Quaternion.fromAxisAngle(
            new Vector3(1, 2, 3),
            90
        );

        assertEquals(1, result.magnitude(), EPSILON);
    }

    // --------------------------------------------------
    // ROTATION
    // --------------------------------------------------

    @Test
    void rotateByIdentityPreservesVector() {
        Vector3 vector = new Vector3(1, 2, 3);

        Vector3 result = Quaternion.identity().rotate(vector);

        assertVectorEquals(vector, result);
    }

    @Test
    void rotate90DegreesAroundZAxis() {
        Quaternion rotation = Quaternion.fromAxisAngle(
            Vector3.FORWARD_VECTOR.copy(),
            90
        );

        Vector3 result = rotation.rotate(Vector3.RIGHT_VECTOR.copy());

        assertVectorEquals(0, 1, 0, result);
    }

    @Test
    void rotate90DegreesAroundXAxis() {
        Quaternion rotation = Quaternion.fromAxisAngle(
            Vector3.RIGHT_VECTOR.copy(),
            90
        );

        Vector3 result = rotation.rotate(Vector3.UP_VECTOR.copy());

        assertVectorEquals(0, 0, 1, result);
    }

    @Test
    void rotate90DegreesAroundYAxis() {
        Quaternion rotation = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            90
        );

        Vector3 result = rotation.rotate(Vector3.FORWARD_VECTOR.copy());

        assertVectorEquals(1, 0, 0, result);
    }

    @Test
    void rotatePreservesVectorLength() {
        Vector3 original = new Vector3(1, 2, 3);

        Quaternion rotation = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            45
        );

        Vector3 result = rotation.rotate(original);

        assertEquals(
            original.getLength(),
            result.getLength(),
            EPSILON
        );
    }

    @Test
    void rotateDoesNotModifyOriginalVector() {
        Vector3 original = new Vector3(1, 2, 3);

        Quaternion rotation = Quaternion.fromAxisAngle(
            Vector3.UP_VECTOR.copy(),
            45
        );

        rotation.rotate(original);

        assertVectorEquals(1, 2, 3, original);
    }

    @Test
    void rotateWithNonNormalizedQuaternionWorks() {
        Quaternion rotation = Quaternion.fromAxisAngle(
            Vector3.FORWARD_VECTOR.copy(),
            90
        );

        Quaternion scaledRotation = new Quaternion(
            rotation.w * 5,
            rotation.x * 5,
            rotation.y * 5,
            rotation.z * 5
        );

        Vector3 result = scaledRotation.rotate(Vector3.RIGHT_VECTOR.copy());

        assertVectorEquals(0, 1, 0, result);
    }

    // --------------------------------------------------
    // 2D VECTOR ROTATION
    // --------------------------------------------------

    @Test
    void rotate2D90DegreesRotatesVector() {
        Quaternion rotation = Quaternion.fromAngle2D(90);

        Vector3 result = rotation.rotate2D(
            new Vector3(1, 0, 0)
        );

        assertVectorEquals(0, 1, 0, result);
    }

    @Test
    void rotate2DDoesNotPreserveOriginalZCoordinate() {
        Quaternion rotation = Quaternion.identity();

        Vector3 result = rotation.rotate2D(
            new Vector3(1, 2, 5)
        );

        assertEquals(0, result.z, EPSILON);
    }

    // --------------------------------------------------
    // NEGATION
    // --------------------------------------------------

    @Test
    void negateReturnsNegativeQuaternion() {
        Quaternion result = quaternion.negate();

        assertQuaternionEquals(-1, -2, -3, -4, result);
    }

    @Test
    void negateDoesNotModifyOriginalQuaternion() {
        quaternion.negate();

        assertQuaternionEquals(1, 2, 3, 4, quaternion);
    }

    @Test
    void negateTwiceReturnsOriginalQuaternion() {
        Quaternion result = quaternion.negate().negate();

        assertQuaternionEquals(quaternion, result);
    }

    // --------------------------------------------------
    // SUBTRACTION
    // --------------------------------------------------

    @Test
    void subtractReturnsCorrectValue() {
        Quaternion other = new Quaternion(5, 6, 7, 8);

        Quaternion result = quaternion.subtract(other);

        assertQuaternionEquals(-4, -4, -4, -4, result);
    }

    @Test
    void subtractDoesNotModifyOriginalQuaternions() {
        Quaternion other = new Quaternion(5, 6, 7, 8);

        quaternion.subtract(other);

        assertQuaternionEquals(1, 2, 3, 4, quaternion);
        assertQuaternionEquals(5, 6, 7, 8, other);
    }

    // --------------------------------------------------
    // DOT PRODUCT
    // --------------------------------------------------

    @Test
    void dotReturnsCorrectValue() {
        Quaternion other = new Quaternion(5, 6, 7, 8);

        assertEquals(70, quaternion.dot(other), EPSILON);
    }

    @Test
    void dotProductOfIdentityAndQuaternionReturnsW() {
        assertEquals(
            quaternion.w,
            Quaternion.identity().dot(quaternion),
            EPSILON
        );
    }

    // --------------------------------------------------
    // EXPONENTIAL
    // --------------------------------------------------

    @Test
    void expOfZeroQuaternionReturnsIdentity() {
        Quaternion zero = new Quaternion(0, 0, 0, 0);

        Quaternion result = zero.exp();

        assertQuaternionEquals(1, 0, 0, 0, result);
    }

    @Test
    void expOfRealQuaternionReturnsExponential() {
        Quaternion result = new Quaternion(1, 0, 0, 0).exp();

        assertQuaternionEquals((float) Math.E, 0, 0, 0, result);
    }

    @Test
    void expOfPureImaginaryQuaternion() {
        Quaternion result = new Quaternion(0, 0, 0, 1).exp();

        assertQuaternionEquals(
            (float) Math.cos(1),
            0,
            0,
            (float) Math.sin(1),
            result
        );
    }

    @Test
    void expDoesNotModifyOriginalQuaternion() {
        quaternion.exp();

        assertQuaternionEquals(1, 2, 3, 4, quaternion);
    }

    // --------------------------------------------------
    // SLERP
    // --------------------------------------------------

    @Test
    void slerpAtZeroReturnsStartQuaternion() {
        Quaternion start = Quaternion.identity();
        Quaternion end = Quaternion.fromAngle2D(90);

        Quaternion result = Quaternion.slerp(start, end, 0);

        assertQuaternionEquals(start, result);
    }

    @Test
    void slerpAtOneReturnsEndQuaternion() {
        Quaternion start = Quaternion.identity();
        Quaternion end = Quaternion.fromAngle2D(90);

        Quaternion result = Quaternion.slerp(start, end, 1);

        assertQuaternionEquals(end, result);
    }

    @Test
    void slerpAtHalfInterpolatesRotation() {
        Quaternion start = Quaternion.identity();
        Quaternion end = Quaternion.fromAngle2D(90);

        Quaternion result = Quaternion.slerp(start, end, 0.5f);

        Quaternion expected = Quaternion.fromAngle2D(45);

        assertQuaternionEquals(expected, result);
    }

    @Test
    void slerpClampsNegativeInterpolationFactor() {
        Quaternion start = Quaternion.identity();
        Quaternion end = Quaternion.fromAngle2D(90);

        Quaternion result = Quaternion.slerp(start, end, -1);

        assertQuaternionEquals(start, result);
    }

    @Test
    void slerpClampsInterpolationFactorAboveOne() {
        Quaternion start = Quaternion.identity();
        Quaternion end = Quaternion.fromAngle2D(90);

        Quaternion result = Quaternion.slerp(start, end, 2);

        assertQuaternionEquals(end, result);
    }

    @Test
    void slerpReturnsUnitQuaternion() {
        Quaternion start = Quaternion.fromAngle2D(0);
        Quaternion end = Quaternion.fromAngle2D(120);

        Quaternion result = Quaternion.slerp(start, end, 0.5f);

        assertEquals(1, result.magnitude(), EPSILON);
    }

    @Test
    void slerpHandlesIdenticalQuaternions() {
        Quaternion start = Quaternion.identity();
        Quaternion end = Quaternion.identity();

        Quaternion result = Quaternion.slerp(start, end, 0.5f);

        assertQuaternionEquals(start, result);
    }

    @Test
    void slerpHandlesOppositeRepresentationsOfSameRotation() {
        Quaternion start = Quaternion.identity();
        Quaternion end = start.negate();

        Quaternion result = Quaternion.slerp(start, end, 0.5f);

        assertQuaternionEquals(start, result);
    }

    @Test
    void slerpHandlesVeryCloseRotations() {
        Quaternion start = Quaternion.fromAngle2D(0);
        Quaternion end = Quaternion.fromAngle2D(0.01f);

        Quaternion result = Quaternion.slerp(start, end, 0.5f);

        assertEquals(1, result.magnitude(), EPSILON);
        assertQuaternionEquals(
            Quaternion.fromAngle2D(0.005f),
            result
        );
    }

    // --------------------------------------------------
    // STRING REPRESENTATION
    // --------------------------------------------------

    @Test
    void toStringReturnsQuaternionComponents() {
        assertEquals(
            "Quaternion(1.0, 2.0, 3.0, 4.0)",
            quaternion.toString()
        );
    }

    // --------------------------------------------------
    // HELPERS
    // --------------------------------------------------

    private static void assertQuaternionEquals(
        float w,
        float x,
        float y,
        float z,
        Quaternion actual
    ) {
        assertAll(
            () -> assertEquals(w, actual.w, EPSILON, "W component"),
            () -> assertEquals(x, actual.x, EPSILON, "X component"),
            () -> assertEquals(y, actual.y, EPSILON, "Y component"),
            () -> assertEquals(z, actual.z, EPSILON, "Z component")
        );
    }

    private static void assertQuaternionEquals(
        Quaternion expected,
        Quaternion actual
    ) {
        assertQuaternionEquals(
            expected.w,
            expected.x,
            expected.y,
            expected.z,
            actual
        );
    }

    private static void assertVectorEquals(
        double x,
        double y,
        double z,
        Vector3 actual
    ) {
        assertAll(
            () -> assertEquals(x, actual.x, EPSILON, "X coordinate"),
            () -> assertEquals(y, actual.y, EPSILON, "Y coordinate"),
            () -> assertEquals(z, actual.z, EPSILON, "Z coordinate")
        );
    }

    private static void assertVectorEquals(
        Vector3 expected,
        Vector3 actual
    ) {
        assertVectorEquals(
            expected.x,
            expected.y,
            expected.z,
            actual
        );
    }

    private static boolean quaternionEquals(
        Quaternion first,
        Quaternion second
    ) {
        return Math.abs(first.w - second.w) < EPSILON
            && Math.abs(first.x - second.x) < EPSILON
            && Math.abs(first.y - second.y) < EPSILON
            && Math.abs(first.z - second.z) < EPSILON;
    }
}
