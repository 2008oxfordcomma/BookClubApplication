//primary author: Benjamin
package bookClub;

import javax.swing.*;
import java.awt.*;

public class Bookworm_LoginGUI extends JFrame {

	// Components
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton createAccountButton;
	private JButton loginButton;
	private Bookworm bookworm;

	public Bookworm_LoginGUI(Bookworm bookworm) {
			this.bookworm = bookworm;
	        // -----------------------------
	        // Window Settings
	        // -----------------------------
	        setTitle("Please login to continue");
	        setSize(600, 350);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null); // Center the window

	        // Use a simple layout
	        setLayout(null);

	        // -----------------------------
	        // Username Label
	        // -----------------------------
	        usernameLabel = new JLabel("Username:");
	        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 23));
	        usernameLabel.setBounds(97, 98, 130, 35);
	        add(usernameLabel);

	        // -----------------------------
	        // Username Text Field
	        // -----------------------------
	        usernameField = new JTextField();
	        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
	        usernameField.setBounds(235, 98, 257, 27);
	        add(usernameField);

	        // -----------------------------
	        // Password Label
	        // -----------------------------
	        passwordLabel = new JLabel("Password:");
	        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 23));
	        passwordLabel.setBounds(105, 137, 125, 35);
	        add(passwordLabel);

	        // -----------------------------
	        // Password Text Field
	        // -----------------------------
	        passwordField = new JPasswordField();
	        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
	        passwordField.setBounds(235, 137, 257, 27);
	        add(passwordField);

	        // -----------------------------
	        // Create Account Button
	        // -----------------------------
	        createAccountButton = new JButton("Create account");
	        createAccountButton.setFont(new Font("Arial", Font.PLAIN, 20));
	        createAccountButton.setBounds(98, 194, 200, 32);
	        add(createAccountButton);

	        // -----------------------------
	        // Login Button
	        // -----------------------------
	        loginButton = new JButton("Login");
	        loginButton.setFont(new Font("Arial", Font.PLAIN, 20));
	        loginButton.setBounds(322, 194, 170, 32);
	        add(loginButton);

	        // -----------------------------
	        // Button Actions
	        // -----------------------------

	        loginButton.addActionListener(e -> {

	        String username = usernameField.getText();
	        String password = new String(passwordField.getPassword());

	            if (username.isEmpty() || password.isEmpty()) {
	                JOptionPane.showMessageDialog(
	                        this,
	                        "Please enter your username and password."
	                );
	            } else {
	                JOptionPane.showMessageDialog(
	                        this,
	                        "Login successful!"
	                        
	                );
	                if (bookworm.getAuthority() == 2) new AdminView(bookworm).setVisible(true);
	                else new RegisteredUserView(bookworm).setVisible(true);
	                
	                this.dispose();
	            }
	        });

	        createAccountButton.addActionListener(e -> {
	        	Bookworm_Create_AccountGUI cag = new Bookworm_Create_AccountGUI();
	        	cag.setVisible(true);
	        	this.dispose();
	        });
			
	    }
}
