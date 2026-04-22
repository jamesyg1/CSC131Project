package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class ReceiptPage {
	
	JFrame frame = new JFrame();
	JLabel mainPage = new JLabel("Hello!");
			
	ReceiptPage(String userID){
		
		mainPage.setBounds(0,0,200,35);
		mainPage.setFont(new Font(null,Font.PLAIN,25));
		mainPage.setText("Hello " + userID + "!");
		
		frame.add(mainPage);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420,420);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	

}
