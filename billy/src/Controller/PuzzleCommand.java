package Controller;

import GameExceptions.GameException;
import Model.PuzzleRewardDB;

import java.util.ArrayList;

public class PuzzleCommand {
    /**
     method: getPuzzleState
     Purpose: get a state for puzzle
     */
    public String getPuzzleState (int roomID, String usedName) throws GameException {
        PuzzleCollider puzzleCollider = new PuzzleCollider();
        PuzzleCollider puzzleColliderData = puzzleCollider.getPuzzleState(roomID, usedName);
        if(puzzleColliderData.isPuzzleValid()){
            puzzleCollider.resetPhase(roomID);
            return "valid";
        }
        return "invalid";
    }

    /**
     method: getChoiceContent
     Purpose: support get a string to reply player choice
     */
    public String getChoiceContent (String playerChoice, int roomID) throws GameException {
        PuzzleChoice puzzleChoice = new PuzzleChoice();
        PuzzleChoice puzzleChoiceData = puzzleChoice.getPuzzleChoice(playerChoice, roomID);
        return puzzleChoiceData.getChoiceContent();
    }

    /**
     method: getDescription
     Purpose: support get a string for puzzle description
     */
    public String getPuzzleDescriptionData (int roomID) throws GameException {
        Puzzle puzzle = new Puzzle();
        Puzzle puzzleData = puzzle.getPuzzle(roomID);
        return puzzleData.getPuzzleDescription();
    }

    /**
     method: getAllDescription
     Purpose: support get all string for puzzle description
     */
    public ArrayList<String> getAllPlayerChoice (int roomID) throws GameException {
        PuzzleChoice puzzleChoice = new PuzzleChoice();
        ArrayList<PuzzleChoice> allChoice = null;
        allChoice = puzzleChoice.getAllPlayerChoice(roomID);
        ArrayList<String> allPlayerChoice = new ArrayList<>();
        for (PuzzleChoice eachChoice : allChoice) {
            allPlayerChoice.add(eachChoice.getPlayerChoice());
        }
        return allPlayerChoice;
    }

    /**
     method: isPlayPuzzleOver
     Purpose: if the play puzzle over update boolean
     */
    public Boolean isOver () throws GameException {
        Character character = new Character();
        Character characterData = character.getCharacter();
        PuzzleCollider puzzleCollider = new PuzzleCollider();
        PuzzleCollider puzzleColliderData = puzzleCollider.getPuzzleCollider(characterData);
        return puzzleColliderData.isPhaseOver();
    }

    /**
     method: isPuzzleValidPlay
     Purpose: if the puzzle valid to play
     */
    public boolean isPuzzleType() throws GameException {
        Character character = new Character();
        Character characterDate = character.getCharacter();
        Room room = new Room();
        Room roomData = room.getRoom(characterDate.getCharacterCurrentRoomID());
        String type = roomData.getRoomType();
        return type.equals("puzzle");
    }

    /**
     method: checkLetterInput
     Purpose: check player input is a letter and check(only for puzzle"Rainbow button")
     */
    public String checkLetterInput (String playerChoice) throws GameException {
        PuzzleCollider puzzleCollider = new PuzzleCollider();
        Character character = new Character();
        Character characterData = character.getCharacter();
        int characterID = characterData.getCharacterID();
        int roomID = characterData.getCharacterCurrentRoomID();
        if(playerChoice.equals("R") || playerChoice.equals("O") || playerChoice.equals("Y") || playerChoice.equals("G") || playerChoice.equals("B") || playerChoice.equals("I") || playerChoice.equals("V")) {
            Puzzle puzzle = new Puzzle();
            Puzzle puzzleData = puzzle.getPuzzle(roomID);
            String solution = puzzleData.getPuzzleSolution();
            Puzzle inputData = puzzle.getInput(characterID);
            ArrayList<String> word = new ArrayList<>();
            char[] word_char = (inputData.getPlayerInput()).toCharArray();
            for (char i : word_char) {
                word.add(String.valueOf(i));
            }
            for (String i : word) {
                if (i.equals(playerChoice)) {
                    return getChoiceContent("Repeat", roomID);
                }
            }
            String newWord = inputData.getPlayerInput() + playerChoice;
            puzzle.updateInput(newWord, characterID);
            inputData = puzzle.getInput(characterID);
            String result = inputData.getPlayerInput();
            if (result.length() == 7) {
                if (solution.equals(result)) {
                    puzzleCollider.updatePhase(characterData);
                    puzzleCollider.updatePuzzle(characterData);
                    //add item to player inventory
                    return getChoiceContent(result, roomID);
                }
                puzzle.clearInput(characterID);
                puzzleCollider.updatePhase(characterData);
                return getChoiceContent("Incorrect", roomID);
            }
            return "(Current Entered: " + inputData.getPlayerInput() + ")" + getChoiceContent("Next", roomID);
        }
        return getChoiceContent("Wrong Letter", roomID);
    }
}
