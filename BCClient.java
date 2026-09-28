// Author: Rachal Thornton
// Desc: A basic client to connect to the Book Club Application server

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BCClient {
    public static void main(String[] args) {
        // Try to connect client to server via host and port
        try {
            try (Socket socket = new Socket("199.17.161.90", 9090);
                    Scanner scanner = new Scanner(System.in)) {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Success message
                String greeting = in.readLine();
                System.out.println("Server: " + greeting);
                System.out.println("Enter command: ");

                // Read and print client command
                String command = scanner.nextLine();
                out.println(command);

                // Read and print server response
                String response = in.readLine();
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            System.err.println("Could not connect client to Book Club Server.");
            e.printStackTrace();
        }
    }
}
