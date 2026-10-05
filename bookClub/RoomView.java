/* Primary Author: Dre Harm
 * 9/29/26
 */

package bookClub;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class RoomView extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String SERVER_HOST = "199.17.161.90";
	private static final int SERVER_PORT = 9090;

	private final Bookworm bookworm;
	private Room room; // I think the room will need to be replaced on every server push
	private final User user;

	private JTextArea chatArea;
	private JTextField textMessage;
	private JButton buttonPost;
	private JButton buttonPass;
	private DefaultListModel<String> usersModel;
	private JList<String> userList;
	private JLabel labelTurn;

	private Socket socket;
	private PrintWriter outServer;
	private ObjectInputStream inServer;
	private Thread listenerThread;

	// I'll need to modify this later, but for testing, it connects to room #1 as a guest
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Bookworm bw = new Bookworm();
			bw.guestUser();
			Room room = new Room(1, java.time.ZonedDateTime.now(), java.time.ZonedDateTime.now().plusHours(1), new Book(0,"0","a book","an author"), new Meeting(1, true, null));
			new RoomView(bw, room).setVisible(true);
		});
	}

	public RoomView(Bookworm bookworm, Room room) {
		this.bookworm = bookworm;
		this.room = room;
		this.user = bookworm.getUser();

		setupGUI();
		connectToRoom();
	}

	private void setupGUI() {
		setTitle("Bookworm - Room " + room.getID());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(250, 250, 1000, 700);
		setResizable(false);

		JPanel contentPane = new JPanel(new BorderLayout(0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel(new BorderLayout(0, 0));
		topPanel.add(new JLabel("Bookworm"), BorderLayout.WEST);

		// VVVV This got fixed so it doesn't matter that much VVVV
		// I'm not sure what was happening with the book either being an ID or an
		// object, so since Room.getBook() may be null right now, it's falling back to the ID.
		String bookInfo = (room.getBook() != null) ? room.getBook().getTitle() + "  by  " + room.getBook().getAuthor() : "Room #" + room.getID();
		topPanel.add(new JLabel(bookInfo), BorderLayout.CENTER);

		String who = (user != null && user.isLoggedIn()) ? user.username() : "Guest";
		topPanel.add(new JLabel("Hello " + who + "!"), BorderLayout.EAST);
		contentPane.add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout(5, 0));
		contentPane.add(centerPanel, BorderLayout.CENTER);

		JPanel chatPanel = new JPanel(new BorderLayout(0, 5));
		chatPanel.setBorder(new TitledBorder("Comments"));

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

		JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
		textMessage = new JTextField();
		buttonPost = new JButton("Post");
		buttonPass = new JButton("Pass");
		inputPanel.add(buttonPass, BorderLayout.WEST);
		inputPanel.add(textMessage, BorderLayout.CENTER);
		inputPanel.add(buttonPost, BorderLayout.EAST);
		chatPanel.add(inputPanel, BorderLayout.SOUTH);

		centerPanel.add(chatPanel, BorderLayout.CENTER);

		JPanel usersPanel = new JPanel(new BorderLayout(0, 5));
		usersPanel.setBorder(new TitledBorder("In the room"));
		usersPanel.setPreferredSize(new Dimension(200, 0));

		usersModel = new DefaultListModel<>();
		userList = new JList<>(usersModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

		labelTurn = new JLabel("Turn: -");
		usersPanel.add(labelTurn, BorderLayout.SOUTH);

		centerPanel.add(usersPanel, BorderLayout.EAST);

		refreshUserList();
		refreshCommentArea();
		updateInputState();

		buttonPost.addActionListener(e -> postComment());
		textMessage.addActionListener(e -> postComment());
		buttonPass.addActionListener(e -> passTurn());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});
	}

	// in theory this should work, but I'm kind just guessing until we get actual registered users in
	/**
	 * {@summary redraws the user panel on the right side of the window based on room.getActiveUsers()} 
	 */
	private void refreshUserList() {
		usersModel.clear();
		ArrayList<User> users = room.getActiveUsers();
		User turn = room.getCurrentTurn();
		Meeting meeting = room.getMeeting(); // just to check who the moderator is

		if (users != null) {
			for (User user : users) {
				if (user == null) continue; // skip the blank spaces in the array
				
				// gets the username then tacks on mod or current turn label before displaying
				String label = user.username();
				if (isModerator(user, meeting)) label += "  (mod)"; // I also added a tag to show who the moderators are in the room
				if (turn != null && turn.equals(user)) label += " <"; // we can change this to whatever looks best, but I wanted a way to show whose turn it is
				usersModel.addElement(label);
			}
		}
		labelTurn.setText("Turn: " + (turn != null ? turn.username() : "-"));
	}

	/**
	 * {@summary redraws the comment box based on room.getComments() }
	 */
	private void refreshCommentArea() {
		StringBuilder stringbuilder = new StringBuilder();
		ArrayList<Comment> comments = room.getComments();
		if (comments != null) {
			for (Comment comment : comments) {
				if (comment == null) continue;
				String speaker = (comment.getUser() != null) ? comment.getUser().username() : "Guest";
				stringbuilder.append(speaker).append(": ").append(comment.getText()).append('\n');
			}
		}
		chatArea.setText(stringbuilder.toString());
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}
	
	/**
	 * {@summary enables or disables the post and pass button, and the text field }
	 */
	private void updateInputState() {
		// since I didn't want to make another view and not a lot of stuff changes between unregistered and registered users, I'm just enabling and disabling elements
		boolean loggedIn = (user != null && user.isLoggedIn());
		boolean canPost = loggedIn && (isModerator(user, room.getMeeting()) || isMyTurn());

		textMessage.setEnabled(canPost);
		buttonPost.setEnabled(canPost);
		buttonPass.setEnabled(loggedIn && isMyTurn());

		if (!loggedIn) textMessage.setToolTipText("You must login to chat");
		else if (!canPost) textMessage.setToolTipText("Wait for your turn");
		else textMessage.setToolTipText(null);
	}

	/**
	 * {@summary Returns true if room.getCurrentTurn() is not null and equals the current user.}
	 */
	private boolean isMyTurn() {
		User turn = room.getCurrentTurn();
		return turn != null && user != null && turn.equals(user);
	}

	/**
	 * {@summary returns true if any admin equals the given user.}
	 */
	private static boolean isModerator(User user, Meeting meeting) {
		if (user == null || meeting == null) return false;
		
		for (User admin : meeting.getAdmins()) {
			if (admin != null && admin.equals(user))return true;
		}
		
		return false;
	}

	/**
	 * {@summary Gets the text field and bails if empty. Otherwise it sends comment and clears the field.}
	 */
	private void postComment() {
		if (outServer == null) return;
		
		String text = textMessage.getText().trim();
		
		if (text.isEmpty()) return;
		
		outServer.println("COMMENT " + text);
		textMessage.setText("");
	}

	/**
	 * {@summary Sends PASS to the server}
	 */
	private void passTurn() {
		if (outServer == null) return;
		outServer.println("PASS");
	}

	/**
	 * {@summary Tries to do some cleanup}
	 */
	private void disconnect() {
		try {
			if (outServer != null) outServer.println("QUIT");
			if (socket != null && !socket.isClosed()) socket.close();
		} catch (Exception e) { }
	}

	/**
	 * {@summary Opens a socket and wraps the output -> PrintWriter, input -> ObjectInputStream, then sends the join line}
	 */
	private void connectToRoom() {
		try {
			socket = new Socket(SERVER_HOST, SERVER_PORT);
			outServer = new PrintWriter(socket.getOutputStream(), true);
			inServer = new ObjectInputStream(socket.getInputStream());

			// to tell the server which room we want to go in, JOIN <roomID> <userID> 
			// *note: took this from Alyssa for consistency, but I'm using userID = 0 for guests. Not sure how this will act with other rooms
			
			int UID = (user != null && user.isLoggedIn()) ? user.getID() : 0;
			outServer.println("JOIN " + room.getID() + " " + UID);

			listenerThread = new Thread(this::listen, "RoomView-" + room.getID());
			listenerThread.setDaemon(true);
			listenerThread.start();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Couldn't connect to BCServer on port " + SERVER_PORT + ":\n" + e.getMessage(), "There was a connection error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void listen() {
		try {
			while (true) {
				Object object = inServer.readObject();
				if (object instanceof Room updated) SwingUtilities.invokeLater(() -> applyRoomUpdate(updated));
				else if (object instanceof String msg) SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
			}
		} catch (Exception e) {}
	}

	/**
	 * {@summary By refreshing everything, it creates a live view}
	 */
	private void applyRoomUpdate(Room updated) {
		if (updated != null) this.room = updated;
		refreshUserList();
		refreshCommentArea();
		updateInputState();
	}
}
