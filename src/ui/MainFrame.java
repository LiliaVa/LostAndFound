
package ui;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.util.prefs.Preferences;


public class MainFrame extends JFrame {
    private User currentUser;


    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JLabel logoLabel;
    private JLabel userStatusLabel;
    private JToggleButton darkModeToggle;


    private static final Color LIGHT_BG = new Color(255, 255, 255);
    private static final Color LIGHT_HEADER_BG = new Color(70, 130, 180);
    private static final Color LIGHT_TEXT = new Color(0, 0, 0);
    private static final Color LIGHT_COMPONENT_BG = new Color(240, 240, 240);
    private static final Color LIGHT_BORDER = new Color(200, 200, 200);

    private static final Color DARK_BG = new Color(50, 50, 50);
    private static final Color DARK_HEADER_BG = new Color(25, 25, 25);
    private static final Color DARK_TEXT = new Color(220, 220, 220);
    private static final Color DARK_COMPONENT_BG = new Color(80, 80, 80);
    private static final Color DARK_BORDER = new Color(100, 100, 100);


    private boolean isDarkMode = false;


    private Preferences preferences;


    private LoginScreen loginScreen;
    private RegistrationScreen registrationScreen;
    private HomeScreen homeScreen;
    private LostItemScreen lostItemScreen;
    private FoundItemScreen foundItemScreen;
    private ReportItemScreen reportItemScreen;


    private static final String LOGIN_SCREEN = "LoginScreen";
    private static final String REGISTRATION_SCREEN = "RegistrationScreen";
    private static final String HOME_SCREEN = "HomeScreen";
    private static final String LOST_ITEM_SCREEN = "LostItemScreen";
    private static final String FOUND_ITEM_SCREEN = "FoundItemScreen";
    private static final String REPORT_ITEM_SCREEN = "ReportItemScreen";


    public MainFrame() {
        setTitle("Lost and Found System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout());


        preferences = Preferences.userNodeForPackage(MainFrame.class);
        isDarkMode = preferences.getBoolean("darkMode", false);


        createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);


        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        add(contentPanel, BorderLayout.CENTER);


        initializeScreens();


        enableDarkMode(isDarkMode);


        cardLayout.show(contentPanel, LOGIN_SCREEN);


        setLocationRelativeTo(null);
    }


    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(isDarkMode ? DARK_HEADER_BG : LIGHT_HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


        logoLabel = new JLabel("Lost & Found System");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);


        userStatusLabel = new JLabel("Not logged in");
        userStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        userStatusLabel.setForeground(Color.WHITE);


        darkModeToggle = new JToggleButton(isDarkMode ? "Light Mode" : "Dark Mode");
        darkModeToggle.setFocusPainted(false);
        darkModeToggle.setSelected(isDarkMode);
        darkModeToggle.addActionListener(e -> enableDarkMode(darkModeToggle.isSelected()));


        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(darkModeToggle);
        rightPanel.add(Box.createHorizontalStrut(15));
        rightPanel.add(userStatusLabel);


        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
    }


    private void initializeScreens() {

        loginScreen = new LoginScreen(this);
        contentPanel.add(loginScreen, LOGIN_SCREEN);


        registrationScreen = new RegistrationScreen(this);
        contentPanel.add(registrationScreen, REGISTRATION_SCREEN);


    }


    public void showLoginScreen() {
        cardLayout.show(contentPanel, LOGIN_SCREEN);
        loginScreen.clear();
        updateUserStatusLabel("Not logged in");
    }

    public void showRegistrationScreen() {
        cardLayout.show(contentPanel, REGISTRATION_SCREEN);
    }

    public void showHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen(this);
            contentPanel.add(homeScreen, HOME_SCREEN);
        }


        homeScreen.refreshData();
        cardLayout.show(contentPanel, HOME_SCREEN);
        updateUserStatusLabel("Logged in as: " + currentUser.getName());
    }

    public void showLostItemScreen() {
        if (lostItemScreen == null) {
            lostItemScreen = new LostItemScreen(this);
            contentPanel.add(lostItemScreen, LOST_ITEM_SCREEN);
        }


        lostItemScreen.refreshData();
        cardLayout.show(contentPanel, LOST_ITEM_SCREEN);
    }

    public void showFoundItemScreen() {
        if (foundItemScreen == null) {
            foundItemScreen = new FoundItemScreen(this);
            contentPanel.add(foundItemScreen, FOUND_ITEM_SCREEN);
        }


        foundItemScreen.refreshData();
        cardLayout.show(contentPanel, FOUND_ITEM_SCREEN);
    }

    public void showReportItemScreen() {
        if (reportItemScreen == null) {
            reportItemScreen = new ReportItemScreen(this);
            contentPanel.add(reportItemScreen, REPORT_ITEM_SCREEN);
        }

        cardLayout.show(contentPanel, REPORT_ITEM_SCREEN);
    }


    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserStatusLabel("Logged in as: " + user.getName());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
        updateUserStatusLabel("Not logged in");
        showLoginScreen();
    }


    private void updateUserStatusLabel(String status) {
        userStatusLabel.setText(status);
    }


    public void initializeEventHandlers() {
        loginScreen.addEventHandlers();


        addKeyboardShortcuts();
    }


    private void addKeyboardShortcuts() {

        KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action escAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (isLoggedIn()) {
                    showHomeScreen();
                }
            }
        };


        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKeyStroke, "escAction");
        getRootPane().getActionMap().put("escAction", escAction);


        KeyStroke darkModeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
        Action darkModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                darkModeToggle.setSelected(!darkModeToggle.isSelected());
                enableDarkMode(darkModeToggle.isSelected());
            }
        };

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(darkModeKeyStroke, "darkModeAction");
        getRootPane().getActionMap().put("darkModeAction", darkModeAction);
    }


    public void enableDarkMode(boolean darkMode) {
        isDarkMode = darkMode;


        preferences.putBoolean("darkMode", darkMode);


        if (darkModeToggle != null) {
            darkModeToggle.setText(darkMode ? "Light Mode" : "Dark Mode");
        }


        Color backgroundColor = darkMode ? DARK_BG : LIGHT_BG;
        Color headerColor = darkMode ? DARK_HEADER_BG : LIGHT_HEADER_BG;
        Color textColor = darkMode ? DARK_TEXT : LIGHT_TEXT;
        Color componentBgColor = darkMode ? DARK_COMPONENT_BG : LIGHT_COMPONENT_BG;
        Color borderColor = darkMode ? DARK_BORDER : LIGHT_BORDER;


        contentPanel.setBackground(backgroundColor);
        headerPanel.setBackground(headerColor);


        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("TextField.background", componentBgColor);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("TextArea.background", componentBgColor);
        UIManager.put("TextArea.foreground", textColor);
        UIManager.put("ComboBox.background", componentBgColor);
        UIManager.put("ComboBox.foreground", textColor);
        UIManager.put("Button.background", componentBgColor);
        UIManager.put("Button.foreground", textColor);
        UIManager.put("List.background", componentBgColor);
        UIManager.put("List.foreground", textColor);
        UIManager.put("Table.background", componentBgColor);
        UIManager.put("Table.foreground", textColor);
        UIManager.put("TableHeader.background", headerColor);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("CheckBox.background", backgroundColor);
        UIManager.put("CheckBox.foreground", textColor);
        UIManager.put("RadioButton.background", backgroundColor);
        UIManager.put("RadioButton.foreground", textColor);
        UIManager.put("ScrollPane.background", backgroundColor);
        UIManager.put("Viewport.background", backgroundColor);
        UIManager.put("TabbedPane.background", backgroundColor);
        UIManager.put("TabbedPane.foreground", textColor);


        if (loginScreen != null) applyThemeToScreen(loginScreen);
        if (registrationScreen != null) applyThemeToScreen(registrationScreen);
        if (homeScreen != null) applyThemeToScreen(homeScreen);
        if (lostItemScreen != null) applyThemeToScreen(lostItemScreen);
        if (foundItemScreen != null) applyThemeToScreen(foundItemScreen);
        if (reportItemScreen != null) applyThemeToScreen(reportItemScreen);


        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }


    private void applyThemeToScreen(JPanel screen) {
        screen.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
        applyThemeToComponents(screen);
    }


    private void applyThemeToComponents(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
            } else if (comp instanceof JTextField || comp instanceof JTextArea) {
                comp.setBackground(isDarkMode ? DARK_COMPONENT_BG : LIGHT_COMPONENT_BG);
                comp.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            } else if (comp instanceof JButton && !(comp instanceof JToggleButton)) {
                comp.setBackground(isDarkMode ? DARK_COMPONENT_BG : LIGHT_COMPONENT_BG);
                comp.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            } else if (comp instanceof JLabel) {
                comp.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            } else if (comp instanceof JList || comp instanceof JTable) {
                comp.setBackground(isDarkMode ? DARK_COMPONENT_BG : LIGHT_COMPONENT_BG);
                comp.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            } else if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                scrollPane.getViewport().setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
                Component viewComp = scrollPane.getViewport().getView();
                if (viewComp != null) {
                    viewComp.setBackground(isDarkMode ? DARK_COMPONENT_BG : LIGHT_COMPONENT_BG);
                    if (viewComp instanceof JTextComponent) {
                        ((JTextComponent) viewComp).setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
                    }
                }
            }


            if (comp instanceof Container) {
                applyThemeToComponents((Container) comp);
            }
        }
    }


    public boolean isDarkModeEnabled() {
        return isDarkMode;
    }

    public Color getBorderColor() {
        return isDarkMode ? DARK_BORDER : LIGHT_BORDER;
    }
}
