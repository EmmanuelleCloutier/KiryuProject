package Tests;

import KiryuEngine.KiryuMath.Matrix4;
import KiryuEngine.KiryuMath.Vector3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix4Test {

  private static final double EPSILON = 1e-8;

  private Matrix4 matrix;

  @BeforeEach
  void setUp() {
    matrix = new Matrix4();
  }

  // --------------------------------------------------
  // CONSTRUCTORS
  // --------------------------------------------------

  @Test
  void defaultConstructorCreatesIdentityMatrix() {
    assertMatrixEquals(new double[]{
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    }, matrix);
  }

  @Test
  void arrayConstructorCopiesElements() {
    double[] elements = {
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    };

    Matrix4 result = new Matrix4(elements);

    elements[0] = 999;

    assertEquals(1, result.getElement(0));
  }

  @Test
  void arrayConstructorWithInvalidLengthCreatesIdentity() {
    Matrix4 result = new Matrix4(new double[]{1, 2, 3});

    assertMatrixEquals(identity(), result);
  }

  @Test
  void copyConstructorCreatesIndependentCopy() {
    Matrix4 original = new Matrix4(new double[]{
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    });

    Matrix4 copy = new Matrix4(original);

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
    matrix.setElement(5, 42);

    assertEquals(42, matrix.getElement(5));
  }

  @Test
  void getElementWithInvalidIndexReturnsFirstElement() {
    assertEquals(1, matrix.getElement(-1));
    assertEquals(1, matrix.getElement(16));
  }

  @Test
  void getLengthReturns16() {
    assertEquals(16, matrix.getLength());
  }

  @Test
  void getIdentityCreatesIndependentMatrix() {
    Matrix4 first = Matrix4.getIdentity();
    Matrix4 second = Matrix4.getIdentity();

    first.setElement(0, 999);

    assertEquals(1, second.getElement(0));
  }

  // --------------------------------------------------
  // SETTERS
  // --------------------------------------------------

  @Test
  void setElementsUpdatesMatrix() {
    double[] elements = {
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
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
    matrix.setElement(10, 42);

    assertEquals(42, matrix.getElement(10));
  }

  @Test
  void setElementWithInvalidIndexDoesNothing() {
    matrix.setElement(-1, 42);
    matrix.setElement(16, 42);

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
        2, 0, 0, 0,
        0, 3, 0, 0,
        0, 0, 4, 0,
        0, 0, 0, 5
    });

    assertEquals(120, matrix.getDeterminant(), EPSILON);
  }

  @Test
  void singularMatrixHasZeroDeterminant() {
    matrix.setElements(new double[]{
        1, 2, 3, 4,
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12
    });

    assertEquals(0, matrix.getDeterminant(), EPSILON);
  }

  // --------------------------------------------------
  // COFACTOR MATRIX
  // --------------------------------------------------

  @Test
  void identityMatrixHasIdentityCofactorMatrix() {
    Matrix4 result = matrix.getCofactorMatrix();

    assertMatrixEquals(identity(), result);
  }

  @Test
  void diagonalMatrixHasCorrectCofactorMatrix() {
    matrix.setElements(new double[]{
        1, 0, 0, 0,
        0, 2, 0, 0,
        0, 0, 3, 0,
        0, 0, 0, 4
    });

    Matrix4 result = matrix.getCofactorMatrix();

    assertMatrixEquals(new double[]{
        24, 0, 0, 0,
        0, 12, 0, 0,
        0, 0, 8, 0,
        0, 0, 0, 6
    }, result);
  }

  @Test
  void cofactorMatrixDoesNotModifyOriginalMatrix() {
    double[] original = {
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
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
    Matrix4 result = matrix.getInverse();

    assertNotNull(result);
    assertMatrixEquals(identity(), result);
  }

  @Test
  void diagonalMatrixHasCorrectInverse() {
    matrix.setElements(new double[]{
        2, 0, 0, 0,
        0, 4, 0, 0,
        0, 0, 5, 0,
        0, 0, 0, 10
    });

    Matrix4 result = matrix.getInverse();

    assertNotNull(result);

    assertMatrixEquals(new double[]{
        0.5, 0, 0, 0,
        0, 0.25, 0, 0,
        0, 0, 0.2, 0,
        0, 0, 0, 0.1
    }, result);
  }

  @Test
  void singularMatrixHasNoInverse() {
    matrix.setElements(new double[]{
        1, 2, 3, 4,
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12
    });

    assertNull(matrix.getInverse());
  }

  @Test
  void matrixMultipliedByItsInverseProducesIdentity() {
    matrix.setElements(new double[]{
        1, 2, 0, 0,
        0, 1, 3, 0,
        0, 0, 1, 4,
        0, 0, 0, 1
    });

    Matrix4 inverse = matrix.getInverse();

    assertNotNull(inverse);

    Matrix4 result = new Matrix4(matrix);
    result.multiplyMatrix(inverse);

    assertMatrixEquals(identity(), result);
  }

  // --------------------------------------------------
  // TRANSPOSE
  // --------------------------------------------------

  @Test
  void transposeSwapsRowsAndColumns() {
    matrix.setElements(new double[]{
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    });

    Matrix4 result = matrix.getTranspose();

    assertMatrixEquals(new double[]{
        1, 5, 9, 13,
        2, 6, 10, 14,
        3, 7, 11, 15,
        4, 8, 12, 16
    }, result);
  }

  @Test
  void transposeDoesNotModifyOriginalMatrix() {
    double[] original = {
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
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
    matrix.multiplyScalar(2);

    assertMatrixEquals(new double[]{
        2, 0, 0, 0,
        0, 2, 0, 0,
        0, 0, 2, 0,
        0, 0, 0, 2
    }, matrix);
  }

  @Test
  void multiplyScalarByZeroProducesZeroMatrix() {
    matrix.multiplyScalar(0);

    assertMatrixEquals(new double[16], matrix);
  }

  // --------------------------------------------------
  // MATRIX MULTIPLICATION
  // --------------------------------------------------

  @Test
  void multiplyMatrixProducesCorrectResult() {
    matrix.setElements(new double[]{
        2, 0, 0, 0,
        0, 3, 0, 0,
        0, 0, 4, 0,
        0, 0, 0, 1
    });

    Matrix4 other = new Matrix4(new double[]{
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    });

    matrix.multiplyMatrix(other);

    assertMatrixEquals(new double[]{
        2, 4, 6, 8,
        15, 18, 21, 24,
        36, 40, 44, 48,
        13, 14, 15, 16
    }, matrix);
  }

  @Test
  void multiplyingByIdentityDoesNotChangeMatrix() {
    matrix.setElements(new double[]{
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    });

    double[] original = matrix.getRawElements();

    matrix.multiplyMatrix(Matrix4.getIdentity());

    assertMatrixEquals(original, matrix);
  }

  // --------------------------------------------------
  // MATRIX ADDITION
  // --------------------------------------------------

  @Test
  void addMatrixAddsCorrespondingElements() {
    matrix.setElements(new double[]{
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    });

    matrix.addMatrix(new Matrix4(new double[]{
        1, 1, 1, 1,
        1, 1, 1, 1,
        1, 1, 1, 1,
        1, 1, 1, 1
    }));

    assertMatrixEquals(new double[]{
        2, 3, 4, 5,
        6, 7, 8, 9,
        10, 11, 12, 13,
        14, 15, 16, 17
    }, matrix);
  }

  // --------------------------------------------------
  // MATRIX SUBTRACTION
  // --------------------------------------------------

  @Test
  void subMatrixSubtractsCorrespondingElements() {
    matrix.setElements(new double[]{
        2, 3, 4, 5,
        6, 7, 8, 9,
        10, 11, 12, 13,
        14, 15, 16, 17
    });

    matrix.subMatrix(new Matrix4(new double[]{
        1, 1, 1, 1,
        1, 1, 1, 1,
        1, 1, 1, 1,
        1, 1, 1, 1
    }));

    assertMatrixEquals(new double[]{
        1, 2, 3, 4,
        5, 6, 7, 8,
        9, 10, 11, 12,
        13, 14, 15, 16
    }, matrix);
  }

  // --------------------------------------------------
  // TRANSLATION
  // --------------------------------------------------

  @Test
  void translateUpdatesTranslationComponents() {
    matrix.translate(5, 10, 15);

    assertMatrixEquals(new double[]{
        1, 0, 0, 5,
        0, 1, 0, 10,
        0, 0, 1, 15,
        0, 0, 0, 1
    }, matrix);
  }

  @Test
  void translateAccumulatesTranslation() {
    matrix.translate(1, 2, 3);
    matrix.translate(4, 5, 6);

    assertEquals(5, matrix.getElement(3));
    assertEquals(7, matrix.getElement(7));
    assertEquals(9, matrix.getElement(11));
  }

  // --------------------------------------------------
  // ROTATIONS
  // --------------------------------------------------

  @Test
  void rotateX90Degrees() {
    matrix.rotateX(90);

    assertMatrixEquals(new double[]{
        1, 0, 0, 0,
        0, 0, -1, 0,
        0, 1, 0, 0,
        0, 0, 0, 1
    }, matrix);
  }

  @Test
  void rotateY90Degrees() {
    matrix.rotateY(90);

    assertMatrixEquals(new double[]{
        0, 0, 1, 0,
        0, 1, 0, 0,
        -1, 0, 0, 0,
        0, 0, 0, 1
    }, matrix);
  }

  @Test
  void rotateZ90Degrees() {
    matrix.rotateZ(90);

    assertMatrixEquals(new double[]{
        0, -1, 0, 0,
        1, 0, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    }, matrix);
  }

  @Test
  void rotateZeroDegreesDoesNotChangeMatrix() {
    matrix.rotateX(0);
    matrix.rotateY(0);
    matrix.rotateZ(0);

    assertMatrixEquals(identity(), matrix);
  }

  @Test
  void rotateVector3MatchesIndividualEulerOverload() {
    Vector3 angles = new Vector3(30, 45, 60);

    Matrix4 first = new Matrix4();
    Matrix4 second = new Matrix4();

    first.rotate(angles);
    second.rotate(30, 45, 60);

    assertMatrixEquals(first.getRawElements(), second);
  }

  @Test
  void rotateDoubleOverloadWithZeroAnglesDoesNotChangeMatrix() {
    matrix.rotate(0, 0, 0);

    assertMatrixEquals(identity(), matrix);
  }

  // --------------------------------------------------
  // HELPERS
  // --------------------------------------------------

  private static double[] identity() {
    return new double[]{
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };
  }

  private static void assertMatrixEquals(
      double[] expected,
      Matrix4 actual
  ) {
    assertArrayEquals(
        expected,
        actual.getRawElements(),
        EPSILON
    );
  }
}