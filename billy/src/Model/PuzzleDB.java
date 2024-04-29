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
    /**
     * Method: getPuzzle
     * Purpose: Gets puzzle information based upon the supplied character current roomID.
     */
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

    /**
     * Method: getChoiceContent
     * Purpose: Gets a choice content based upon the supplied player input and which room puzzle.
     */
    public PuzzleChoice getChoiceContent(String playerChoice, int roomID) throws GameException {
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

    /**
     * Method: getPlayerInput
     * Purpose: Gets a string stored in database what player choice before and return a string.(only for puzzle "rainbow button")
     */
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

    /**
     * Method: getAllPlayerChoice
     * Purpose: Gets all player valid input to choice puzzle.
     */
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

    /**
     * Method: getPuzzleCollider
     * Purpose: Gets puzzle state and play puzzle state.
     */
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

    /**
     * Method: getpuzzleState
     * Purpose: Gets a puzzle state.
     */
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

    /**
     * Method: resetPhase
     * Purpose: Update phase state mean play puzzle not over
     */
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

    /**
     * Method: updatePhase
     * Purpose: update phase state mean play puzzle over
     */
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

    /**
     * Method: updatePuzzle
     * Purpose: update puzzle state mean the puzzle solve or cant play again.
     */
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

    /**
     * Method: updateInput
     * Purpose: create a new player input use characterID. (only for puzzle "rainbow button")
     */
    public void createInput(int characterID) throws SQLException, ClassNotFoundException {
        SQLiteDB sqLiteDB = new SQLiteDB();
        try {
            sqLiteDB.updateDB("INSERT INTO PlayerInput VALUES('" + characterID + "', '')");
        }
        catch (SQLException e){
            throw new SQLException("Cant create in the database.");
        }
        sqLiteDB.close();
    }

    /**
     * Method: updateInput
     * Purpose: update to stored player input or player choice. (only for puzzle "rainbow button")
     */
    public void updateInput(String input, int characterID) throws GameException {
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

    /**
     * Method: clearInput
     * Purpose: update to clear player input or player choice when incorrect choice.(only for puzzle "rainbow button")
     */
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
