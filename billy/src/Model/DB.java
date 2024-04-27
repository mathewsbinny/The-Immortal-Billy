package Model;

import Controller.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public abstract class DB {
    Account account = new Account();

    protected String dbName = account.buildPasswordCombination(); //Change to make account create DB name
    protected String sJdbc;
    protected String sDriverName;
    protected Connection conn;
    protected String sDbUrl;
    protected int timeout = 5;

    public ResultSet queryDB(String sql) throws SQLException {
        ResultSet rs = null;
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(timeout);
        rs = stmt.executeQuery(sql);
        return rs;
    }

    public boolean updateDB(String SQL) throws SQLException {
        Statement stmt = conn.createStatement();
        boolean success = stmt.execute(SQL);
        return success;
    }

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
}
