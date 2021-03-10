package GameWorld;

import GameUnit.*;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Iterator;


public class GameWorld extends JComponent implements Runnable {

    private Thread thread;

    private LazarusPosition lazarus;

    private GameKeypad gameKeypad;

    public static boolean goLeft, goRight, goingLeft, goingRight;

    private List<FallingBox> boxes;

    private DropBoxes dropBoxes;

    public static int X, Y;

    public static int width = 50, pointLeft, pointRight;

    private CollisionDetector collision;

    private String[][] map;
    private String type;
    private Sound sound;
    private GameAnimation gameAnimation;
    private Constants.GameState game;

    private int gameLevel;

    public GameWorld() throws Exception {
        this.gameLevel = Constants.LEVEL_ONE;
        this.map = GameMap.gameMap(gameLevel);
        this.game = Constants.GameState.READY;
        this.boxes = new ArrayList<FallingBox>();
        this.collision = new CollisionDetector(map);
        setFocusable(true);

        // index
        startIndex();
        this.lazarus = new LazarusPosition(X, Y, Constants.HEALTH_LIVES, this);

        // validate and determine user input in keypad
        this.gameKeypad = new GameKeypad(this.lazarus, this);
        addKeyListener(gameKeypad);

        // keeps generating boxes
        this.dropBoxes = new DropBoxes(boxes, lazarus);

        // game sound
        sound = new Sound(this, "resources/GameSound.wav");
        sound.soundPlay();
        sound.iterate();
    }

    //initializes and resets lazarus to the specific level
    public void initLevel(int nextLevel) throws Exception {
        this.gameLevel = nextLevel;
        this.map = GameMap.gameMap(gameLevel);
        this.game = Constants.GameState.READY;
        this.collision = new CollisionDetector(map);
        freeBox();
        startIndex();
        this.lazarus.adjustIndex(X, Y);
    }

    //goes to next level
    public void nextLevel() throws Exception {
        int nextLevel = gameLevel + 1;
        if (nextLevel > Constants.LEVEL_TWO) {
            setGamePlay(Constants.GameState.GAMEWON);
            return;
        }
        initLevel(nextLevel);
    }

    //free boxes
    public void freeBox() {
        synchronized (boxes) {
            this.boxes.clear();
        }
    }

    //set the state of the game
    public void setGamePlay(Constants.GameState state) {
        this.game = state;
    }

    //get the state of the game
    public Constants.GameState getGamePlay() {
        return game;
    }


    //validates 2D paint display for game Ready, GameWon or GameOver
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        if (game == Constants.GameState.READY) { //ready state
            makeGameReady(graphics2D);
            return;
        }

        if (game == Constants.GameState.GAMEOVER) { //game over state
            makeGameOver(graphics2D);
            sound.abrupt();
            return;
        }

        if (game == Constants.GameState.GAMEWON) { //game won
            makeGameWon(graphics2D);
            sound.abrupt();
            return;
        }
        makeBackground(graphics2D);
        makeGameMap(graphics2D);
        paintLazarus(graphics2D, lazarus.posX, lazarus.posY);
        makeGameLevel(graphics2D);
        makeLazarusLives(graphics2D);

        synchronized (boxes) {
            makeBoxes(graphics2D);
        }
        makeNextBox(graphics2D);
    }

    //validates game play and collision
    private void play() throws Exception {

        if (game != Constants.GameState.RUNNING) {
            return;
        }

        synchronized (boxes) {
            moveBoxes();
        }
        controlDirection(); //direct movement of lazarus

        if (collision.boxCollision(lazarus.posX, lazarus.posY)) {
            livesDown();
            return;
        }
        if (collision.checkStopBlockCollision(lazarus.posX, lazarus.posY)) {
            if (gameLevel == Constants.LEVEL_TWO) {
                setGamePlay(Constants.GameState.GAMEWON);
            } else {
                setGamePlay(Constants.GameState.READY);
                nextLevel();
            }
        }
    }

    //makes and displays health lives image from resources
    private void makeLazarusLives(Graphics2D graphics2D) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_stand.png");
        for (int i = 0; i <= lazarus.lazarusLives; i++) {
            graphics2D.drawImage(img, 25 + (i * 30), 25, 40, 40, this);
            graphics2D.finalize();
        }
    }

    //makes and displays game is ready to play image from resources
    private void makeGameReady(Graphics2D graphics2D) {
        String fileName = ((gameLevel == Constants.LEVEL_ONE) ? "GameStart.png" : "NextLevel.png");
        Image img = Toolkit.getDefaultToolkit().getImage("resources/" + fileName);
        graphics2D.drawImage(img, 0, 0, Constants.GAMEBOARDSIZE, Constants.GAMEBOARDSIZE, this);
        graphics2D.finalize();
    }

    //gets and displays game over image from resources
    private void makeGameOver(Graphics2D graphics2D) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/gameOver.png");
        graphics2D.drawImage(img, 0, 0, Constants.GAMEBOARDSIZE, Constants.GAMEBOARDSIZE, this);
        graphics2D.finalize();
    }

    //gets and displays game won image from resources
    private void makeGameWon(Graphics2D graphics2D) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/GameWon.png");
        graphics2D.drawImage(img, 0, 0, Constants.GAMEBOARDSIZE, Constants.GAMEBOARDSIZE, this);
        graphics2D.finalize();
    }


    //gets and displays upcoming falling box on corner screen
    private void makeNextBox(Graphics2D graphics2D) {

        makeBox(graphics2D, dropBoxes.getNextFallingBox(), 0, 700);
    }

    //validates and handles the movement in game running condition
    //checks for boxes underneath, two boxes pilled next, box collision to lazarus
    //creates left and right movement animation
    public void controlDirection() {
        int newX;
        while (!collision.checkCollision(lazarus.posX, lazarus.posY + Constants.BLOCKSIZE)) {
            lazarus.posY++;
        }

        if (goLeft) {

            if (goingLeft) {

                newX = lazarus.posX - Constants.BLOCKSIZE;

                if (collision.checkCollision(newX, lazarus.posY - Constants.BLOCKSIZE)) {
                    goingLeft = false;

                    return;
                }

                if (collision.checkCollision(newX, lazarus.posY)) {
                    gameAnimation = new Movement(lazarus.posX, lazarus.posY, "jumpleft");
                    type = "jumpleft";

                } else {
                    gameAnimation = new Movement(lazarus.posX, lazarus.posY, "left");
                    type = "left";

                }

                if (lazarus.posX == pointLeft) {
                    goingLeft = false;

                    return;
                }
            }
        }

        if (goRight) {

            if (goingRight) {
                newX = lazarus.posX + Constants.BLOCKSIZE;
                if (collision.checkCollision(newX, lazarus.posY - Constants.BLOCKSIZE)) {
                    goingRight = false;

                    return;
                }
                if (collision.checkCollision(newX, lazarus.posY)) {
                    gameAnimation = new Movement(lazarus.posX, lazarus.posY, "jumpright");
                    type = "jumpright";

                } else {
                    gameAnimation = new Movement(lazarus.posX, lazarus.posY, "right");
                    type = "right";

                }

                if (lazarus.posX == pointRight) {
                    goingRight = false;

                    return;
                }
            }
        }
    }

    //validates, animates and updates lazarus lives when it dies
    //frees falling boxes when lazarus dies and updates its position in game board
    private void livesDown() {
        gameAnimation = new Movement(lazarus.posX, lazarus.posY, "squished");
        type = "squished";
        freeBox();
        lazarus.adjustLazarusPosition();
        if (--lazarus.lazarusLives < 0) {
            setGamePlay(Constants.GameState.GAMEOVER); //lives finish game over
        }
    }

    //makes and displays types of falling boxes
    private void makeBoxes(Graphics2D graphics2D) {
        for (FallingBox box : boxes) {
            makeBox(graphics2D, box.getBoxType(), box.getPosX(), box.getPosY());
        }
    }

    //makes and displays specific box
    private void makeBox(Graphics2D graphics2D, String boxType, int x, int y) {
        Image img = null;
        if (boxType.equals(GameMap.CARDBOARDBOX)) {
            img = Toolkit.getDefaultToolkit().getImage("resources/CardBox.gif");
        } else if (boxType.equals(GameMap.WOODBOX)) {
            img = Toolkit.getDefaultToolkit().getImage("resources/WoodBox.gif");
        } else if (boxType.equals(GameMap.STONEBOX)) {
            img = Toolkit.getDefaultToolkit().getImage("resources/StoneBox.gif");
        } else if (boxType.equals(GameMap.METALBOX)) {
            img = Toolkit.getDefaultToolkit().getImage("resources/MetalBox.gif");
        } else {
            System.err.println("ERROR: unknown type - " + boxType);
        }
        graphics2D.drawImage(img, x, y, Constants.BLOCKSIZE, Constants.BLOCKSIZE, this);
        graphics2D.finalize();
    }

    //validates collision and moves boxes on box type priority
    public void moveBoxes() {
        Iterator<FallingBox> boxIterator = boxes.iterator();
        FallingBox box;
        while (boxIterator.hasNext()) {
            box = boxIterator.next();

            if (collision.checkBoxBoxCollision(box)) { //boxes priority
                abruptBoxCollision(box, boxIterator);

            } else if (collision.checkBoxWallCollision(box)) { //boxes collision with wall
                map[box.getPosY() / Constants.BLOCKSIZE][box.getPosX() / Constants.BLOCKSIZE] = box.getBoxType();
                boxIterator.remove();

            } else {
                box.goDownward();
            }
        }
    }


    //stops box colliding with the lower stationary blocks on game board
    //stops bottom box from being broken
    private void abruptBoxCollision(FallingBox liveBox, Iterator<FallingBox> boxIterator) {
        int newX = liveBox.getPosX();
        int newY = liveBox.getBoxIndexDownward();
        String bottomBoxType = collision.getMap(newX, newY);
        int bottomBoxPriority = FallingBox.getBoxType(bottomBoxType);
        int currentBoxPriority = liveBox.getBoxType(liveBox.getBoxType());

        if (bottomBoxPriority >= currentBoxPriority) {
            map[liveBox.getPosY() / Constants.BLOCKSIZE][liveBox.getPosX() / Constants.BLOCKSIZE] = liveBox.getBoxType();
            boxIterator.remove();
        } else {
            map[newY / Constants.BLOCKSIZE][newX / Constants.BLOCKSIZE] = GameMap.SPACE;
        }
    }

    //gets and displays background image in 2D in game screen
    public void makeBackground(Graphics2D graphics2D) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/Background.png");
        graphics2D.drawImage(img, 0, 0, Constants.GAMEBOARDSIZE, Constants.GAMEBOARDSIZE, this);
        graphics2D.finalize();
    }

    //gets and displays game level image in 2D in game screen
    public void makeGameLevel(Graphics2D graphics2D) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/level" + gameLevel + ".png");
        graphics2D.drawImage(img, 250, 65, 300, 120, this);
        graphics2D.finalize();
    }

    //determines the starting index based on horizontal adn vertical position
    public void startIndex() {
        for (int horizontalPos = 0; horizontalPos < Constants.NUMBER_MAX_BLOCKS; horizontalPos++) {
            for (int verticalPos = 0; verticalPos < Constants.NUMBER_MAX_BLOCKS; verticalPos++) {
                String val = map[horizontalPos][verticalPos];
                int y = horizontalPos * Constants.BLOCKSIZE;
                int x = verticalPos * Constants.BLOCKSIZE;
                if (val.equals(GameMap.LAZARUS)) {
                    X = x;
                    Y = y;
                    continue;
                }
            }
        }
    }

    private void makeGameMap(Graphics2D graphics2D) {
        for (int horizontalPos = 0; horizontalPos < Constants.NUMBER_MAX_BLOCKS; horizontalPos++) {
            for (int verticalPos = 0; verticalPos < Constants.NUMBER_MAX_BLOCKS; verticalPos++) {
                String val = map[horizontalPos][verticalPos];
                int y = horizontalPos * Constants.BLOCKSIZE;
                int x = verticalPos * Constants.BLOCKSIZE;
                if (val.equals(GameMap.WALL)) {
                    makeWall(graphics2D, x, y);
                    continue;
                }
                if (val.equals(GameMap.STOPBLOCK)) {
                    makeButton(graphics2D, x, y);
                    continue;
                }
                if (val.equals(GameMap.SPACE)) {
                    continue;
                }
                if (val.equals(GameMap.LAZARUS)) {
                    X = x;
                    Y = y;
                    continue;
                }
                if (GameMap.ALL_BOX_SET.contains(val)) {
                    makeBox(graphics2D, val, x, y);
                }
            }
        }
    }

    //makes animated wall in game board
    private void makeWall(Graphics2D graphics2D, int x, int y) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/Wall.gif");
        graphics2D.drawImage(img, x, y, Constants.BLOCKSIZE, Constants.BLOCKSIZE, this);
        graphics2D.finalize();
    }

    //makes animated button
    private void makeButton(Graphics2D graphics2D, int x, int y) {
        Image img = Toolkit.getDefaultToolkit().getImage("resources/Button.gif");
        graphics2D.drawImage(img, x, y, Constants.BLOCKSIZE, Constants.BLOCKSIZE, this);
        graphics2D.finalize();
    }

    //validates animation and draws lazarus
    private void paintLazarus(Graphics2D graphics2D, int x, int y) {

        if (this.gameAnimation != null) {
            Image img = gameAnimation.validateImage();
            if (img == null) {
                gameAnimation.index(this.lazarus, type);
                gameAnimation = null;
            } else {
                if (collision.boxCollision(lazarus.posX, lazarus.posY)) {
                    goLeft = false;
                    goRight = false;

                }
                graphics2D.drawImage(img, x, y, Constants.BLOCKSIZE, Constants.BLOCKSIZE, this);
                graphics2D.finalize();

            }

        } else {
            Image img = Toolkit.getDefaultToolkit().getImage("resources/Lazarus_stand.png");
            graphics2D.drawImage(img, x, y, Constants.BLOCKSIZE, Constants.BLOCKSIZE, this);
            graphics2D.finalize();

        }
    }

    //run the game
    public void run() {
        Thread thread = Thread.currentThread();
        while (this.thread == thread) {
            repaint();
            try {
                play();
                this.thread.sleep(15);
            } catch (Exception exception) {
                exception.printStackTrace();
                break;
            }
        }
    }

    //spawn boxes with timer
    public void start() throws Exception {
        Timer timer = new Timer();
        timer.schedule(dropBoxes, 0, 2000);
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        thread.join();
    }

}
