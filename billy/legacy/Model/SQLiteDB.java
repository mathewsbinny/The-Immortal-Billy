package Model;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class: SQLiteDB
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860
 * Written: April 28 2024
 * This class creates the wrapper around SQLite specific initializations
 */

public class SQLiteDB extends DB{

    /** Constructor: SQLiteDB
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public SQLiteDB() throws SQLException, ClassNotFoundException {
        //Build the connection String
        sJdbc = "jdbc:sqlite";
        sDriverName = "org.sqlite.JDBC";
        // register the driver
        Class.forName(sDriverName);
        sDbUrl = sJdbc + ":" + dbName;
        conn = DriverManager.getConnection(sDbUrl);
    }

    /** Constructor: SQLiteDB
     * @param dbName
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public SQLiteDB(String dbName) throws SQLException, ClassNotFoundException {
        //Build the connection String
        sJdbc = "jdbc:sqlite";
        sDriverName = "org.sqlite.JDBC";
        // register the driver
        Class.forName(sDriverName);
        sDbUrl = sJdbc + ":" + dbName;
        this.dbName = dbName;
        conn = DriverManager.getConnection(sDbUrl);
    }

    /**
     * Method: close
     * Purpose: Close the database connection
     */
    public void close() throws SQLException {
        conn.close();
    }


}
