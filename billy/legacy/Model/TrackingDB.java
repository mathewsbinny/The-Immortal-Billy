package Model;

import Controller.Tracking;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrackingDB {
    /**
     * Method: getTrackingStats
     * Purpose: gets all tracking data
     * @return ArrayList<Tracking>
     * @throws SQLException
     */
    public ArrayList<Tracking> getTrackingStats() throws GameException {
        ArrayList<Tracking> stats = new ArrayList<Tracking>();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from HelpMenu";
            ResultSet rs = sdb.queryDB(sql);
            while (rs.next()) {
                Tracking hm = new Tracking();
                hm.setTrackCharacterID(rs.getInt("characterID"));
                hm.setTrackCharacterLevel(rs.getInt("characterLevel"));
                hm.setTrackRoomsVisited(rs.getInt("trackRoomsVisited"));
                hm.setTrackPlayTime(rs.getDouble("trackPlayTime"));
                hm.setTrackCharacterDeaths(rs.getInt("trackCharacterDeaths"));
                hm.setTrackMonstersKilled(rs.getInt("trackMonstersKilled"));
                stats.add(hm);
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Problem reading database");
        }
        return stats;
    }

    /** Method: updateCharacterLevel
     * @param tracking
     * This method updates Tracking table in DB for Character Level
     */
    public void updateCharacterLevel(Tracking tracking) throws GameException {
        String sql = "UPDATE Tracking SET trackCharacterLevel = " + tracking.getTrackCharacterLevel() + " WHERE trackCharacterID = " + tracking.getTrackCharacterID();
        /**
         * Alternative SQL Query
         * String sql = "UPDATE Tracking SET trackCharacterLevel = " + tracking.getTrackCharacterLevel() + " WHERE characterID = 1";
         */
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update for roomVisited encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateRoomsVisited
     * @param tracking
     * This method updates Tracking table in DB for total rooms visited
     */
    public void updateRoomsVisited(Tracking tracking) throws GameException {
        String sql = "UPDATE Tracking SET trackRoomsVisited = " + tracking.getTrackRoomsVisited() + " WHERE trackCharacterID = " + tracking.getTrackCharacterID();
        /**
         * Alternative SQL Query
         * String sql = "UPDATE Tracking SET trackRoomsVisited = " + tracking.getTrackRoomsVisited() + " WHERE characterID = 1";
         */
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update for roomVisited encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updatePlayTime
     * @param tracking
     * This method updates Tracking table in DB for total Play Time
     */
    public void updatePlayTime(Tracking tracking) throws GameException {
        String sql = "UPDATE Tracking SET trackPlayTime = " + tracking.getTrackPlayTime() + " WHERE trackCharacterID = " + tracking.getTrackCharacterID();
        /**
         * Alternative SQL Query
         * String sql = "UPDATE Tracking SET trackPlayTime = " + tracking.getTrackPlayTime() + " WHERE characterID = 1";
         */
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update for roomVisited encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterDeaths
     * @param tracking
     * This method updates Tracking table in DB for total Character Deaths
     */
    public void updateCharacterDeaths(Tracking tracking) throws GameException {
        String sql = "UPDATE Tracking SET trackCharacterDeaths = " + tracking.getTrackCharacterDeaths() + " WHERE trackCharacterID = " + tracking.getTrackCharacterID();
        /**
         * Alternative SQL Query
         * String sql = "UPDATE Tracking SET trackCharacterDeaths = " + tracking.getTrackCharacterDeaths() + " WHERE characterID = 1";
         */
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update for roomVisited encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateMonstersKilled
     * @param tracking
     * This method updates Tracking table in DB for total Monsters Killed
     */
    public void updateMonstersKilled(Tracking tracking) throws GameException {
        String sql = "UPDATE Tracking SET trackMonstersKilled = " + tracking.getTrackMonstersKilled() + " WHERE trackCharacterID = " + tracking.getTrackCharacterID();
        /**
         * Alternative SQL Query
         * String sql = "UPDATE Tracking SET trackMonstersKilled = " + tracking.getTrackMonstersKilled() + " WHERE characterID = 1";
         */
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update for roomVisited encountered a problem.\n" + e.getMessage());
        }
    }
}
