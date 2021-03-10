package GameUnit;

import java.awt.*;

public abstract class GameAnimation {

    public float posX;
    public float posY;

    public GameAnimation() {

    }

    public abstract Image validateImage(); //checks for image or null

    public float getPosX() {

        return posX;
    }

    public float getPosY() {

        return posY;
    }

    public abstract void index(LazarusPosition lazarus, String index); //updates position
}
