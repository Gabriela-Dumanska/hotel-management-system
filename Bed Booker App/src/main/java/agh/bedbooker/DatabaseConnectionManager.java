package agh.bedbooker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static Connection connection = null;
    private static final String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7711060";
    private static final String DB_USER = "sql7711060";
    private static final String DB_PASSWORD = "UR4SRQ2k1P";

    static {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database!", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
