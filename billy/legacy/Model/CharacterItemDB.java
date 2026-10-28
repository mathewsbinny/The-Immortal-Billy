package Model;

import Controller.Item;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: CharacterItemDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class tracks the Character inventory and handles all inventory related queries to DB
 */

public class CharacterItemDB {

    /**
     * WARNING: NEED TO IMPLEMENT INVENTORY FIRST
     * Method: getInventory
     * This method gets the player's inventory
     * @param characterID
     * @return ArrayList<Item> - items
     * @throws GameException
     *
    public ArrayList<Item> getInventory(int characterID) throws GameException {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from PlayerItem WHERE characterID = " + characterID + ")";
            ResultSet rs = sdb.queryDB(sql);
            while (rs.next()) {
                Item inventory = new Item();
                inventory.setItemID(rs.getInt("itemID"));
                inventory.setItemName(rs.getString("itemName"));
                inventory.setItemType(rs.getString("itemType"));
                inventory.setItemIsConsumable(rs.getInt("itemIsConsumable"));
                inventory.setEquipped(rs.getInt("equipped"));
                items.add(inventory);
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Problem reading database");
        }
        return items;
    }
    */

    /**
     * Method: addItemToInventory
     * This method adds the selected item to their inventory
     * @param itemID
     * @param characterID
     */
    public Item AddItemToInventory(int itemID, int characterID, String itemName, String itemType, int itemIsConsumable) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("INSERT INTO CharacterItem VALUES (" + itemID + "," + characterID + ", '" + itemName + "'," + ", '" + itemType + "'," + itemIsConsumable + ", 0)");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * Method: consumeItemFromInventory
     * This item deletes an item from the inventory, used for consumables after use.
     * @param itemID
     */
    public Item consumeItemFromInventory(int itemID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("DELETE FROM CharacterItem WHERE itemID LIKE ('" + itemID + "') AND itemIsConsumable = 1");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * Method: dropItemFromInventory
     * This item removes an item from the inventory, for dropping items
     * @param itemID
     */
    public Item dropItemFromInventory(int itemID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("DELETE FROM CharacterItem WHERE itemID LIKE ('" + itemID + "')");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * WARNING: MAKE SURE TO IMPLEMENT & CALL UPDATE METHOD HERE TO CHARACTER DB AS WELL TO REFLECT NEW STATS
     * Method: updateWeaponEquipped
     * This item marks an item as equipped.
     * @param itemID
     */
    public Item updateWeaponEquipped(int itemID, int characterID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from CharacterItem WHERE ItemID = " + itemID + " AND equipped = 0 AND itemType = 'Weapon'";
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("UPDATE CharacterItem SET equipped = 1");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * WARNING: MAKE SURE TO IMPLEMENT & CALL UPDATE METHOD HERE TO CHARACTER DB AS WELL TO REFLECT NEW STATS
     * Method: updateWeaponUnequipped
     * This item marks an item as unequipped.
     * @param itemID
     */
    public Item updateWeaponUnequipped(int itemID, int characterID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from CharacterItem WHERE ItemID = " + itemID + " AND equipped = 1 AND itemType = 'Weapon'";
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("UPDATE CharacterItem SET equipped = 0");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * WARNING: MAKE SURE TO IMPLEMENT & CALL UPDATE METHOD HERE TO CHARACTER DB AS WELL TO REFLECT NEW STATS
     * Method: updateWeaponEquipped
     * This item marks an item as equipped.
     * @param itemID
     */
    public Item updateArmorEquipped(int itemID, int characterID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from CharacterItem WHERE ItemID = " + itemID + " AND equipped = 0 AND itemType = 'Armor'";
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("UPDATE CharacterItem SET equipped = 1");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * WARNING: MAKE SURE TO IMPLEMENT & CALL UPDATE METHOD HERE TO CHARACTER DB AS WELL TO REFLECT NEW STATS
     * Method: updateWeaponUnequipped
     * This item marks an item as unequipped.
     * @param itemID
     */
    public Item updateArmorUnequipped(int itemID, int characterID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from CharacterItem WHERE ItemID = " + itemID + " AND equipped = 1 AND itemType = 'Armor'";
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("UPDATE CharacterItem SET equipped = 0");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }
}
