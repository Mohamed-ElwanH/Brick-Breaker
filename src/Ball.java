import java.awt.*;

public class Ball extends GameObject{

    private int ballSpeedX;
    private int ballSpeedY;

    public Ball(int posX, int posY, int width, int height, int speedX, int speedY, String path) {
        super(posX, posY, width, height, path);
        this.ballSpeedX = speedX;
        this.ballSpeedY = speedY;
        this.gameObjCollider = new Collider(this.getGameObjPos().x,this.getGameObjPos().y,this.getGameObjWidth(), this.getGameObjHeight());

    }
    public void setBallSpeed(int speedX, int speedY)
    {
        this.ballSpeedX = speedX;
        this.ballSpeedY = speedY;
    }
    public Point getBallSpeed()
    {
        Point ballSpeed = new Point(ballSpeedX, ballSpeedY);
        return ballSpeed;
    }
    protected void moveBall()
    {
        int newSpeedX = this.getGameObjPos().x + ballSpeedX;
        int newSpeedY = this.getGameObjPos().y+ ballSpeedY;
        this.setGameObjPos(newSpeedX, newSpeedY);
    }

    public void update()
    {
        this.moveBall();
        updateCollider();
    }
}
