import javax.swing.*;


public class BBUI extends JFrame {

    GamePanel gamePanel;


    BBUI() {
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setTitle("BrickBreaker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        
        this.setVisible(true);
    }


}
