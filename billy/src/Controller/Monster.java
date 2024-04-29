package Controller;

import GameExceptions.GameException;
import Model.MonsterDB;

import java.util.ArrayList;

public class Monster {
    private int monsterID;
    private String monsterName;
    private String monsterCategory;
    private double monsterCurrentHP;
    private double monsterMaxHP;
    private double monsterDP;
    private double monsterAP;
    private int monsterBossPhase;
    private int monsterIsDefeated;

    /**
     * Constructor: Monster
     */
    public Monster() throws GameException {
        MonsterDB mdb = new MonsterDB();
        //monsterID = mdb.getNextMonsterID();
    }

    /**
     * Method: getMonster
     * Purpose: Gets a specified monster from the Monster table
     * @param id
     * @return Monster
     * @throws GameException
     */
    public Monster getMonster(int id) throws GameException {
        MonsterDB mdb = new MonsterDB();
        return mdb.getMonster(id);
    }

    /**
     * Method: getAllMonsters
     * Purpose: gets all monsters from the Monster table
     * @return ArrayList<Monster>
     * @throws GameException
     */
    public ArrayList<Monster> getAllMonsters() throws GameException {
        MonsterDB mdb = new MonsterDB();
        return mdb.getAllMonsters();
    }

    /**
     * Method: getMonsterID
     * @return the monsterID
     */
    public int getMonsterID() {
        return monsterID;
    }

    /**
     * Method: setMonsterID
     *
     * @param monsterID the monsterID to set
     */
    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    /**
     * Method: getMonsterName
     * @return the monsterName
     */
    public String getMonsterName() {
        return monsterName;
    }

    /**
     * Method: setMonsterName
     * @param monsterName the monsterName to set
     */
    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    /**
     * Method: getMonsterCategory
     * @return the monsterCategory
     */
    public String getMonsterCategory() {
        return monsterCategory;
    }

    /**
     * Method: setMonsterCategory
     *
     * @param monsterCategory the monsterCategory to set
     */
    public void setMonsterCategory(String monsterCategory) {
        this.monsterCategory = monsterCategory;
    }

    /**
     * Method: getMonsterCurrentHP
     * @return the monsterCurrentHP
     */
    public double getMonsterCurrentHP() {
        return monsterCurrentHP;
    }

    /**
     * Method: setMonsterCurrentHP
     *
     * @param monsterCurrentHP the monsterCurrentHP to set
     */
    public void setMonsterCurrentHP(double monsterCurrentHP) {
        this.monsterCurrentHP = monsterCurrentHP;
    }

    /**
     * Method: getMonsterMaxHP
     * @return the monsterMaxHP
     */
    public double getMonsterMaxHP() {
        return monsterMaxHP;
    }

    /**
     * Method: setMonsterMaxHP
     *
     * @param monsterMaxHP the monsterMaxHP to set
     */
    public void setMonsterMaxHP(double monsterMaxHP) {
        this.monsterMaxHP = monsterMaxHP;
    }

    /**
     * Method: getMonsterDP
     * @return the monsterDP
     */
    public double getMonsterDP() {
        return monsterDP;
    }

    /**
     * Method: setMonsterDP
     *
     * @param monsterDP the monsterDP to set
     */
    public void setMonsterDP(double monsterDP) {
        this.monsterDP = monsterDP;
    }

    /**
     * Method: getMonsterAP
     * @return the monsterAP
     */
    public double getMonsterAP() {
        return monsterAP;
    }

    /**
     * Method: setMonsterAP
     *
     * @param monsterAP the monsterAP to set
     */
    public void setMonsterAP(double monsterAP) {
        this.monsterAP = monsterAP;
    }

    /**
     * Method: getMonsterBossPhase
     * @return the monsterBossPhase
     */
    public int getMonsterBossPhase() {
        return monsterBossPhase;
    }

    /**
     * Method: setMonsterBossPhase
     *
     * @param monsterBossPhase the monsterBossPhase to set
     */
    public void setMonsterBossPhase(int monsterBossPhase) {
        this.monsterBossPhase = monsterBossPhase;
    }

    /**
     * Method: getMonsterIsDefeated
     * @return the monsterIsDefeated
     */
    public int getMonsterIsDefeated() {
        return monsterIsDefeated;
    }

    /**
     * Method: setMonsterIsDefeated
     *
     * @param monsterIsDefeated the monsterIsDefeated to set
     */
    public void setMonsterIsDefeated(int monsterIsDefeated) {
        this.monsterIsDefeated = monsterIsDefeated;
    }

    /**
     * Method: toString
     * Purpose: Returns a String of the Monster class
     * @return
     */
    @Override
    public String toString() {
        return "Monster monster ID = " + monsterID + "\nmonster Name = " + monsterName + "\nmonster Category = "
                + monsterCategory + "\nmonster current HP = " + monsterCurrentHP + "\nmonster max HP = " + monsterMaxHP + "\nmonster DP = "
                + monsterDP + "\nmonster AP = " + monsterAP + "\nmonster boss phase = " + monsterBossPhase + "\nmonster is defeated = " + monsterIsDefeated;
    }
}
