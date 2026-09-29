package bookClub;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String username;
	private String password;
	private boolean loggedIn;
	
	/**
	 * Constructor for user class
	 * @param id unique user id
	 * @param name user name
	 * @param username the name that appear to other users
	 * @param password password to log in
	 */
	public User(int id, String name, String username, String password) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.loggedIn = true;
	}
	
	/**
	 * Constructor for guest users
	 */
	public User() {
		this.id = 0;
		this.name = "Guest";
		this.loggedIn = false;
	}
	
	/**
	 * check if users are equal by ID
	 */
	public boolean equals(User user) {
		if(user.getID() == this.id) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return unique user id
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @return real name of the user
	 */ 
	public String name() {
		return name;
	}
	
	/**
	 * 
	 * @return username displayed to other users
	 */
	public String username() {
		return username;
	}
	
	/**
	 * 
	 * @return user's password
	 */
	public String password() {
		return password;
	}
	
	/**
	 * 
	 * @return true if the user is logged in false if not
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	/**
	 * 
	 * @param loggedIn set users log in to true or false
	 */
	public void setLogIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		if(loggedIn == false) {
			name = "Guest";
		}
	}
}
