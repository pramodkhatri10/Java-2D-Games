package GameWorld;

import GameUnit.FallingBox;
import GameUnit.LazarusPosition;
import GameUnit.GameMap;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class DropBoxes extends TimerTask {

    private LazarusPosition lazarus;
    private List<FallingBox> fallingBoxes;
    private String[] BOXES;
    private String nextFallingBox;

    public DropBoxes(List<FallingBox> fallingBoxes, LazarusPosition lazarus) {
        this.fallingBoxes = fallingBoxes;
        this.lazarus = lazarus;
        this.BOXES = GameMap.ALL_BOX_SET.toArray(new String[GameMap.ALL_BOX_SET.size()]);
        this.nextFallingBox = BOXES[getIndex()];
    }

    //gets random index
    private int getIndex() {
        Random random = new Random();
        return random.nextInt(BOXES.length);
    }

    //adds and sets box to drop
    public void run() {
        synchronized (fallingBoxes) {
            fallingBoxes.add(new FallingBox(lazarus.posX, 0, nextFallingBox));
            nextFallingBox = BOXES[getIndex()];
        }
    }

    //gets and reruns next box type
    public String getNextFallingBox() {
        return nextFallingBox;
    }
}
