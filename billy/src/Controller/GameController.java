package Controller;

import GameExceptions.GameException;

import java.io.File;

public class GameController {
    public static final int FIRST_ROOM = 1;

    /**
     * Method : start
     * Checks to see if the DB field exists and if not creates it
     * by calling GameDBCreate buildTables().
     */
    public void startDB() throws GameException {
        File dbFile = new File("billy.db");
        if (!dbFile.exists()) {
            CreateFilesController cfc = new CreateFilesController();
            cfc.createFile("billy.db");
        }
    }

}
