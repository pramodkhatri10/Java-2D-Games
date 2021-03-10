package GameWorld;

import GameUnit.Constants;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    public static void main(String[] args) throws Exception {
       GameWorld lazarus = new GameWorld();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Get Lazarus Out of the Pit");
        frame.setFont(new Font("Bodoni", Font.BOLD, 25));
        frame.setForeground(Color.black);
        frame.setIconImage(new ImageIcon("resources/lazarus.png").getImage());
        frame.setBounds(50, 50, Constants.GAMEBOARDSIZE , Constants.GAMEBOARDSIZE );
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(lazarus, BorderLayout.CENTER);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        lazarus.start();
    }

}
