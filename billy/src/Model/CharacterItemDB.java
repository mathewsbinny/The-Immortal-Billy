package Model;

import Controller.Item;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterItemDB {

    /**
     * Method: addItemToInventory
     * This method adds the selected item to their inventory
     * @param itemID
     * @param characterID
     */
    public Item AddItemToInventory(int itemID, int characterID) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("INSERT INTO CharacterItem VALUES ('" + itemID + "','" + characterID + "')");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * Method: consumeItemFromInventory
     * This item removes an item from the inventory, used for consumables after use.
     * @param itemID
     */
    public Item consumeItemFromInventory(int itemID, int itemIsConsumable) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("DELETE FROM PlayerItem WHERE itemID LIKE ('" + itemID + "') AND itemIsConsumable = 1");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }

    /**
     * Method: dropItemFromInventory
     * This item removes an item from the inventory, used for consumables after use.
     * @param itemID
     */
    public Item dropItemFromInventory(int itemID, int itemIsConsumable) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Item WHERE ItemID = " + itemID;
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("DELETE FROM PlayerItem WHERE itemID LIKE ('" + itemID + "')");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }
    //---------------------------------------------------------------------------------------------------------------------
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
