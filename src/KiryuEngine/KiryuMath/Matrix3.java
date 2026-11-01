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
   * the determinant of the matrix
   */
  private double determinant;
  /**
   * determines if we need to solve for the determinant or not
   */
  private boolean determinantDirty = false;
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
      solveDeterminant();
    }
    else{
      this.elements = identity.clone();
      this.determinant = 1;
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
      solveDeterminant();
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
   * returns the determinant of the matrix, if the matrix was modified,
   * the determinant should be dirty and will be solved before returning
   * @return <font color="orange">{@code double}</font>
   * - the determinant of the matrix.
   */
  public double getDeterminant(){
    if (determinantDirty){
      solveDeterminant();
    }
    return this.determinant;
  }
  /**
   * @return <font color="yellow">{@link Matrix3}</font>
   * - the identity Matrix of a 3x3 matrix as a new object
   */
  @Contract(" -> new")
  public static @NotNull Matrix3 getIdentity(){
    return new Matrix3();
  }
  //endregion

  /*
  SETTER
   */
  //region
  /**
   *
   * @param elements
   */
  public void setElements(double[] elements){
    if (elements.length == 9) {
      this.elements = elements.clone();
      solveDeterminant();
    }
  }
  /**
   *
   * @param index
   * @param value
   */
  public void setElement(int index, double value){
    if (index > 8 || index < 0)return;
    this.elements[index] = value;
    this.determinantDirty = true;
  }

  //endregion

  /**
   *
   */
  private void solveDeterminant(){
    /*
        ┌       ┐
        │ a b c │
    A = │ d e f │
        │ g h i │
        └       ┘
      det(A) = a(ei-fh)-b(di-fg)+c(dh-eg)
     */
    this.determinant =
        (elements[0] *
        ((elements[4]*elements[8]) - (elements[5]*elements[7]))) -
        (elements[1] *
            ((elements[3]*elements[8])-(elements[5]*elements[6])))+
        (elements[2] *
            ((elements[3]*elements[7])-(elements[4]*elements[6])));

    this.determinantDirty = false;
  }

  /**
   *
   * @return
   */
  public @Nullable Matrix3 getInverse(){
    if (determinantDirty){
      solveDeterminant();
    }
    if (this.determinant >-1e-8 && this.determinant<1e-8){
      return null;
    }
    return null;
  }

  /**
   *
   * @param scalar
   */
  public void multiplyScalar(double scalar){

    for (double value:elements
         ) {
      value *= scalar;
    }
    determinantDirty = true;
  }

  /**
   *
   * @param vector
   */
  public void multiplyVector3(Vector3 vector){
    determinantDirty = true;
  }

  /**
   *
   * @return
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

  /**
   *
   * @param mat
   * @return
   */
  public boolean equals(Matrix3 mat){
    for (int i = 0; i < 9; i++) {
      if (this.elements[i] != mat.getElement(i)){
        return false;
      }
    }
    return true;
  }
}
