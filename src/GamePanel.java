import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GamePanel extends JPanel {
    //GameObjects
    private Ball ballObj;

    private Paddle paddleObj;

    protected static Timer gameLoop;

    private Point ballObjPos;
    private Point ballObjSpeed;
    private Point brickObjPos;
    private Point paddleObjPos;

    private final int ballObjWidth;
    private final int ballObjHeight;
    private final int brickObjWidth;
    private final int brickObjHeight;
    private final int paddleObjWidth;
    private final int paddleObjHeight;
    private final int paddleSpeedX;


    private final String ballObjPath;
    private final String paddleObjPath;

    protected static ArrayList<Brick> bricksObj;

    private InputHandler inputHandler;



    private Collider ballCollider;
    private Collider paddleCollider;


    public GamePanel()
    {
        ballObjPos = new Point(400-15, 470);
        ballObjSpeed = new Point(8, 8); //maybe make a difficulty level and make the ball speed based on it
        ballObjWidth = 30;
        ballObjHeight = 30;
        brickObjWidth = 80;
        brickObjHeight = 20;
        brickObjPos = new Point(200, 200);
        ballObjPath = "/UIElements/ball.png";

        paddleObjWidth = 140;
        paddleObjHeight = 30;
        paddleSpeedX = 15;
        paddleObjPos = new Point(400-70, 500);
        paddleObjPath = "/UIElements/paddle.png";

        GameManager.setGameScore(0);
        GameManager.setRemainingHits(30);
        GameManager.setBallLife(true);




        inputHandler = new InputHandler();

        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.blue);
        this.setFocusable(true);
        this.addKeyListener(inputHandler);
        this.requestFocusInWindow();



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
                paddleSpeedX,
                paddleObjPath);
        paddleCollider = paddleObj.getGameObjCollider();
        ballCollider = ballObj.getGameObjCollider();
        setBricks();
        gameLoop = new Timer(16, e->{
            update();
            repaint();
        });
        gameLoop.start();
    }
    public void update()
    {
        if(GameManager.isLose())
        {
            if(InputHandler.isEnterKeyPressed())
            {
                restartGame();
                //InputHandler.setEnterKey(false);
            }
            return;
        }
        ballObj.update();
        ballObj.checkPaddleCollision(paddleObj);
        paddleObj.movePaddle(inputHandler);
        paddleCollider.Collided(ballCollider.getCollider());
        paddleObj.update();
        checkBallBorderCollision();
        checkPaddleBorderCollision();
        Iterator<Brick> brickIt = bricksObj.iterator();
        while (brickIt.hasNext()) //TO DO: put inside a method
        {
            Brick brick = brickIt.next();
            if(brick.isDestroyed())
                brickIt.remove();
            else
                ballObj.checkSideCollision(brick);

        }
        gameWon();
        gameLost();


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
        g.setColor(Color.white);
        createGameText(g);

    }
    private void createGameText(Graphics g) //maybe set it in BBUI
    {
        JLabel gameScoreLabel;
        JLabel remainingHitsLabel;
        gameScoreLabel = new JLabel("Game Score: " + GameManager.getGameScore());
        remainingHitsLabel = new JLabel("Remaining Hits: " + GameManager.getRemainingHits());
        g.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        g.drawString(gameScoreLabel.getText(), 10, 500);
        g.drawString(remainingHitsLabel.getText(), 325, 580);
        if (GameManager.isWin()) {
            g.setColor(new Color(0, 0, 0, 150)); // semi transparent black overlay
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.GREEN);
            g.setFont(new Font(Font.SERIF, Font.BOLD, 50));
            g.drawString("GAME WON!!", 200, 300);
        }
        if (GameManager.isLose()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.setFont(new Font(Font.SERIF, Font.BOLD, 50));
            g.drawString("LOSER NIGGER!!", 200, 300);
        }
    }
    private void gameWon()
    {
        if(GameManager.isWin())
        {

            JLabel winLabel = new JLabel("GAME WON!!");
            winLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));


            gameLoop.stop();
            repaint();
        }
    }
    private void gameLost()
    {
        if(GameManager.isLose())
        {
//            JLabel loseLabel = new JLabel("LOSER NIGGER!!");
//            loseLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
//            loseLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//            loseLabel.setVerticalTextPosition(SwingConstants.CENTER);
//            this.add(loseLabel);
//            gameLoop.stop();
//            if(InputHandler.isEnterKeyPressed())
//                restartGame();
            repaint();
        }
    }
    private void checkBallBorderCollision()
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
        if(ballObj.getGameObjPos().y <=0)
        {
            ballObj.setGameObjPos(ballObj.getGameObjPos().x, 0);
            int speedX = ballObj.getBallSpeed().x;
            int invertedSpeedY = -ballObj.getBallSpeed().y;
            ballObj.setBallSpeed(speedX, invertedSpeedY);
        }
        if(ballObj.getGameObjPos().y>=(this.getHeight()-ballObj.getGameObjHeight()))//TO DO: change so that the player loses a life
        {
            GameManager.setBallLife(false);

//            ballObj.setGameObjPos(ballObj.getGameObjPos().x, this.getHeight()-ballObj.getGameObjHeight());
//            int speedX = ballObj.getBallSpeed().x;
//            int invertedSpeedY = -ballObj.getBallSpeed().y;
//            ballObj.setBallSpeed(speedX, invertedSpeedY);
        }
    }
    private void checkPaddleBorderCollision()
    {
        if(this.getWidth() == 0 || this.getHeight() ==0)
            return;
        if(paddleObj.getGameObjPos().x <= 0)
        {
            paddleObj.setGameObjPos(0, paddleObj.getGameObjPos().y);//so the paddle doesn't get stuck
//            int invertedSpeedX = -paddleObj.getPaddleSpeedX();
//            paddleObj.setPaddleSpeedX(invertedSpeedX);
        }
        if(paddleObj.getGameObjPos().x >= (this.getWidth()-paddleObj.getGameObjWidth()))
        {
            paddleObj.setGameObjPos(this.getWidth()-paddleObj.getGameObjWidth(), paddleObj.getGameObjPos().y);//so the paddle doesn't get stuck
//            int invertedSpeedX = -paddleObj.getPaddleSpeedX();
//            paddleObj.setPaddleSpeedX(invertedSpeedX);
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
    private void restartGame()
    {
        GameManager.setGameScore(0);
        GameManager.setRemainingHits(30);
        GameManager.setBallLife(true);

        ballObj.setGameObjPos(ballObjPos.x, ballObjPos.y);
        ballObj.setBallSpeed(ballObjSpeed.x, ballObjSpeed.y);

        paddleObj.setGameObjPos(paddleObjPos.x, paddleObjPos.y);
        ballCollider = ballObj.getGameObjCollider();
        paddleCollider = paddleObj.getGameObjCollider();
        bricksObj.clear();
        setBricks();
        gameLoop.start();
        repaint();
    }


}


//TO DO: paddle, instantiate bricks, input handling
//set hp for bricks