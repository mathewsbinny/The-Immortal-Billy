package billy.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Thin helper around the SQLite JDBC driver.
 */
public final class Database {

    public static final String DRIVER = "org.sqlite.JDBC";

    private Database() {
    }

    public static void registerDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("SQLite JDBC driver (" + DRIVER + ") is not on the classpath.", e);
        }
    }

    /**
     * Opens a connection to a SQLite database file, or to a fresh in-memory
     * database when {@code dbName} is {@code ":memory:"}.
     */
    public static Connection connect(String dbName) throws SQLException {
        registerDriver();
        String url = ":memory:".equals(dbName) ? "jdbc:sqlite::memory:" : "jdbc:sqlite:" + dbName;
        return DriverManager.getConnection(url);
    }
}
