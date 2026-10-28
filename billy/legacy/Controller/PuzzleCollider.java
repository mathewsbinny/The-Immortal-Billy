package Controller;

import GameExceptions.GameException;
import Model.PuzzleDB;

public class PuzzleCollider {
    private int puzzleRoomID;
    private String usedName;
    private boolean isPuzzleValid;
    private boolean isPhaseOver;

    public PuzzleCollider getPuzzleState(int puzzleRoomID, String usedName) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        return puzzleDB.getPuzzleState(puzzleRoomID, usedName);
    }

    public PuzzleCollider getPuzzleCollider(Character character) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        return puzzleDB.getPuzzleCollider(character);
    }

    public void resetPhase(int roomID) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        puzzleDB.resetPhase(roomID);
    }

    public void updatePhase(Character character) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        puzzleDB.updatePhase(character);
    }

    public void updatePuzzle(Character character) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        puzzleDB.updatePuzzle(character);
    }

    public int getPuzzleRoomID() {
        return puzzleRoomID;
    }

    public void setPuzzleRoomID(int puzzleRoomID) {
        this.puzzleRoomID = puzzleRoomID;
    }

    public String getUsedName() {
        return usedName;
    }

    public void setUsedName(String usedName) {
        this.usedName = usedName;
    }

    public boolean isPuzzleValid() {
        return isPuzzleValid;
    }

    public void setPuzzleValid(boolean valid) {
        isPuzzleValid = valid;
    }

    public boolean isPhaseOver() {
        return isPhaseOver;
    }

    public void setPhaseOver(boolean phaseOver) {
        isPhaseOver = phaseOver;
    }
}
