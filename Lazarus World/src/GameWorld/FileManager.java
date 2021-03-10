package GameWorld;

import GameUnit.Constants;

import java.io.File;
import java.io.FileNotFoundException;

;

public class FileManager {

    //validates map files existence
    public static void checkFileExist(String fName) throws FileNotFoundException {
        File file = new File(fName);
        if (!file.exists()) {
            throw new FileNotFoundException("ERROR: map file not found.");
        }
    }

    //validates map file name and throws exception if could not get
    public static String getmapfilename(int level) throws Exception {
        if (level == 1) return Constants.FILE_MAP1;
        if (level == 2) return Constants.FILE_MAP2;
        throw new Exception();
    }

}

