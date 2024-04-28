package Model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Controller.Room;
import GameExceptions.GameException;
import Controller.Monster;

import java.sql.SQLException;

public class MonsterDB {

    /**
     * //DEPRECATED
     * Method: getNextMonsterID
     * Purpose: Gets the id for the next monster.
     * @return int
     *
    public int getNextMonsterID() throws GameException {
        int next = 0;
        try {
            SQLiteDB sdb = null;
            try {
                sdb = new SQLiteDB();
                next = sdb.getMaxValue("monsterID", "Monster") + 1;
            } catch (ClassNotFoundException | SQLException e) {
                throw new GameException("Unable to get next monsterID.");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException ex) {
            throw new GameException("Next monsterID not found.");
        }
        return next;
    }
    */

    /**
     * Method: getMonster
     * Purpose: handles db interactions to retrieve a single Monster
     * @param id
     * @return Monster
     * @throws GameException
     */
    public Monster getMonster(int id) throws GameException {
        Monster mon;
        try {
            SQLiteDB sdb = new SQLiteDB();
            mon = new Monster();
            String sql = "Select * from Monster WHERE monsterID = " + id;
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
                throw new SQLException("Monster " + id + " not found.");
            }

            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Monster with monster number " + id + " not found");
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
        String sql = "UPDATE Monster SET monsterIsDefeated = '" + 1 + "' WHERE monsterID = " + monster.getMonsterID() ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

    /** Method: updateMonsterCurrentHP
     * This method updates Monster table in DB to track monster's current HP
     */
    public void updateMonsterCurrentHP(Monster monster) throws GameException {
        String sql = "UPDATE Monster SET monsterCurrentHP = '" + 1 + "' WHERE monsterID = " + monster.getMonsterID() ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }

}
