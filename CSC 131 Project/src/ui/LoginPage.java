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
import model.Item;
import model.Receipt;

public class LoginPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton loginButton = new JButton("Login");
	JButton registerButton = new JButton("Register");
	JTextField userIDField = new JTextField();
	JPasswordField userPassword = new JPasswordField();
	JLabel userIDLabel = new JLabel("Email/Username:");
	JLabel userPasswordLabel = new JLabel("Password:");
	JButton searchButton = new JButton("Search For Receipt Without Logging In");
	JLabel messageLabel = new JLabel();
	HashMap<String,User> loginInfo = new HashMap<String,User>();
	
	LoginPage(HashMap<String,User> loginInfoOriginal){
		
		//Button layouts
		loginInfo = loginInfoOriginal;
		userIDLabel.setBounds(80, 160, 150, 25);
		userPasswordLabel.setBounds(80, 220, 75, 25);
		userIDField.setBounds(80, 185, 340, 35);
		userPassword.setBounds(80, 245, 340, 35);
		loginButton.setBounds(270, 310, 120, 35);
		loginButton.addActionListener(this);
		messageLabel.setBounds(80, 360, 340, 25);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
		registerButton.setBounds(110, 310, 120, 35);
		registerButton.addActionListener(this);
		
		//Search receipt in login page
		searchButton.setBounds(80, 480, 340, 35);
		searchButton.addActionListener(this);
		frame.add(searchButton);
		
		//Buttons added onto frame
		frame.add(registerButton);
		frame.add(userIDLabel);
		frame.add(userPasswordLabel);
		frame.add(userIDField);
		frame.add(userPassword);
		frame.add(loginButton);
		frame.add(messageLabel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,600);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e){

	    if (e.getSource() == loginButton){

	        String userID = userIDField.getText();
	        String password = String.valueOf(userPassword.getPassword());

	        User matchedUser = null;

	        for (User user : loginInfo.values()){
	            if ((user.getName().equals(userID) || user.getEmail().equals(userID)) && user.getPassword().equals(password)) {
	                matchedUser = user;
	                break;
	            }
	        }
	        
	        if (userID.isEmpty() || password.isEmpty()) {
	            messageLabel.setForeground(Color.RED);
	            messageLabel.setText("All fields are required.");
	            return;
	        }

	        if (matchedUser != null){
	            messageLabel.setForeground(Color.GREEN);
	            messageLabel.setText("Login Successful");
	            frame.dispose();
	            new ReceiptPage(matchedUser);
	        } else {
	            messageLabel.setForeground(Color.RED);
	            messageLabel.setText("Invalid email/username or password.");
	        }
	    }

	    if (e.getSource() == registerButton){
	        frame.dispose();
	        new RegisterPage(loginInfo);
	    }
	    
	    if (e.getSource() == searchButton) {
	        String id = JOptionPane.showInputDialog(frame, "Enter Receipt ID:");
	        if (id == null || id.trim().isEmpty()) {
	        	return;
	        }

	        try {
	            int receiptID = Integer.parseInt(id.trim());
	            Receipt found = null;

	            for (Receipt receipt : UserInfo.getInstance().getAllReceipts()) {
	                if (receipt.getReceiptID() == receiptID) {
	                    found = receipt;
	                    break;
	                }
	            }

	            if (found != null) {
	                StringBuilder sb = new StringBuilder();
	                sb.append("Receipt: ").append(found.getName()).append("\n");
	                sb.append("Date: ").append(found.getDate()).append("\n");
	                sb.append("Items:\n");
	                for (Item item : found.getItems()) {
	                	sb.append(item.getName()).append(" x").append(item.getCount()).append(" - $").append(String.format("%.2f", item.getPrice() * item.getCount())).append("\n");
	                }
	                sb.append("Total: $").append(String.format("%.2f", found.itemSum()));
	                JOptionPane.showMessageDialog(frame, sb.toString(), "Receipt #" + receiptID, JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                JOptionPane.showMessageDialog(frame, "Receipt not found.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(frame, "Invalid receipt ID.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
		
	}
	

}
