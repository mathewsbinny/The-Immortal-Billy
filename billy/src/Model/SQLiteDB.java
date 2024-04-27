package Model;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDB extends DB{

    public SQLiteDB() throws java.sql.SQLException, ClassNotFoundException {
        sJdbc = "jdbc:sqlite";
        sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        sDbUrl = sJdbc + ":" + dbName;
        conn = DriverManager.getConnection(sDbUrl);
    }

    public SQLiteDB(String dbName) throws java.sql.SQLException, ClassNotFoundException {
        sJdbc = "jdbc:sqlite";
        sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        sDbUrl = sJdbc + ":" + dbName;
        this.dbName = dbName;
        conn = DriverManager.getConnection(sDbUrl);
    }

    public void close() throws SQLException {
        conn.close();
    }


}
