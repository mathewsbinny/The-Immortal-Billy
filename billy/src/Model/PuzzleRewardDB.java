package Model;

import Controller.Character;
import Controller.PuzzleReward;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PuzzleRewardDB {
    public PuzzleReward getRewardValue (int roomID) throws GameException {
        PuzzleReward puzzleReward;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            puzzleReward = new PuzzleReward();
            String sql = "Select * from PuzzleReward Where puzzleRoomID = " + roomID;
            ResultSet rs = sqLiteDB.queryDB(sql);
            if (rs.next()) {
                puzzleReward.setRoomID(rs.getInt("puzzleRoomID"));
                puzzleReward.setHP(rs.getInt("HP"));
                puzzleReward.setAP(rs.getInt("AP"));
                puzzleReward.setMaxHP(rs.getInt("maxHP"));
                puzzleReward.setTarget(rs.getString("target"));
                sqLiteDB.close();
            } else {
                throw new SQLException("PuzzleReward in roomID " + roomID + " not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("PuzzleReward in roomID " + roomID + " not found." + e.getMessage());
        }
        return puzzleReward;
    }

    public void updateCharacter (Character character) throws GameException {
        String sql = "UPDATE Character SET maxHP = " + character.getCharacterMaxHP() + " WHERE characterID = " + character.getCharacterID();
        String sql2 = "UPDATE Character SET AP = " + character.getCharacterAP() + " WHERE characterID = " + character.getCharacterID();
        String sql3 = "UPDATE Character SET HP = " + character.getCharacterCurrentHP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.updateDB(sql2);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void updateWeapon (int newAP, String target) throws GameException {
        String sql = "UPDATE Weapon SET AP += " + newAP + " WHERE weaponName = " + target;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void updateArmor (int newDP, String target) throws GameException {
        String sql = "UPDATE Armor SET DP += " + newDP + " WHERE armorName = " + target;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
