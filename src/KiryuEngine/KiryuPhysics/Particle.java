package KiryuEngine.KiryuPhysics;

import KiryuEngine.KiryuPhysics.Collision.Collider;

public class Particle {

  private final RigidBody rigidbody;

//constructeur qui cre une particule seulement avec une masse (tout est 0,0,0)
    public Particle(World world, double mass, double size) {
        this.rigidbody = new RigidBody(mass,size);
        world.addRigidBody(this.rigidbody);
    }

    
//constructeur qui cree une particule avec une masse,
//mais aussi avec une position et une vitesse initiale deja definies
//utile pour creer un projectile directement a sa position de depart
//avec sa vitesse de lancement
    public Particle(
        World world,
        double mass,
        double size,
        Vector3 position,
        Vector3 linearVelocity
    ) {

        this.rigidbody = new RigidBody(mass,size);
        this.rigidbody.setPosition(position);
        this.rigidbody.setLinearVelocity(linearVelocity);
        world.addRigidBody(this.rigidbody);
    }

    /**
     * @return the mass of the particle
     */
    public double getMass() {
        return rigidbody.getMass();
    }


    /**
     * @return 1 / mass of the particle
     */
    public double getInverseMass() {
        return rigidbody.getInverseMass();
    }


    /**
     * @return particle's acceleration
     */
    public Vector3 getAcceleration() {
        return rigidbody.getForce();
    }


    /**
     * @param acceleration sets the particle's acceleration
     */
    public void setAcceleration(Vector3 acceleration) {
        this.rigidbody.setForce(acceleration);
    }


    /**
     * @return particle's position
     */
    public Vector3 getPosition() {
        return rigidbody.getPosition();
    }


    /**
     * @param position sets the particle's position
     */
    public void setPosition(Vector3 position) {
        this.rigidbody.setPosition(position);
    }


    /**
     * @return particle's linear velocity
     */
    public Vector3 getLinearVelocity() {
        return rigidbody.getLinearVelocity();
    }


    /**
     * @param linearVelocity sets the particle's velocity
     */
    public void setLinearVelocity(Vector3 linearVelocity) {
        this.rigidbody.setLinearVelocity(linearVelocity);
    }


    /**
     * @return damping coefficient
     */
    public double getDamping() {
        return rigidbody.getDamping();
    }

    public Vector3 getDirection() {
        return rigidbody.getDirection();
    }

    public void setDirection(Vector3 direction) {
        this.rigidbody.setDirection(direction);
    }

    public Collider getCollider() {
        return rigidbody.getCollider();
    }

    public RigidBody getRigidbody() {
        return rigidbody;
    }
}