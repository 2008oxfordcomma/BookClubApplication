package bookClub;
import java.time.ZonedDateTime;

public class Comment {
	private int id;
	private String text;
	private ZonedDateTime time;
	private Room room;
	private User user;

	/**
	 * Constructor for the Comment class
	 * @param id unique id for this comment
	 * @param text text content provided by user
	 * @param time the time the comment was posted
	 * @param room the room the comment was posted in
	 */
	public Comment(int id, String text, ZonedDateTime time, Room room,User user) {
		this.id = id;
		this.text = text;
		this.time = time;
		this.room = room;
		this.user = user;
	}
	
	/**
	 * 
	 * @return unique comment ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @return text content of the comment
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * 
	 * @return time that the comment was posted
	 */
	public ZonedDateTime getTime() {
		return time;
	}
	
	/**
	 * 
	 * @return the room the comment was posted in
	 */
	public Room getRoom() {
		return room;
	}
	
	/**
	 * 
	 * @return the user who posted this comment
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 
	 * @param new text content of the comment
	 */
	public void setText(String text) {
		this.text = text;
	}
}
