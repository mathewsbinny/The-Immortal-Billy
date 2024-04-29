package Model;

import Controller.Combat;
import Controller.Monster;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: MonsterAttackDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class handles all the DB queries for MonsterAttacks
 */

public class MonsterAttackDB {

    //UNCOMMENT & EDIT THIS WHEN COMBAT IS PREPARED.
    /**
     * Method: getMonsterSpecialAttack
     * Purpose: handles db interactions to retrieve a special Monster attack
     * @param monsterID
     * @return Combat
     * @throws GameException
     *
    public Combat getMonsterSpecialAttack(int monsterID) throws GameException {
        Combat attack;
        try {
            SQLiteDB sdb = new SQLiteDB();
            attack = new Combat();
            String sql = "Select * from MonsterAttack WHERE monsterID = " + monsterID;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                attack.setAttackID(rs.getInt("attackID"));
                attack.setAttackName(rs.getString("attackName"));
                attack.setMonsterID(rs.getInt("monsterID"));
                attack.setAttackAddHP(rs.getInt("attackAddHP"));
                attack.setAttackAddAP(rs.getInt("attackAddAP"));
                attack.setAttackProbability(rs.getDouble("attackProbability"));
                attack.setAttackDescription(rs.getString("attackDescription"));
            } else {
                return null;
                throw new SQLException("MonsterAttack for " + monsterID + " not found.");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            return null;
            throw new GameException("Monster attack with monsterID " + monsterID + " not found");
        }
        return attack;
    }

    /**
     * Method: getMonsterGlobalAttack
     * Purpose: handles db interactions to retrieve generic Monster attack
     * @return Combat
     * @throws GameException
     *
    public Combat getMonsterGlobalAttack() throws GameException {
        Combat attack;
        try {
            SQLiteDB sdb = new SQLiteDB();
            attack = new Combat();
            String sql = "Select * from MonsterAttack WHERE attackID = " + 1;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                attack.setAttackID(rs.getInt("attackID"));
                attack.setAttackName(rs.getString("attackName"));
                attack.setMonsterID(rs.getInt("monsterID"));
                attack.setAttackAddHP(rs.getInt("attackAddHP"));
                attack.setAttackAddAP(rs.getInt("attackAddAP"));
                attack.setAttackProbability(rs.getDouble("attackProbability"));
                attack.setAttackDescription(rs.getString("attackDescription"));
            } else {
                throw new SQLException("GlobalMonsterAttack not found.");
            }

            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("GlobalMonsterAttack not found.");
        }
        return attack;
    }
    */
}
