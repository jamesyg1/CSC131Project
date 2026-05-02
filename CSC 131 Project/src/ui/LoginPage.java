package ui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;

import model.User;


public class LoginPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton loginButton = new JButton("Login");
	JButton registerButton = new JButton("Register");
	JTextField userIDField = new JTextField();
	JPasswordField userPassword = new JPasswordField();
	JLabel userIDLabel = new JLabel("Email:");
	JLabel userPasswordLabel = new JLabel("Password:");
	JLabel messageLabel = new JLabel();
	HashMap<String,User> loginInfo = new HashMap<String,User>();
	
	LoginPage(HashMap<String,User> loginInfoOriginal){
		
		//Button layouts
		loginInfo = loginInfoOriginal;
		userIDLabel.setBounds(50,100,75,25);
		userPasswordLabel.setBounds(50,150,75,25);
		userIDField.setBounds(125,100,200,25);
		userPassword.setBounds(125,150,200,25);
		loginButton.setBounds(225,200,100,25);
		loginButton.addActionListener(this);
		messageLabel.setBounds(100, 250, 300, 35);
		registerButton.setBounds(115, 200, 100, 25);
		registerButton.addActionListener(this);
		
		//Buttons added onto frame
		frame.add(registerButton);
		frame.add(userIDLabel);
		frame.add(userPasswordLabel);
		frame.add(userIDField);
		frame.add(userPassword);
		frame.add(loginButton);
		frame.add(messageLabel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420,420);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == loginButton) {
			
			String userID = userIDField.getText();
			String password = String.valueOf(userPassword.getPassword());
			 
			//Checks if user exists
			if(loginInfo.containsKey(userID)) {
				
				//Account exists and valid, redirect to main page
				if(loginInfo.get(userID).getPassword().equals(password)) {
					messageLabel.setForeground(Color.GREEN);
					messageLabel.setText("Login Sucessful");
					frame.dispose();
					User curUser = loginInfo.get(userID);
					ReceiptPage mainPage = new ReceiptPage(curUser);

				}
				//Incorrect password
				else { 
					messageLabel.setForeground(Color.RED);
					messageLabel.setText("Invalid password. Please try again.");					
				}
			//Incorrect email or password
			} else {
				messageLabel.setForeground(Color.RED);
				messageLabel.setText("Invalid email or password. Please try again.");
				
			}
		}
		
		//Redirect to register page when button is clicked
		if (e.getSource() == registerButton) {
		    frame.dispose();
		    RegisterPage registerPage = new RegisterPage(loginInfo);
		}
		
	}
	

}
