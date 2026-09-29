package bookClub;

import java.io.Serializable;

public class Meeting implements Serializable{
	private static final long serialVersionUID = 1L;
	public static int MAX_USERS = 10;
	private int id;
	private boolean active;
	private User[] users;
	private User[] admins;
	private Book book;
	
	/**
	 * Constructor for the meeting class
	 * @param active whether the meeting is currently active
	 */
	public Meeting(int id, boolean active, Book book) {
		this.id = id;
		this.active = active;
		this.book = book;
		this.users = new User[MAX_USERS];
		this.admins = new User[MAX_USERS];
	}
	
	/**
	 * 
	 * @return unique meeting ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @return True if the meeting is currently active, False if not
	 */
	public boolean active() {
		return active;
	}
	
	/**
	 * 
	 * @return the book associated with this meeting
	 */
	public Book getBook() {
		return book;
	}
	
	/**
	 * 
	 * @return an array of all users registerd for meeting
	 */
	public User[] getUsers() {
		return users;
	}
	
	/**
	 * 
	 * @return an array of all admin users for this meeting
	 */
	public User[] getAdmins() {
		return admins;
	}
	
	/**
	 * 
	 * @param active: is the room active or not
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * set the meeting to active to open it
	 */
	public void startMeeting() {
		active = true;
	}
	
	/**
	 * set the meeting to inactive to close it
	 */
	public void endMeeting() {
		active = false;
	}
}
