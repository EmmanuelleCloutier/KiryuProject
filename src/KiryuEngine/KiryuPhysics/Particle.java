package KiryuEngine.KiryuPhysics;

public class Particle {

    private final double mass;
    private final double inverseMass;

    private Vector3 acceleration;
    private Vector3 position;
    private Vector3 linearVelocity;
    private Vector3 direction;

    private final double damping = 1.f;

//constructeur qui cre une particule seulement avec une masse (tout est 0,0,0)
    public Particle(double mass) {

        this.mass = mass;
        this.inverseMass = 1 / this.mass;

        this.acceleration = Vector3.ZERO_VECTOR;
        this.position = Vector3.ZERO_VECTOR;
        this.linearVelocity = Vector3.ZERO_VECTOR;
        this.direction = Vector3.ZERO_VECTOR;
    }

    
//constructeur qui cree une particule avec une masse,
//mais aussi avec une position et une vitesse initiale deja definies
//utile pour creer un projectile directement a sa position de depart
//avec sa vitesse de lancement
    public Particle(
        double mass,
        Vector3 position,
        Vector3 linearVelocity
    ) {

        this.mass = mass;
        this.inverseMass = 1 / this.mass;

        this.acceleration = new Vector3();
        this.position = new Vector3(position);
        this.linearVelocity = new Vector3(linearVelocity);
    }


    /**
     * @return the mass of the particle
     */
    public double getMass() {
        return mass;
    }


    /**
     * @return 1 / mass of the particle
     */
    public double getInverseMass() {
        return inverseMass;
    }


    /**
     * @return particle's acceleration
     */
    public Vector3 getAcceleration() {
        return acceleration;
    }


    /**
     * @param acceleration sets the particle's acceleration
     */
    public void setAcceleration(Vector3 acceleration) {
        this.acceleration = acceleration;
    }


    /**
     * @return particle's position
     */
    public Vector3 getPosition() {
        return position;
    }


    /**
     * @param position sets the particle's position
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }


    /**
     * @return particle's linear velocity
     */
    public Vector3 getLinearVelocity() {
        return linearVelocity;
    }


    /**
     * @param linearVelocity sets the particle's velocity
     */
    public void setLinearVelocity(Vector3 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }


    /**
     * @return damping coefficient
     */
    public double getDamping() {
        return damping;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

}