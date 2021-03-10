package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Bullet extends Object {
    private String BulletHolder;
    private boolean isOkay = false;
    private boolean SmallExplosion = true;
    boolean BulletCollision = false;
    static private BufferedImage TankBullet;
    static private BufferedImage Explosion;
    static private BufferedImage LargeExplosion;
    private int CollisionIterations = 0;

    String getOwner() {
        return this.BulletHolder;
    } //gets bullet owner

    void setOwner(String owner) {
        this.BulletHolder = owner;
    } //sets for bullet owner

    boolean getIsInactive() {
        return this.isOkay;
    } //gets status

    static void setBufferedImage(BufferedImage img) { // used to set the static bullet image
        TankBullet = img;
    } //sets static image for bullet

    static void setExplosion(BufferedImage exp) { // used to set the explosion image
        Explosion = exp;
    } //sets static image for  explosion

    void setSmallExplosion(boolean val){ //if val is true, we use the small explosion image, if val is false, we use the big explosion image
        this.SmallExplosion = val;
    } //sets static image for small explosion

    static void setLargeExplosion(BufferedImage e){
        LargeExplosion = e;
    } //sets static image for large explosion

    Bullet(int x, int y, int angle) {
        this.X = x;
        this.Y = y;
        this.VX = (int) Math.round(3 * Math.cos(Math.toRadians(angle)));
        this.VY = (int) Math.round(3 * Math.sin(Math.toRadians(angle)));
        this.Angle = angle;
        this.getShape = new Rectangle(x, y, TankBullet.getWidth(), TankBullet.getHeight());
    }

    /****
     * dictates bullet keeps going until collides with other game objects
     * lets bullet disappear after collision
     */
    public void amend() {
        if (!BulletCollision) {
            this.X = X + VX;
            this.Y = Y + VY;
            this.checkBorder();
        } else {
            CollisionIterations++;
        }
        this.getShape.setLocation(X, Y);
    }

    /****
     * dictates bullet collision and time
     * determines if there is sufficient time to view bullet explosion
     * @param graph
     */
    public void draw(Graphics2D graph) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(X, Y);
        rotation.rotate(Math.toRadians(Angle), TankBullet.getWidth() / 2.0, TankBullet.getHeight() / 2.0);

        if (BulletCollision && SmallExplosion) {

            graph.drawImage(Explosion, rotation, null);

            if (CollisionIterations >= 5) {
                this.isOkay = true;
            }

        }else if(BulletCollision && !SmallExplosion){
            graph.drawImage(LargeExplosion, rotation, null);

            if (CollisionIterations >= 5) {
                this.isOkay = true;
            }
        } else {
            graph.drawImage(TankBullet, rotation, null);
        }
    }

    /****
     * states the bullet collision status
     */
    public void collision() {
        BulletCollision = true;
    }

    /****
     * Determines whether bullet is in the map
     * Bullet is removed if not in active
     */
    private void checkBorder() {
        int left_limit = 30;
        if (X < left_limit) {
            this.isOkay = true;
        }
        int right_limit = GameWorld.WidthOfWalls - 65;
        if (X >= right_limit) {
            this.isOkay = true;
        }
        int lower_limit = 40;
        if (Y < lower_limit) {
            this.isOkay = true;
        }
        int upper_limit = GameWorld.HeightOfWalls - 60;
        if (Y >= upper_limit) {
            this.isOkay = true;
        }
    }
}
