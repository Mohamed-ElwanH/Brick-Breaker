import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class GameObject {
    private Collider gameObjCollider;
    private Image gameObjSprite;

    private  Point gameObjPos;
    private int gameObjWidth;
    private int gameObjHeight;

    public GameObject(int posX, int posY, int width, int height, String path)
    {
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
            gameObjCollider.getCollider().setLocation(gameObjPos.x,gameObjPos.y);
        else
            throw new RuntimeException("gameObjCollider is null");
    }
    public void setGameObjSprite(Image other)
    {
        this.gameObjSprite = other;
    }
    public Image getGameObjSprite()
    {
        return this.gameObjSprite;
    }
    public void setGameObjPos(int posX, int posY)
    {
        gameObjPos.x = posX;
        gameObjPos.y = posY;
    }
    public abstract void onHit();
    public abstract void update();
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
    public Collider getGameObjCollider(){return gameObjCollider;}
    public void setGameObjCollider(Collider other){this.gameObjCollider = other;}

}
