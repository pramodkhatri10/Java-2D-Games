package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WallBreakable extends WallUnbreakable {
    private int HealthBar = 100;
    private static BufferedImage breakableWall;
    private boolean TankDestroyed = false;

    WallBreakable(int x, int y) {
        this.X = x;
        this.Y = y;
        this.getShape = new Rectangle(x, y, breakableWall.getWidth(), breakableWall.getHeight());
    }

    private void excludeHealthBar(int amt) { //private because only the specific wall being taken down by the tank is destroyed
        if (HealthBar - amt < 0) {
            HealthBar = 0;
            TankDestroyed = true;
        } else {
            HealthBar -= amt;
        }
    }

    static void setBreakableWall(BufferedImage img) {
        WallBreakable.breakableWall = img;
    }

    int getHealthBar() {
        return this.HealthBar;
    }

    public void amend() {

    }

    public void draw(Graphics2D graph) {

        if (!TankDestroyed) {
            graph.drawImage(breakableWall, X, Y, null);
        }

    }

    public void collision() {
        this.excludeHealthBar(50);

    }
}
