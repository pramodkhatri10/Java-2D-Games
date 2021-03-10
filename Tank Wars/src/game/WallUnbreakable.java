package game;

import java.awt.*;
//import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class WallUnbreakable extends Object {

    private boolean ValidBackground;
    private static BufferedImage Background;
    private static BufferedImage UnbreakableWall;


    public static void setUnbreakableWall(BufferedImage image) {
        UnbreakableWall = image;
    }


    public static void setBackground(BufferedImage image) {
        Background = image;
    }

    public WallUnbreakable() {

    }

    public WallUnbreakable(int x, int y, boolean isBackground) {
        this.X = x;
        this.Y = y;
        this.ValidBackground = isBackground;
        this.getShape = new Rectangle(x, y, 32, 32);

    }

    public void amend() {

    }

    public void draw(Graphics2D graph) {

        if (this.ValidBackground) { //Validates the background image

            graph.drawImage(Background, X, Y, null);

        } else {              //Initializes the wall that is unbreakable
            graph.drawImage(UnbreakableWall, X, Y, null);
        }

    }

    public void collision() { //Used only if the wall is breakable

    }

}
