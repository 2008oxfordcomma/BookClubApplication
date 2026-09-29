// Author: Rachal Thornton
// Desc: A basic client to connect to the Book Club Application server
package bookClub;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BCClient {
	
    public static void main(String[] args) throws ClassNotFoundException {
        // Try to connect client to server via host and port
        try {
            try (Socket socket = new Socket("199.17.161.90", 9090);
                    Scanner scanner = new Scanner(System.in)) {

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("GAR");
                ArrayList<Room> activeRooms = (ArrayList<Room>) in.readObject();
                System.out.println("###-------Room(s)-------###\n" + activeRooms);
                out.println("QUIT");
                socket.close();  
            }

        } catch (IOException e) {
            System.err.println("Could not connect client to Book Club Server.");
            e.printStackTrace();
        }
    }
}
