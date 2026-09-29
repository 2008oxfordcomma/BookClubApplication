// Author: Rachal Thornton
// Desc: A basic server to connect to the MySQL database and host the Book Club Application
// Must run from root folder w/javac -cp "server/mysql-connector-j-26.7.0.jar" server/BCServer.java && java -cp ".:server/mysql-connector-j-26.7.0.jar" server.BCServer

package server;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import server.Queries;
import bookClub.User;
import bookClub.Book;
import bookClub.Room;
import bookClub.Comment;

public class BCServer {

    private static final int PORT = 9090; // Example port for BCServer, not in use yet
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"; // Local host since running on same server as
                                                                         // MySQL database
    
    private static Connection connection;
    private static Statement statement;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "8OMZ0E01G0SqVI1jSysdind6485s";
      
    
    public static void main(String[] args) throws Exception{
    	try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			statement = connection.createStatement();
			System.out.println("Successfully connected to DB");
    	} catch (SQLException sqle) {
    		System.err.println("ERROR: failed to connect to DB");
    	}
    	
    	
    	try (var srvSocket = new ServerSocket(PORT)) {
			System.out.println("BCServer is now running");
			var pool = Executors.newFixedThreadPool(6);
			while (true) {
				pool.execute(new BCClientHandler(srvSocket.accept()));
			}
		}
    }

    public static class BCClientHandler implements Runnable {
    	
    	private Socket socket;
		public BCClientHandler(Socket accept) {
			this.socket = accept;
			System.out.println("Incoming device: " + socket);
		}
		
		@Override
		public void run() {
			try {
				Scanner in = new Scanner(socket.getInputStream());
//				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				var out = new ObjectOutputStream(socket.getOutputStream());
				Queries qry = new Queries(statement);
				while(in.hasNextLine()) {
					String line = in.nextLine();
					switch(line) {
						//Get Active Rooms
						case "GAR":
						    System.out.println("Received GAR command");
						    ArrayList<Room> activeRooms = qry.getActiveRooms();
						    System.out.println("Sending Active Rooms to");
						    out.writeObject(activeRooms);
						    out.flush();
						    
						    break;
						case "QUIT":
							System.out.println("client is shutting down");
							socket.close();
						default:
							System.out.println("Unknown command");
					}
					
				}
			} catch (IOException ioe) {}
			
		}
		
		
    }
    
    
//    public static void
    	
//		// Try to connect to MySQL server
//		try {
//			
//			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//			statement = connection.createStatement();
//			
//		// Error messages if failed to connect
//		} catch (SQLException e) {
//            System.err.println(
//                    "CONNECTION ERROR: Could not reach MySQL server.");
//
//            System.err.println("Error Message: " + e.getMessage());
//            System.err.println("Error Code: " + e.getErrorCode());
//		}
//
//        // Try listening for clients
//        System.out.println("Starting Book Club Server...");
//
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Success: Server is listening on port " + PORT);
//            System.out.println("Waiting for client...");
//
//            Socket clientSocket = serverSocket.accept();
//            System.out.println();
//            System.out.println("Success: Client connected on address: " + clientSocket.getInetAddress());
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//
//            // Tell client they connected successfully
//            out.println("Success: Connected to Book Club Server!");
//
//            // Wait for client command
//            String command = in.readLine();
//            System.out.println("Client command: " + command);
//
//            // Send response
//            out.println("Server received command: " + command);
//
//            // Close socket
//            clientSocket.close();
//            System.out.println("Client disconected.");
//        } catch (IOException e) {
//            System.err.println("Server error: " + e.getMessage());
//            e.printStackTrace();
//            ;
//        }
//    }
}