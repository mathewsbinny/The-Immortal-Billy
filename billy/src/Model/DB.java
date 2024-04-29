package Model;

import Controller.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class: DB.java
 * @author: Mathews Binny
 * @version: 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * This class controls basic DB functionality
 * Purpose:Has Query and Update DB
 */

public abstract class DB {

    protected String dbName = "billy.db";  //account.buildPasswordCombination(); //Change to make account create DB name
    protected String sJdbc;
    protected String sDriverName;
    protected Connection conn;
    protected String sDbUrl;
    protected int timeout = 30;

    /**
     * Method: queryDB
     * Purpose: read from the database
     * @param sql
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet queryDB(String sql) throws SQLException {
        ResultSet rs = null;
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(timeout);
        rs = stmt.executeQuery(sql);
        return rs;
    }

    /**
     * Method: updateDB
     * Purpose: Updates the database
     * @param SQL
     * @return boolean
     * @throws SQLException
     */
    public boolean updateDB(String SQL) throws SQLException {
        Statement stmt = conn.createStatement();
        boolean success = stmt.execute(SQL);
        return success;
    }

    /**
     * Method: count
     * Purpose: Gets the count of records in the specified table
     * @param table
     * @return int
     */
    public int count(String table){
        int cnt = 0;
        try{
            Statement stmt = conn.createStatement();
            String sql = "select count(*) as count from \"" + table + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            cnt = rs.getInt(1);
        }catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
        return cnt;
    }

    /**
     * Method: getMaxValue
     * Purpose: Gets max value for a particular field in a particular table
     * @param columnName
     * @param table
     * @return int
     */
    public int getMaxValue(String columnName, String table){
        int max = 0;
        try{
            Statement stmt = conn.createStatement();
            String sql = "select MAX("+ columnName + ") from " + table;
            ResultSet rs = stmt.executeQuery(sql);
            max = rs.getInt(1);
        }catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
        return max;
    }

    /**
     * Method: close
     * Purpose: Close the database connection
     */
    public void close() throws java.sql.SQLException {
        conn.close();
    }
}
