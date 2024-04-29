package Controller;

import GameExceptions.GameException;
import Model.PuzzleDB;

import java.io.File;

public class GameController {
    public static final int FIRST_ROOM = 1;

    /**
     * Method : start
     * Checks to see if the DB field exists and if not creates it
     * by calling GameDBCreate buildTables().
     */
    public void startDB() throws GameException {
        File dbFile = new File("billy.db");
        if (!dbFile.exists()) {
            CreateFilesController cfc = new CreateFilesController();
            cfc.createFile("billy.db");
        }
    }


    public String executeCommand(String cmd) throws GameException {
        Commands commands = new Commands();
       return commands.executeCommand(cmd);
    }

    /**
     method: checkPreconditionSpecialAction
     Purpose: use this method precondition is when player did a special action and puzzle room
     check puzzle state and play/reward for action type puzzle
     */
    public String checkSpecialAction() throws GameException {
        PuzzleCommand puzzleCommand = new PuzzleCommand();
        Character character = new Character();
        Character characterData = character.getCharacter();
        int roomID = characterData.getCharacterCurrentRoomID();
        String actionName = characterData.getLastUsedName();
        String valid = puzzleCommand.getPuzzleState(roomID, actionName);
        if(valid.equals("valid")){
            return puzzleCommand.getPuzzleDescriptionData(roomID);
        }
        return "";
    }

    /**
     method: getChoiceContext-specialAction puzzle
     Purpose: check player choice and display reply(Choice content)
     */
    public String replyPlayerChoiceData_SpecialAction (String playerChoice) throws GameException {
        PuzzleCommand puzzleCommand = new PuzzleCommand();
        Character character = new Character();
        Character characterData = character.getCharacter();
        int roomID = characterData.getCharacterCurrentRoomID();
        String actionName = characterData.getLastUsedName();
        Puzzle puzzle = new Puzzle();
        Puzzle puzzleData = puzzle.getPuzzle(roomID);
        String solution = puzzleData.getPuzzleSolution();
        String valid = puzzleCommand.getPuzzleState(roomID, actionName);
        if(roomID == 6 || roomID == 11 || roomID == 13) {
            for (String i : puzzleCommand.getAllPlayerChoice(roomID)) {
                if (i.equals(playerChoice) && valid.equals("valid")) {
                    if (solution.equals(playerChoice)) {
                        return isCorrectChoice(playerChoice);
                    }
                    return isIncorrectChoice(playerChoice);
                }
            }
        }
        else if(roomID == 23 && valid.equals("valid")) {
            return puzzleCommand.checkLetterInput(playerChoice);
        }
        if(valid.equals("valid")) {
            return puzzleCommand.getChoiceContent("Invalid", 0);
        }
        return "";
    }

    /**
     method: isChoiceCorrect
     Purpose: check player's input if correct after process
     */
    public String isCorrectChoice (String playerChoice) throws GameException {
        PuzzleCommand puzzleCommand = new PuzzleCommand();
        Character character = new Character();
        Character characterData = character.getCharacter();
        int roomID = characterData.getCharacterCurrentRoomID();
        PuzzleDB puzzleDB = new PuzzleDB();
        if(roomID == 6){
            puzzleDB.updatePuzzle(characterData);
            puzzleDB.updatePhase(characterData);
            PuzzleReward puzzleReward = new PuzzleReward();
            PuzzleReward puzzleRewardData = puzzleReward.getPuzzleReward(roomID);
            if(characterData.getCharacterCurrentHP() + puzzleRewardData.getHP() >= characterData.getCharacterMaxHP() + puzzleRewardData.getMaxHP() ) {
                characterData.setCharacterCurrentHP(characterData.getCharacterMaxHP() + characterData.getCharacterMaxHP());
            }
            else{
                characterData.setCharacterCurrentHP(characterData.getCharacterCurrentHP() + puzzleRewardData.getHP());
            }
            characterData.setCharacterMaxHP(characterData.getCharacterMaxHP() + puzzleRewardData.getMaxHP());
            characterData.setCharacterAP(characterData.getCharacterAP() + puzzleRewardData.getAP());
            puzzleReward.updateCharacter(characterData);
            return puzzleCommand.getChoiceContent(playerChoice, roomID);
        }
        else if (roomID == 11) {
            puzzleDB.updatePuzzle(characterData);
            puzzleDB.updatePhase(characterData);
            return puzzleCommand.getChoiceContent(playerChoice, roomID);
        }
        else if (roomID == 13) {
            puzzleDB.updatePuzzle(characterData);
            puzzleDB.updatePhase(characterData);
            //add Item to character inventory method
            return puzzleCommand.getChoiceContent(playerChoice, roomID);
        }
        return "";
    }

    /**
     method: isChoiceIncorrect
     Purpose: check player's input if incorrect after process
     */
    public String isIncorrectChoice (String playerChoice) throws GameException {
        PuzzleCommand puzzleCommand = new PuzzleCommand();
        Character character = new Character();
        Character characterData = character.getCharacter();
        int roomID = characterData.getCharacterCurrentRoomID();
        PuzzleDB puzzleDB = new PuzzleDB();
        if(roomID == 6){
            puzzleDB.updatePhase(characterData);
            return puzzleCommand.getChoiceContent(playerChoice, roomID);
        }
        else if (roomID == 11) {
            //player HP will be 0, when display message after game over
            puzzleDB.updatePhase(characterData);
            return puzzleCommand.getChoiceContent(playerChoice, roomID);
        }
        else if (roomID == 13) {
            //remove special action
            puzzleDB.updatePhase(characterData);
            return puzzleCommand.getChoiceContent(playerChoice, roomID);
        }
        return "";
    }

    /**
     method: checkPreconditionItem
     Purpose: use this method precondition is when player used an item and puzzle room
     check puzzle state and play/reward for item type puzzle
     */
    public String checkItemAction() throws GameException {
        PuzzleCommand puzzleCommand = new PuzzleCommand();
        Character character = new Character();
        Character characterData = character.getCharacter();
        PuzzleCollider puzzleCollider = new PuzzleCollider();
        int roomID = characterData.getCharacterCurrentRoomID();
        String itemName = characterData.getLastUsedName();
        PuzzleReward puzzleReward = new PuzzleReward();
        PuzzleReward puzzleRewardData = puzzleReward.getPuzzleReward(roomID);
        String valid = puzzleCommand.getPuzzleState(roomID, itemName);
        String target = puzzleRewardData.getTarget();
        if(valid.equals("valid") && target.equals("Character")){
            if(characterData.getCharacterCurrentHP() + puzzleRewardData.getHP() >= characterData.getCharacterMaxHP() + puzzleRewardData.getMaxHP()) {
                characterData.setCharacterCurrentHP(characterData.getCharacterMaxHP() + puzzleRewardData.getMaxHP());
            }
            else{
                characterData.setCharacterCurrentHP(characterData.getCharacterCurrentHP() + puzzleRewardData.getHP());
            }
            characterData.setCharacterMaxHP(characterData.getCharacterMaxHP() + puzzleRewardData.getMaxHP());
            characterData.setCharacterAP(characterData.getCharacterAP() + puzzleRewardData.getAP());
            puzzleReward.updateCharacter(characterData);
            puzzleCollider.updatePhase(characterData);
            puzzleCollider.updatePuzzle(characterData);
            return puzzleCommand.getChoiceContent(itemName, roomID);
        }
        return "";
    }

    /**
     method: checkPreconditionSpell
     Purpose: use this method precondition is when player cast a spell and puzzle room
     check puzzle state and play/reward for spell type puzzle
     */
    public String checkSpellPuzzle() throws GameException {
        PuzzleCommand puzzleCommand = new PuzzleCommand();
        Character character = new Character();
        Character characterData = character.getCharacter();
        PuzzleCollider puzzleCollider = new PuzzleCollider();
        int roomID = characterData.getCharacterCurrentRoomID();
        String spellName = characterData.getLastUsedName();
        String valid = puzzleCommand.getPuzzleState(roomID, spellName);
        Boolean learned = true;
        //get spellName;
        //Spell spell = new Spell;
        //Spell calss's getIsLearned();
        if(roomID == 24) {
            if (learned && valid.equals("valid")) {
                puzzleCollider.updatePhase(characterData);
                puzzleCollider.updatePuzzle(characterData);
                return puzzleCommand.getChoiceContent("Learned", roomID);
            } else if (!learned && valid.equals("valid")) {
                puzzleCollider.updatePhase(characterData);
                return puzzleCommand.getChoiceContent("Not Learned", roomID);
            }
            puzzleCollider.updatePhase(characterData);
            return puzzleCommand.getChoiceContent("Wrong Spell", roomID);
        }
        else if(roomID == 31 && valid.equals("valid") && learned) {
            puzzleCollider.updatePuzzle(characterData);
            return puzzleCommand.getPuzzleDescriptionData(roomID);
        }
        return "";
    }
}
