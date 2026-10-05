// Authors: Ethan and Rachal

// Imports
package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
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
	
	// Retrieve a single Book object
	public static Book getBook(int bookID) {
		// Book object and query
		Book book = null;
		String query = "SELECT book_id, isbn, title, author_name FROM Book Where book_id = " + bookID;
		
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
	
	// Retrieve all Book objects
	public static ArrayList<Book> getBooks() {
		// Arraylist of books and query
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT book_id, isbn, title, author_name FROM Book";
		
		// Try to execute query and find all books in database
		try {
			ResultSet result = statement.executeQuery(query);
			
			// Create new Book object from database info
			while(result.next()) {
				Book book = new Book(
						result.getInt("book_id"),
						result.getString("isbn"),
						result.getString("title"),
						result.getString("author_name"));
				
				books.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return books;	
	}
	
	// Retrieve active rooms
    public static ArrayList<Room> getActiveRooms() {
    	ArrayList<Room> activeRooms = new ArrayList<>();
    	try {
			String time = Instant.now().toString().replace('T', ' ').substring(0, 19);
	    	String query = "SELECT r.room_id, r.start_time, r.end_time, "
            + "b.book_id, b.isbn, b.title, b.author_name "
            + "FROM Room r "
            + "JOIN Book b ON r.book_id = b.book_id "
            + "WHERE r.end_time IS NULL OR r.end_time > '" + time + "'";
	    	ResultSet result = statement.executeQuery(query);
	    	System.out.println();
	    	while(result.next()) {
	    		int roomID = result.getInt("room_id");
	    		//int bookID = result.getInt("book_id");
	    		ZonedDateTime startTime = result.getTimestamp("start_time").toInstant().atZone(ZoneId.of("UTC"));
	    		Timestamp endTimestamp = result.getTimestamp("end_time");
	    		ZonedDateTime endTime = (endTimestamp == null) ? null : endTimestamp.toInstant().atZone(ZoneId.of("UTC"));
//	    		try {
//	    			endTime = result.getTimestamp("end_time").toInstant().atZone(ZoneId.of("UTC"));
//	    		} catch (NullPointerException npe) { }
//	    		Book book = getBook(bookID);
//	    		if (book != null) {
//	    		}
	    		Book book = new Book(result.getInt("book_id"), result.getString("isbn"), result.getString("title"), result.getString("author_name"));
	    		System.out.println("adding new room");
	    		activeRooms.add(new Room(roomID, startTime, endTime, book, null));
	    	}
	    			
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    	return activeRooms;
    }
    
    // Retrieve once specific room by ID
	public static Room getRoom(int roomID) {
		Room room = null;
	    int bookID;
	    ZonedDateTime startTime;
	    ZonedDateTime endTime;
	    boolean meetingActive;
	
	    try {
	        String query = "SELECT * FROM Room WHERE room_id = " + roomID;
	        ResultSet result = statement.executeQuery(query);
	
	        if (!result.next()) {
	            result.close();
	            return null;
	        }
	
	        // Get all values before running another query
	        bookID = result.getInt("book_id");
	
	        startTime = result.getTimestamp("start_time")
	            .toInstant().atZone(ZoneId.of("UTC"));
	
	        java.sql.Timestamp endTimestamp = result.getTimestamp("end_time");
	        endTime = (endTimestamp == null)
	            ? null
	            : endTimestamp.toInstant().atZone(ZoneId.of("UTC"));
	
	        meetingActive = (endTime == null);
	
	        result.close();
	
	        Book book = getBook(bookID);
	
	        if (book == null) {
	            return null;
	        }
	
	        Meeting meeting = new Meeting(roomID, meetingActive, book);
	        room = new Room(roomID, startTime, endTime, book, meeting);
	
	        room.getActiveUsers().addAll(getUsersInRoom(roomID));
	        room.getComments().addAll(getCommentsByRoom(roomID, room));
	
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

            int rowsInserted = ps.executeUpdate();

            ps.close();

            return rowsInserted == 1;

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

                String name = String.join(" ", Objects.toString(firstName, ""), Objects.toString(lastName, "")).trim();

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
    
    // Add user to database
    public static boolean addUser(String username, String firstName, String lastName, String password) {
    	// Query to insert user into User table
    	String query = "INSERT INTO User (username, first_name, last_name, password) "
    			+ "VALUES (?, ?, ?, ?)";
    	
    	// Try to insert user into User table
    	try {
    		PreparedStatement ps = statement.getConnection().prepareStatement(query);
    		ps.setString(1, username);
    		ps.setString(2, firstName);
    		ps.setString(3, lastName);
    		ps.setString(4, password);
    		
    		int rowsInserted = ps.executeUpdate();
    		ps.close();
    		
    		return rowsInserted > 0;
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    		return false;
    	}
    	
    }
    
    // Get user by username
    public static User getUser(String username) {
    	// Query
    	String query = "SELECT user_id, username, first_name, last_name, password "
    			+ "FROM User WHERE username = ?";
    	
    	// Try to execute query
    	try {
    		PreparedStatement ps = statement.getConnection().prepareStatement(query);
    		ps.setString(1, username);
    		
    		ResultSet result = ps.executeQuery();
    		if (result.next()) {
    			int id = result.getInt("user_id");
    			String firstName = result.getString("first_name");
    			String lastName = result.getString("last_name");
    			String password = result.getString("password");
    			
    			String name = String.join(" ", Objects.toString(firstName, ""), Objects.toString(lastName, "")).trim();
    			
    			User user = new User(id, name, username, password);
    			return user;
    		}
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    	return null;
    }
    
    // Add user to room
    public static boolean addUserToRoom(int roomID, int userID) {
    	// Query
    	String query = "INSERT INTO Meeting (room_id, user_id) VALUES (?, ?)";
    	
    	// Try to execute query
    	try {
    		PreparedStatement ps = statement.getConnection().prepareStatement(query);
    		ps.setInt(1, roomID);
    		ps.setInt(2, userID);
    		
    		int rowsInserted = ps.executeUpdate();
    		ps.close();
    		
    		return rowsInserted > 0;
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    		return false;
    	}
    }
    
    // Remove user from room
    public static boolean removeUserFromRoom(int roomID, int userID) {
    	// Query
    	String query = "DELETE FROM Meeting WHERE room_id = ? AND user_id = ?";
    	
    	try {
    		PreparedStatement ps = statement.getConnection().prepareStatement(query);
    		ps.setInt(1, roomID);
    		ps.setInt(2, userID);
    		
    		int rowsDeleted = ps.executeUpdate();
    		ps.close();
    		
    		return rowsDeleted > 0;
    	} catch (SQLException sqle) {
    		sqle.printStackTrace();
    		return false;
    	}
    }
    
    public static int findOrCreateBook(String title, String author) {
      try {
          PreparedStatement selectBookStatement = statement.getConnection().prepareStatement("SELECT book_id FROM Book WHERE title = ?");
          selectBookStatement.setString(1, title);
          ResultSet resultSet = selectBookStatement.executeQuery();
          if (resultSet.next()) {
              int bookID = resultSet.getInt("book_id");
              selectBookStatement.close();
              return bookID;
          }
          selectBookStatement.close();

          PreparedStatement addBookStatement = statement.getConnection().prepareStatement("INSERT INTO Book (isbn, title, author_name) VALUES ('', ?, ?)", Statement.RETURN_GENERATED_KEYS);
          addBookStatement.setString(1, title);
          addBookStatement.setString(2, author);
          addBookStatement.executeUpdate();

          ResultSet keys = addBookStatement.getGeneratedKeys();
          int newID = keys.next() ? keys.getInt(1) : -1;
          addBookStatement.close();
          return newID;
      } catch (Exception e) {
          e.printStackTrace();
          return -1;
      }
  }

  public static int addRoom(int bookID, String startTime, String endTime) {
      try {
          PreparedStatement addRoomStatment = statement.getConnection().prepareStatement("INSERT INTO Room (start_time, end_time, book_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
          addRoomStatment.setString(1, startTime);
          
          if (endTime == null || endTime.isBlank()) addRoomStatment.setNull(2, Types.TIMESTAMP);
          else addRoomStatment.setString(2, endTime);
          
          addRoomStatment.setInt(3, bookID);
          addRoomStatment.executeUpdate();
          
          ResultSet keys = addRoomStatment.getGeneratedKeys();
          int roomID = keys.next() ? keys.getInt(1) : -1;
          addRoomStatment.close();
          
          return roomID;
      } catch (SQLException e) {
          e.printStackTrace();
          return -1;
      }
  }
}
