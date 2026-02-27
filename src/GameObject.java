import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class GameObject {
    protected Collider gameObjCollider;
    protected final Image gameObjSprite;
//    protected int gameObjPosX;
//    protected int gameObjPosY;
    private  Point gameObjPos;
    private int gameObjWidth;
    private int gameObjHeight;
    public void setGameObjPos(int posX, int posY)
    {
        gameObjPos.x = posX;
        gameObjPos.y = posY;
    }
    public Point getGameObjPos()
    {
        return gameObjPos;
    }
    public int getGameObjWidth()
    {
        return gameObjWidth;
    }
    public int getGameObjHeight()
    {
        return gameObjHeight;
    }
    public GameObject(int posX, int posY, int width, int height, String path)
    {
//        this.gameObjPosX = posX;
//        this.gameObjPosY = posY;
        this.gameObjPos = new Point(posX, posY);
        this.gameObjWidth = width;
        this.gameObjHeight = height;
        this.gameObjSprite = loadSprite(path, this.gameObjWidth, this.gameObjHeight);
    }
    protected Image loadSprite(String path, int width, int height)
    {
        URL url = getClass().getResource(path);
        if(url == null)
            throw new RuntimeException("Sprite not found at: " + path);
        Image image = new ImageIcon(url).getImage();
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    protected void updateCollider()
    {
        if(gameObjCollider != null)
            gameObjCollider.collider.setLocation(gameObjPos.x,gameObjPos.y);
        else
            throw new RuntimeException("gameObjCollider is null");
    }
}