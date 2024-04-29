package Model;

import Controller.Character;
import Controller.PuzzleReward;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PuzzleRewardDB {
    /**
     * Method: getRewardValue
     * Purpose: get the puzzle solved after reward information or value.
     */
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

    /**
     * Method: updateCharacter
     * Purpose: update character value.
     */
    public void updateCharacter (Character character) throws GameException {
        String sql = "UPDATE Character SET characterMaxHP = " + character.getCharacterMaxHP() + " WHERE characterID = " + character.getCharacterID();
        String sql2 = "UPDATE Character SET characterAP = " + character.getCharacterAP() + " WHERE characterID = " + character.getCharacterID();
        String sql3 = "UPDATE Character SET characterHP = " + character.getCharacterCurrentHP() + " WHERE characterID = " + character.getCharacterID();
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.updateDB(sql2);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
