package Controller;

import GameExceptions.GameException;
import Model.PuzzleDB;

public class Puzzle {
    private int puzzleRoomID;
    private String puzzleRegion;
    private String puzzleName;
    private String puzzleDescription;
    private String puzzleSolution;
    private String puzzleType;
    private String playerInput;


    public Puzzle getPuzzle(int roomID) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        return puzzleDB.getPuzzle(roomID);
    }

    public Puzzle getInput(int characterID) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        return puzzleDB.getPlayerInput(characterID);
    }


    public String getPuzzleRegion() {
        return puzzleRegion;
    }

    public void setPuzzleRegion(String puzzleRegion) {
        this.puzzleRegion = puzzleRegion;
    }

    public String getPuzzleName() {
        return puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public String getPuzzleDescription() {
        return puzzleDescription;
    }

    public void setPuzzleDescription(String puzzleDescription) {
        this.puzzleDescription = puzzleDescription;
    }

    public String getPuzzleSolution() {
        return puzzleSolution;
    }

    public void setPuzzleSolution(String puzzleSolution) {
        this.puzzleSolution = puzzleSolution;
    }

    public int getPuzzleRoomID() {
        return puzzleRoomID;
    }

    public void setPuzzleRoomID(int puzzleRoomID) {
        this.puzzleRoomID = puzzleRoomID;
    }

    public String getPuzzleType() {
        return puzzleType;
    }

    public void setPuzzleType(String puzzleType) {
        this.puzzleType = puzzleType;
    }

    public String getPlayerInput() {
        return playerInput;
    }

    public void setPlayerInput(String playerInput) {
        this.playerInput = playerInput;
    }
}
