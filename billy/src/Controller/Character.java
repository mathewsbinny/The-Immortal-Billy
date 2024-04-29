package Controller;

import GameExceptions.GameException;
import Model.CharacterDB;

public class Character {
    private int accountID;
    private int characterCurrentRoomID;
    private int characterID;
    private String characterName;
    private double characterCurrentHP;
    private double characterMaxHP;
    private int characterMPLevel;
    private double characterDP;
    private double characterAP;
    private int characterXP;
    private int characterLevel;
    private String lastUsedName;

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    private Room currentRoom;

    CharacterDB cdb;

    /**
     * Constructor: Character
     *
    public Character() throws GameException {
        CharacterDB cdb = new CharacterDB();
        characterID = cdb.getNextCharacterID();
    }
     */

    public int getAccountID(){
        return this.accountID;
    }

    public void setAccountID(int accountID){
        this.accountID = accountID;
    }

    public int getCharacterCurrentRoomID(){
        return characterCurrentRoomID;
    }

    public void setCharacterCurrentRoomID(int characterCurrentRoomID) {
        this.characterCurrentRoomID = characterCurrentRoomID;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public double getCharacterCurrentHP() {
        return characterCurrentHP;
    }

    public void setCharacterCurrentHP(double characterCurrentHP) {
        this.characterCurrentHP = characterCurrentHP;
    }

    public double getCharacterMaxHP() {
        return characterMaxHP;
    }

    public void setCharacterMaxHP(double characterMaxHP) {
        this.characterMaxHP = characterMaxHP;
    }

    public int getCharacterMPLevel() {
        return characterMPLevel;
    }

    public void setCharacterMPLevel(int characterMPLevel) {
        this.characterMPLevel = characterMPLevel;
    }

    public double getCharacterDP() {
        return characterDP;
    }

    public void setCharacterDP(double characterDP) {
        this.characterDP = characterDP;
    }

    public double getCharacterAP() {
        return characterAP;
    }

    public void setCharacterAP(double characterAP) {
        this.characterAP = characterAP;
    }

    public int getCharacterXP() {
        return characterXP;
    }

    public void setCharacterXP(int characterXP) {
        this.characterXP = characterXP;
    }

    public int getCharacterLevel() {
        return characterLevel;
    }

    public void setCharacterLevel(int characterLevel) {
        this.characterLevel = characterLevel;
    }

    public String getLastUsedName() {
        return lastUsedName;
    }

    public void setLastUsedName(String lastUsedName) {
        this.lastUsedName = lastUsedName;
    }

    public void updateCharacterCurrentRoom() throws GameException {
        cdb = new CharacterDB();
        cdb.updateCharacterCurrentRoom(this);
    }

    public Character getCharacter() throws GameException {
        CharacterDB characterDB = new CharacterDB();
        return characterDB.getCharacterByAccID(accountID);
    }

    /**
     * Method: toString
     * Purpose: Returns a String of the Character class
     * @return
     */
    @Override
    public String toString() {
        return "Character character ID = " + characterID + "\ncharacter Name = " + characterName + "\ncharacter current HP = "
                + characterCurrentHP + "\ncharacter max HP = " + characterMaxHP + "\ncharacter MP level = " + characterMPLevel
                + "\ncharacter DP = " + characterDP + "\ncharacter AP = " + characterAP + "\ncharacter XP = " + characterXP + "\ncharacter level = " + characterLevel;
    }
} // End Character Class
