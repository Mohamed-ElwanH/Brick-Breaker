import java.security.PublicKey;

public class Paddle extends GameObject{
    GameObject other;
    Paddle(int posX, int posY, int width, int height, String path)
    {
        super(posX, posY, width, height, path);
        this.setGameObjCollider(new BoxCollider(posX, posY, width, height, this));

    }

    @Override
    public void onHit()
    {

    }
    @Override
    public void update()
    {

    }
}
