package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.User;

public class LoginPage implements ActionListener {

	JFrame frame = new JFrame();
	JButton loginButton = new JButton("Login");
	JButton registerButton = new JButton("Register");
	JButton searchReceiptButton = new JButton("Search Receipt by ID");
	JTextField userIDField = new JTextField();
	JPasswordField userPassword = new JPasswordField();
	JLabel userIDLabel = new JLabel("Email/Username:");
	JLabel userPasswordLabel = new JLabel("Password:");
	JButton searchButton = new JButton("Search For Receipt Without Logging In");
	JLabel messageLabel = new JLabel();
	HashMap<String, User> loginInfo = new HashMap<String, User>();

	LoginPage(HashMap<String, User> loginInfoOriginal) {

		loginInfo = loginInfoOriginal;
		userIDLabel.setBounds(80, 160, 150, 25);
		userPasswordLabel.setBounds(80, 220, 75, 25);
		userIDField.setBounds(80, 185, 340, 35);
		userPassword.setBounds(80, 245, 340, 35);
		loginButton.setBounds(270, 310, 120, 35);
		loginButton.addActionListener(this);
		messageLabel.setBounds(80, 410, 340, 25);
		messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
		registerButton.setBounds(110, 310, 120, 35);
		registerButton.addActionListener(this);
		searchReceiptButton.setBounds(120, 360, 200, 30);
		searchReceiptButton.addActionListener(this);

		frame.add(registerButton);
		frame.add(userIDLabel);
		frame.add(userPasswordLabel);
		frame.add(userIDField);
		frame.add(userPassword);
		frame.add(loginButton);
		frame.add(searchReceiptButton);
		frame.add(messageLabel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == loginButton) {

			String userID = userIDField.getText();
			String password = String.valueOf(userPassword.getPassword());

			User matchedUser = null;

			for (User user : loginInfo.values()) {
				if ((user.getName().equals(userID) || user.getEmail().equals(userID))
						&& user.getPassword().equals(password)) {
					matchedUser = user;
					break;
				}
			}

			if (matchedUser != null) {
				messageLabel.setForeground(Color.GREEN);
				messageLabel.setText("Login Successful");
				frame.dispose();
				new ReceiptPage(matchedUser);
			} else {
				messageLabel.setForeground(Color.RED);
				messageLabel.setText("Invalid email/username or password.");
			}
		}

		if (e.getSource() == registerButton) {
			frame.dispose();
			new RegisterPage(loginInfo);
		}

		if (e.getSource() == searchReceiptButton) {
			frame.dispose();
			new SearchPage();
		}
	}
}