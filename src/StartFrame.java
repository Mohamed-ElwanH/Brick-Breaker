import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    JLabel label = new JLabel();
    ImageIcon imageIcon = new ImageIcon("image.png");
    StartFrame()
    {
        this.setTitle("BrickBreaker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(420, 420);
        this.setVisible(true);

        this.getContentPane().setBackground(new Color(255, 0, 0));
        this.setIconImage(imageIcon.getImage());

        label.setText("start");

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setIcon(imageIcon);
        this.add(label);

    }
}
