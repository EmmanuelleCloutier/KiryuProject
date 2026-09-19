package KiryuEngine.KiryuPhysics;

public class Particle {

    private final double mass;
    private final double inverseMass;

    private Vector3 acceleration;
    private Vector3 position;
    private Vector3 linearVelocity;

    private final double damping = 1.f;


    Particle(double mass) {

        this.mass = mass;
        this.inverseMass = 1 / this.mass;

        this.acceleration = new Vector3();
        this.position = new Vector3();
        this.linearVelocity = new Vector3();
    }


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
}