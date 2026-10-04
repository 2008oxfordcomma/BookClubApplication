// Primary Author: Benjamin

package bookClub;

import javax.swing.*;
import java.awt.*;

public class Bookworm_Meetings_Form extends JFrame {

	// Components
	private JLabel bookNameLabel;
	private JLabel maxCapacityLabel;
	private JLabel startDateLabel;
	private JLabel endDateLabel;

	private JTextField bookNameField;
	private JTextField maxCapacityField;
	private JTextField startDateField;
	private JTextField endDateField;

	private JButton scheduleButton;
	private JButton cancelButton;
	
	private final Bookworm bookworm;
	private final AdminView parentView;

	public Bookworm_Meetings_Form() {
		this(new Bookworm(), null);
	}
	
	public Bookworm_Meetings_Form(Bookworm bookworm, AdminView parentView) {
		this.bookworm = bookworm;
		this.parentView = parentView;

		// -----------------------------
		// Window Settings
		// -----------------------------
		setTitle("Meetings Form");
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Absolute positioning
		setLayout(null);

		// -----------------------------
		// Book Name
		// -----------------------------
		bookNameLabel = new JLabel("Book Name:");
		bookNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		bookNameLabel.setBounds(97, 54, 130, 30);
		add(bookNameLabel);

		bookNameField = new JTextField();
		bookNameField.setFont(new Font("Arial", Font.PLAIN, 18));
		bookNameField.setBounds(235, 54, 257, 27);
		add(bookNameField);

		// -----------------------------
		// Max Capacity
		// -----------------------------
		maxCapacityLabel = new JLabel("Max Capacity:");
		maxCapacityLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		maxCapacityLabel.setBounds(97, 92, 140, 30);
		add(maxCapacityLabel);

		maxCapacityField = new JTextField();
		maxCapacityField.setFont(new Font("Arial", Font.PLAIN, 18));
		maxCapacityField.setBounds(235, 92, 257, 27);
		add(maxCapacityField);

		// -----------------------------
		// Start Date
		// -----------------------------
		startDateLabel = new JLabel("Start Date:");
		startDateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		startDateLabel.setBounds(97, 130, 130, 30);
		add(startDateLabel);

		startDateField = new JTextField();
		startDateField.setFont(new Font("Arial", Font.PLAIN, 18));
		startDateField.setBounds(235, 130, 257, 27);
		add(startDateField);

		// -----------------------------
		// End Date
		// -----------------------------
		endDateLabel = new JLabel("End Date:");
		endDateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		endDateLabel.setBounds(97, 168, 130, 30);
		add(endDateLabel);

		endDateField = new JTextField();
		endDateField.setFont(new Font("Arial", Font.PLAIN, 18));
		endDateField.setBounds(235, 168, 257, 27);
		add(endDateField);

		// -----------------------------
		// Schedule Button
		// -----------------------------
		scheduleButton = new JButton("Schedule");
		scheduleButton.setFont(new Font("Arial", Font.PLAIN, 20));
		scheduleButton.setBounds(98, 217, 169, 32);
		add(scheduleButton);

		// -----------------------------
		// Cancel Button
		// -----------------------------
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Arial", Font.PLAIN, 20));
		cancelButton.setBounds(322, 217, 170, 32);
		add(cancelButton);

		scheduleButton.addActionListener(e -> {

			String bookName = bookNameField.getText().trim();
			String maxCapacity = maxCapacityField.getText().trim();
			String startDate = startDateField.getText().trim();
			String endDate = endDateField.getText().trim();

			if (bookName.isEmpty() || maxCapacity.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please fill in all of the fields.");
				return;
			}
			
			Boolean ok = bookworm.scheduleMeeting(bookName, "", startDate, endDate);
			
			if (ok) {
				JOptionPane.showMessageDialog(this, "The meeting was scheduled successfully!");
				if (parentView != null) parentView.refreshTables();
				dispose();
			} else {
          JOptionPane.showMessageDialog(this, "The meeting couldn't be scheduled, so please check the date format\n" + "(expected: YYYY-MM-DD HH:MM:SS)");
			};
		});

		// -----------------------------
		// Cancel Button Action
		// -----------------------------
		cancelButton.addActionListener(e -> {

			dispose();

		});
	}

	// -----------------------------
	// Main Method
	// -----------------------------
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			Bookworm_Meetings_Form meetingsForm = new Bookworm_Meetings_Form();
			meetingsForm.setVisible(true);

		});
	}
}
