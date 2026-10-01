package bookworm;

import javax.swing.*;
import java.awt.*;


public class Bookworm_Create_AccountGUI extends JFrame {

	    // Components
	    private JLabel usernameLabel;
	    private JLabel passwordLabel;
	    private JLabel confirmPasswordLabel;

	    private JTextField usernameField;
	    private JPasswordField passwordField;
	    private JPasswordField confirmPasswordField;

	    private JButton createButton;

	    public Bookworm_Create_AccountGUI() {

	        // -----------------------------
	        // Window Settings
	        // -----------------------------
	        setTitle("Create account");
	        setSize(600, 350);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        // Use absolute positioning
	        setLayout(null);

	        // -----------------------------
	        // Username
	        // -----------------------------
	        usernameLabel = new JLabel("Username:");
	        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 23));
	        usernameLabel.setBounds(97, 78, 130, 35);
	        add(usernameLabel);

	        usernameField = new JTextField();
	        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
	        usernameField.setBounds(235, 78, 257, 27);
	        add(usernameField);

	        // -----------------------------
	        // Password
	        // -----------------------------
	        passwordLabel = new JLabel("Password:");
	        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 23));
	        passwordLabel.setBounds(105, 117, 125, 35);
	        add(passwordLabel);

	        passwordField = new JPasswordField();
	        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
	        passwordField.setBounds(235, 117, 257, 27);
	        add(passwordField);

	        // -----------------------------
	        // Confirm Password
	        // -----------------------------
	        confirmPasswordLabel = new JLabel("Confirm Password:");
	        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 23));
	        confirmPasswordLabel.setBounds(97, 156, 225, 35);
	        add(confirmPasswordLabel);

	        confirmPasswordField = new JPasswordField();
	        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 18));
	        confirmPasswordField.setBounds(327, 156, 165, 27);
	        add(confirmPasswordField);

	        // -----------------------------
	        // Create Button
	        // -----------------------------
	        createButton = new JButton("Create");
	        createButton.setFont(new Font("Arial", Font.PLAIN, 20));
	        createButton.setBounds(211, 215, 169, 32);
	        add(createButton);

	        // -----------------------------
	        // Create Button Action
	        // -----------------------------
	        createButton.addActionListener(e -> {

	            String username = usernameField.getText();
	            String password = new String(passwordField.getPassword());
	            String confirmPassword =
	                    new String(confirmPasswordField.getPassword());

	            if (username.isEmpty() ||
	                password.isEmpty() ||
	                confirmPassword.isEmpty()) {

	                JOptionPane.showMessageDialog(
	                        this,
	                        "Please fill in all fields."
	                );

	            } else if (!password.equals(confirmPassword)) {

	                JOptionPane.showMessageDialog(
	                        this,
	                        "Passwords do not match."
	                );

	            } else {

	                JOptionPane.showMessageDialog(
	                        this,
	                        "Account created successfully!"
	                );
	            }
	        });
	    }

	    // -----------------------------
	    // Main Method
	    // -----------------------------
	    public static void main(String[] args) {

	        SwingUtilities.invokeLater(() -> {

	        	Bookworm_Create_AccountGUI createAccountGUI =
	                    new Bookworm_Create_AccountGUI();

	            createAccountGUI.setVisible(true);
	        });
	    }
	}