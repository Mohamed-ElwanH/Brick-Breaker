import java.awt.*;

public class Collider {
    private int colliderWidth;
    private int colliderHeight;
    private int colliderArea;
    protected Rectangle collider;

    public Collider(int posX, int posY,int width, int height) {
        colliderWidth = width;
        colliderHeight = height;
        colliderArea = colliderHeight * colliderWidth;
        collider = new Rectangle(posX, posY,colliderWidth, colliderHeight);
    }

    public boolean Collided(Rectangle other) {
        if (this.collider.intersects(other)) {
            OnCollisionEnter();
            return true;
        }
        return false;
    }

    public int getColliderHeight() {
        return colliderHeight;
    }

    public int getColliderWidth() {
        return colliderWidth;
    }
    public int getColliderArea()
    {
        return colliderArea;
    }

    public Rectangle getCollider() {
        return collider;
    }

    protected void OnCollisionEnter() {}


}
