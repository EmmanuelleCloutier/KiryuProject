package Tests;

import KiryuEngine.KiryuPhysics.Vector3;

public class TestsUnitaires {

  private String errorMessage(String test, String cas) {
    return "Erreur " + test + " - cas " + cas + ".";
  }

  private void testVector3() {
    String test;
    Vector3 a = new Vector3(1, 2, 4);
    Vector3 b = new Vector3(0, -2, 0);

    test = "initialisation";
    assert new Vector3(1, 2, 4).equals(a) : errorMessage(test, "quelconque");
    assert new Vector3().equals(Vector3.ZERO_VECTOR) : errorMessage(test, "nul");

    test = "calcul norme";
    assert a.getLength() == Math.sqrt(21) : errorMessage(test, "quelconque");
    assert Vector3.ZERO_VECTOR.getLength() == 0 : errorMessage(test, "nul");

    test = "normalisation";
    assert b.normalize().equals(0, -1, 0) : errorMessage(test, "quelconque");
    assert a.normalize().getLength() == 1 : errorMessage(test, "norme");
    assert Vector3.ZERO_VECTOR.normalize().equals(0, 0, 0) : errorMessage(test, "nul");

    test = "multiplication";
    assert a.scale(3).equals(3, 6, 12) : errorMessage(test, "quelconque");
    assert a.scale(-0.5).equals(-0.5, -1, -2) : errorMessage(test, "quelconque");
    assert Vector3.ZERO_VECTOR.scale(3).equals(0, 0, 0) : errorMessage(test, "nul");

    test = "addition";
    assert a.add(b).equals(1, 0, 4) : errorMessage(test, "quelconque");
    assert a.add(Vector3.ZERO_VECTOR).equals(a) : errorMessage(test, "nul");

    test = "produit scalaire";
    assert a.dotProduct(b) == -4 : errorMessage(test, "quelconque");
    assert a.dotProduct(Vector3.ZERO_VECTOR) == 0 : errorMessage(test, "nul");

    test = "produit vectoriel";
    assert a.crossProduct(b).equals(8, 0, -2) : errorMessage(test, "quelconque");
    assert a.crossProduct(Vector3.ZERO_VECTOR).equals(0, 0, 0) : errorMessage(test, "nul");

    test = "orthogonalité";
    assert a.isOrthogonal(a.crossProduct(b)) : errorMessage(test, "vrai");
    assert !a.isOrthogonal(b) : errorMessage(test, "faux");
    assert a.isOrthogonal(Vector3.ZERO_VECTOR) : errorMessage(test, "nul");

    test = "colinéarité";
    assert a.isCollinear(a.normalize()) : errorMessage(test, "vrai");
    assert !a.isCollinear(b) : errorMessage(test, "faux");
    assert a.isCollinear(Vector3.ZERO_VECTOR) : errorMessage(test, "nul");

  }

  void main() {
    System.out.println("TEST Vector3...");
    testVector3();
    System.out.println("TEST Vector3 terminé.");
  }
}