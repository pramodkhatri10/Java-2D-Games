package game;

import java.awt.*;

public class Options {

    public void draw(Graphics graph) {
        Font font = new Font("Bodoni", Font.BOLD, 30);
        graph.setFont(font);
        graph.setColor(Color.ORANGE);
        graph.drawString("PLAY GAME", 380, 465);
        graph.drawString("INSTRUCTIONS", 375, 545);
        graph.drawString("QUIT GAME", 397, 625);
        graph.setColor(Color.LIGHT_GRAY);
        graph.drawRect(360, 416, 280, 70);
        graph.drawRect(360, 496, 280, 70);
        graph.drawRect(360, 576, 280, 70);
    }
}
