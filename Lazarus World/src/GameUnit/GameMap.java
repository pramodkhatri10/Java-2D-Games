package GameUnit;

import GameWorld.FileManager;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class GameMap {

	public static String LAZARUS = "LAZ";
	public static String WALL = "WAL";
	public static String SPACE = "000";
    public static String CARDBOARDBOX = "CBX";
    public static String WOODBOX = "WBX";
    public static String STONEBOX = "SBX";
    public static String METALBOX = "MBX";
	public static String STOPBLOCK = "STP";


	public static final Set<String> ALL_BOX_SET = new HashSet<String>(Arrays.asList(GameMap.CARDBOARDBOX,
	GameMap.STONEBOX, GameMap.WOODBOX, GameMap.METALBOX));;

	//validates and reads game map
	public static String[][] gameMap(int level) throws Exception {
		String fileName = FileManager.getmapfilename(level);
		String[][] arr = new String[Constants.NUMBER_MAX_BLOCKS][Constants.NUMBER_MAX_BLOCKS];
		FileManager.checkFileExist(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String s;
		int rowPos = 0, colPos = 0; 
		while ((s = reader.readLine()) != null) {
			colPos = 0;
			String[] elements = s.split(",");
			for (String value : elements) {
				arr[rowPos][colPos] = value;
				colPos += 1;
			}
			rowPos += 1;
		}
		reader.close();
		return arr;
	}

}