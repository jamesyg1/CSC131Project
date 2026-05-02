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
import model.UserFactory;

public class RegisterPage implements ActionListener {

    JFrame frame = new JFrame();
    JButton registerButton = new JButton("Register");
    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField();
    JLabel nameLabel = new JLabel("Name:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    JLabel messageLabel = new JLabel();
    HashMap<String, User> loginInfo;
    JButton backButton = new JButton("Back");


    RegisterPage(HashMap<String, User> loginInfo) {

        this.loginInfo = loginInfo;

        //Button layouts
        nameLabel.setBounds(50, 80, 120, 25);
        emailLabel.setBounds(50, 120, 120, 25);
        passwordLabel.setBounds(50, 160, 120, 25);
        confirmPasswordLabel.setBounds(50, 200, 120, 25);
        nameField.setBounds(175, 80, 200, 25);
        emailField.setBounds(175, 120, 200, 25);
        passwordField.setBounds(175, 160, 200, 25);
        confirmPasswordField.setBounds(175, 200, 200, 25);
        registerButton.setBounds(225, 250, 100, 25);
        registerButton.addActionListener(this);
        backButton.setBounds(100, 250, 100, 25);
        backButton.addActionListener(this);
        messageLabel.setBounds(100, 290, 300, 35);

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
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == registerButton) {

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = String.valueOf(passwordField.getPassword());
            String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

            //User needs to fill in all the inputs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("All fields are required.");
                return;
            }

            //Checks if the password and confirm password are the same
            if (!password.equals(confirmPassword)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Passwords do not match. Please try again.");
                return;
            }

            //Checks if the username exist already
            if (loginInfo.containsKey(name)) {
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
        if (e.getSource() == backButton) {
            frame.dispose();
            new LoginPage(loginInfo);
        }
    }
}
