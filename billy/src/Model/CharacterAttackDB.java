package Model;

import Controller.Combat;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class: CharacterAttacks
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class handles all the DB queries for CharacterAttacks
 */

public class CharacterAttackDB {

    //UNCOMMENT & EDIT THIS WHEN COMBAT IS PREPARED.
    /**
     * Method: getLearnedAttacksNonBattle
     * Purpose: handles DB interactions to retrieve learned attacks for nonbattle UI
     * @return Combat
     * @throws GameException
     *
    public Combat getLearnedAttacksNonBattle() throws GameException {
    Combat attack;
    try {
    SQLiteDB sdb = new SQLiteDB();
    attack = new Combat();
    String sql = "Select * from CharacterAttack WHERE attackLearned = 1 AND attackUIType = 'Nonbattle'";
    ResultSet rs = sdb.queryDB(sql);
    if (rs.next()) {
    attack.setAttackID(rs.getInt("attackID"));
    attack.setAttackName(rs.getString("attackName"));
    attack.setAttackUIType(rs.getString("attackUIType"));
    attack.setMonsterID(rs.getInt("monsterID"));
    attack.setAttackMPLevel(rs.getInt("attackMPLevel"));
    attack.setAttackAddHP(rs.getInt("attackAddHP"));
    attack.setAttackAddAP(rs.getInt("attackAddAP"));
    attack.setAttackDescription(rs.getString("attackDescription"));
    attack.setAttackLearned(rs.getInt("attackLearned"));
    } else {
    return null;
    throw new SQLException("Learned CharacterAttacks not found.");
    }
    //Close the SQLiteDB connection since SQLite only allows one updater
    sdb.close();
    }
    catch (SQLException | ClassNotFoundException ex) {
    return null;
    throw new GameException("Learned CharacterAttacks not found.");
    }
    return attack;
    }
    */

    /**
     * Method: getLearnedAttacksBattle
     * Purpose: handles DB interactions to retrieve learned attacks for battle UI
     * @return Combat
     * @throws GameException
     *
    public Combat getLearnedAttacksBattle() throws GameException {
        Combat attack;
        try {
            SQLiteDB sdb = new SQLiteDB();
            attack = new Combat();
            String sql = "Select * from CharacterAttack WHERE attackLearned = 1 AND attackUIType = 'Battle'";
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                attack.setAttackID(rs.getInt("attackID"));
                attack.setAttackName(rs.getString("attackName"));
                attack.setAttackUIType(rs.getString("attackUIType"));
                attack.setMonsterID(rs.getInt("monsterID"));
                attack.setAttackMPLevel(rs.getInt("attackMPLevel"));
                attack.setAttackAddHP(rs.getInt("attackAddHP"));
                attack.setAttackAddAP(rs.getInt("attackAddAP"));
                attack.setAttackDescription(rs.getString("attackDescription"));
                attack.setAttackLearned(rs.getInt("attackLearned"));
            } else {
                return null;
                throw new SQLException("Learned CharacterAttacks not found.");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            return null;
            throw new GameException("Learned CharacterAttacks not found.");
        }
        return attack;
    }
    */

    /** Method: updateAttackLearned
     * This method updates CharacterAttack when player learns attack
     */
    public void updateAttackLearned(int monsterID) throws GameException {
        String sql = "UPDATE CharacterAttack SET attackLearned = 1 WHERE attackMonsterID = " + monsterID ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
}
