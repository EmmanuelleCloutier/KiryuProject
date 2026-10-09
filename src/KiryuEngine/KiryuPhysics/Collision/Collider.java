package KiryuEngine.KiryuPhysics.Collision;

public interface Collider {
    public default void onCollision(Collider collider) {};
}
