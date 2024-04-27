package Controller;

import GameExceptions.GameException;
import Model.GameDBCreate;

public class CreateFilesController {
    /**
     * Method: createFile
     * Purpose: Create the database for MiniGame2
     * @return void
     */
    public void createFile() throws GameException {
        GameDBCreate sdb = new GameDBCreate();
        sdb.buildTables();
    }

    /**
     * Method: createFile
     * Purpose: Create the database for MiniGame2
     * @return void
     */
    public void createFile(String dbName) throws GameException {
        GameDBCreate sdb = new GameDBCreate();
        sdb.buildTables();
    }
}
