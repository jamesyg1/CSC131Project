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
import java.awt.Font;

import model.User;
import model.UserFactory;

public class RegisterPage implements ActionListener {

    JFrame frame = new JFrame();
    JButton registerButton = new JButton("Register");
    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField();
    JLabel nameLabel = new JLabel("Username:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    JLabel messageLabel = new JLabel();
    HashMap<String, User> loginInfo;
    JButton backButton = new JButton("Back");


    RegisterPage(HashMap<String, User> loginInfo) {

        this.loginInfo = loginInfo;

        //Button layouts
        nameLabel.setBounds(80, 80, 120, 25);
        nameField.setBounds(80, 108, 340, 35);
        emailLabel.setBounds(80, 165, 120, 25);
        emailField.setBounds(80, 193, 340, 35);
        passwordLabel.setBounds(80, 250, 120, 25);
        passwordField.setBounds(80, 278, 340, 35);
        confirmPasswordLabel.setBounds(80, 335, 120, 25);
        confirmPasswordField.setBounds(80, 363, 340, 35);
        registerButton.setBounds(270, 430, 120, 35);
        backButton.setBounds(110, 430, 120, 35);
        messageLabel.setBounds(80, 475, 340, 25);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(this);
        backButton.addActionListener(this);
        


        //Add buttons to the frame
        frame.add(backButton);
        frame.add(nameLabel);
        frame.add(emailLabel);
        frame.add(passwordLabel);
        frame.add(confirmPasswordLabel);
        frame.add(nameField);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(confirmPasswordField);
        frame.add(registerButton);
        frame.add(messageLabel);
        frame.add(backButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == registerButton){

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = String.valueOf(passwordField.getPassword());
            String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

            //User needs to fill in all the inputs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("All fields are required.");
                return;
            }
            
            //Email has to contain an @
            if (!email.contains("@")){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Please enter a valid email.");
                return;
            }

            //Checks if the password and confirm password are the same
            if (!password.equals(confirmPassword)){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Passwords do not match. Please try again.");
                return;
            }

            //Checks if the username exist already
            if (loginInfo.containsKey(name)){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Username already exists.");
                return;
            }
            
            //Redirects to login page after account is created
            int newID = loginInfo.size() + 1;
            loginInfo.put(name, UserFactory.createUser(newID, email, password, name));
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Registration successful!");
            frame.dispose();
            LoginPage loginPage = new LoginPage(loginInfo);
        }
        
        //Goes back to login page from register page
        if (e.getSource() == backButton){
            frame.dispose();
            new LoginPage(loginInfo);
        }
    }
}
