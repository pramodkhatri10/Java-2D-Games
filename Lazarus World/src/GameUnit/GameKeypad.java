package GameUnit;

import GameWorld.GameWorld;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeypad extends KeyAdapter {

    private GameWorld gameWorld;
    private Sound sound;
    private LazarusPosition player;



    public GameKeypad(LazarusPosition lazarus, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.player = lazarus;
    }

    public void keyPressed(KeyEvent event) {

        int keyInput = event.getKeyCode();
        sound = new Sound(this.gameWorld, "resources/Move.wav");

        //checks and validates game state
        if (gameWorld.getGamePlay() == Constants.GameState.READY) {
            if (keyInput == KeyEvent.VK_ENTER) { //press ENTER key to play the game
                gameWorld.setGamePlay(Constants.GameState.RUNNING);
                gameWorld.freeBox();
            }
            return;
        }

        //checks if game is won
        if (gameWorld.getGamePlay() == Constants.GameState.GAMEWON) {
            if (keyInput == KeyEvent.VK_SPACE) { //press SPACE key to exit the game
                System.exit(0);
            }
            return;
        }

        //checks if game is over
        if (gameWorld.getGamePlay() == Constants.GameState.GAMEOVER) {
            if (keyInput == KeyEvent.VK_ESCAPE) { //press ESCAPE key to exit the game
                System.exit(0);
            }
            return;
        }

        //checks lazarus movement direction
        if (keyInput == KeyEvent.VK_RIGHT) { //right direction movement
            sound.soundPlay();
            GameWorld.X = player.posX;
            GameWorld.pointRight = GameWorld.X + GameWorld.width;
            GameWorld.goingRight = true;
            GameWorld.goRight = true;
        }
        if (keyInput == KeyEvent.VK_LEFT) { //left direction movement
            sound.soundPlay();
            GameWorld.X = player.posX;
            GameWorld.pointLeft = GameWorld.X - GameWorld.width;
            GameWorld.goingLeft = true;
            GameWorld.goLeft = true;
        }

    }

    //when gameKepad is free
    public void keyReleased(KeyEvent event) {
        int keyInput = event.getKeyCode();
        if (keyInput == KeyEvent.VK_RIGHT) {
            GameWorld.goRight = false;
        }
        if (keyInput == KeyEvent.VK_LEFT) {
            GameWorld.goLeft = false;
        }

    }
}