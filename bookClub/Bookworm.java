//Primary Author: Alyssa 
//Handles back end logic for rooms, users, etc created 9/28/26
package bookClub;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Bookworm {
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
		//~FOR TESTING DELETE LATER~
		Scanner sc = new Scanner(System.in);
		System.out.println("Username: ");
		String name = sc.nextLine();
		System.out.println("Password: ");
		String pass = sc.nextLine();
		System.out.println(bookworm.logIn(name,pass));
		bookworm.addRoom(new Room(0,ZonedDateTime.now(),ZonedDateTime.now().plusDays((long) 1.0),new Book(0,"a book","an author"),new Meeting(0,true,new Book(0,"a book","an author"))));
		for(Room r : bookworm.getRooms()) {
			System.out.println(r.getBook());
		}
		int index = sc.nextInt();
		bookworm.enterRoom(index);
		System.out.println("Your Turn! Enter a comment: ");
		String comment = sc.nextLine();
		if(bookworm.currentAuthority > 0) {
			bookworm.activeRoom.post(new Comment(0,comment,ZonedDateTime.now(),bookworm.activeRoom,bookworm.user));
		}
		System.out.println(bookworm.activeRoom.getComments());
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
		//TODO: Query the database to verify this username and password combo store as boolean
		Boolean valid = false;
		if (valid) {
			currentAuthority = 1;
			user = new User(1,"test","",""); //TODO: initialize this user with ID and info from database
			return true;
		} else {
			guestUser();
			return false;
			
		}
		
	}
	
	/**
	 * create a new user account ***NEEDS DATABASE CONNECTION***
	 * @param username for the newly created account
	 * @param password for the newly created account
	 */
	public void createAccount(int id,String username, String password) {
		user = new User(id,"",username,password);
		currentAuthority = 1;
	}
	
	/**
	 * view the rooms as a guest user
	 */
	public void guestUser() {
		user = new User();
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
