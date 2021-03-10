package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseTracker implements MouseListener {


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int X_Coordinate_Mouse = e.getX();
        int Y_Coordinate_Mouse = e.getY();

        //scans for game play, instructions and quiting the game
        if (X_Coordinate_Mouse >= 360 && X_Coordinate_Mouse <= 360 + 280) {
            if (Y_Coordinate_Mouse >= 416 && Y_Coordinate_Mouse <= 416 + 70 && GameWorld.task != GameWorld.GameTask.HowToPlay) {
                GameWorld.task = GameWorld.GameTask.Game;
            } else if (Y_Coordinate_Mouse >= 416 + 90 && Y_Coordinate_Mouse <= 416 + 90 + 70 && GameWorld.task == GameWorld.GameTask.GameMenu) {
                GameWorld.task = GameWorld.GameTask.HowToPlay;
            } else if (Y_Coordinate_Mouse >= 416 + 90 + 90 && Y_Coordinate_Mouse <= 416 + 90 + 90 + 70 && GameWorld.task == GameWorld.GameTask.GameMenu) {
                GameWorld.task = GameWorld.GameTask.ExitApp;
            }
        }

        //Scans clickable region for returning to the main menu from the instructions screen
        if (GameWorld.task == GameWorld.GameTask.HowToPlay) {
            if (X_Coordinate_Mouse >= 15 && X_Coordinate_Mouse <= 308) {
                if (Y_Coordinate_Mouse >= 553 && Y_Coordinate_Mouse <= 659) {
                    GameWorld.task = GameWorld.GameTask.GameMenu;
                }
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
