import java.security.PublicKey;

public class Paddle extends GameObject{
    private GameObject other;
    private int paddleSpeedX;
    Paddle(int posX, int posY, int width, int height, int paddleSpeedX, String path)
    {
        super(posX, posY, width, height, path);
        this.setGameObjCollider(new BoxCollider(posX, posY, width, height, this));
        this.paddleSpeedX = paddleSpeedX;
    }
    public void movePaddle(InputHandler keyInput)
    {
        if(keyInput.isRightKeyPressed())
            this.setGameObjPos(this.getGameObjPos().x+paddleSpeedX, this.getGameObjPos().y);
        if(keyInput.isLeftKeyPressed())
            this.setGameObjPos(this.getGameObjPos().x-paddleSpeedX, this.getGameObjPos().y);
    }
    public int getPaddleSpeedX()
    {
        return paddleSpeedX;
    }
    public void setPaddleSpeedX(int speedX)
    {
        paddleSpeedX = speedX;
    }
    @Override
    public void onHit() {}
    @Override
    public void update()
    {
        updateCollider();
    }
}
