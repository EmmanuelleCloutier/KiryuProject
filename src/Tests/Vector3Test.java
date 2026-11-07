package Tests;

import KiryuEngine.KiryuMath.Vector3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class Vector3Test {

  private static final double EPSILON = 1e-5;

  private Vector3 vector;

  @BeforeEach
  void setUp() {
    vector = new Vector3(1, 2, 3);
  }

  // --------------------------------------------------
  // CONSTRUCTORS
  // --------------------------------------------------

  @Test
  void defaultConstructorCreatesZeroVector() {
    Vector3 result = new Vector3();

    assertEquals(0, result.x);
    assertEquals(0, result.y);
    assertEquals(0, result.z);
  }

  @Test
  void parameterizedConstructorSetsCoordinates() {
    Vector3 result = new Vector3(1, 2, 3);

    assertVectorEquals(1, 2, 3, result);
  }

  @Test
  void copyConstructorCopiesCoordinates() {
    Vector3 result = new Vector3(vector);

    assertVectorEquals(vector, result);
    assertNotSame(vector, result);
  }

  // --------------------------------------------------
  // CONSTANTS
  // --------------------------------------------------

  @Test
  void zeroVectorHasZeroCoordinates() {
    assertVectorEquals(0, 0, 0, Vector3.ZERO_VECTOR);
  }

  @Test
  void onesVectorHasOneCoordinates() {
    assertVectorEquals(1, 1, 1, Vector3.ONES_VECTOR);
  }

  @Test
  void unitVectorHasLengthOne() {
    assertEquals(1.0, Vector3.UNIT_VECTOR.getLength(), EPSILON);
  }

  @Test
  void directionConstantsHaveCorrectCoordinates() {
    assertVectorEquals(0, 1, 0, Vector3.UP_VECTOR);
    assertVectorEquals(0, -1, 0, Vector3.DOWN_VECTOR);

    assertVectorEquals(1, 0, 0, Vector3.RIGHT_VECTOR);
    assertVectorEquals(-1, 0, 0, Vector3.LEFT_VECTOR);

    assertVectorEquals(0, 0, 1, Vector3.FORWARD_VECTOR);
    assertVectorEquals(0, 0, -1, Vector3.BACKWARD_VECTOR);
  }

  // --------------------------------------------------
  // COPY
  // --------------------------------------------------

  @Test
  void copyReturnsIndependentVector() {
    Vector3 result = vector.copy();

    assertVectorEquals(vector, result);
    assertNotSame(vector, result);

    result.x = 100;

    assertEquals(1, vector.x);
  }

  // --------------------------------------------------
  // SETTERS
  // --------------------------------------------------

  @Test
  void setCoordinatesReplacesAllCoordinates() {
    vector.set(4, 5, 6);

    assertVectorEquals(4, 5, 6, vector);
  }

  @Test
  void setVectorCopiesCoordinates() {
    Vector3 other = new Vector3(4, 5, 6);

    vector.set(other);

    assertVectorEquals(other, vector);
    assertNotSame(other, vector);
  }

  // --------------------------------------------------
  // EQUALITY
  // --------------------------------------------------

  @Test
  void equalsReturnsTrueForIdenticalVectors() {
    Vector3 other = new Vector3(1, 2, 3);

    assertTrue(vector.equals(other));
  }

  @Test
  void equalsReturnsFalseForDifferentVectors() {
    Vector3 other = new Vector3(1, 2, 4);

    assertFalse(vector.equals(other));
  }

  @Test
  void equalsUsesEpsilonTolerance() {
    Vector3 other = new Vector3(
        1 + EPSILON / 2,
        2,
        3
    );

    assertTrue(vector.equals(other));
  }

  @Test
  void equalsCoordinatesReturnsTrueForMatchingCoordinates() {
    assertTrue(vector.equals(1, 2, 3));
  }

  @Test
  void equalsCoordinatesReturnsFalseForDifferentCoordinates() {
    assertFalse(vector.equals(1, 2, 4));
  }

  // --------------------------------------------------
  // STRING REPRESENTATION
  // --------------------------------------------------

  @Test
  void toStringFormatsCoordinatesToThreeDecimals() {
    Locale previousLocale = Locale.getDefault();

    try {
      Locale.setDefault(Locale.US);

      assertEquals("(1.000, 2.000, 3.000)", vector.toString());
    } finally {
      Locale.setDefault(previousLocale);
    }
  }

  // --------------------------------------------------
  // LENGTH
  // --------------------------------------------------

  @Test
  void getLengthSquaredReturnsCorrectValue() {
    assertEquals(14, vector.getLengthSquared(), EPSILON);
  }

  @Test
  void getLengthReturnsCorrectValue() {
    assertEquals(Math.sqrt(14), vector.getLength(), EPSILON);
  }

  @Test
  void zeroVectorHasZeroLength() {
    assertEquals(0, Vector3.ZERO_VECTOR.getLength(), EPSILON);
  }

  // --------------------------------------------------
  // DISTANCE
  // --------------------------------------------------

  @Test
  void distanceSquaredReturnsCorrectValue() {
    Vector3 other = new Vector3(4, 6, 3);

    assertEquals(25, vector.distanceSquared(other), EPSILON);
  }

  @Test
  void distanceReturnsCorrectValue() {
    Vector3 other = new Vector3(4, 6, 3);

    assertEquals(5, vector.distance(other), EPSILON);
  }

  @Test
  void distanceToSameVectorIsZero() {
    assertEquals(0, vector.distance(vector), EPSILON);
  }

  // --------------------------------------------------
  // NORMALIZATION
  // --------------------------------------------------

  @Test
  void normalizeReturnsUnitVector() {
    Vector3 result = vector.normalize();

    assertEquals(1, result.getLength(), EPSILON);
  }

  @Test
  void normalizeModifiesOriginalVector() {
    Vector3 result = vector.normalize();

    assertSame(vector, result);
    assertEquals(1, vector.getLength(), EPSILON);
  }

  @Test
  void normalizePreservesDirection() {
    vector.normalize();

    assertTrue(vector.x > 0);
    assertTrue(vector.y > 0);
    assertTrue(vector.z > 0);
  }

  @Test
  void normalizeZeroVectorReturnsZeroCopy() {
    Vector3 zero = new Vector3();

    Vector3 result = zero.normalize();

    assertVectorEquals(0, 0, 0, result);
    assertNotSame(Vector3.ZERO_VECTOR, result);
  }

  @Test
  void getVectorNormalReturnsNormalizedCopy() {
    Vector3 original = new Vector3(1, 2, 3);

    Vector3 result = Vector3.getVectorNormal(original);

    assertEquals(1, result.getLength(), EPSILON);
    assertVectorEquals(1, 2, 3, original);
    assertNotSame(original, result);
  }

  @Test
  void getVectorNormalHandlesZeroVector() {
    Vector3 zero = new Vector3();

    Vector3 result = Vector3.getVectorNormal(zero);

    assertVectorEquals(0, 0, 0, result);
    assertNotSame(Vector3.ZERO_VECTOR, result);
  }

  // --------------------------------------------------
  // SCALAR OPERATIONS
  // --------------------------------------------------

  @Test
  void scaleMultipliesAllCoordinates() {
    Vector3 result = vector.scale(2);

    assertVectorEquals(2, 4, 6, result);
    assertSame(vector, result);
  }

  @Test
  void scaleByZeroProducesZeroVector() {
    vector.scale(0);

    assertVectorEquals(0, 0, 0, vector);
  }

  @Test
  void scaleByNegativeValueReversesAndScalesCoordinates() {
    vector.scale(-2);

    assertVectorEquals(-2, -4, -6, vector);
  }

  @Test
  void reverseNegatesAllCoordinates() {
    Vector3 result = vector.reverse();

    assertVectorEquals(-1, -2, -3, result);
    assertSame(vector, result);
  }

  // --------------------------------------------------
  // VECTOR ARITHMETIC
  // --------------------------------------------------

  @Test
  void addAddsCoordinates() {
    Vector3 other = new Vector3(4, 5, 6);

    Vector3 result = vector.add(other);

    assertVectorEquals(5, 7, 9, result);
    assertSame(vector, result);
  }

  @Test
  void subSubtractsCoordinates() {
    Vector3 other = new Vector3(4, 5, 6);

    Vector3 result = vector.sub(other);

    assertVectorEquals(-3, -3, -3, result);
    assertSame(vector, result);
  }

  @Test
  void mulMultipliesCoordinatesComponentWise() {
    Vector3 other = new Vector3(4, 5, 6);

    Vector3 result = vector.mul(other);

    assertVectorEquals(4, 10, 18, result);
    assertSame(vector, result);
  }

  // --------------------------------------------------
  // DOT PRODUCT
  // --------------------------------------------------

  @Test
  void dotProductReturnsCorrectValue() {
    Vector3 other = new Vector3(4, 5, 6);

    assertEquals(32, vector.dotProduct(other), EPSILON);
  }

  @Test
  void dotProductOfOrthogonalVectorsIsZero() {
    assertEquals(
        0,
        Vector3.RIGHT_VECTOR.dotProduct(Vector3.UP_VECTOR),
        EPSILON
    );
  }

  // --------------------------------------------------
  // CROSS PRODUCT
  // --------------------------------------------------

  @Test
  void crossProductReturnsCorrectVector() {
    Vector3 result = Vector3.RIGHT_VECTOR.crossProduct(
        Vector3.UP_VECTOR
    );

    assertVectorEquals(0, 0, 1, result);
  }

  @Test
  void crossProductReturnsNewVector() {
    Vector3 other = new Vector3(4, 5, 6);

    Vector3 result = vector.crossProduct(other);

    assertNotSame(vector, result);
    assertNotSame(other, result);
  }

  @Test
  void crossProductOfParallelVectorsIsZero() {
    Vector3 result = new Vector3(1, 2, 3)
        .crossProduct(new Vector3(2, 4, 6));

    assertVectorEquals(0, 0, 0, result);
  }

  // --------------------------------------------------
  // ORTHOGONALITY / COLLINEARITY
  // --------------------------------------------------

  @Test
  void isOrthogonalReturnsTrueForPerpendicularVectors() {
    assertTrue(
        Vector3.RIGHT_VECTOR.isOrthogonal(Vector3.UP_VECTOR)
    );
  }

  @Test
  void isOrthogonalReturnsFalseForNonPerpendicularVectors() {
    assertFalse(
        Vector3.RIGHT_VECTOR.isOrthogonal(Vector3.ONES_VECTOR)
    );
  }

  @Test
  void isCollinearReturnsTrueForParallelVectors() {
    Vector3 first = new Vector3(1, 2, 3);
    Vector3 second = new Vector3(2, 4, 6);

    assertTrue(first.isCollinear(second));
  }

  @Test
  void isCollinearReturnsTrueForOppositeVectors() {
    Vector3 first = new Vector3(1, 2, 3);
    Vector3 second = new Vector3(-1, -2, -3);

    assertTrue(first.isCollinear(second));
  }

  @Test
  void isCollinearReturnsFalseForNonParallelVectors() {
    assertFalse(
        Vector3.RIGHT_VECTOR.isCollinear(Vector3.UP_VECTOR)
    );
  }

  // --------------------------------------------------
  // CLEAR
  // --------------------------------------------------

  @Test
  void clearResetsAllCoordinatesToZero() {
    vector.clear();

    assertVectorEquals(0, 0, 0, vector);
  }

  // --------------------------------------------------
  // HELPERS
  // --------------------------------------------------

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
}