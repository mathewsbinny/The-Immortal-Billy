package Model;

import Controller.Item;
import Controller.Room;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: ItemRoomDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class handles all the DB queries for Items in Rooms objects
 */

public class ItemRoomDB {

    /**
     * Method: removeItemFromRoom
     * This item removes an item from the room.
     * @param itemID
     */
    public Item removeItemFromRoom(int itemID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("DELETE FROM ItemRoom WHERE itemID LIKE (" + itemID + ")");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * Method: addItemToRoom
     * This adds an item to the room, for example if character drops item
     * @param itemID
     */
    public Item addItemToRoom(int itemID, int roomID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("INSERT INTO ItemRoom VALUES (" + itemID + "," + roomID + ")");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * Method: updateItemLocation
     * This updating an item's current location
     * @param itemID
     */
    public Item updateItemLocation(int itemID, int roomID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("UPDATE ItemRoom SET itemID = " + itemID + " WHERE roomID = " + roomID + ")");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * NOT FINISHED
     * Method: getAllItemsInRoom
     * Purpose: gets all items in room
     * @param roomID
     * @return ArrayList<Room>
     * @throws SQLException
     */
    public ArrayList<Item> getAllItemsInRoom(int roomID) throws GameException {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Item WHERE itemRoomID = " + roomID + ")";
            ResultSet rs = sdb.queryDB(sql);
            while (rs.next()) {
                Item itemRoom = new Item();
                itemRoom.setItemRoomID(rs.getInt("itemRoomID"));
                itemRoom.setItemID(rs.getInt("itemID"));
                itemRoom.setItemName(rs.getString("itemName"));
                itemRoom.setItemType(rs.getString("itemType"));
                itemRoom.setItemAddHP(rs.getInt("itemAddHP"));
                itemRoom.setItemAddMaxHP(rs.getInt("itemAddMaxHP"));
                itemRoom.setItemAddMP(rs.getInt("itemAddMP"));
                itemRoom.setItemAddDP(rs.getInt("itemAddDP"));
                itemRoom.setItemAddAP(rs.getInt("itemAddAP"));
                itemRoom.setItemIsConsumable(rs.getInt("itemIsConsumable"));
                items.add(itemRoom);
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Problem reading database");
        }
        return items;
    }
}
