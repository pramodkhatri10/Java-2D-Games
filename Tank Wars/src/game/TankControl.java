package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TankControl implements KeyListener {

    private Tank tank;
    private final int GoForward;
    private final int GoBackward;
    private final int GoRight;
    private final int GoLeft;
    private final int Fire;

    public TankControl(Tank tank, int Forward, int Backward, int Left, int Right, int Fire) {
        this.tank = tank;
        this.GoForward = Forward;
        this.GoBackward = Backward;
        this.GoRight = Right;
        this.GoLeft = Left;
        this.Fire = Fire;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == GoForward) {
            this.tank.setPressedUp();
        }
        if (keyPressed == GoBackward) {
            this.tank.setPressedDown();
        }
        if (keyPressed == GoLeft) {
            this.tank.setPressedLeft();
        }
        if (keyPressed == GoRight) {
            this.tank.setPressedRight();
        }
        if (keyPressed == Fire) {
            this.tank.setPressedFire();
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased == GoForward) {
            this.tank.removePressedUp();
        }
        if (keyReleased == GoBackward) {
            this.tank.removePressedDown();
        }
        if (keyReleased == GoLeft) {
            this.tank.removePressedLeft();
        }
        if (keyReleased == GoRight) {
            this.tank.removePressedRight();
        }
        if (keyReleased == Fire) {
            this.tank.removePressedFire();
        }


    }
}
