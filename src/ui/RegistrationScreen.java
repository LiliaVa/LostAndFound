
package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class RegistrationScreen extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;

    private MainFrame parent;


    public RegistrationScreen(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));


        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);


        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);

        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);
        formPanel.add(backButton);
        formPanel.add(registerButton);


        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusPanel.add(statusLabel);


        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);


        addEventHandlers();
    }


    private void addEventHandlers() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clearFields();
                parent.showLoginScreen();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {

                    User newUser = new User();
                    newUser.setName(nameField.getText());
                    newUser.setEmail(emailField.getText());
                    newUser.setPassword(new String(passwordField.getPassword()));


                    newUser.setUserID((int)(Math.random() * 10000));


                    UserDAO userDAO = new UserDAO();
                    boolean success = userDAO.createUser(newUser);

                    if (success) {
                        JOptionPane.showMessageDialog(parent,
                                "Registration successful! You can now log in.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        parent.showLoginScreen();
                    } else {
                        statusLabel.setText("Error creating account. Please try again.");
                    }
                }
            }
        });
    }


    private boolean validateForm() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());


        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("All fields are required");
            return false;
        }


        if (!isValidEmail(email)) {
            statusLabel.setText("Please enter a valid email address");
            return false;
        }


        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            return false;
        }


        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters long");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {

        return email.contains("@") && email.contains(".");
    }


    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        statusLabel.setText("");
    }
}
