package GameUnit;

import java.io.IOException;

public class CollisionDetector {

    private String[][] map;

    public CollisionDetector(String[][] map) throws IOException {
        this.map = map;
    }

    /**
     *checks if lazarus collides with other game objects
     * @return true if collides
     */
    public boolean checkCollision(int x, int y) {
        return boundaryCollision(x, y) || wallCollision(x, y)
                || boxCollision(x, y);
    }

    /**
     * checks if lazarus collides to boxes
     * @return true if collides to wall
     */
    public boolean boxCollision(int x, int y){
        String value = getMap(x, y);

        if(value.equals(GameMap.CARDBOARDBOX) || value.equals(GameMap.WOODBOX)
                || value.equals(GameMap.STONEBOX) || value.equals(GameMap.METALBOX)) {
            return true;
        }
        return false;
    }

    /**
     * checks if lazarus collides to wall
     * @return true if collides to wall
     */
    private boolean wallCollision(int x, int y){
        String value = getMap(x, y);
        if(value.equals(GameMap.WALL)) {
            return true;
        }
        return false;
    }

    /**
     * checks if lazarus collides to boundary
     * @return true if collides
     */
    private boolean boundaryCollision(int x, int y){
        return (x < 0 || x > Constants.GAMEBOARDSIZE - Constants.BLOCKSIZE || y < 0);
    }

    /**
     *gets map
     * @return the value
     */
    public String getMap(int x, int y) {
        int boxX = x / Constants.BLOCKSIZE;
        int boxY = y / Constants.BLOCKSIZE;

        String val = map[boxY][boxX];
        return val;
    }

    /**
     * checks collision of two boxes
     * @return true if two boxes collide
     */
    public boolean checkBoxBoxCollision(FallingBox fallingBox) {
        int x = fallingBox.getPosX();
        int y = fallingBox.getBoxIndexDownward();

        String val = getMap(x, y);

        if(GameMap.ALL_BOX_SET.contains(val)) {
            return true;
        }
        return false;
    }

    /**
     * checks for lazarus and stop block collision
     * @return true if box collides stop block
     */
    public boolean checkStopBlockCollision(int x, int y) {
        String val = getMap(x, y);
        if(val.equals(GameMap.STOPBLOCK)) {
            return true;
        }
        return false;
    }

    /**
     *checks collision of moving box and wall
     * @return true if box collides to wall
     */
    public boolean checkBoxWallCollision(FallingBox fallingBox) {
        int x = fallingBox.getPosX();
        int y = fallingBox.getBoxIndexDownward();

        String val = getMap(x, y);

        if (val.equals(GameMap.WALL)) {
            return true;
        }
        return false;
    }
}