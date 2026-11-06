package KiryuEngine.KiryuMath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Matrix4 {
  /**
   * the elements of the matrix
   * for optimization this is a flat array
   */
  private double[] elements;
  /**
   * the number of elements in the matrix
   */
  private final int length = 16;
  /**
   * the identity matrix layout for a 4x4 matrix.
   * this is a static reference only used internally in the class.
   * to get a usable identity matrix use getIdentity()
   */
  private static final double[] identity = new double[]{
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
  };
  /**
   * empty constructor, sets the matrix to the identity matrix
   */
  public Matrix4() {
    this.elements = identity.clone();
  }
  /**
   * Array constructor
   * @param elements <font color="orange">{@code double[]}</font>
   * - the elements to initialize the matrix with. It MUST have 16 elements
   */
  public Matrix4(double[] elements) {
    if (elements.length == 16) {
      this.elements = elements.clone();
    } else {
      this.elements = identity.clone();
    }
  }
  /**
   * Matrix constructor
   * @param mat4 <font color="yellow">{@link Matrix4}</font>
   * - the matrix to initialize a new matrix with. it clones the elements.
   */
  public Matrix4(@NotNull Matrix4 mat4) {
    this.elements = mat4.getRawElements();
  }

  /*
   GETTER
   */
  //region
  /**
   * @return <font color="orange">{@code double[]}</font>
   * - the elements of the matrix as a copy
   */
  public double[] getRawElements() {
    return this.elements.clone();
  }
  /**
   * @param index <font color="orange">{@code int}</font>
   * - the index between 0 & 15 of the element
   * @return <font color="orange">{@code double}</font>
   * - the value of the element at the specified index
   */
  public double getElement(int index) {
    if (index > 15 || index < 0) return this.elements[0];
    return this.elements[index];
  }
  /**
   * @return <font color="yellow">{@link Matrix4}</font>
   * - the identity Matrix of a 4x4 matrix as a new object
   */
  @Contract(" -> new")
  public static @NotNull Matrix4 getIdentity() {
    return new Matrix4();
  }
  /**
   * @return <font color="orange">{@code int}</font>
   * - the number of elements in the matrix
   */
  public int getLength() {
    return this.length;
  }
  /**
   * @return <font color="orange">{@code double}</font>
   * - the determinant of this 4x4 matrix using Laplace expansion
   */
  public double getDeterminant() {
    double det = 0;
    // Laplace expansion along the first row
    for (int col = 0; col < 4; col++) {
      double subDet = getMinorDeterminant(0, col);
      int sign = (col % 2 == 0) ? 1 : -1;
      det += sign * this.elements[col] * subDet;
    }
    return det;
  }
  /**
   * Helper method to find the determinant of the 3x3 submatrix after removing a row and a column.
   * @param excludeRow <font color="orange">{@code int}</font>
   * - the row to exclude
   * @param excludeCol <font color="orange">{@code int}</font>
   * - the column to exclude
   * @return <font color="orange">{@code double}</font>
   * - the minor determinant of the 3x3 submatrix
   */
  private double getMinorDeterminant(int excludeRow, int excludeCol) {
    double[] subElements = new double[9];
    int index = 0;
    for (int r = 0; r < 4; r++) {
      if (r == excludeRow) continue;
      for (int c = 0; c < 4; c++) {
        if (c == excludeCol) continue;
        subElements[index++] = this.elements[r * 4 + c];
      }
    }
    // Standard 3x3 determinant math
    return (subElements[0] * ((subElements[4] * subElements[8]) - (subElements[5] * subElements[7]))) -
        (subElements[1] * ((subElements[3] * subElements[8]) - (subElements[5] * subElements[6]))) +
        (subElements[2] * ((subElements[3] * subElements[7]) - (subElements[4] * subElements[6])));
  }
  /**
   * @return <font color="yellow">{@link Matrix4}</font>
   * - the cofactor matrix of this matrix
   */
  public @NotNull Matrix4 getCofactorMatrix() {
    double[] cofactor = new double[this.length];
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        double minorDet = getMinorDeterminant(row, col);
        int sign = ((row + col) % 2 == 0) ? 1 : -1;
        cofactor[row * 4 + col] = sign * minorDet;
      }
    }
    return new Matrix4(cofactor);
  }
  /**
   * @return <font color="yellow">{@link Matrix4}</font>
   * - the inverse matrix, this can be null if the determinant approaches 0
   */
  public @Nullable Matrix4 getInverse() {
    double determinant = this.getDeterminant();
    if (Math.abs(determinant) < 1e-8) {
      return null;
    }
    double inverseDeterminant = 1.0 / determinant;
    Matrix4 cofactorMat = this.getCofactorMatrix();
    Matrix4 adjugateMat = cofactorMat.getTranspose();
    adjugateMat.multiplyScalar(inverseDeterminant);

    return adjugateMat;
  }

  /**
   * @return <font color="yellow">{@link Matrix4}</font>
   * - the transpose of this matrix
   */
  public @NotNull Matrix4 getTranspose() {
    double[] transpose = {
        this.elements[0],this.elements[4],this.elements[8],this.elements[12],
        this.elements[1],this.elements[5],this.elements[9],this.elements[13],
        this.elements[2],this.elements[6],this.elements[10],this.elements[14],
        this.elements[3],this.elements[7],this.elements[11],this.elements[15]
    };
    return new Matrix4(transpose);
  }
  //endregion

  /*
  SETTER
   */
  //region
  /**
   * @param elements <font color="orange">{@code double[]}</font>
   * - the array of elements the matrix needs to take value of
   */
  public void setElements(double[] elements) {
    if (elements.length == 16) {
      this.elements = elements.clone();
    }
  }
  /**
   * @param index <font color="orange">{@code int}</font>
   * - the index of the element that needs to be changed must be between 0 & 15
   * @param value <font color="orange">{@code double}</font>
   * - the new value of the element
   */
  public void setElement(int index, double value) {
    if (index > 15 || index < 0) return;
    this.elements[index] = value;
  }
  //endregion
  /**
   * @param scalar <font color="orange">{@code double}</font>
   * - the scalar value to multiply the matrix with
   */
  public void multiplyScalar(double scalar) {
    for (int i = 0; i < this.length; i++) {
      this.elements[i] *= scalar;
    }
  }
  /**
   * @param mat <font color="yellow">{@link Matrix4}</font>
   * - the matrix to multiply with
   */
  public void multiplyMatrix(@NotNull Matrix4 mat) {
    double[] multipliedMatrix = new double[this.length];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        int outIndex = i * 4 + j;
        multipliedMatrix[outIndex] = 0;
        for (int k = 0; k < 4; k++) {
          multipliedMatrix[outIndex] += this.elements[i * 4 + k] * mat.getElement(k * 4 + j);
        }
      }
    }
    this.elements = multipliedMatrix;
  }
  /**
   * @param mat <font color="yellow">{@link Matrix4}</font>
   * - the matrix to add
   */
  public void addMatrix(@NotNull Matrix4 mat) {
    for (int i = 0; i < this.length; i++) {
      this.elements[i] += mat.getElement(i);
    }
  }
  /**
   * @param mat <font color="yellow">{@link Matrix4}</font>
   * - the matrix to subtract
   */
  public void subMatrix(@NotNull Matrix4 mat) {
    for (int i = 0; i < this.length; i++) {
      this.elements[i] -= mat.getElement(i);
    }
  }

  /*
   KIRYU ENGINE GRAPHICS EXTENSIONS (Homogeneous Transformations)
   */
  //region

  /**
   * @param dx <font color="orange">{@code double}</font>
   * - how many units to translate on the X axis
   * @param dy <font color="orange">{@code double}</font>
   * - how many units to translate on the Y axis
   * @param dz <font color="orange">{@code double}</font>
   * - how many units to translate on the Z axis
   */
  public void translate(double dx, double dy, double dz) {
    this.elements[3]  += dx;
    this.elements[7]  += dy;
    this.elements[11] += dz;
  }
  /**
   * @param eulerAngle <font color="orange">{@code double}</font>
   * - the angle, in degrees, to rotate on the X axis
   */
  public void rotateX(double eulerAngle) {
    double angleRadians = Math.toRadians(eulerAngle);
    double cos = Math.cos(angleRadians);
    double sin = Math.sin(angleRadians);
    double[] rotationX = new double[]{
        1, 0, 0, 0,
        0, cos, -sin, 0,
        0, sin, cos, 0,
        0, 0, 0, 1
    };
    this.multiplyMatrix(new Matrix4(rotationX));
  }
  /**
   * @param eulerAngle <font color="orange">{@code double}</font>
   * - the angle, in degrees, to rotate on the Y axis
   */
  public void rotateY(double eulerAngle) {
    double angleRadians = Math.toRadians(eulerAngle);
    double cos = Math.cos(angleRadians);
    double sin = Math.sin(angleRadians);
    double[] rotationY = new double[]{
        cos, 0, sin, 0,
        0, 1, 0, 0,
        -sin, 0, cos, 0,
        0, 0, 0, 1
    };
    this.multiplyMatrix(new Matrix4(rotationY));
  }
  /**
   * @param eulerAngle <font color="orange">{@code double}</font>
   * - the angle, in degrees, to rotate on the Z axis
   */
  public void rotateZ(double eulerAngle) {
    double angleRadians = Math.toRadians(eulerAngle);
    double cos = Math.cos(angleRadians);
    double sin = Math.sin(angleRadians);
    double[] rotationZ = new double[]{
        cos, -sin, 0, 0,
        sin, cos, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };
    this.multiplyMatrix(new Matrix4(rotationZ));
  }
  /**
   * @param eulerAngles <font color="orange">{@link Vector3}</font>
   * - the angle, in degrees, to rotate on each axis.
   */
  public void rotate(Vector3 eulerAngles) {

    double radX = Math.toRadians(eulerAngles.x);
    double radY = Math.toRadians(eulerAngles.y);
    double radZ = Math.toRadians(eulerAngles.z);

    double cx = Math.cos(radX); double sx = Math.sin(radX);
    double cy = Math.cos(radY); double sy = Math.sin(radY);
    double cz = Math.cos(radZ); double sz = Math.sin(radZ);
     /*
        ┌                                                                                                ┐
        │ cos(y)*cos(z) + sin(y)*sin(x)*sin(z) , -cos(y)*sin(z) + sin(y)*sin(x)*cos(z), sin(y)*cos(x) , 0│
        │ cos(x)*cos(z)                        , cos(x)*cos(z)                        , -sin(x)       , 0│
        │ -sin(y)*cos(z) + cos(y)*sin(x)*sin(z), sin(y)*sin(z) + cos(y)*sin(x)*cos(z) , cos(y)*cos(x) , 0│
        │                  0                   ,                 0                    ,       0       , 1│
        └                                                                                                ┘
     */

    double[] combinedRotation = new double[]{
        cy * cz + sy * sx * sz,   -cy * sz + sy * sx * cz,   sy * cx,   0,
        cx * sz,                  cx * cz,                  -sx,        0,
        -sy * cz + cy * sx * sz,   sy * sz + cy * sx * cz,   cy * cx,   0,
        0,                        0,                         0,         1
    };

    this.multiplyMatrix(new Matrix4(combinedRotation));
  }
  /**
   * @param pitch <font color="orange">{@code double}</font>
   * - how many degrees to rotate on the X axis
   * @param yaw <font color="orange">{@code double}</font>
   * - how many degrees to rotate on the Y axis
   * @param roll <font color="orange">{@code double}</font>
   * - how many degrees to rotate on the Z axis
   */
  public void rotate(double pitch, double yaw, double roll) {

    double radX = Math.toRadians(pitch);
    double radY = Math.toRadians(yaw);
    double radZ = Math.toRadians(roll);

    double cx = Math.cos(radX); double sx = Math.sin(radX);
    double cy = Math.cos(radY); double sy = Math.sin(radY);
    double cz = Math.cos(radZ); double sz = Math.sin(radZ);
     /*
        ┌                                                                                                ┐
        │ cos(y)*cos(z) + sin(y)*sin(x)*sin(z) , -cos(y)*sin(z) + sin(y)*sin(x)*cos(z), sin(y)*cos(x) , 0│
        │ cos(x)*cos(z)                        , cos(x)*cos(z)                        , -sin(x)       , 0│
        │ -sin(y)*cos(z) + cos(y)*sin(x)*sin(z), sin(y)*sin(z) + cos(y)*sin(x)*cos(z) , cos(y)*cos(x) , 0│
        │                  0                   ,                 0                    ,       0       , 1│
        └                                                                                                ┘
     */

    double[] combinedRotation = new double[]{
        cy * cz + sy * sx * sz,   -cy * sz + sy * sx * cz,   sy * cx,   0,
        cx * sz,                  cx * cz,                  -sx,        0,
        -sy * cz + cy * sx * sz,   sy * sz + cy * sx * cz,   cy * cx,   0,
        0,                        0,                         0,         1
    };

    this.multiplyMatrix(new Matrix4(combinedRotation));
  }
  //endregion
}
