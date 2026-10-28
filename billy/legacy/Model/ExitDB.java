package Model;

import Controller.Exit;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: ExitDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class handles the Exit interaction with the DB.
 */

public class ExitDB {
    private SQLiteDB sdb = null;

    /**
     * Method: getExits
     * Purpose: Gets exits based upon the supplied ID
     * @param roomID
     * @return exits
     * @throws SQLException
     */
    public java.util.ArrayList<Exit> getExits(int roomID) throws GameException {
        List<Exit> exits = new ArrayList<Exit>();

        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Exit WHERE exitRoomID = " + roomID;
            ResultSet rs = sdb.queryDB(sql);
            while (rs.next()) {
                Exit exi = new Exit();
                exi.setExitID(rs.getInt("exitID"));
                exi.setExitRoomID(rs.getInt("exitRoomID"));
                exi.setDirection(rs.getString("exitDirection"));
                exi.setExitDestinationID(rs.getInt("exitDestinationID"));
                exits.add(exi);
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Room ID " + roomID + "was not found.");
        }
        return (ArrayList<Exit>) exits;
    }

    /** Method: updateRandomizedExit
     * This method updates exits to reflect room randomization
     */
    public void updateRandomizedExit(int nextRoomID) throws GameException {
        String sql = "UPDATE Exit SET exitDestinationID = " + nextRoomID + " WHERE exitDestinationID = 0" ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
