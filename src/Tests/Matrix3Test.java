package Tests;

import KiryuEngine.KiryuMath.Matrix3;
import KiryuEngine.KiryuMath.Vector3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix3Test {

  private static final double EPSILON = 1e-8;

  private Matrix3 matrix;

  @BeforeEach
  void setUp() {
    matrix = new Matrix3();
  }

  // --------------------------------------------------
  // CONSTRUCTORS
  // --------------------------------------------------

  @Test
  void defaultConstructorCreatesIdentityMatrix() {
    assertMatrixEquals(identity(), matrix);
  }

  @Test
  void arrayConstructorCopiesElements() {
    double[] elements = {
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    };

    Matrix3 result = new Matrix3(elements);

    elements[0] = 999;

    assertEquals(1, result.getElement(0));
  }

  @Test
  void arrayConstructorWithInvalidLengthCreatesIdentity() {
    Matrix3 result = new Matrix3(new double[]{1, 2, 3});

    assertMatrixEquals(identity(), result);
  }

  @Test
  void copyConstructorCreatesIndependentCopy() {
    Matrix3 original = new Matrix3(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    });

    Matrix3 copy = new Matrix3(original);

    original.setElement(0, 999);

    assertEquals(1, copy.getElement(0));
  }

  // --------------------------------------------------
  // GETTERS
  // --------------------------------------------------

  @Test
  void getRawElementsReturnsCopy() {
    double[] elements = matrix.getRawElements();

    elements[0] = 999;

    assertEquals(1, matrix.getElement(0));
  }

  @Test
  void getElementReturnsCorrectValue() {
    matrix.setElement(4, 42);

    assertEquals(42, matrix.getElement(4));
  }

  @Test
  void getElementWithInvalidIndexReturnsFirstElement() {
    assertEquals(1, matrix.getElement(-1));
    assertEquals(1, matrix.getElement(9));
  }

  @Test
  void getIdentityCreatesIndependentMatrix() {
    Matrix3 first = Matrix3.getIdentity();
    Matrix3 second = Matrix3.getIdentity();

    first.setElement(0, 999);

    assertEquals(1, second.getElement(0));
  }

  @Test
  void getLengthReturnsNine() {
    assertEquals(9, matrix.getLength());
  }

  // --------------------------------------------------
  // SETTERS
  // --------------------------------------------------

  @Test
  void setElementsUpdatesMatrix() {
    double[] elements = {
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    };

    matrix.setElements(elements);

    assertMatrixEquals(elements, matrix);
  }

  @Test
  void setElementsCopiesArray() {
    double[] elements = identity();

    matrix.setElements(elements);

    elements[0] = 999;

    assertEquals(1, matrix.getElement(0));
  }

  @Test
  void setElementsWithInvalidLengthDoesNothing() {
    matrix.setElement(0, 42);

    matrix.setElements(new double[]{1, 2, 3});

    assertEquals(42, matrix.getElement(0));
  }

  @Test
  void setElementUpdatesCorrectIndex() {
    matrix.setElement(5, 42);

    assertEquals(42, matrix.getElement(5));
  }

  @Test
  void setElementWithInvalidIndexDoesNothing() {
    matrix.setElement(-1, 42);
    matrix.setElement(9, 42);

    assertMatrixEquals(identity(), matrix);
  }

  // --------------------------------------------------
  // DETERMINANT
  // --------------------------------------------------

  @Test
  void identityMatrixHasDeterminantOne() {
    assertEquals(1, matrix.getDeterminant(), EPSILON);
  }

  @Test
  void diagonalMatrixHasCorrectDeterminant() {
    matrix.setElements(new double[]{
        2, 0, 0,
        0, 3, 0,
        0, 0, 4
    });

    assertEquals(24, matrix.getDeterminant(), EPSILON);
  }

  @Test
  void matrixHasCorrectDeterminant() {
    matrix.setElements(new double[]{
        1, 2, 3,
        0, 1, 4,
        5, 6, 0
    });

    assertEquals(1, matrix.getDeterminant(), EPSILON);
  }

  @Test
  void singularMatrixHasZeroDeterminant() {
    matrix.setElements(new double[]{
        1, 2, 3,
        1, 2, 3,
        4, 5, 6
    });

    assertEquals(0, matrix.getDeterminant(), EPSILON);
  }

  // --------------------------------------------------
  // COFACTOR MATRIX
  // --------------------------------------------------

  @Test
  void identityMatrixHasIdentityCofactorMatrix() {
    Matrix3 result = matrix.getCofactorMatrix();

    assertMatrixEquals(identity(), result);
  }

  @Test
  void diagonalMatrixHasCorrectCofactorMatrix() {
    matrix.setElements(new double[]{
        1, 0, 0,
        0, 2, 0,
        0, 0, 3
    });

    Matrix3 result = matrix.getCofactorMatrix();

    assertMatrixEquals(new double[]{
        6, 0, 0,
        0, 3, 0,
        0, 0, 2
    }, result);
  }

  @Test
  void matrixHasCorrectCofactorMatrix() {
    matrix.setElements(new double[]{
        1, 2, 3,
        0, 1, 4,
        5, 6, 0
    });

    Matrix3 result = matrix.getCofactorMatrix();

    assertMatrixEquals(new double[]{
        -24, 20, -5,
        18, -15, 4,
        5, -4, 1
    }, result);
  }

  @Test
  void cofactorMatrixDoesNotModifyOriginalMatrix() {
    double[] original = {
        1, 2, 3,
        0, 1, 4,
        5, 6, 0
    };

    matrix.setElements(original);

    matrix.getCofactorMatrix();

    assertMatrixEquals(original, matrix);
  }

  // --------------------------------------------------
  // INVERSE
  // --------------------------------------------------

  @Test
  void identityMatrixHasIdentityInverse() {
    Matrix3 result = matrix.getInverse();

    assertNotNull(result);
    assertMatrixEquals(identity(), result);
  }

  @Test
  void diagonalMatrixHasCorrectInverse() {
    matrix.setElements(new double[]{
        2, 0, 0,
        0, 4, 0,
        0, 0, 5
    });

    Matrix3 result = matrix.getInverse();

    assertNotNull(result);

    assertMatrixEquals(new double[]{
        0.5, 0, 0,
        0, 0.25, 0,
        0, 0, 0.2
    }, result);
  }

  @Test
  void matrixHasCorrectInverse() {
    matrix.setElements(new double[]{
        1, 2, 3,
        0, 1, 4,
        5, 6, 0
    });

    Matrix3 result = matrix.getInverse();

    assertNotNull(result);

    assertMatrixEquals(new double[]{
        -24, 18, 5,
        20, -15, -4,
        -5, 4, 1
    }, result);
  }

  @Test
  void singularMatrixHasNoInverse() {
    matrix.setElements(new double[]{
        1, 2, 3,
        1, 2, 3,
        4, 5, 6
    });

    assertNull(matrix.getInverse());
  }

  @Test
  void matrixMultipliedByItsInverseProducesIdentity() {
    matrix.setElements(new double[]{
        1, 2, 3,
        0, 1, 4,
        5, 6, 0
    });

    Matrix3 inverse = matrix.getInverse();

    assertNotNull(inverse);

    Matrix3 result = new Matrix3(matrix);
    result.multiplyMatrix(inverse);

    assertMatrixEquals(identity(), result);
  }

  // --------------------------------------------------
  // TRANSPOSE
  // --------------------------------------------------

  @Test
  void transposeSwapsRowsAndColumns() {
    matrix.setElements(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    });

    Matrix3 result = matrix.getTranspose();

    assertMatrixEquals(new double[]{
        1, 4, 7,
        2, 5, 8,
        3, 6, 9
    }, result);
  }

  @Test
  void transposeDoesNotModifyOriginalMatrix() {
    double[] original = {
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    };

    matrix.setElements(original);

    matrix.getTranspose();

    assertMatrixEquals(original, matrix);
  }

  // --------------------------------------------------
  // SCALAR MULTIPLICATION
  // --------------------------------------------------

  @Test
  void multiplyScalarMultipliesAllElements() {
    matrix.setElements(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    });

    matrix.multiplyScalar(2);

    assertMatrixEquals(new double[]{
        2, 4, 6,
        8, 10, 12,
        14, 16, 18
    }, matrix);
  }

  @Test
  void multiplyScalarByZeroProducesZeroMatrix() {
    matrix.multiplyScalar(0);

    assertMatrixEquals(new double[9], matrix);
  }

  // --------------------------------------------------
  // MATRIX MULTIPLICATION
  // --------------------------------------------------

  @Test
  void multiplyMatrixProducesCorrectResult() {
    matrix.setElements(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    });

    Matrix3 other = new Matrix3(new double[]{
        9, 8, 7,
        6, 5, 4,
        3, 2, 1
    });

    matrix.multiplyMatrix(other);

    assertMatrixEquals(new double[]{
        30, 24, 18,
        84, 69, 54,
        138, 114, 90
    }, matrix);
  }

  @Test
  void multiplyingByIdentityDoesNotChangeMatrix() {
    matrix.setElements(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    });

    double[] original = matrix.getRawElements();

    matrix.multiplyMatrix(Matrix3.getIdentity());

    assertMatrixEquals(original, matrix);
  }

  // --------------------------------------------------
  // MATRIX ADDITION
  // --------------------------------------------------

  @Test
  void addMatrixAddsCorrespondingElements() {
    matrix.setElements(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    });

    matrix.addMatrix(new Matrix3(new double[]{
        1, 1, 1,
        1, 1, 1,
        1, 1, 1
    }));

    assertMatrixEquals(new double[]{
        2, 3, 4,
        5, 6, 7,
        8, 9, 10
    }, matrix);
  }

  // --------------------------------------------------
  // MATRIX SUBTRACTION
  // --------------------------------------------------

  @Test
  void subMatrixSubtractsCorrespondingElements() {
    matrix.setElements(new double[]{
        2, 3, 4,
        5, 6, 7,
        8, 9, 10
    });

    matrix.subMatrix(new Matrix3(new double[]{
        1, 1, 1,
        1, 1, 1,
        1, 1, 1
    }));

    assertMatrixEquals(new double[]{
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    }, matrix);
  }

  // --------------------------------------------------
  // EQUALITY
  // --------------------------------------------------

  @Test
  void equalMatricesReturnTrue() {
    Matrix3 other = new Matrix3();

    assertTrue(matrix.equals(other));
  }

  @Test
  void differentMatricesReturnFalse() {
    Matrix3 other = new Matrix3();

    other.setElement(0, 42);

    assertFalse(matrix.equals(other));
  }

  @Test
  void equalMatricesWithSameValuesReturnTrue() {
    double[] values = {
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    };

    Matrix3 first = new Matrix3(values);
    Matrix3 second = new Matrix3(values);

    assertTrue(first.equals(second));
  }

  // --------------------------------------------------
  // SCALING
  // --------------------------------------------------

  @Test
  void scaleByOneDoesNotChangeMatrix() {
    matrix.scale(new Vector3(1, 1, 1));

    assertMatrixEquals(identity(), matrix);
  }

  @Test
  void scaleByZeroProducesZeroMatrix() {
    matrix.scale(new Vector3(0, 0, 0));

    assertMatrixEquals(new double[9], matrix);
  }

  @Test
  void scaleAppliesCorrectAxisScaling() {
    matrix.scale(new Vector3(2, 3, 4));

    assertMatrixEquals(new double[]{
        2, 0, 0,
        0, 3, 0,
        0, 0, 4
    }, matrix);
  }

  // --------------------------------------------------
  // HELPERS
  // --------------------------------------------------

  private static double[] identity() {
    return new double[]{
        1, 0, 0,
        0, 1, 0,
        0, 0, 1
    };
  }

  private static void assertMatrixEquals(
      double[] expected,
      Matrix3 actual
  ) {
    assertArrayEquals(
        expected,
        actual.getRawElements(),
        EPSILON
    );
  }
}