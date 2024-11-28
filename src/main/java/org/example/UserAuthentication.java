package org.example;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class UserAuthentication {


    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, User> users = new HashMap<>();

    public UserAuthentication() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(3, 2));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        // Attach action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // Convert char[] to String

        // Validate (for this example, we're not hashing the password for simplicity)
        User user = users.get(username);
        if (user != null && user.getHashedPassword().equals(password)) {
            // Load the game with the user's saved state
            new AngelDemonGame();
            loginFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // Convert char[] to String

        if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(loginFrame, "Username already exists!");
        } else {
            // For this example, we're not hashing the password for simplicity.
            users.put(username, new User(username, password, new GameState()));
            JOptionPane.showMessageDialog(loginFrame, "Registered successfully!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserAuthentication());
    }
}
