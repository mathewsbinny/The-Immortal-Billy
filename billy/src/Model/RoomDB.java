package Model;

import Controller.Room;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * Method: getRoom
     * Purpose: Gets a room based upon the supplied ID
     * @param id
     * @return Room
     * @throws SQLException
     */
    public Room getRoom(int id) throws GameException {
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

    /*
    /** Method: updateRoom
     * This method show how to update a SQL table.
     *
    public void updateRoom(Room room) throws GameException {
        String sql = "UPDATE room SET exits = '" + room.getExits() + "' WHERE roomID = " + room.getRoomID() ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
    */
}
