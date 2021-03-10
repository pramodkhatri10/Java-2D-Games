package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;

public class GameWorld extends JPanel {

    private static final int HeightOfScreen = 672;
    private static final int WidthOfScreen = 960;
    static final int HeightOfWalls = 1920; //60 walls high makes Game World height
    static final int WidthOfWalls = 1536;  //48 walls wide makes Game World width
    private final int X_SpawnTank1 = 200;
    private final int Y_SpawnTank1 = 350;
    private final int Angle_SpawnTank1 = 0;
    private final int X_SpawnTank2 = 1288;
    private final int Y_SpawnTank2 = 1530;
    private final int Angle_SpawnTank2 = 180;
    private BufferedImage Walls;
    private Graphics2D Buffer;
    private JFrame jFrame;
    private Tank TankA;
    private Tank TankB;
    private static BufferedImage Tank1Victory;
    private static BufferedImage Tank2Victory;
    private static BufferedImage Menu;
    private static BufferedImage HowToPlay;
    private boolean Tank1Wins = false;
    private boolean Tank2Wins = false;
    private Sound playMusic;
    private CollisionDetector handle;
    static private Options m;


    void setGameObj(Object obj) { //package private
        this.GameObj.add(obj);
    }

    private ArrayList<Object> GameObj = new ArrayList<>();
    private int Tank1Lives = 3;
    private int Tank2Lives = 3;

    enum GameTask {
        GameMenu, Game, HowToPlay, ExitApp,
    }

    static GameTask task = GameTask.GameMenu;

    public static void main(String[] args) {
        GameWorld Obj = new GameWorld();
        Obj.handle = new CollisionDetector();
        Obj.init();
        try {

            while (true) {

                Obj.repaint();

                if (GameWorld.task == GameTask.Game) {

                    for (int i = 0; i < Obj.GameObj.size(); i++) {
                        if (Obj.GameObj.get(i) instanceof Bullet) {
                            if (((Bullet) Obj.GameObj.get(i)).getIsInactive()) { //condition for bullet being inactive
                                Obj.GameObj.remove(i);
                                i--;
                            } else {
                                Obj.GameObj.get(i).amend();
                            }
                        }
                        if (Obj.GameObj.get(i) instanceof Tank) {
                            if (((Tank) Obj.GameObj.get(i)).getHealth() == 0) { //Tank has been destroyed
                                if ((((Tank) Obj.GameObj.get(i)).getTag()).equals("Tank1")) { //Player 1's tank (game.Tank 1) is destroyed
                                    if (Obj.Tank1Lives > 1) {
                                        Obj.Tank1Lives--;

                                        //Player 1 respawn
                                        ((Tank) Obj.GameObj.get(i)).setHealth(100);  //Refilling Tank 1's health back to full health
                                        Obj.GameObj.get(i).setX(Obj.X_SpawnTank1);
                                        Obj.GameObj.get(i).setY(Obj.Y_SpawnTank1);
                                        Obj.GameObj.get(i).setAngle(Obj.Angle_SpawnTank1);


                                    } else { //Player 2 wins the game
                                        Obj.Tank1Lives = 0;
                                        Obj.Tank2Wins = true;
                                        break;
                                    }
                                }
                                if ((((Tank) Obj.GameObj.get(i)).getTag()).equals("Tank2")) { //Player 2's tank (game.Tank 2) is destroyed
                                    if (Obj.Tank2Lives > 1) { //The game carries on (Player 2 has this many lives lives remaining)
                                        Obj.Tank2Lives--;

                                        //Player 2 respawn
                                        ((Tank) Obj.GameObj.get(i)).setHealth(100); //Refilling game.Tank 2's health back to full health
                                        Obj.GameObj.get(i).setX(Obj.X_SpawnTank2);
                                        Obj.GameObj.get(i).setY(Obj.Y_SpawnTank2);
                                        Obj.GameObj.get(i).setAngle(Obj.Angle_SpawnTank2);


                                    } else { //Player 1 wins the game
                                        Obj.Tank2Lives = 0;
                                        Obj.Tank1Wins = true;
                                        break;
                                    }
                                }

                            }
                        }
                        if (((Obj.GameObj.get(i) instanceof WallBreakable) && ((WallBreakable) Obj.GameObj.get(i)).getHealthBar() == 0)) {
                            Obj.GameObj.remove(i);
                        }
                    }

                    Obj.GameObj = Obj.handle.collide(Obj.GameObj);  //Handles the object collisions


                    Obj.TankA.amend();
                    Obj.TankB.amend();

                    Thread.sleep(1000 / 144);
                } else if (task == GameTask.ExitApp) { //Exits the Tank Game
                    Obj.jFrame.dispose();
                    System.exit(0);
                }
            }
        } catch (InterruptedException ignored) {

        }

    }

    private void init() {
        this.jFrame = new JFrame("TANK GAME");
        this.Walls = new BufferedImage(GameWorld.WidthOfWalls, GameWorld.HeightOfWalls, BufferedImage.TYPE_INT_RGB);
        BufferedImage setTankImg = null, TankBullet, Background, unbreakableWall, breakableWall, exp_img, LargeExplosion;

        //images for the game
        try {
            BufferedImage tmp;

            playMusic = new Sound(1, "/GameSound.wav");
            
            Background = ImageIO.read(getClass().getResource("/GameBackground.bmp"));
            WallUnbreakable.setBackground(Background);

            GameWorld.Menu = ImageIO.read(getClass().getResource("/GameTitle.png"));
            GameWorld.HowToPlay = ImageIO.read(getClass().getResource("/GameInstructions.png"));

            breakableWall = ImageIO.read(getClass().getResource("/BreakableWall.png"));
            WallBreakable.setBreakableWall(breakableWall);

            unbreakableWall = ImageIO.read(getClass().getResource("/UnbreakableWall.png"));
            WallUnbreakable.setUnbreakableWall(unbreakableWall);

            setTankImg = ImageIO.read(getClass().getResource("/Tank.png"));

            TankBullet = ImageIO.read(getClass().getResource("/Bullet.gif"));
            Bullet.setBufferedImage(TankBullet); //setting the bullet image

            exp_img = ImageIO.read(getClass().getResource("/SmallExplosion.gif"));
            Bullet.setExplosion(exp_img);

            LargeExplosion = ImageIO.read(getClass().getResource("/BigExplosion.gif"));
            Bullet.setLargeExplosion(LargeExplosion);

            PowerUp.setHealthImg(ImageIO.read(getClass().getResource("/Health.png")));
            PowerUp.setBoostImg(ImageIO.read(getClass().getResource("/PowerUp.png")));

            GameWorld.Tank1Victory = ImageIO.read(getClass().getResource("/Tank1Winner.png"));
            GameWorld.Tank2Victory = ImageIO.read(getClass().getResource("/Tank2Winner.png"));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //Creates the tanks
        TankA = new Tank(X_SpawnTank1, Y_SpawnTank1, 0, 0, Angle_SpawnTank1, setTankImg);
        TankA.setTag("Tank1");
        TankB = new Tank(X_SpawnTank2, Y_SpawnTank2, 0, 0, Angle_SpawnTank2, setTankImg);
        TankB.setTag("Tank2");

        m = new Options(); //Sets up the menu

        //background for the game field
        for (int i = 0; i < WidthOfWalls; i = i + 320) {
            for (int j = 0; j < HeightOfWalls; j = j + 240) {
                GameObj.add(new WallUnbreakable(i, j, true)); //Validates the background image
            }
        }
        //Game map structure
        int[] MapStructure = {
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 5, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 5, 5, 5, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 5, 5, 5, 5, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 5, 5, 5, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 5, 5, 5, 5, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};


        int Column = 0;
        int Index = 0;

        for (int i = 0; i < 60; i++) { //loops up and down(entire horizontal row)

            for (int j = 0; j < 48; j++) {
                if (Column == 60) {
                    Column = 0;
                }
                int wall = MapStructure[Index];
                if (wall != 0) { //game.Tank can move around if wall == 0
                    if (wall == 5) { //The wall is breakable
                        GameObj.add(new WallBreakable(j * 32, i * 32));
                    } else {
                        GameObj.add(new WallUnbreakable(j * 32, i * 32, false));
                    }
                }
                Column++;
                Index++;
            }
        }


        GameObj.add(TankA);
        TankA.setGameWorld(this);
        GameObj.add(TankB);
        TankB.setGameWorld(this);



        PowerUp power1 = new PowerUp(50, 50, true, false); //the first health game.PowerUp
        PowerUp power2 = new PowerUp(1430, 50, false, true);  //the first boost game.PowerUp
        PowerUp power3 = new PowerUp(700, 1810, false, true); //the second boost game.PowerUp
        PowerUp power4 = new PowerUp(825, 1810, true, false); //the second health game.PowerUp

        GameObj.add(power1);
        GameObj.add(power2);
        GameObj.add(power3);
        GameObj.add(power4);

        TankControl tc1 = new TankControl(TankA, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Q); //adding control
        TankControl tc2 = new TankControl(TankB, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this); //Adds game.GameWorld to the Jframe


        this.jFrame.addKeyListener(tc1);
        this.jFrame.addKeyListener(tc2);
        this.addMouseListener(new MouseTracker());

        this.jFrame.setSize(GameWorld.WidthOfScreen + 20, GameWorld.HeightOfScreen + 40);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);

        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);


    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graph = (Graphics2D) g;
        Buffer = Walls.createGraphics();
        super.paintComponent(graph);

        if (GameWorld.task == GameTask.GameMenu) {       //Game Menu graphics setup
            (g).drawImage(Menu, 0, 0, WidthOfScreen + 2, HeightOfScreen, null);
            m.draw(g);
        } else if (GameWorld.task == GameTask.HowToPlay) {  //Instructions graphics setup
            (g).drawImage(HowToPlay, 0, 0, WidthOfScreen + 2, HeightOfScreen, null);
        } else if (GameWorld.task == GameTask.Game) {  //Game graphics setup


            for (int i = 0; i < GameObj.size(); i++) {

                GameObj.get(i).draw(Buffer);

            }

            int X_Coordinate_Tank1 = TankA.getX();
            int X_Coordinate_Tank2 = TankB.getX();
            int Y_Coordinate_Tank1 = TankA.getY();
            int Y_Coordinate_Tank2 = TankB.getY();


            if (X_Coordinate_Tank1 < WidthOfScreen / 4) {
                X_Coordinate_Tank1 = WidthOfScreen / 4;
            }
            if (X_Coordinate_Tank2 < WidthOfScreen / 4) {
                X_Coordinate_Tank2 = WidthOfScreen / 4;
            }
            if (X_Coordinate_Tank1 > WidthOfWalls - WidthOfScreen / 4) {
                X_Coordinate_Tank1 = WidthOfWalls - WidthOfScreen / 4;
            }
            if (X_Coordinate_Tank2 > WidthOfWalls - WidthOfScreen / 4) {
                X_Coordinate_Tank2 = WidthOfWalls - WidthOfScreen / 4;
            }
            if (Y_Coordinate_Tank1 < HeightOfScreen / 2) {
                Y_Coordinate_Tank1 = HeightOfScreen / 2;
            }
            if (Y_Coordinate_Tank2 < HeightOfScreen / 2) {
                Y_Coordinate_Tank2 = HeightOfScreen / 2;
            }
            if (Y_Coordinate_Tank1 > HeightOfWalls - HeightOfScreen / 2) {
                Y_Coordinate_Tank1 = HeightOfWalls - HeightOfScreen / 2;
            }
            if (Y_Coordinate_Tank2 > HeightOfWalls - HeightOfScreen/ 2) {
                Y_Coordinate_Tank2 = HeightOfWalls - HeightOfScreen / 2;
            }


            BufferedImage left_split_screen = Walls.getSubimage(X_Coordinate_Tank1 - WidthOfScreen / 4, Y_Coordinate_Tank1 - HeightOfScreen / 2, WidthOfScreen / 2, HeightOfScreen);
            BufferedImage right_split_screen = Walls.getSubimage(X_Coordinate_Tank2 - WidthOfScreen / 4, Y_Coordinate_Tank2 - HeightOfScreen / 2, WidthOfScreen / 2, HeightOfScreen);

            graph.drawImage(left_split_screen, 0, 0, null);
            graph.drawImage(right_split_screen, WidthOfScreen / 2 + 5, 0, null);

            graph.drawImage(Walls, WidthOfScreen / 2 - GameWorld.WidthOfWalls / 6 / 2, HeightOfScreen - GameWorld.HeightOfWalls / 6, GameWorld.WidthOfWalls / 6, HeightOfWalls / 6, null);
            graph.setFont(new Font("Verdana", Font.BOLD, 25));
            graph.setColor(new Color(255, 255, 255));
            graph.drawString("Tank1 Lives: " + this.Tank1Lives, 10, 28);
            graph.drawString("Tank2 Lives: " + this.Tank2Lives, WidthOfScreen / 2 + 10, 28);


            graph.drawString("(", 10, 58);
            graph.drawString("(", WidthOfScreen / 2 + 10, 58);
            graph.drawString(")", 230, 58);
            graph.drawString(")", WidthOfScreen / 2 + 230, 58);
            graph.setColor(Color.green);


            graph.fillRoundRect(25, 40, 2 * TankA.getHealth(), 20, 10, 20);
            graph.fillRoundRect(WidthOfScreen / 2 + 25, 40, 2 * TankB.getHealth(), 20, 10, 20);


            if (Tank1Wins) {
                graph.drawImage(Tank1Victory, 0, 0, WidthOfScreen + 10, HeightOfScreen, null);
            }
            if (Tank2Wins) {
                graph.drawImage(Tank2Victory, 0, 0, WidthOfScreen + 10, HeightOfScreen, null);
            }

        }
    }


}
