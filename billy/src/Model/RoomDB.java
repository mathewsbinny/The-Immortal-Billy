package Model;

import Controller.Room;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: RoomDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28 2024
 * This class handles all of the DB queries for Room objects
 */

public class RoomDB {
    /**
     * Method: getNextRoomID
     * Purpose: gets the next ID for a room
     * @return int
     */
    public int getNextRoomID() throws GameException {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            next = sdb.getMaxValue("roomID", "Room") + 1;
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException sqe) {
            throw new GameException("Could not get next room id");
        }

        return next;
    }

    /**
     * Method: getRoomByRoomID
     * Purpose: Gets a room based upon the supplied room ID
     * @param id
     * @return Room
     * @throws SQLException
     */
    public Room getRoomByRoomID(int id) throws GameException {
        Room rm = new Room();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Room WHERE roomID = " + id;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                System.out.println(rs.getInt("roomID"));
                rm.setRoomID(rs.getInt("roomID"));
                rm.setRoomRegion(rs.getString("roomRegion"));
                rm.setRoomRandomization(rs.getString("roomRandomization"));
                rm.setRoomType(rs.getString("roomType"));
                rm.setRoomName(rs.getString("roomName"));
                rm.setRoomDescription(rs.getString("roomDescription"));
                rm.setRoomIsVisited(rs.getInt("roomVisited"));
                //rm.setExits(rs.getString("exits"));
            } else {
                throw new SQLException("Room " + id + " not found");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Room id " + id + "was not found.");
        }
        return rm;
    }

    /**
     * Method: getRoomByRandomizationID
     * Purpose: Gets a room based upon the supplied roomRandomization ID
     * @param regionID
     * @param randomizationValue
     * @return Room
     * @throws SQLException
     */
    public Room getRoomByRandomization(int regionID, int randomizationValue) throws GameException {
        Room rm = new Room();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Room WHERE roomRandomization = " + regionID + randomizationValue;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                System.out.println(rs.getInt("roomID"));
                rm.setRoomID(rs.getInt("roomID"));
                rm.setRoomRegion(rs.getString("roomRegion"));
                rm.setRoomRandomization(rs.getString("roomRandomization"));
                rm.setRoomType(rs.getString("roomType"));
                rm.setRoomName(rs.getString("roomName"));
                rm.setRoomDescription(rs.getString("roomDescription"));
                rm.setRoomIsVisited(rs.getInt("roomVisited"));
            } else {
                throw new SQLException("Room " + regionID + randomizationValue + " not found");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Room id " + regionID + randomizationValue + "was not found.");
        }
        return rm;
    }

    /**
     * Method: getAllRooms
     * Purpose: gets all rooms
     * @return ArrayList<Room>
     * @throws SQLException
     */
    public ArrayList<Room> getAllRooms() throws GameException {
        ArrayList<Room> rooms = new ArrayList<Room>();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Room";

            ResultSet rs = sdb.queryDB(sql);

            while (rs.next()) {
                Room rm = new Room();
                rm.setRoomID(rs.getInt("roomID"));
                rm.setRoomRegion(rs.getString("roomRegion"));
                rm.setRoomRandomization(rs.getString("roomRandomization"));
                rm.setRoomType(rs.getString("roomType"));
                rm.setRoomName(rs.getString("roomName"));
                rm.setRoomDescription(rs.getString("roomDescription"));
                rm.setRoomIsVisited(rs.getInt("roomVisited"));
                //rm.setExits(rs.getString("Exits"));
                rooms.add(rm);
            }

            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Problem reading database");
        }
        return rooms;
    }


    /** Method: updateRoomVisited
     * This method updates Room table in DB to mark it as visited.
     */
    public void updateRoomVisited(Room room) throws GameException {
        String sql = "UPDATE Room SET roomVisited = '" + room.getRoomIsVisited() + "' WHERE roomID = " + room.getRoomID() ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

}
