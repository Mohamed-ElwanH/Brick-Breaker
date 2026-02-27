import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


public class GamePanel extends JPanel {
    //GameObjects
    private Ball ballObj;
   // private Brick brickObj;
    private GameObject paddle;

    private Timer gameLoop;

    private Point ballObjPos = new Point(400, 300);
    private Point ballObjSpeed = new Point(3, 3);

    private int ballObjWidth = 20;
    private int ballObjHeight = 20;

    private int brickWidth = 80;
    private int brickHeight = 40;

    private Point brickPos = new Point(200, 200);

    private String ballObjPath = "/UIElements/ball.png";
    private String brickPath = "/UIElements/brick.png";

    private ArrayList<Brick> bricksObj = new ArrayList<>();
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.blue);
        this.setFocusable(true);



        ballObj = new Ball(ballObjPos.x, ballObjPos.y, ballObjWidth, ballObjHeight, ballObjSpeed.x, ballObjSpeed.y, ballObjPath);
        //brickObj = new Brick(brickPos.x, brickPos.y, brickWidth, brickHeight, brickPath);
        bricksObj.add(new Brick(brickPos.x, brickPos.y, brickWidth, brickHeight, brickPath));
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
    }
    public void update()
    {
        ballObj.update();
        //brickObj.update();
        //ballObj.gameObjCollider.Collided(brickObj.gameObjCollider.getCollider());
        checkBorderCollision();
        Iterator<Brick> brickIt = bricksObj.iterator();
        while (brickIt.hasNext()) //TO DO: put inside a method
        {
            Brick brick = brickIt.next();
            if(brick.isDestroyed())
            {
               brickIt.remove();
            }
            else
            {
                ballObj.checkSideCollision(brick);
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(ballObj.gameObjSprite,
                ballObj.getGameObjPos().x,
                ballObj.getGameObjPos().y,
                ballObj.getGameObjWidth(),
                ballObj.getGameObjHeight(),
                null);
        for(Brick brick : bricksObj)
            g.drawImage(brick.gameObjSprite,
                brick.getGameObjPos().x,
                brick.getGameObjPos().y,
                brick.getGameObjWidth(),
                brick.getGameObjHeight(),
                null);
    }

}

//TO DO: paddle, instantiate bricks, input handling