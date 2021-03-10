package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends Object {
    boolean BoostGain = false;
    boolean HealthGain = false;
    boolean isActive = true;
    static private BufferedImage Img_Health;
    static private BufferedImage Img_Boost;

    public PowerUp(int X, int Y, boolean isHealth, boolean isBoost){
        this.X = X;
        this.Y = Y;
        this.getShape = new Rectangle(X, Y, 40, 40);
        this.HealthGain = isHealth;
        this.BoostGain = isBoost;
    }

    public static void setBoostImg(BufferedImage BoostImg) {
        PowerUp.Img_Boost = BoostImg;
    }

    static void setHealthImg(BufferedImage HealthImg){
        PowerUp.Img_Health = HealthImg;
    }

    public void amend() {

    }

    public void draw(Graphics2D graph) {
        if(this.HealthGain){
            graph.drawImage(Img_Health, X, Y, 40,40 , null);
        }
        if(this.BoostGain){
            graph.drawImage(Img_Boost, X, Y, 40,40 , null);
        }
    }

    public void collision() { //Called when a tank collides with a PowerUp
        this.isActive = false;
    }
}
