import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GamePanel extends JPanel {
    //GameObjects
    private Ball ballObj;

    private Paddle paddleObj;

    private Timer gameLoop;

    private Point ballObjPos;
    private Point ballObjSpeed;
    private Point brickObjPos;
    private Point paddleObjPos;

    private int ballObjWidth;
    private int ballObjHeight;
    private int brickObjWidth;
    private int brickObjHeight;
    private int paddleObjWidth;
    private int paddleObjHeight;



    private String ballObjPath;
    private String paddleObjPath;

    private ArrayList<Brick> bricksObj;

    public GamePanel()
    {
        ballObjPos = new Point(400-15, 470);
        ballObjSpeed = new Point(6, 6);
        ballObjWidth = 30;
        ballObjHeight = 30;
        brickObjWidth = 80;
        brickObjHeight = 20;
        brickObjPos = new Point(200, 200);
        ballObjPath = "/UIElements/ball.png";

        paddleObjWidth = 140;
        paddleObjHeight = 30;
        paddleObjPos = new Point(400-70, 500);
        paddleObjPath = "/UIElements/paddle.png";



        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.blue);
        this.setFocusable(true);

        bricksObj = new ArrayList<>();
        ballObj = new Ball(ballObjPos.x,
                ballObjPos.y,
                ballObjWidth,
                ballObjHeight,
                ballObjSpeed.x,
                ballObjSpeed.y,
                ballObjPath);
        paddleObj = new Paddle(paddleObjPos.x,
                paddleObjPos.y,
                paddleObjWidth,
                paddleObjHeight,
                paddleObjPath);
        setBricks();
        gameLoop = new Timer(16, e->{
            update();
            repaint();
        });
        gameLoop.start();
    }
    public void update()
    {
        ballObj.update();
       ballObj.checkPaddleCollision(paddleObj);
        checkBorderCollision();
        Iterator<Brick> brickIt = bricksObj.iterator();
        while (brickIt.hasNext()) //TO DO: put inside a method
        {
            Brick brick = brickIt.next();
            if(brick.isDestroyed())
                brickIt.remove();
            else
                ballObj.checkSideCollision(brick);

        }

    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(ballObj.getGameObjSprite(),
                ballObj.getGameObjPos().x,
                ballObj.getGameObjPos().y,
                ballObj.getGameObjWidth(),
                ballObj.getGameObjHeight(),
                null);
        g.drawImage(paddleObj.getGameObjSprite(),
                paddleObj.getGameObjPos().x,
                paddleObj.getGameObjPos().y,
                paddleObj.getGameObjWidth(),
                paddleObj.getGameObjHeight(),
                null);
        for(Brick brick : bricksObj)
            g.drawImage(brick.getGameObjSprite(),
                    brick.getGameObjPos().x,
                    brick.getGameObjPos().y,
                    brick.getGameObjWidth(),
                    brick.getGameObjHeight(),
                    null);
    }
    private void checkBorderCollision()
    {
        if(this.getWidth() == 0 || this.getHeight() ==0)
            return;
        if(ballObj.getGameObjPos().x <= 0)
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
    private String chooseRandomBrick()
    {
        Random rnd = new Random();
        int choice = rnd.nextInt(6);
        switch (choice)
        {
            case 0:
                return "blueBrick";
            case 1:
                return "greenBrick";
            case 2:
                return "yellowBrick";
            case 3:
                return "orangeBrick";
            case 4:
                return "redBrick";
            case 5:
                return "darkRedBrick";
            default:
                break;
        }
        return "";
    }
    private void setBricks()
    {
        int cols = 800/ brickObjWidth;
        int rows = 7;
        int bricksOffset = 50;
        int posX;
        int posY;
        int hits;
        String brickType;
        Random rnd = new Random();

        for(int i = 0; i < cols; i++)
        {
            for(int j = 0; j < rows; j++)
            {   posX = i* brickObjWidth;
                posY = j* brickObjHeight;
                brickType = chooseRandomBrick();
                hits = rnd.nextInt(3)+1;
                bricksObj.add(new Brick(posX,
                        posY,
                        brickObjWidth,
                        brickObjHeight,
                        brickType,
                        hits));
            }
        }
    }


}


//TO DO: paddle, instantiate bricks, input handling
//set hp for bricks