import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {
    //GameObjects
    private Ball ballObj;
    private GameObject brickObj;
    private GameObject paddle;

    //Screen Borders


    private Timer gameLoop;

    private Point ballObjPos = new Point(400, 300);
    private Point ballObjSpeed = new Point(3, 3);

    private int ballObjWidth = 20;
    private int ballObjHeight = 20;

    private String ballObjPath = "/UIElements/ball.png";

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.blue);
        this.setFocusable(true);



        ballObj = new Ball(ballObjPos.x, ballObjPos.y, ballObjWidth, ballObjHeight, ballObjSpeed.x, ballObjSpeed.y, ballObjPath);

        gameLoop = new Timer(16, e->{
            update();
            repaint();
        });
        gameLoop.start();
    }
    private void checkBorderCollision()
    {
        if(ballObj.getGameObjPos().x <= 0  )
        {
            ballObj.setGameObjPos(0, ballObj.getGameObjPos().y);//so the ball doesn't get stuck
            int invertedSpeedX = -ballObj.getBallSpeed().x;
            int speedY = ballObj.getBallSpeed().y;
            ballObj.setBallSpeed(invertedSpeedX, speedY);
        }
        if(ballObj.getGameObjPos().x >= (this.getWidth()-ballObj.getGameObjWidth()))
        {
            ballObj.setGameObjPos(this.getWidth()-ballObj.getGameObjWidth(), ballObj.getGameObjPos().y);//so the ball doesn't get stuck
            int invertedSpeedX = -ballObj.getBallSpeed().x;
            int speedY = ballObj.getBallSpeed().y;
            ballObj.setBallSpeed(invertedSpeedX, speedY);
        }
        if(ballObj.getGameObjPos().y <=0) //TO DO: change so that the player loses a life
        {
            ballObj.setGameObjPos(ballObj.getGameObjPos().x, 0);
            int speedX = ballObj.getBallSpeed().x;
            int invertedSpeedY = -ballObj.getBallSpeed().y;
            ballObj.setBallSpeed(speedX, invertedSpeedY);
        }
        if(ballObj.getGameObjPos().y>=(this.getHeight()-ballObj.getGameObjHeight()))
        {
            ballObj.setGameObjPos(ballObj.getGameObjPos().x, this.getHeight()-ballObj.getGameObjHeight());
            int speedX = ballObj.getBallSpeed().x;
            int invertedSpeedY = -ballObj.getBallSpeed().y;
            ballObj.setBallSpeed(speedX, invertedSpeedY);
        }
        else
            System.out.println("W: " + this.getWidth() + " H: " + this.getHeight());
    }
    public void update()
    {
        ballObj.update();
        checkBorderCollision();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(ballObj.gameObjSprite, ballObj.getGameObjPos().x, ballObj.getGameObjPos().y, ballObj.getGameObjWidth(), ballObj.getGameObjHeight(), null);
    }

}
