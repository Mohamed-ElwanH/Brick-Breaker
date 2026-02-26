import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class BBUI extends JFrame {




    BBUI() {
        this.setTitle("BrickBreaker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 800);
        URL ballURL = getClass().getResource("/UIElements/image.png");
        System.out.println(ballURL);
        if(ballURL != null) {
            ImageIcon ballIcon = new ImageIcon(ballURL);
            this.setIconImage(ballIcon.getImage());
        }
        else
            throw new RuntimeException("Image not loaded");

        this.setVisible(true);
    }


}
