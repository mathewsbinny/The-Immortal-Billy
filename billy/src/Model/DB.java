package Model;

import Controller.Account;

import java.sql.Connection;



public abstract class DB {
    Account account = new Account();

    protected String dbName = account.buildPasswordCombination();
    protected String sJdbc;
    protected String sDriverName;
    protected Connection conn;
    protected String sDbUrl;
    protected int timeout = 5;
}
