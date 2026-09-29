package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import bookClub.Room;


public class Queries {
	
	static Statement statement;
	
	//NOTES:
	//
	//TO INSERT, UPDATE, USE, DROP, DELETE use statement.executeUpdate();
	//to query using SELECT WHERE FROM use statement.executeQuery();
	//
	
	public Queries(Statement statement) {
		this.statement = statement;
		try {
			this.statement.executeUpdate("USE `BookDB`;");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
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
}
