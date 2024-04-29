package Model;

import Controller.Account;
import Controller.Character;
import Controller.Item;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterDB {
    /**
     * Method: getCharacterByAccID
     * Purpose: Gets a Character based upon the supplied accountID
     * @param accountID
     * @return Account
     * @throws SQLException
     */
    public Character getCharacterByAccID(int accountID) throws GameException {
        Character character = new Character();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Character WHERE accountID = " + accountID;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                character.setAccountID(rs.getInt("accountID"));
                character.setCharacterCurrentRoomID(rs.getInt("characterCurrentRoom"));
                character.setCharacterID(rs.getInt("characterID"));
                character.setCharacterName(rs.getString("characterName"));
                character.setCharacterCurrentHP(rs.getInt("characterCurrentHP"));
                character.setCharacterMaxHP(rs.getInt("characterMaxHP"));
                character.setCharacterMPLevel(rs.getInt("characterMPLevel"));
                character.setCharacterDP(rs.getInt("characterDP"));
                character.setCharacterAP(rs.getInt("characterAP"));
                character.setCharacterXP(rs.getInt("characterXP"));
                character.setCharacterLevel(rs.getInt("characterLevel"));
            } else {
                throw new SQLException("Character for Account " + accountID + " not found");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Character for " + accountID + "was not found.");
        }
        return character;
    }

    /**
     * Method: getCharacterByCharID
     * Purpose: Gets a Character based upon the supplied characterID
     * @param characterID
     * @return Account
     * @throws SQLException
     */
    public Character getCharacterByCharID(int characterID) throws GameException {
        Character character = new Character();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Character WHERE accountID = " + characterID;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                character.setAccountID(rs.getInt("accountID"));
                character.setCharacterCurrentRoomID(rs.getInt("characterCurrentRoom"));
                character.setCharacterID(rs.getInt("characterID"));
                character.setCharacterName(rs.getString("characterName"));
                character.setCharacterCurrentHP(rs.getInt("characterCurrentHP"));
                character.setCharacterMaxHP(rs.getInt("characterMaxHP"));
                character.setCharacterMPLevel(rs.getInt("characterMPLevel"));
                character.setCharacterDP(rs.getInt("characterDP"));
                character.setCharacterAP(rs.getInt("characterAP"));
                character.setCharacterXP(rs.getInt("characterXP"));
                character.setCharacterLevel(rs.getInt("characterLevel"));
            } else {
                throw new SQLException("Character for Character " + characterID + " not found");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Character for " + characterID + "was not found.");
        }
        return character;
    }

    /** Method: updateCharacterCurrentHP
     * This method updates Character table in DB to track character's current HP
     */
    public void updateCharacterCurrentHP(Character character) throws GameException {
        String sql = "UPDATE Character SET characterCurrentHP = " + character.getCharacterCurrentHP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterMaxHP
     * This method updates Character table in DB to track character's max HP
     */
    public void updateCharacterMaxHP(Character character) throws GameException {
        String sql = "UPDATE Character SET characterMaxHP = " + character.getCharacterMaxHP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterMPLevel
     * This method updates Character table in DB to track character's Magic Proficiency Level
     */
    public void updateCharacterMPLevel(Character character) throws GameException {
        String sql = "UPDATE Character SET characterMPLevel = " + character.getCharacterMPLevel() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterDP
     * This method updates Character table in DB to track character's Defense Points
     */
    public void updateCharacterDP(Character character) throws GameException {
        String sql = "UPDATE Character SET characterDP = " + character.getCharacterDP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterAP
     * This method updates Character table in DB to track character's Attack Points
     */
    public void updateCharacterAP(Character character) throws GameException {
        String sql = "UPDATE Character SET characterAP = " + character.getCharacterAP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterXP
     * This method updates Character table in DB to track character's Experience Points
     */
    public void updateCharacterXP(Character character) throws GameException {
        String sql = "UPDATE Character SET characterXP = " + character.getCharacterXP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterLevel
     * This method updates Character table in DB to track character's Level
     */
    public void updateCharacterLevel(Character character) throws GameException {
        String sql = "UPDATE Character SET characterLevel = " + character.getCharacterLevel() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateCharacterCurrentRoom
     * This method updates Character table in DB to track character's location
     */
    public void updateCharacterCurrentRoom(Character character) throws GameException {
        String sql = "UPDATE Character SET characterCurrentRoom = " + character.getCharacterCurrentRoomID() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /**
     * Method: addCharacter
     * This creates new Character
     * @param account
     */
    public Item addCharacter(Account account) throws SQLException, GameException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        try {
            String sql = "Select * from Character WHERE accountID = " + account.getAccountID();
            ResultSet rs = sdb.queryDB(sql);
            sdb.updateDB("INSERT INTO Character VALUES (" + account.getAccountID() + ", 1, 1, 'Billy', 100, 100, 1, 10, 25, 0, 1");
        } catch (SQLException e) {
            throw new SQLException("Can't find DB");
        }
        sdb.close();
        return null;
    }
}
