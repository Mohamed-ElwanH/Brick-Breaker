import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class GameObject {
    protected Collider gameObjCollider;
    protected Image gameObjSprite;
    protected int gameObjPosX;
    protected int gameObjPosY;
    protected int gameObjWidth;
    protected int gameObjHeight;

    public GameObject(int posX, int posY, int width, int height)
    {
        this.gameObjPosX = posX;
        this.gameObjPosY = posY;
        this.gameObjWidth = width;
        this.gameObjHeight = height;
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
            gameObjCollider.collider.setLocation(gameObjPosX,gameObjPosY);
        throw new RuntimeException("gameObjCollider is null");
    }
}