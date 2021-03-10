package GameUnit;

import GameWorld.GameWorld;

public class LazarusPosition {

    public int posX, posY;
    public GameWorld lazarus;
    public int lazarusLives;


    public LazarusPosition(int posX, int posY, int lazarusLives, GameWorld lazarus){
        this.posX = posX;
        this.posY = posY;
        this.lazarusLives = lazarusLives;
        this.lazarus = lazarus;
    }

    //resets lazarus above the falling box if it dies
    public void adjustLazarusPosition() {

        this.posY -= Constants.BLOCKSIZE;
    }

    //resets index in the game board
    public void adjustIndex(int startX, int startY) {
        this.posX = startX;
        this.posY = startY;
    }
}