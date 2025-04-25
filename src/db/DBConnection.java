
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;


public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/LostandFound";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Lilia_0107_Vanrouge_2601";


    private static final ConcurrentLinkedQueue<Connection> connectionPool = new ConcurrentLinkedQueue<>();
    private static final int MAX_POOL_SIZE = 10;


    static {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }


    public static Connection getConnection() {
        Connection connection = connectionPool.poll();

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Created new database connection");
            } catch (SQLException e) {
                System.err.println("Database connection error: " + e.getMessage());
                return null;
            }
        }

        try {

            if (connection != null && !connection.isValid(2)) {
                System.out.println("Connection is invalid, creating new one");
                connection.close();
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error validating connection: " + e.getMessage());
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException ex) {
                System.err.println("Failed to create new connection: " + ex.getMessage());
                return null;
            }
        }

        return connection;
    }


    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connection.isValid(2)) {
                    if (connectionPool.size() < MAX_POOL_SIZE) {
                        connectionPool.offer(connection);
                    } else {
                        connection.close();
                    }
                } else {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error releasing connection: " + e.getMessage());
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.err.println("Error closing invalid connection: " + ex.getMessage());
                }
            }
        }
    }


    public static void closeAllConnections() {
        Connection connection;
        while ((connection = connectionPool.poll()) != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        System.out.println("All database connections closed");
    }
}
