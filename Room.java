package bookClub;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Room {
	private int id;
	private ZonedDateTime start;
	private ZonedDateTime end;
	private Book book; //placeholder book will probably be a object
	private ArrayList<User> activeUsers;
	private int numUsers;
	private ArrayList<Comment> comments;
	private Meeting meeting;
	
	/**
	 * Constructor for room class
	 * @param id unique room id
	 * @param start start time
	 * @param end end time
	 * @param book book being discussed
	 * @param meeting meeting attached to the room
	 */
	public Room(int id, ZonedDateTime start, ZonedDateTime end, Book book, Meeting meeting) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.book = book;
		this.meeting = meeting;
		this.comments = new ArrayList<Comment>();
		this.activeUsers = new ArrayList<User>();
		numUsers = 0;
	}
	
	/**
	 * 
	 * @return unique room ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @return start time that the room will open
	 */
	public ZonedDateTime getStartTime() {
		return start;
	}
	
	/**
	 * 
	 * @return end time that the room will close
	 */
	public ZonedDateTime getEndTime() {
		return end;
	}
	
	/**
	 * 
	 * @return the book discussed in this room
	 */
	public Book getBook() {
		return book;
	}
	
	/**
	 * 
	 * @return list of users active in the room
	 */
	public ArrayList<User> getActiveUsers() {
		return activeUsers;
	}
	
	/**
	 * 
	 * @return list of comments posted in the room
	 */
	public ArrayList<Comment> getComments(){
		return comments;
	}
	
	/**
	 * 
	 * @return meeting associated with the room
	 */
	public Meeting getMeeting() {
		return meeting;
	}
	
	/**
	 * 
	 * @return the number of active users
	 */ 
	public int getNumUsers() {
		return numUsers;
	}
	
	/**
	 * 
	 * @param start - new start time for the room to open
	 */
	public void setStartTime(ZonedDateTime start) {
		this.start = start;
	}
	
	/**
	 * 
	 * @param end - new end time for the room to close
	 */
	public void setEndTime(ZonedDateTime end) {
		this.end = end;
	}
	
	/**
	 * 
	 * @param book - new book associated with this room
	 */
	public void setBook(Book book) {
		this.book = book;
	}
	
	/**
	 * 
	 * @param user to be added to the room
	 * @return true if user was added successfully
	 */
	public boolean join(User user) {
		if(numUsers == meeting.MAX_USERS) {
			return false;
		}
		activeUsers.add(user);
		numUsers++;
		return true;
	}
	
	/**
	 * 
	 * @param user to be removed from active room users
	 */
	public void leave(User user) {
		activeUsers.remove(user);
		numUsers--;
	}
	
	/**
	 * 
	 * @param comment to be posted in the room
	 */
	public void post(Comment comment) {
		comments.add(comment);
	}
	
	/**
	 * 
	 * @param comment to be removed from the room
	 */
	public void removeComment(Comment comment) {
		comments.remove(comment);
	}
}
