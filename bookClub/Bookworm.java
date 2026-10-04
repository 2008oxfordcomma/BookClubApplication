//Primary Author: Alyssa 
//Handles back end logic for rooms, users, etc created 9/28/26
package bookClub;

import java.awt.image.PixelInterleavedSampleModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Bookworm {
	private static final String SERVER_HOST = "199.17.161.90";
	private static final int SERVER_PORT = 9090;
	
	private ArrayList<Comment> comments; //list of comments associated with the current meeting
	private ArrayList<User> users; //list of users associated with the current meeting
	private ArrayList<Book> books; //list of books associated with rooms
	private ArrayList<Meeting> meetings; //list of meetings associated with rooms
	private ArrayList<Room> rooms; //list of possible rooms
	private ArrayList<Room> futureRooms;
	private Room activeRoom;  //room that is currently being looked at
	private int currentAuthority; //privilege level 
	private User user;  //the user associated with this client? Depends on how this works with the client
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bookworm bookworm = new Bookworm();
		boolean ok = bookworm.logIn("testuser", "testpass");
		System.out.println("Login success: " + ok);
		System.out.println("User: " + (bookworm.getUser() == null ? "null" : bookworm.getUser().username()));
		//~FOR TESTING DELETE LATER~
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Username: ");
//		String name = sc.nextLine();
//		System.out.println("Password: ");
//		String pass = sc.nextLine();
//		System.out.println(bookworm.logIn(name,pass));
//		bookworm.addRoom(new Room(0,ZonedDateTime.now(),ZonedDateTime.now().plusDays((long) 1.0),new Book(0,"0","a book","an author"),new Meeting(0,true,new Book(0,"0","a book","an author"))));
//		for(Room r : bookworm.getRooms()) {
//			System.out.println(r.getBook());
//		}
//		int index = sc.nextInt();
//		bookworm.enterRoom(index);
//		System.out.println("Your Turn! Enter a comment: ");
//		String comment = sc.nextLine();
//		if(bookworm.currentAuthority > 0) {
//			bookworm.activeRoom.post(new Comment(0,comment,ZonedDateTime.now(),bookworm.activeRoom,bookworm.user));
//		}
//		System.out.println(bookworm.activeRoom.getComments());
		//~~~~TESTING ENDS HERE~~~~
	}
	
	/**
	 * TODO: query the database to fill in the rooms
	 */
	public Bookworm() {
		activeRoom = null;
		currentAuthority = 0;
		rooms = new ArrayList<Room>();
		futureRooms = new ArrayList<Room>();
		comments = new ArrayList<Comment>();
	}
	
	/**
	 * 
	 * @return the rooms available to view
	 */
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	/**
	 * 
	 * @return the rooms available to view for the future
	 */
	public ArrayList<Room> getFutureRooms() {
		return futureRooms;
	}
	
	/**
	 * 
	 * @return the list of upcoming meetings
	 */
	public ArrayList<Meeting> getMeetings(){
		return meetings;
	}
	
	/**
	 * 
	 * @return the user for this instance
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 
	 * @return the current authority of the active user
	 */
	public int getAuthority() {
		return currentAuthority;
	}
	
	/**
	 * validate the users log in credentials, query the server to compare
	 * THIS METHOD ALWAYS RETURNS THE VALUE OF VALID RIGHT NOW FOR TESTING PURPOSES
	 * @param username
	 * @param password
	 */
	public Boolean logIn(String username, String password) {
		try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
				 ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

				out.println("LOGIN");
				out.println(username);
				out.println(password);

				User loggedIn = (User) in.readObject();
				Boolean isMod = (Boolean) in.readObject();

				out.println("QUIT");
				//TODO: Query the database to verify this username and password combo store as boolean
				if (loggedIn != null) {
					currentAuthority = (isMod != null && isMod) ? 2 : 1; 
					user = loggedIn;
					return true;
				}

			} catch (Exception e) {
				System.err.println("Couldn't log in: " + e.getMessage());
			}

			guestUser();
			return false;
	}
	
	/**
	 * create a new user account ***NEEDS DATABASE CONNECTION***
	 * @param username for the newly created account
	 * @param password for the newly created account
	 */
	public boolean createAccount(String username, String password) {
		try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
				 ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

				out.println("CREATE");
				out.println(username);
				out.println(password);

				User created = (User) in.readObject();

				out.println("QUIT");

				if (created != null) {
					user = created;
					currentAuthority = 1;
					return true;
				}

			} catch (Exception e) {
					System.err.println("Couldn't create an account: " + e.getMessage());
			}
			return false;
	}
	
	public boolean scheduleMeeting(String title, String author, String start, String end) {
				try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
						 ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					   PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					
						out.println("SCHEDULE");
						out.println(title);
						out.println(author);
						out.println(start);
						out.println(end);
						out.println(user != null ? user.getID() : 0);
						
						Boolean ok = (Boolean) in.readObject();
						out.println("QUIT");
						
						if (ok != null && ok) {
							reloadRooms();
							return true;
						}
						return false;
			  } catch (Exception e) {
			  	System.err.println("Couldn't schedule the meeting: " + e.getMessage());
					return false;
				}
	}
	
	public void reloadRooms() {
    rooms.clear();
    futureRooms.clear();

    try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

        out.println("GAR");
        Object objects = in.readObject();
        out.println("QUIT");

        if (objects instanceof ArrayList<?>) 
            for (Object object : (ArrayList<?>) objects) 
            	if (object instanceof Room) addRoom((Room) object);
                
    } catch (Exception e) {
        System.err.println("Couldn't reload the rooms: " + e.getMessage());
    }
	}
	
	/**
	 * view the rooms as a guest user
	 */
	public void guestUser() {
		user = new User();
		currentAuthority = 0;
	}
	
	/**TODO: MUST QUERY DATABASE FOR THE USERS AND COMMENTS IN THE ROOM
	 * Enter a specific room
	 * @param index index of the room the user will view
	 */
	public void enterRoom(int index) {
		activeRoom = rooms.get(index);
		User[] admins = activeRoom.getMeeting().getAdmins();
		for(User u : admins) {
			if(u != null && this.user.equals(u)) {
				currentAuthority = 2;
				break;
			}
		}
	}
	
	/**
	 * reset the active room, authority, users, and comments after leaving the room
	 */
	public void exitRoom() {
		activeRoom = null;
		users = null;
		comments = null;
		if(user.isLoggedIn()) {
			currentAuthority = 1;
		}
	}
	
	/**
	 * 
	 * @param room to be added to either active rooms or future rooms
	 */
	public void addRoom(Room room) {
		if(room.isActive()) {
			rooms.add(room);
		} else {
			futureRooms.add(room);
		}
	}
	

}
