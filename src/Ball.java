import java.awt.*;

public class Ball extends GameObject{

    private int ballSpeedX;
    private int ballSpeedY;

    public Ball(int posX, int posY, int width, int height, int speedX, int speedY, String path) {
        super(posX, posY, width, height, path);
        this.ballSpeedX = speedX;
        this.ballSpeedY = speedY;
        this.setGameObjCollider(new BallCollider(this.getGameObjPos().x,
                this.getGameObjPos().y,
                this.getGameObjWidth(),
                this.getGameObjHeight(), this));

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
        int newBallSpeedX = this.getGameObjPos().x + ballSpeedX;
        int newBallSpeedY = this.getGameObjPos().y+ ballSpeedY;
        this.setGameObjPos(newBallSpeedX, newBallSpeedY);
    }
    public void checkSideCollision(Brick brick)
    {
        Rectangle ballRect = this.getGameObjCollider().getCollider();
        Rectangle brickRect = brick.getGameObjCollider().getCollider();
        if(ballRect.intersects(brickRect)) {
            double ballTop = ballRect.getMinY();
            double ballBottom = ballRect.getMaxY();
            double ballRight = ballRect.getMaxX();
            double ballLeft = ballRect.getMinX();

            double brickTop = brickRect.getMinY();
            double brickBottom = brickRect.getMaxY();
            double brickRight = brickRect.getMaxX();
            double brickLeft = brickRect.getMinX();

            //overlap relevant to the brick
            double topOverlap = ballBottom - brickTop;
            double bottomOverlap = brickBottom - ballTop;
            double rightOverlap = brickRight - ballLeft;
            double leftOverlap = ballRight - brickLeft;

            //smallest overlap
            double minOverLap = Math.min(Math.min(topOverlap, bottomOverlap), Math.min(rightOverlap, leftOverlap));
            if (minOverLap == topOverlap || minOverLap == bottomOverlap)
                this.setBallSpeed(ballSpeedX, -ballSpeedY);
            if (minOverLap == rightOverlap || minOverLap == leftOverlap)
                this.setBallSpeed(-ballSpeedX, ballSpeedY);

            brick.onHit();
        }
    }
    public void checkPaddleCollision(Paddle paddleObj)
    {
        Rectangle ballRect = this.getGameObjCollider().getCollider();
        Rectangle paddleRect = paddleObj.getGameObjCollider().getCollider();
        if(ballRect.intersects(paddleRect))
        {
            this.setGameObjPos(this.getGameObjPos().x, paddleObj.getGameObjPos().y - this.getGameObjHeight());
            this.setBallSpeed(this.getBallSpeed().x, -Math.abs(this.getBallSpeed().y));
        }


    }
    @Override
    public void update()
    {
        this.moveBall();
        updateCollider();
    }

    @Override
    public void onHit() //called when hits the paddle
    {
        this.setBallSpeed(ballSpeedX, -ballSpeedY);
    }
}
