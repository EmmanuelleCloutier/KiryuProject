package Tests;

import KiryuEngine.KiryuPhysics.Collision.*;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;

public class TestsCollision {



    //Test Collision Entre 2 Particules
    public static void test_overlapping_2_particle() {

        Collider c1 = new CollisionSphere(2);
        Collider c2 = new CollisionSphere(3);

        Particle p1 = new Particle(0, new Vector3(0,0,0), new Vector3(0,0,0));
        Particle p2 = new Particle(0, new Vector3(10,0,0), new Vector3(0,0,0));

        p1.setCollider(c1);
        p2.setCollider(c2);

        //  Pas de Collision
        assert !CollisionDetector.isOverlapping(p1, p2) : "Error : Test Overlapping 2 Particules : Pas de collision";

        //  Collision Parfaite
        p2.setPosition(new Vector3(5, 0, 0));
        assert CollisionDetector.isOverlapping(p1, p2) : "Error : Test Overlapping 2 Particules : collision parfaite";

        //  Interpénétration
        p2.setPosition(new Vector3(4, 3, 0));
        assert CollisionDetector.isOverlapping(p1, p2) : "Error : Test Overlapping 2 Particules : interpénétration";

    }



    //Test Collision Câble
    public static void test_cable() {

        Collider c1 = new CollisionSphere(2);
        Collider c2 = new CollisionSphere(3);

        Particle p1 = new Particle(0, new Vector3(0,0,0), new Vector3(0,0,0));
        Particle p2 = new Particle(0, new Vector3(5,0,0), new Vector3(0,0,0));

        p1.setCollider(c1);
        p2.setCollider(c2);

        Cable c = new Cable(p1, p2, 10);

        //  Distance Valide
        assert !c.isColliding() : "Error : Test Cable : distance valide";

        //  Distance Limite
        p2.setPosition(new Vector3(10,0,0));
        assert !c.isColliding() : "Error : Test Cable : distance limite";

        //  Distance dépassé
        p2.setPosition(new Vector3(100,0,0));
        assert c.isColliding() : "Error : Test Cable : distance dépassé";

    }

    //Test Collision Tige
    public static void test_tige() {

        Collider c1 = new CollisionSphere(2);
        Collider c2 = new CollisionSphere(3);

        Particle p1 = new Particle(0, new Vector3(0,0,0), new Vector3(0,0,0));
        Particle p2 = new Particle(0, new Vector3(5,0,0), new Vector3(0,0,0));

        p1.setCollider(c1);
        p2.setCollider(c2);

        Rod r = new Rod(p1, p2, 10);

        //  Distance Trop Proche
        assert r.isColliding() : "Error : Test Cable : distance trop proche";

        //  Distance Valide
        p2.setPosition(new Vector3(10,0,0));
        assert !r.isColliding() : "Error : Test Cable : distance limite";

        //  Distance Trop Loin
        p2.setPosition(new Vector3(100,0,0));
        assert r.isColliding() : "Error : Test Cable : distance trop loin";

    }

    public static void test_contact() {
        Collider c1 = new CollisionSphere(1);
        Collider c2 = new CollisionSphere(2);

        Particle p1 = new Particle(0, new Vector3(1,1,0), new Vector3(0,0,0));
        Particle p2 = new Particle(0, new Vector3(4,1,0), new Vector3(0,0,0));

        p1.setCollider(c1);
        p2.setCollider(c2);

        Contact contact = new Contact(p1, p2);

        assert contact.getContactPoint().equals(new Vector3(2, 1, 0)) : "Error : Point de contact incorrect";
        assert contact.getNormale().equals(new Vector3(3, 0, 0)) : "Error : Normale incorrect";

    }

    static void main() {

        System.out.println("Début des tests\n");
        TestsCollision.test_overlapping_2_particle();
        TestsCollision.test_cable();
        TestsCollision.test_tige();
        TestsCollision.test_contact();
        System.out.println("Fin des tests\n");

    }
}
