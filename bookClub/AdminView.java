//primary author: Alyssa
package bookClub;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.time.ZonedDateTime;

import javax.swing.border.EmptyBorder;

import java.awt.Component;

public class AdminView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminView frame = new AdminView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminView() {
		Bookworm bookworm = new Bookworm();
		//POPULATE TEST DATA DELETE LATER 
		bookworm.addRoom(new Room(0,ZonedDateTime.now(),ZonedDateTime.now().plusDays((long) 1.0),new Book(0,"0","a book","an author"),new Meeting(0,true,new Book(0,"0","a book","an author"))));
		bookworm.addRoom(new Room(0,ZonedDateTime.now().plusHours((long)1.0),ZonedDateTime.now().plusDays((long) 1.0),new Book(0,"0","a book","an author"),new Meeting(0,true,new Book(0,"0","a book","an author"))));
		//POPULATE TEST DATA DELETE LATER ^^^^
		setResizable(false);
		setTitle("Bookworm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 250, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Bookworm");
		panel.add(lblNewLabel);
		
		JLabel lblUserGreeting = new JLabel("Hello " + bookworm.getUser().username() + "!");
		panel.add(lblUserGreeting, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUpcomingMeetings = new JLabel("Upcoming Meetings");
		panel_2.add(lblUpcomingMeetings, BorderLayout.NORTH);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);
		
		//Set up the table with the rooms
		String [][] data = new String[bookworm.getRooms().size()][3];
		int i = 0;
		for(Room r : bookworm.getRooms()) {
			data[i][0] = r.getBook().getTitle();
			data[i][1] = r.getBook().getAuthor();
			data[i][2] = " " + r.getActiveUsers().size();
		}
		String[] headersRooms = {"Title","Author","Users"};
		
		
		//Set up the table with the meetings
		String[][] meetingsData = new String[bookworm.getFutureRooms().size()][3];
		i = 0;
		for(Room fr : bookworm.getFutureRooms()) {
			meetingsData[i][0] = fr.getBook().getTitle();
			meetingsData[i][1] = fr.getBook().getAuthor();
			meetingsData[i][2] = fr.getStartTime().toString();
		}
		String[] headersFutureRooms = {"Title","Author","Start Time"};
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.WEST);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAvailableRooms = new JLabel("Available Rooms: " + bookworm.getRooms().size());
		panel_3.add(lblAvailableRooms, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_3.add(scrollPane, BorderLayout.CENTER);

		//do not allow users to edit the data in the rows
		table = new JTable(data, headersRooms) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		
		table_1 = new JTable(meetingsData, headersFutureRooms) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		
		//handles double click on row to enter the room
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        if (e.getClickCount() == 2) {
		            int row = table.getSelectedRow();
		            if (row >= 0) {
		                Room r = bookworm.getRooms().get(row);
		                new RoomView(bookworm, r).setVisible(true);
		                AdminView.this.dispose(); //have to specify admin view otherwise it wont dispose of the window
		            }
		        }
		    }
		});
		
		scrollPane_1.setViewportView(table_1);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		//open the new meeting form
		JButton btnSchedule = new JButton("Schedule Meeting");
		panel_4.add(btnSchedule, BorderLayout.WEST);
		btnSchedule.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        Bookworm_Meetings_Form meeting = new Bookworm_Meetings_Form();
		        meeting.setVisible(true);   
		    }
		});
		
		JButton btnRemove = new JButton("Remove Meeting");
		panel_4.add(btnRemove, BorderLayout.EAST);
		scrollPane.setViewportView(table);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(50);
		panel_1.add(horizontalStrut_2, BorderLayout.CENTER);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut, BorderLayout.SOUTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut_1, BorderLayout.EAST);

	}

}
