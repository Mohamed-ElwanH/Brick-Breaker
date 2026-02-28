public class GameManager  {
    private static int gameScore;
    private static final int hitScore = 10;
    private static boolean ballAlive;

    private GameManager() {}
//    public static int getRemainingHits()
//    {
//        return remainingHits;
//    }
    public static int getGameScore()
    {
        return gameScore;
    }
    public static void  setGameScore(int score)
    {
        gameScore = score;
    }
    public static void setBallLife(boolean state){ballAlive = state;}
    public static int getHitScore(){return hitScore;}
    public static boolean getBallLife(){return ballAlive;}
    public static boolean isWin()
    {
        return (ballAlive && GamePanel.bricksObj.isEmpty());
    }
    public static boolean isLose(){return (!ballAlive);}
}
