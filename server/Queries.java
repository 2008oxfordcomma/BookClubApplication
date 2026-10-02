// Authors: Ethan and Rachal
// TODO: add methods for addUser(), getUser(), addUserToRoom(), removeUserFromRoom(), etc.

// Imports
package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Objects;

import bookClub.Room;
import bookClub.Comment;
import bookClub.Meeting;
import bookClub.Book;
import bookClub.User;


public class Queries {
	
	static Statement statement;
	
	//NOTES:
	//
	//TO INSERT, UPDATE, USE, DROP, DELETE use statement.executeUpdate();
	//to query using SELECT WHERE FROM use statement.executeQuery();
	//
	
	public Queries(Statement statement) {
		Queries.statement = statement;
		try {
			Queries.statement.executeUpdate("USE `BookDB`;");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	// Retrieve a Book object
	public static Book getBook(int bookID) {
		// Book object and query
		Book book = null;
		String query = "SELECT book_id, title, author_name FROM Book Where book_id = " + bookID;
		
		// Try to execute query and find book in database
		try {
			ResultSet result = statement.executeQuery(query);
			
			// Create new Book object from database info
			while(result.next()) {
				book = new Book(
						result.getInt("book_id"),
						result.getString("isbn"),
						result.getString("title"),
						result.getString("author_name"));
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return book;	
	}
	
	// Retrieve active rooms
    public static ArrayList<Room> getActiveRooms() {
    	ArrayList<Room> activeRooms = new ArrayList<>();
    	try {
			String time = Instant.now().toString().replace('T', ' ').substring(0, 19);
	    	String query = "SELECT * FROM Room WHERE end_time IS NULL OR end_time > '" + time + "'";
	    	ResultSet result = statement.executeQuery(query);
	    	while(result.next()) {
	    		int roomID = result.getInt("room_id");
	    		ZonedDateTime startTime = result.getTimestamp("start_time").toInstant().atZone(ZoneId.of("UTC"));
	    		ZonedDateTime endTime = null;
	    		try {
	    			endTime = result.getTimestamp("end_time").toInstant().atZone(ZoneId.of("UTC"));
	    		} catch (NullPointerException npe) {}
	    		int bookID = result.getInt("book_id");
	    		activeRooms.add(new Room(roomID, startTime, endTime, bookID, null));
	    	}
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    	return activeRooms;
    }
    
    // Retrieve once specific room by ID
    public static Room getRoom(int roomID) {
    	// Room object and query
    	Room room = null;
    	String query = "SELECT room_id, start_time, end_time, book_id FROM Room WHERE room_id = " + roomID;
    	
    	// Try to execute query and find room in database
    	try {
    		ResultSet result = statement.executeQuery(query);
    		while(result.next()) {
    			int id = result.getInt("room_id");
    			int bookID = result.getInt("book_id");
    			
    			Timestamp startTimestamp = result.getTimestamp("start_time");
    			Timestamp endTimestamp = result.getTimestamp("end_time");
    			
    			ZonedDateTime startTime = startTimestamp.toInstant().atZone(ZoneId.of("UTC"));
    			ZonedDateTime endTime = null;
    			if (endTimestamp != null) {
    				endTime = endTimestamp.toInstant().atZone(ZoneId.of("UTC"));
    			}
    			
    			Book book = getBook(bookID);
    			
    			if (book != null) {
    				boolean meetingActive = endTime == null || endTime.isAfter(ZonedDateTime.now());
    				Meeting meeting  = new Meeting(id, meetingActive, book);
    				room = new Room(id, startTime, endTime, book, meeting);
    				room.getActiveUsers().addAll(getUsersInRoom(id));
    				room.getComments().addAll(getCommentsByRoom(id, room));
    			}
    		}
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    	return room;
    }
    
    // Retrieve all comments for a room
    public static ArrayList<Comment> getCommentsByRoom(int roomID, Room room) {
    	// List of comments and query
        ArrayList<Comment> comments = new ArrayList<>();
    	String query = "SELECT c.comment_id, c.text, c.time, "
    			+ "u.user_id, u.username, u.first_name, u.last_name "
    			+ "FROM Comment c "
    			+ "JOIN User u ON c.user_id = u.user_id "
    			+ "WHERE c.room_id = " + roomID + " "
    			+ "ORDER BY c.time, c.comment_id";
    	
    	// Try to execute query
    	try {
    		ResultSet result = statement.executeQuery(query);
    		while(result.next()) {
    			int commentID = result.getInt("comment_id");
    			String text = result.getString("text");
    			
    			Timestamp timestamp = result.getTimestamp("time");
    			ZonedDateTime time = timestamp.toInstant().atZone(ZoneId.of("UTC"));
    			
    			int userID = result.getInt("user_id");
    			String username = result.getString("username");
    			String firstName = result.getString("first_name");
    			String lastName = result.getString("last_name");
    			
    			String name = String.join(" ", Objects.toString(firstName, ""), Objects.toString(lastName, ""));

    			User user = new User(userID, name, username, null);
    			Comment comment = new Comment(commentID, text, time, room, user);
    			comments.add(comment);
    		}
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    	return comments;
    }
    
    // Insert comment into database
    public static boolean addComment(int roomID, int userID, String text) {

    	// Check if comment exists and is in correct format
        if (text == null || text.isBlank() || text.length() > 2000) {
            return false;
        }

        String query = "INSERT INTO Comment "
                     + "(user_id, text, time, room_id) "
                     + "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";

        // Use prepared statement to avoid sql-breaking strings
        try {
            PreparedStatement ps = statement.getConnection().prepareStatement(query);

            ps.setInt(1, userID);
            ps.setString(2, text);
            ps.setInt(3, roomID);

            int rowsAffected = ps.executeUpdate();

            ps.close();

            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all users associated with a specific room
    public static ArrayList<User> getUsersInRoom(int roomID) {
        // List of users and query
    	ArrayList<User> users = new ArrayList<>();
        String query = "SELECT u.user_id, u.username, "
                     + "u.first_name, u.last_name "
                     + "FROM Meeting m "
                     + "JOIN User u ON m.user_id = u.user_id "
                     + "WHERE m.room_id = " + roomID + " "
                     + "ORDER BY u.username";

        // Try to execute query
        try {
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int userID = result.getInt("user_id");
                String username = result.getString("username");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");

                String name = String.join(" ", Objects.toString(firstName, ""), Objects.toString(lastName, ""));

                User user = new User(userID, name.trim(), username, null);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Check if user is a moderator
    public static boolean isModerator(int userID) {
        // User is not a moderator by default
    	boolean moderator = false;
        String query = "SELECT admin_id FROM Moderator WHERE admin_id = " + userID;

        // Try to execute query
        try {
            ResultSet result = statement.executeQuery(query);
            
            // If userID is found as moderator, turn status to true
            while (result.next()) {
                moderator = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
       return moderator;
    }
}
