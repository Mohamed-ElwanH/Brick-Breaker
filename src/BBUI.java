import javax.swing.*;
import java.net.URL;


public class BBUI extends JFrame {

    GamePanel gamePanel;
    ImageIcon icon;
    URL url;
    BBUI() {
        url = getClass().getResource("/UIElements/ball.png");
        icon = new ImageIcon(url);
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setIconImage(icon.getImage());
        this.setTitle("BrickBreaker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        
        this.setVisible(true);
    }


}
