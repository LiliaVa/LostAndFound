
package ui;

import dao.UserDAO;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class RegistrationScreen extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;
    private JButton selectImageButton;
    private JLabel profilePictureLabel;
    private byte[] profileImageData;
    private BufferedImage profileImage;

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

        JPanel profilePicPanel = new JPanel(new BorderLayout(10, 5));
        profilePicPanel.setOpaque(false);
        profilePicPanel.setBorder(new EmptyBorder(5, 0, 10, 0));

        JLabel profilePicLabel = new JLabel("Profile Picture:");
        profilePicLabel.setFont(MainFrame.SUBTITLE_FONT);
        profilePicPanel.add(profilePicLabel, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        imagePanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 5, 0));

        selectImageButton = new JButton("Select Image");
        selectImageButton.setFont(MainFrame.BUTTON_FONT);
        selectImageButton.setPreferredSize(MainFrame.STANDARD_BUTTON_SIZE);
        selectImageButton.setFocusPainted(false);

        profilePictureLabel = new JLabel("No image selected");
        profilePictureLabel.setFont(MainFrame.BODY_FONT);
        profilePictureLabel.setPreferredSize(new Dimension(120, 120));
        profilePictureLabel.setBorder(BorderFactory.createLineBorder(parent.getBorderColor(), 1));
        profilePictureLabel.setHorizontalAlignment(JLabel.CENTER);

        imagePanel.add(selectImageButton);
        imagePanel.add(profilePictureLabel);
        profilePicPanel.add(imagePanel, BorderLayout.CENTER);

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

        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        String filename = f.getName().toLowerCase();
                        return filename.endsWith(".jpg") || filename.endsWith(".jpeg") ||
                                filename.endsWith(".png") || filename.endsWith(".gif");
                    }

                    @Override
                    public String getDescription() {
                        return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
                    }
                });

                int result = fileChooser.showOpenDialog(RegistrationScreen.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        profileImage = ImageIO.read(selectedFile);
                        if (profileImage != null) {
                            // Scale image for display
                            Image scaledImage = profileImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                            ImageIcon icon = new ImageIcon(scaledImage);
                            profilePictureLabel.setText("");
                            profilePictureLabel.setIcon(icon);

                            // Convert to byte array for storage
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.') + 1);
                            ImageIO.write(profileImage, fileExtension, baos);
                            profileImageData = baos.toByteArray();
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(RegistrationScreen.this,
                                "Error reading image file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
        profilePictureLabel.setIcon(null);
        profilePictureLabel.setText("No image selected");
        profileImageData = null;
        profileImage = null;
    }
}
