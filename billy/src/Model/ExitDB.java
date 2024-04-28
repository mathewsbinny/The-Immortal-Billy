package Model;

import Controller.Exit;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                System.out.println(rs.getInt("exitID"));
                exi.setExitID(rs.getInt("exitID"));
                exi.setExitRoomID(rs.getInt("exitRoomID"));
                exi.setDirection(rs.getString("direction"));
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
}
