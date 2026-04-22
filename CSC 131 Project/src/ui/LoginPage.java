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



public class LoginPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton loginButton = new JButton("Login");
	JButton resetButton = new JButton("Reset");
	JTextField userIDField = new JTextField();
	JPasswordField userPassword = new JPasswordField();
	JLabel userIDLabel = new JLabel("UserID:");
	JLabel userPasswordLabel = new JLabel("Password:");
	JLabel messageLabel = new JLabel();
	HashMap<String,String> loginInfo = new HashMap<String,String>();
	
	LoginPage(HashMap<String,String> loginInfoOriginal){
		
		loginInfo = loginInfoOriginal;
		userIDLabel.setBounds(50,100,75,25);
		userPasswordLabel.setBounds(50,150,75,25);
		userIDField.setBounds(125,100,200,25);
		userPassword.setBounds(125,150,200,25);
		loginButton.setBounds(225,200,100,25);
		loginButton.addActionListener(this);;
		messageLabel.setBounds(125,250,250,35);
		
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
			 
			if(loginInfo.containsKey(userID)) {
				if(loginInfo.get(userID).equals(password)) {
					messageLabel.setForeground(Color.GREEN);
					messageLabel.setText("Login Sucessful");
					frame.dispose();
					ReceiptPage mainPage = new ReceiptPage(userID);

				}
				else { 
					messageLabel.setForeground(Color.RED);
					messageLabel.setText("Invalid password. Please try again.");					
				}
			} else {
				messageLabel.setForeground(Color.RED);
				messageLabel.setText("Invalid userID or password. Please try again.");
				
			}
		}
		
	}
	

}
