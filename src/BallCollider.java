import org.w3c.dom.css.Rect;

import java.awt.*;

public class BallCollider extends Collider{
    private GameObject gameObject;
    BallCollider(int posX, int posY, int width, int height, Ball other)
    {
        super(posX, posY, width, height);
        this.gameObject = other;
    }


    @Override
    public void OnCollisionEnter() {
        gameObject.onHit();
    }
}
