package KiryuEngine.KiryuMath;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Matrix3 {
  /**
   * the elements of the matrix
   * for optimization this is a flat array
   */
  private double[] elements;
  /**
   * the number of elements in the matrix
   */
  private int length = 9;
  /**
   * the identity matrix is always:
   *       ┌       ┐
   *       │ 1 0 0 │
   *   A = │ 0 1 0 │
   *       │ 0 0 1 │
   *       └       ┘
   *  therefore this is a static reference only used internally in the class
   *  to get a usable identity matrix use getIdentity()
   */
  private static final double[] identity = new double[]{1, 0, 0, 0, 1, 0, 0, 0, 1};
  /**
   * empty constructor, sets the matrix to the identity matrix
   */
  public Matrix3(){
   this.elements = identity.clone();
  }
  /**
   * Array constructor
   * @param elements <font color="orange">{@code double}</font>
   * - the elements to initialize the matrix with. It MUST have 9
   *  elements
   */
  public Matrix3(double[] elements){
    if (elements.length == 9) {
      this.elements = elements.clone();
    }
    else{
      this.elements = identity.clone();
    }
  }
  /**
   * Matrix constructor
   * @param mat3 <font color="yellow">{@link Matrix3}</font>
   * - the matrix to initialize a new matrix with. it only clones
   * the elements and solves the determinant.
   */
  public Matrix3 (@NotNull Matrix3 mat3){
      this.elements = mat3.getRawElements();
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
   * - the index between 0 & 8 of the element
   * @return <font color="orange">{@code double}</font>
   * - the value of the element at the specified index
   */
  public double getElement(int index){
    if (index > 8 || index < 0) return this.elements[0];
    return this.elements[index];
  }
  /**
   * @return <font color="yellow">{@link Matrix3}</font>
   * - the identity Matrix of a 3x3 matrix as a new object
   */
  @Contract(" -> new")
  public static @NotNull Matrix3 getIdentity(){
    return new Matrix3();
  }
  /**
   * @return <font color="orange">{@code int}</font>
   * - the number of elements in the matrix
   */
  public int getLength(){
    return this.length;
  }
  /**
   * @return <font color="orange">{@code double}</font>
   * - the determinant of this matrix
   */
  private double getDeterminant(){
    /*
        ┌       ┐
        │ a b c │
    A = │ d e f │
        │ g h i │
        └       ┘
      det(A) = a(ei-fh)-b(di-fg)+c(dh-eg)
     */
    return
        (elements[0] *
            ((elements[4]*elements[8]) - (elements[5]*elements[7]))) -
            (elements[1] *
                ((elements[3]*elements[8])-(elements[5]*elements[6])))+
            (elements[2] *
                ((elements[3]*elements[7])-(elements[4]*elements[6])));

  }
  /**
   * @return <font color="yellow">{@link Matrix3}</font>
   * - the cofactor matrix of this matrix
   */
  public @NotNull Matrix3 getCofactorMatrix(){
            /*
        ┌       ┐         ┌                   ┐
        │ a b c │         │ ei-fh fg-di dh-eg │
    A = │ d e f │  C(A) = │ ch-bi ai-cg bg-ah │
        │ g h i │         │ bf-ce bg-ah ae-bd │
        └       ┘         └                   ┘
        we also need to apply this pattern:
                    ┌       ┐
                    │ + - + │
                    │ - + - │
                    │ + - + │
                    └       ┘
     */
    double[] cofactor = new double[this.length];

    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        int subRow1 = (row + 1) % 3;
        int subRow2 = (row + 2) % 3;

        int subCol1 = (col + 1) % 3;
        int subCol2 = (col + 2) % 3;

        if (subRow1 > subRow2) {
          int temp = subRow1;
          subRow1 = subRow2;
          subRow2 = temp;
        }

        if (subCol1 > subCol2) {
          int temp = subCol1;
          subCol1 = subCol2;
          subCol2 = temp;
        }

        double minorDet =
            (this.elements[subRow1 * 3 + subCol1] * this.elements[subRow2 * 3 + subCol2]) -
                (this.elements[subRow1 * 3 + subCol2] * this.elements[subRow2 * 3 + subCol1]);

        int sign = ((row + col) % 2 == 0) ? 1 : -1;

        cofactor[row * 3 + col] = sign * minorDet;
      }
    }

    return new Matrix3(cofactor);
  }
  /**
   * @return <font color="yellow">{@link Matrix3}</font>
   * - the inverse matrix, this can be null if the determinant
   *  approaches 0
   */
  public @Nullable Matrix3 getInverse(){

    double determinant = this.getDeterminant();
    if (Math.abs(determinant) < 1e-8){
      //if determinant is near 0 return a null object
      //since this matrix has no inverse
      return null;
    }
            /*
        ┌       ┐
        │ a b c │
    A = │ d e f │  A-1 = 1/det(A) * Ct
        │ g h i │  the inverse is: the inverse of the determinant of A
        └       ┘  multiplying the transpose of the cofactor matrix of A
     */
    double inverseDeterminant = 1/determinant;
    Matrix3 cofactorMat = this.getCofactorMatrix();
    Matrix3 adjugateMat = cofactorMat.getTranspose();
    adjugateMat.multiplyScalar(inverseDeterminant);

    return new Matrix3(adjugateMat);

  }
  /**
   * @return <font color="yellow">{@link Matrix3}</font>
   * - the transpose of this matrix
   */
  public @NotNull Matrix3 getTranspose(){
        /*
        ┌       ┐       ┌       ┐
        │ a b c │       │ a d g │
    A = │ d e f │  At = │ b e h │
        │ g h i │       │ c f i │
        └       ┘       └       ┘
     */
    double[] transpose = new double[]{
        this.elements[0],this.elements[3],this.elements[6],
        this.elements[1],this.elements[4],this.elements[7],
        this.elements[2],this.elements[5],this.elements[8]
    };

    return new Matrix3(transpose);
  }

  //endregion

  /*
  SETTER
   */
  //region
  /**
   * @param elements <font color="orange">{@code double}</font>
   * - the array of elements the matrix needs to take value of
   */
  public void setElements(double[] elements){
    if (elements.length == 9) {
      this.elements = elements.clone();
    }
  }
  /**
   * @param index <font color="orange">{@code int}</font>
   * - the index of the element that needs to be changed must be
   * between 0 & 8
   * @param value <font color="orange">{@code double}</font>
   * - the new value of the element
   */
  public void setElement(int index, double value){
    if (index > 8 || index < 0)return;
    this.elements[index] = value;
  }

  //endregion
  /**
   * @param scalar <font color="orange">{@code double}</font>
   * - the scalar value to multiply the matrix with
   */
  public void multiplyScalar(double scalar){
    for (double value:elements
         ) {
      value *= scalar;
    }
  }
  /**
   * @param mat <font color="yellow">{@link Matrix3}</font>
   * - the matrix to multiply with
   */
  public void multiplyMatrix(@NotNull Matrix3 mat){
    double[] multipliedMatrix = new double[this.length];

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int out_index = i * 3 + j;
        multipliedMatrix[out_index] = 0;

        for (int k = 0; k < 3; k++) {
          multipliedMatrix[out_index] +=
              this.elements[i * 3 + k] * mat.getElement(k * 3 + j);
        }
      }
    }
    this.elements = multipliedMatrix.clone();
  }
  /**
   * @param mat <font color="yellow">{@link Matrix3}</font>
   * - the matrix to add
   */
  public void addMatrix(@NotNull Matrix3 mat){
    for (int i = 0; i < this.length; i++) {
     this.elements[i] += mat.getElement(i);
    }
  }
  /**
   * @param mat <font color="yellow">{@link Matrix3}</font>
   * - the matrix to subtract
   */
  public void subMatrix(@NotNull Matrix3 mat){
    for (int i = 0; i < this.length; i++) {
      this.elements[i] -= mat.getElement(i);
    }
  }
  /**
   * @param mat <font color="yellow">{@link Matrix3}</font>
   * - the matrix to compare
   * @return <font color="orange">{@code boolean}</font>
   * - whether both matrices are equal or not
   */
  public boolean equals(Matrix3 mat){
    for (int i = 0; i < this.length; i++) {
      if (this.elements[i] != mat.getElement(i)){
        return false;
      }
    }
    return true;
  }
  /**
   * @param vector <font color="yellow">{@link Vector3}</font>
   * - the vector to scale the matrix with
   */
  public void scale(Vector3 vector){
    for (int i = 0; i < 3; i++) {
      this.elements[i] *= vector.x;
      this.elements[3 + i] *= vector.y;
      this.elements[5 + i] *= vector.z;
    }
  }
}
