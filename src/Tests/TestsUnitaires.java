import KiryuEngine.KiryuPhysics.Vector3;

private String errorMessage(String test, String cas) {
    return "Erreur " + test + " - cas " + cas + ".";
}

private void testVector3() {
    String test;
    Vector3 a = new Vector3(1, 2, 4);
    Vector3 b = new Vector3(0, -2, 0);

    test = "initialisation";
    assert new Vector3(1, 2, 4).equals(a) : errorMessage(test, "quelconque");
    assert new Vector3().equals(Vector3.NULL_VECTOR) : errorMessage(test, "nul");

    test = "calcul norme";
    assert a.getNorme() == Math.sqrt(21) : errorMessage(test, "quelconque");
    assert Vector3.NULL_VECTOR.getNorme() == 0 : errorMessage(test, "nul");

    test = "normalisation";
    assert b.normalize().equals(0, -1, 0) : errorMessage(test, "quelconque");
    assert a.normalize().getNorme() == 1 : errorMessage(test, "norme");
    assert Vector3.NULL_VECTOR.normalize().equals(0, 0, 0) : errorMessage(test, "nul");

    test = "multiplication";
    assert a.mult(3).equals(3, 6, 12) : errorMessage(test, "quelconque");
    assert a.mult(-0.5).equals(-0.5, -1, -2) : errorMessage(test, "quelconque");
    assert Vector3.NULL_VECTOR.mult(3).equals(0, 0, 0) : errorMessage(test, "nul");

    test = "addition";
    assert a.add(b).equals(1, 0, 4) : errorMessage(test, "quelconque");
    assert a.add(Vector3.NULL_VECTOR).equals(a) : errorMessage(test, "nul");

    test = "produit scalaire";
    assert a.produitScalaire(b) == -4 : errorMessage(test, "quelconque");
    assert a.produitScalaire(Vector3.NULL_VECTOR) == 0 : errorMessage(test, "nul");

    test = "produit vectoriel";
    assert a.produitVectoriel(b).equals(8, 0, -2) : errorMessage(test, "quelconque");
    assert a.produitVectoriel(Vector3.NULL_VECTOR).equals(0, 0, 0) : errorMessage(test, "nul");

    test = "orthogonalité";
    assert a.isOrthogonal(a.produitVectoriel(b)) : errorMessage(test, "vrai");
    assert !a.isOrthogonal(b) : errorMessage(test, "faux");
    assert a.isOrthogonal(Vector3.NULL_VECTOR) : errorMessage(test, "nul");

    test = "colinéarité";
    assert a.isColineaire(a.normalize()) : errorMessage(test, "vrai");
    assert !a.isColineaire(b) : errorMessage(test, "faux");
    assert a.isColineaire(Vector3.NULL_VECTOR) : errorMessage(test, "nul");

}

void main() {
    System.out.println("TEST Vector3...");
    testVector3();
    System.out.println("TEST Vector3 terminé.");
}