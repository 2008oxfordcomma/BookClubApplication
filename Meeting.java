package bookClub;

public class Meeting {
	public static int MAX_USERS = 10;
	private int id;
	private boolean active;
	private User[] users;
	private User[] admins;
	
	/**
	 * Constructor for the meeting class
	 * @param active whether the meeting is currently active
	 */
	public Meeting(int id, boolean active) {
		this.id = id;
		this.active = active;
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
