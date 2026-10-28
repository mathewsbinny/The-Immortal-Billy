package Model;

import Controller.Item;
import Controller.Monster;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: ItemDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class handles all the DB queries for Item objects
 */

public class ItemDB {

    /**
     * Method getItem
     * Returns the Item requested by the ID. Only used in readRooms
     * @param itemID - the ID of the item requested.
     * @return void
     * @throws GameException if the item ID cannot be found
     */
    public Item getItem(int itemID) throws GameException {
        Item item;
        try {
            SQLiteDB sdb = new SQLiteDB();
            item = new Item();
            String sql = "Select * from Item WHERE itemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                item.setItemRoomID(rs.getInt("itemRoomID"));
                item.setItemID(rs.getInt("itemID"));
                item.setItemName(rs.getString("itemName"));
                item.setItemType(rs.getString("itemType"));
                item.setItemAddHP(rs.getInt("monsterCurrentHP"));
                item.setItemAddMaxHP(rs.getInt("monsterMaxHP"));
                item.setItemAddMP(rs.getInt("monsterDP"));
                item.setItemAddDP(rs.getInt("monsterDP"));
                item.setItemAddAP(rs.getInt("monsterAP"));
                item.setItemIsConsumable(rs.getInt("monsterBossPhase"));
            } else {
                throw new SQLException("Item for itemID " + itemID + " not found.");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Item with itemID " + itemID + " not found");
        }
        return item;
    }
}
