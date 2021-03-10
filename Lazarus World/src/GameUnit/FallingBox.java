package GameUnit;

public class FallingBox {

    //box positions
    private int posX;
    private int posY;

    private String boxType; //types of box on basis of weight

    //allows box movements
    public FallingBox(int posX, int posY, String boxType) {
        this.posX = posX;
        this.posY = posY;
        this.boxType = boxType;
    }

    //lets box movement downward
    public void goDownward() {
        assert posX >= 0 : "Error in position value.";
        assert posY >= 0 : "Error in position value.";
        posY += Constants.SPEEDOFBOX;
    }

    //gets falling box positions
    public int getBoxIndexDownward() {

        return posY + Constants.BLOCKSIZE;
    }

    //returns box depending upon their weight
    public static int getBoxType(String box) {
        int boxType = 0;
        if(box.equals(GameMap.CARDBOARDBOX))  boxType = 0; //the lightest
        if(box.equals(GameMap.WOODBOX)) boxType = 1; //2nd lightest
        if(box.equals(GameMap.STONEBOX)) boxType = 2; //2nd heaviest
        if(box.equals(GameMap.METALBOX)) boxType = 3; //the heaviest
        return boxType;
    }

    public int getPosX() {

        return posX;
    }

    public int getPosY() {

        return posY;
    }

    public String getBoxType() {

        return boxType;
    }

}