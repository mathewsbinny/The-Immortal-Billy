package Controller;

import GameExceptions.GameException;
import Model.PuzzleDB;

import java.util.ArrayList;

public class PuzzleChoice {
    private String choiceContent;
    private String playerChoice;
    private ArrayList<String> allPlayerChoice;

    public PuzzleChoice getPuzzleChoice(String playerChoice, int roomID) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        return puzzleDB.getChoiceContent(playerChoice, roomID);
    }

    public ArrayList<PuzzleChoice> getAllPlayerChoice(int roomID) throws GameException {
        PuzzleDB puzzleDB = new PuzzleDB();
        return puzzleDB.getAllPlayerChoice(roomID);
    }

    public String getChoiceContent() {
        return choiceContent;
    }

    public void setChoiceContent(String choiceContent) {
        this.choiceContent = choiceContent;
    }

    public String getPlayerChoice() {
        return playerChoice;
    }

    public void setPlayerChoice(String playerChoice) {
        this.playerChoice = playerChoice;
    }

    public ArrayList<String> getAllPlayerChoice() {
        return allPlayerChoice;
    }

    public void setAllPlayerChoice(ArrayList<String> allPlayerChoice) {
        this.allPlayerChoice = allPlayerChoice;
    }
}
