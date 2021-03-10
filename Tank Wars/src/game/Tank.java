package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Tank extends Object {

    private int VXcontrol;
    private int VYcontrol;
    private int angle;

    private final int R = 2;
    private final int rotationalspeed = 3;
    private int health = 100;


    private BufferedImage img;
    private boolean PressUp;
    private boolean PressDown;
    private boolean PressRight;
    private boolean PressLeft;
    private boolean PressFire;
    private long LastFired = 0;
    private long addBoost = 0;
    private boolean isBoosted;
    private String tag;


    private boolean NoMovement = false;
    private GameWorld gameworld;

    void setGameWorld(GameWorld setGameWorld) { //this is needed for spawning bullets
        this.gameworld = setGameWorld;
    }

    void setHealth(int setHealth) {
        this.health = setHealth;
    }


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.X = x;
        this.Y = y;
        this.VXcontrol = vx;
        this.VYcontrol = vy;
        this.img = img;
        this.angle = angle;
        this.Height = 50;
        this.Width = 50;
        this.getShape = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    void setTag(String initTag) {
        this.tag = initTag;
    }

    String getTag() {
        return tag;
    }

    void setPressedUp() {
        this.PressUp = true;
    }

    void setPressedDown() {
        this.PressDown = true;
    }

    void setPressedRight() {
        this.PressRight = true;
    }

    void setPressedLeft() {
        this.PressLeft = true;
    }

    void removePressedUp() {
        this.PressUp = false;
    }

    void removePressedDown() {
        this.PressDown = false;
    }

    void removePressedRight() {
        this.PressRight = false;
    }

    void removePressedLeft() {
        this.PressLeft = false;
    }

    void setPressedFire() {
        this.PressFire = true;
    }

    void removePressedFire() {
        this.PressFire = false;
    }


    public void amend() {
        this.getShape.setLocation(X, Y);

        if (this.PressUp) {
            this.moveForwards();
        }
        if (this.PressDown) {
            this.moveBackwards();
        }
        if (this.PressRight) {
            this.rotateRight();
        }

        if (this.PressLeft) {
            this.rotateLeft();
        }


        if (this.PressFire && (System.currentTimeMillis() - LastFired > 500)) {   //Creates half of a second delay when tank fires each bullet

            this.SpawnBullet(X, Y, VXcontrol, VYcontrol, angle, gameworld); //Spawns the bullet
            LastFired = System.currentTimeMillis();   //Determines when the bullet has been fired

        }
        this.NoMovement = false;

    }

    private void rotateRight() {
        this.angle += this.rotationalspeed;
    }

    private void rotateLeft() {
        this.angle -= this.rotationalspeed;
    }

    private void moveBackwards() {
        VXcontrol = (int) Math.round(R * Math.cos(Math.toRadians(angle))) * -1;
        VYcontrol = (int) Math.round(R * Math.sin(Math.toRadians(angle))) * -1;
        if (System.currentTimeMillis() - addBoost < 5000 && this.isBoosted) { //Boost lasts for 5 seconds
            VXcontrol = (int) Math.round(4 * R * Math.cos(Math.toRadians(angle))) * -1;
            VYcontrol = (int) Math.round(4 * R * Math.sin(Math.toRadians(angle))) * -1;

        } else if (this.isSpeedBoosted() && (System.currentTimeMillis() - addBoost < 5000)) {
            addBoost = 0;
            this.isBoosted = false;
        }
        if (!NoMovement) {
            X += VXcontrol;
            Y += VYcontrol;
        }

        checkBorder();
    }

    private void moveForwards() {
        VXcontrol = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        VYcontrol = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        if (System.currentTimeMillis() - addBoost < 5000 && isBoosted) {
            VXcontrol = (int) Math.round(4 * R * Math.cos(Math.toRadians(angle)));
            VYcontrol = (int) Math.round(4 * R * Math.sin(Math.toRadians(angle)));
        } else if (this.isSpeedBoosted() && (System.currentTimeMillis() - addBoost < 5000)) {
            addBoost = 0;
            this.isBoosted = false;
        }
        if (!NoMovement) {
            X += VXcontrol;
            Y += VYcontrol;
        }

        checkBorder();
    }

    @Override
    public void setAngle(int angle) {
        this.angle = angle;
    }

    private void checkBorder() {
        if (X < 30) {
            X = 30;
        }
        if (X >= GameWorld.WidthOfWalls - 88) {
            X = GameWorld.WidthOfWalls - 88;
        }
        if (Y < 40) {
            Y = 40;
        }
        if (Y >= GameWorld.HeightOfWalls - 80) {
            Y = GameWorld.HeightOfWalls - 80;
        }
    }


    private void SpawnBullet(int x, int y, int vx, int vy, int angle, GameWorld gameworld) { // private for the just one tank firing the bullet
        Bullet bullet = new Bullet(x, y, angle);
        bullet.setOwner(tag);
        gameworld.setGameObj(bullet);

    }

    public void collision() { //Initializes the decrease in health of tank
        this.removeHealth(10);

    }


    private void removeHealth(int val) { //private for the tank struck by the bullet decreases in health, and not both tanks
        if (health - val < 0) {
            health = 0;
        } else {
            health -= val;
        }
    }

    int getHealth() {
        return this.health;
    }


    public void draw(Graphics2D graph) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(X, Y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (this.health != 0) { //regenerates health of the tank after a tank loses all health
            graph.drawImage(this.img, rotation, null);
        }


    }

    Rectangle getOffsetBounds() {
        return new Rectangle(X + VXcontrol, Y + VYcontrol, 50, 50);
    }

    void setAddBoost(long addBoost) {
        this.addBoost = addBoost;
    }

    private boolean isSpeedBoosted() { //private because only the tank should be able to check if its speed is boosted
        return isBoosted;
    }

    void setSpeedBoost(boolean isBoosted) {
        this.isBoosted = isBoosted;
    }
    void setNoMovement(boolean NoMovement){
        this.NoMovement = NoMovement;
    }
}
