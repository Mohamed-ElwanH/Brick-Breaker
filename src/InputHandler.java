import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    private static boolean rightKeyPressed;
    private static boolean leftKeyPressed;
    private static boolean paused;
    private static boolean enterKeyPressed;
    public InputHandler()
    {
        rightKeyPressed = false;
        leftKeyPressed = false;
        paused = false;
        enterKeyPressed = false;
    }
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            rightKeyPressed = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            leftKeyPressed = true;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            if(!GameManager.isLose() && !GameManager.isWin())
                paused = !paused;
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            enterKeyPressed = true;
    }
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            rightKeyPressed = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            leftKeyPressed = false;
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            enterKeyPressed = false;
    }


    public static boolean isRightKeyPressed(){return rightKeyPressed;}
    public static boolean isLeftKeyPressed(){return leftKeyPressed;}
    public static boolean isPaused(){return paused;}
    public static boolean isEnterKeyPressed(){return enterKeyPressed;}
    public static void setEnterKey(boolean state){enterKeyPressed = state;}
}
