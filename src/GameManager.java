import java.io.*;

public class GameManager  {
    private static int gameScore;
    private static int highscore;
    private static final int hitScore = 10;
    private static boolean ballAlive;
    private GameManager() {}
    public static void loadHighScore() throws IOException {
        File file = new File("F://Repository//Brick Breaker Game//Brick Breaker//src//highscore.txt");
        try
        {
            FileReader reader = new FileReader(file);
            int c;
            StringBuilder sb = new StringBuilder();
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            reader.close();
            String content = sb.toString().trim();
            try {
                highscore = Integer.parseInt(content);
            } catch (NumberFormatException e) {
                System.out.println("Invalid highscore format: '" + content + "'");
                highscore = 0;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("score file not loaded");
            highscore = 0;
        }
        catch (IOException e)
        {
            System.out.println("Error reading highscore file");
            highscore = 0;
        }
    }
    private static void setHighscore(int newHighScore)
    {
        File file = new File("F:/Repository/Brick Breaker Game/Brick Breaker/src/highscore.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(newHighScore));
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error saving highscore");
        }

    }
    public static int getGameScore() {return gameScore;}
    public static int getHighscore(){return highscore;}
    public static void setNewHighScore(int newHighScore)
    {
        if(newHighScore > highscore)
        {
            highscore = newHighScore;
            setHighscore(highscore);
        }
    }
    public static void  setGameScore(int score) {gameScore = score;}
    public static void setBallLife(boolean state){ballAlive = state;}
    public static int getHitScore(){return hitScore;}
    public static boolean getBallLife(){return ballAlive;}
    public static boolean isWin()
    {
        return (ballAlive && GamePanel.bricksObj.isEmpty());
    }
    public static boolean isLose(){return (!ballAlive);}
}
