// Author: Rachal Thornton
// Desc: A basic server to connect to the MySQL database and host the Book Club Application
// Must run from BookClubApplication folder w/javac -cp "server/mysql-connector-j-26.7.0.jar" server/BCServer.java && java -cp ".:server/mysql-connector-j-26.7.0.jar" server.BCServer

package server;

import java.sql.*;

public class BCServer {

    private static final int PORT = 9090; // Example port for BCServer, not in use yet
    private static final String DB_URL = "jdbc:mysql://199.17.161.90:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "8OMZ0E01G0SqVI1jSysdind6485s";

    public static void main(String[] args) {
        System.out.println("Attempting to connect to MySQL server...");

        // Try to connect to MySQL server
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int timeoutInSeconds = 5;

            // Connection status
            if (conn.isValid(timeoutInSeconds)) {
                System.out.println(
                        "SUCCESS: Java Server connected to remote MySQL successfully!");
            } else {
                System.out.println("FAILED: Connection is invalid.");
            }

            // Error messages if failed to connect
        } catch (SQLException e) {
            System.err.println(
                    "CONNECTION ERROR: Could not reach MySQL server.");

            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Error Code: " + e.getErrorCode());
        }
    }
}