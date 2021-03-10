package game;

import java.awt.*;

public abstract class Object {
    int Y;
    int VY;
    int X;
    int VX;
    int Height;
    int Width;
    int Angle;


    Rectangle getShape;

    void setY(int setY) {
        this.Y = setY;
    }

    int getY() {
        return this.Y;
    }

    void setX(int setX) {
        this.X = setX;
    }

    int getX() {
        return this.X;
    }

    void setAngle(int angle) {
        this.Angle = angle;
    }

    public abstract void draw(Graphics2D graph);

    public abstract void amend();

    public abstract void collision();

}

