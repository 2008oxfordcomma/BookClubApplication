package bookClub;

import javax.swing.*;
import java.awt.*;

public class Bookworm_Editing_Meeting_GUI extends JFrame {

	// Components
	private JLabel bookNameLabel;
	private JLabel maxCapacityLabel;
	private JLabel startDateLabel;
	private JLabel endDateLabel;

	private JTextField bookNameField;
	private JTextField maxCapacityField;
	private JTextField startDateField;
	private JTextField endDateField;

	private JButton confirmButton;
	private JButton cancelButton;

	public Bookworm_Editing_Meeting_GUI() {

		// -----------------------------
		// Window Settings
		// -----------------------------
		setTitle("Edit Meetings Form");
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
		// Confirm Button
		// -----------------------------
		confirmButton = new JButton("Confirm");
		confirmButton.setFont(new Font("Arial", Font.PLAIN, 20));
		confirmButton.setBounds(98, 217, 169, 32);
		add(confirmButton);

		// -----------------------------
		// Cancel Button
		// -----------------------------
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Arial", Font.PLAIN, 20));
		cancelButton.setBounds(322, 217, 170, 32);
		add(cancelButton);

		// -----------------------------
		// Confirm Button Action
		// -----------------------------
		confirmButton.addActionListener(e -> {

			String bookName = bookNameField.getText();
			String maxCapacity = maxCapacityField.getText();
			String startDate = startDateField.getText();
			String endDate = endDateField.getText();

			if (bookName.isEmpty() || maxCapacity.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {

				JOptionPane.showMessageDialog(this, "Please fill in all fields.");

			} else {

				JOptionPane.showMessageDialog(this, "Meeting updated successfully!");
			}
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

			Bookworm_Editing_Meeting_GUI editMeetingsForm = new Bookworm_Editing_Meeting_GUI();

			editMeetingsForm.setVisible(true);

		});
	}
}