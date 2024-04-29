package Model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GameExceptions.GameException;
import Controller.Monster;

import java.sql.SQLException;

/**
 * Class: MonsterDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class handles all the DB queries for Monster objects
 */

public class MonsterDB {

    /**
     * Method: getMonster
     * Purpose: handles db interactions to retrieve a single Monster
     * @param roomID
     * @return Monster
     * @throws GameException
     */
    public Monster getMonster(int roomID) throws GameException {
        Monster mon;
        try {
            SQLiteDB sdb = new SQLiteDB();
            mon = new Monster();
            String sql = "Select * from Monster WHERE monsterRoomID = " + roomID;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                mon.setMonsterID(rs.getInt("monsterNumber"));
                mon.setMonsterName(rs.getString("monsterName"));
                mon.setMonsterCategory(rs.getString("monsterCategory"));
                mon.setMonsterCurrentHP(rs.getInt("monsterCurrentHP"));
                mon.setMonsterMaxHP(rs.getInt("monsterMaxHP"));
                mon.setMonsterDP(rs.getInt("monsterDP"));
                mon.setMonsterAP(rs.getInt("monsterAP"));
                mon.setMonsterBossPhase(rs.getInt("monsterBossPhase"));
                mon.setMonsterIsDefeated(rs.getInt("monsterIsDefeated"));
            } else {
                throw new SQLException("Monster for roomID " + roomID + " not found.");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Monster with monsterRoomID " + roomID + " not found");
        }
        return mon;
    }

    /**
     * Method: getMonsterByBossPhase
     * Purpose: handles db interactions to retrieve a single Monster by it's boss phase level
     * @param phase
     * @return Monster
     * @throws GameException
     */
    public Monster getMonsterByBossPhase(int phase) throws GameException {
        Monster mon;
        try {
            SQLiteDB sdb = new SQLiteDB();
            mon = new Monster();
            String sql = "Select * from Monster WHERE monsterBossPhase = " + phase;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                mon.setMonsterID(rs.getInt("monsterNumber"));
                mon.setMonsterName(rs.getString("monsterName"));
                mon.setMonsterCategory(rs.getString("monsterCategory"));
                mon.setMonsterCurrentHP(rs.getInt("monsterCurrentHP"));
                mon.setMonsterMaxHP(rs.getInt("monsterMaxHP"));
                mon.setMonsterDP(rs.getInt("monsterDP"));
                mon.setMonsterAP(rs.getInt("monsterAP"));
                mon.setMonsterBossPhase(rs.getInt("monsterBossPhase"));
                mon.setMonsterIsDefeated(rs.getInt("monsterIsDefeated"));
            } else {
                throw new SQLException("Monster " + phase + " not found.");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Monster with monster number " + phase + " not found");
        }
        return mon;
    }

    /**
     * Method: getAllMonsters
     * Purpose: Handles the DB interactions to retrieve all monsters
     * @return ArrayList<Monster>
     * @throws GameException
     */
    public ArrayList<Monster> getAllMonsters() throws GameException {
        ArrayList<Monster> monsters = new ArrayList<Monster>();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Monster";

            ResultSet rs = sdb.queryDB(sql);

            while (rs.next()) {
                Monster mon = new Monster();
                mon.setMonsterID(rs.getInt("monsterNumber"));
                mon.setMonsterName(rs.getString("monsterName"));
                mon.setMonsterCategory(rs.getString("monsterCategory"));
                mon.setMonsterCurrentHP(rs.getInt("monsterCurrentHP"));
                mon.setMonsterMaxHP(rs.getInt("monsterMaxHP"));
                mon.setMonsterDP(rs.getInt("monsterDP"));
                mon.setMonsterAP(rs.getInt("monsterAP"));
                mon.setMonsterBossPhase(rs.getInt("monsterBossPhase"));
                mon.setMonsterIsDefeated(rs.getInt("monsterIsDefeated"));
                monsters.add(mon);
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Unable to retrieve all monsters");
        }
        return monsters;
    }

    /** Method: updateMonsterIsDefeated
     * This method updates Monster table in DB to mark a monster a defeated
     */
    public void updateMonsterIsDefeated(Monster monster) throws GameException {
        String sql = "UPDATE Monster SET monsterIsDefeated = " + 1 + " WHERE monsterID = " + monster.getMonsterID() ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateMonsterCurrentHP
     * This method updates Monster table in DB to track monster's currentHP
     */
    public void updateMonsterCurrentHP(Monster monster) throws GameException {
        String sql = "UPDATE Monster SET monsterCurrentHP = " + monster.getMonsterCurrentHP() + " WHERE monsterID = " + monster.getMonsterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: removeMonsterFromRoom
     * This method updates Monster table in DB to remove Monster from room after defeat
     */
    public void removeMonsterFromRoom(Monster monster) throws GameException {
        String sql = "UPDATE Monster SET monsterRoomID = 0 WHERE monsterID = " + monster.getMonsterID();
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
