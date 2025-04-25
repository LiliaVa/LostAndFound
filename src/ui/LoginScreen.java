
package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;


public class LoginScreen extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    private JLabel welcomeImageLabel;

    private MainFrame parent;


    public LoginScreen(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 40, 20, 40));
        setBackground(new Color(240, 248, 255));


        JPanel welcomePanel = createWelcomePanel();


        JPanel formPanel = createFormPanel();


        add(welcomePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);


        addDecorativeElements();
    }


    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(10, 10));
        welcomePanel.setOpaque(false);


        JLabel titleLabel = new JLabel("Welcome to Lost and Found System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);


        JLabel messageLabel = new JLabel("Please log in to continue");
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);


        welcomeImageLabel = new JLabel();
        welcomeImageLabel.setPreferredSize(new Dimension(100, 100));
        welcomeImageLabel.setHorizontalAlignment(JLabel.CENTER);


        ImageIcon placeholderIcon = createPlaceholderIcon(100, 100);
        welcomeImageLabel.setIcon(placeholderIcon);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(messageLabel, BorderLayout.CENTER);

        welcomePanel.add(welcomeImageLabel, BorderLayout.NORTH);
        welcomePanel.add(textPanel, BorderLayout.CENTER);

        return welcomePanel;
    }


    private ImageIcon createPlaceholderIcon(int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setColor(new Color(70, 130, 180));
        g2d.fillOval(10, 10, width - 40, height - 40);


        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(width - 25, height - 25, width - 10, height - 10);


        g2d.setColor(Color.WHITE);
        g2d.fillOval(20, 20, width - 60, height - 60);

        g2d.dispose();
        return new ImageIcon(image);
    }


    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setOpaque(false);


        JPanel emailPanel = new JPanel(new BorderLayout(5, 5));
        emailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 30));
        emailPanel.add(emailLabel, BorderLayout.NORTH);
        emailPanel.add(emailField, BorderLayout.CENTER);


        JPanel passwordPanel = new JPanel(new BorderLayout(5, 5));
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 35));
        registerButton.setBackground(new Color(169, 169, 169));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);


        JPanel statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusPanel.add(statusLabel);


        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(emailPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(statusPanel);

        return formPanel;
    }


    private void addDecorativeElements() {


    }


    public void addEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Please enter both email and password");
                    return;
                }


                UserDAO userDAO = new UserDAO();
                User user = userDAO.login(email, password);

                if (user != null) {

                    statusLabel.setText("");
                    JOptionPane.showMessageDialog(parent, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.setCurrentUser(user);
                    parent.showHomeScreen();
                } else {

                    statusLabel.setText("Invalid email or password");
                    passwordField.setText("");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                parent.showRegistrationScreen();
            }
        });


        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };

        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }


    private boolean validateInputs() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty()) {
            statusLabel.setText("Email cannot be empty");
            return false;
        }

        if (!isValidEmail(email)) {
            statusLabel.setText("Please enter a valid email address");
            return false;
        }

        if (password.isEmpty()) {
            statusLabel.setText("Password cannot be empty");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {

        return email.contains("@") && email.contains(".");
    }


    public void clear() {
        emailField.setText("");
        passwordField.setText("");
        statusLabel.setText("");
    }
}
