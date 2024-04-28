package Model;

import Controller.Character;
import Controller.Puzzle;
import Controller.PuzzleChoice;
import Controller.PuzzleCollider;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PuzzleDB {
    public Puzzle getPuzzle(int puzzleRoomID) throws GameException {
        Puzzle puzzle;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            puzzle = new Puzzle();
            String sql = "Select * from Puzzle Where puzzleRoomID = '" + puzzleRoomID + "'";
            ResultSet rs = sqLiteDB.queryDB(sql);
            if (rs.next()) {
                puzzle.setPuzzleName(rs.getString("puzzleName"));
                puzzle.setPuzzleDescription(rs.getString("puzzleDescription"));
                puzzle.setPuzzleSolution(rs.getString("puzzleSolution"));
                sqLiteDB.close();
            } else {
                throw new SQLException("Puzzle in roomID " + puzzleRoomID + " not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Puzzle in roomID " + puzzleRoomID + " not found." + e.getMessage());
        }
        return puzzle;
    }

    public PuzzleChoice getChoiceContext(String playerChoice, int roomID) throws GameException {
        PuzzleChoice puzzleChoice;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            puzzleChoice = new PuzzleChoice();
            String sql = "Select ChoiceContent from PuzzleChoice Where playerChoice = '" + playerChoice + "' And puzzleRoomID = '" + roomID + "'";
            ResultSet rs = sqLiteDB.queryDB(sql);
            if (rs.next()) {
                puzzleChoice.setChoiceContent(rs.getString("choiceContent"));
                sqLiteDB.close();
            } else {
                throw new SQLException("ChoiceContent in playerChoice " + playerChoice + " not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("ChoiceContent in playerChoice " + playerChoice + " not found." + e.getMessage());
        }
        return puzzleChoice;
    }

    public Puzzle getPlayerInput(int characterID) throws GameException {
        Puzzle puzzle;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            puzzle = new Puzzle();
            String sql = "Select input from PlayerInput WHERE characterID = '" + characterID + "'";
            ResultSet rs = sqLiteDB.queryDB(sql);
            if (rs.next()) {
                puzzle.setPlayerInput(rs.getString("input"));
                sqLiteDB.close();
            } else {
                throw new SQLException("Input in puzzleRoomID " + characterID + " not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Input in puzzleRoomID " + characterID + " not found." + e.getMessage());
        }
        return puzzle;
    }

    //return all player valid choice
    public ArrayList<PuzzleChoice> getAllPlayerChoice(int roomID) throws GameException {
        ArrayList<PuzzleChoice> allchoice = new ArrayList<>();
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            String sql = "Select playerChoice from PuzzleChoice Where puzzleRoomID = '" + roomID + "'";
            ResultSet rs = sqLiteDB.queryDB(sql);
            while (rs.next()) {
                PuzzleChoice puzzleChoice = new PuzzleChoice();
                puzzleChoice.setPlayerChoice(rs.getString("playerChoice"));
                allchoice.add(puzzleChoice);
            }
            sqLiteDB.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("All playerChoice in playerChoice " + roomID + " not found.");
        }
        return allchoice;
    }

    public PuzzleCollider getPuzzleCollider (Character character) throws GameException {
        PuzzleCollider puzzleCollider;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            puzzleCollider = new PuzzleCollider();
            String sql = "Select * from PuzzleCollider Where puzzleRoomID = '" + character.getCharacterCurrentRoomID() + "'";
            ResultSet rs = sqLiteDB.queryDB(sql);
            if (rs.next()) {
                puzzleCollider.setPuzzleValid(rs.getBoolean("isPuzzleValid"));
                puzzleCollider.setPhaseOver(rs.getBoolean("isPhaseOver"));
                sqLiteDB.close();
            } else {
                throw new SQLException("PuzzleCollider in roomID " + character.getCharacterCurrentRoomID() + " not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("PuzzleCollider in roomID " + character.getCharacterCurrentRoomID() + " not found." + e.getMessage());
        }
        return puzzleCollider;
    }

    public PuzzleCollider getPuzzleState (int roomID, String usedName) throws GameException {
        PuzzleCollider puzzleCollider;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            puzzleCollider = new PuzzleCollider();
            String sql = "Select isPuzzleValid From PuzzleCollider Where puzzleRoomID = '" + roomID + "' And usedName = '" + usedName + "'";
            ResultSet rs = sqLiteDB.queryDB(sql);
            puzzleCollider.setPuzzleValid(rs.getBoolean("isPuzzleValid"));
            sqLiteDB.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new GameException("isPuzzleValid or puzzleState in roomID " + roomID + " not found." + e.getMessage());
        }
        return puzzleCollider;
    }


    public void resetPhase(int roomID) throws GameException {
        String sql = "UPDATE PuzzleCollider SET isPhaseOver = 0 WHERE puzzleRoomID = " + roomID;
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void updatePhase(Character character) throws GameException {
        String sql = "UPDATE PuzzleCollider SET isPhaseOver = 1 WHERE puzzleRoomID = " + character.getCharacterCurrentRoomID();
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void updatePuzzle(Character character) throws GameException {
        String sql = "UPDATE PuzzleCollider SET isPuzzleValid = 0 WHERE puzzleRoomID = " + character.getCharacterCurrentRoomID();
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void updateInput(String input, int characterID) throws GameException {
        String createSQL = "";
        String sql = "UPDATE PlayerInput SET input = '" + input + "' WHERE characterID = '" + characterID + "'";
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    public void clearInput(int characterID) throws GameException {
        String sql = "UPDATE PlayerInput SET input = '' WHERE characterID = '" + characterID + "'";
        try {
            SQLiteDB sqLiteDB = new SQLiteDB();
            sqLiteDB.updateDB(sql);
            sqLiteDB.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
