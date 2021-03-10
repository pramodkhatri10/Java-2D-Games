package GameUnit;

import java.awt.Image;
import java.awt.Toolkit;

public class Movement extends GameAnimation {

    Image[] img;
    int index;

    public Movement(int x, int y, String type) {
        initialization(x, y, type);
    }

    public void Left(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.index = 0;
        this.img = new Image[7];
        img[0] = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_left.gif");
    }

    public void Right(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.index = 0;
        this.img = new Image[7];
        img[0] = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_right.gif");
    }

    public void jumpLeft(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.index = 0;
        this.img = new Image[7];
        img[0] = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_jump_left.gif");
    }

    public void jumpRight(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.index = 0;
        this.img = new Image[7];
        img[0] = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_jump_right.gif");
    }


    public void Squished(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.index = 0;
        this.img = new Image[11];
        img[0] = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_squished.gif");
    }

    public Image validateImage() {
        if (index >= img.length) {
            return null;

        }
        Image returnImage = img[index];
        index++;

        return returnImage;
    }

    public void initialization(int a, int b, String type) {
        if (type.equalsIgnoreCase("jumpleft")) {
            jumpLeft(a, b);

        } else if (type.equalsIgnoreCase("jumpright")) {
            jumpRight(a, b);

        } else if (type.equalsIgnoreCase("left")) {
            Left(a, b);

        } else if (type.equalsIgnoreCase("right")) {
            Right(a, b);

        } else if (type.equalsIgnoreCase("squished")) {
            Squished(a, b);
        } else {
            System.out.println("There is error in movement calling");
        }
    }

    public void index(LazarusPosition lazarus, String index) {
        if (index.equalsIgnoreCase("jumpleft")) {

            lazarus.posY = lazarus.posY - Constants.BLOCKSIZE;
            lazarus.posX = lazarus.posX - Constants.BLOCKSIZE;

        } else if (index.equalsIgnoreCase("jumpright")) {

            lazarus.posY = lazarus.posY - Constants.BLOCKSIZE;
            lazarus.posX = lazarus.posX + Constants.BLOCKSIZE;
        } else if (index.equalsIgnoreCase("left")) {

            lazarus.posX = lazarus.posX - Constants.BLOCKSIZE;
        } else if (index.equalsIgnoreCase("right")) {

            lazarus.posX += Constants.BLOCKSIZE;
        } else if (index.equalsIgnoreCase("squished")) {

        } else {
            System.out.println("There is error in movement calling");
        }
    }

}
