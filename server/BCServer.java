// Author: Rachal Thornton
// Desc: A basic server to connect to the MySQL database and host the Book Club Application
// Must run from root folder w/javac -cp "server/mysql-connector-j-26.7.0.jar" server/BCServer.java && java -cp ".:server/mysql-connector-j-26.7.0.jar" server.BCServer

package server;

import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BCServer {

    private static final int PORT = 9090; // Example port for BCServer, not in use yet
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"; // Local host since running on same server as
                                                                         // MySQL database
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

        // Try listening for clients
        System.out.println();
        System.out.println("Starting Book Club Server...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Success: Server is listening on port " + PORT);
            System.out.println("Waiting for client...");

            Socket clientSocket = serverSocket.accept();
            System.out.println();
            System.out.println("Success: Client connected on address: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Tell client they connected successfully
            out.println("Success: Connected to Book Club Server!");

            // Wait for client command
            String command = in.readLine();
            System.out.println("Client command: " + command);

            // Send response
            out.println("Server received command: " + command);

            // Close socket
            clientSocket.close();
            System.out.println("Client disconected.");
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
            ;
        }
    }
}