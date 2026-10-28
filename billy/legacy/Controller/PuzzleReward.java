package Controller;

import GameExceptions.GameException;
import Model.PuzzleRewardDB;

public class PuzzleReward {
    private int roomID;
    private int HP;
    private int AP;
    private int maxHP;
    private String target;

    public PuzzleReward getPuzzleReward(int roomID) throws GameException {
        PuzzleRewardDB puzzleRewardDB = new PuzzleRewardDB();
        return puzzleRewardDB.getRewardValue(roomID);
    }

    public void updateCharacter(Character character) throws GameException {
        PuzzleRewardDB puzzleRewardDB = new PuzzleRewardDB();
        puzzleRewardDB.updateCharacter(character);
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getAP() {
        return AP;
    }

    public void setAP(int AP) {
        this.AP = AP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
