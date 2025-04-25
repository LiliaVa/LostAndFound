
package ui;

import dao.LostItemDAO;
import dao.FoundItemDAO;
import model.LostItem;
import model.FoundItem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class HomeScreen extends JPanel {
    private MainFrame parent;


    private JButton lostItemsButton;
    private JButton foundItemsButton;
    private JButton reportItemButton;
    private JButton logoutButton;
    private JPanel recentItemsPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel welcomeLabel;


    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color ACCENT_COLOR = new Color(255, 165, 0);
    private final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color LOST_COLOR = new Color(255, 99, 71);
    private final Color FOUND_COLOR = new Color(60, 179, 113);


    public HomeScreen(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        JPanel titlePanel = createTitlePanel();


        JPanel navPanel = createNavPanel();


        JPanel searchPanel = createSearchPanel();


        JPanel itemsContainer = createItemsPanel();


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(navPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(itemsContainer, BorderLayout.CENTER);


        addEventHandlers();
    }


    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout(10, 5));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        welcomeLabel = new JLabel("Welcome to Lost and Found System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(PRIMARY_COLOR);

        titlePanel.add(welcomeLabel, BorderLayout.CENTER);

        return titlePanel;
    }


    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));


        lostItemsButton = createStyledButton("Lost Items", PRIMARY_COLOR);
        foundItemsButton = createStyledButton("Found Items", PRIMARY_COLOR);
        reportItemButton = createStyledButton("Report Item", ACCENT_COLOR);
        logoutButton = createStyledButton("Logout", Color.GRAY);

        navPanel.add(lostItemsButton);
        navPanel.add(foundItemsButton);
        navPanel.add(reportItemButton);
        navPanel.add(logoutButton);

        return navPanel;
    }


    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));

        return button;
    }


    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));


        searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }


    private JPanel createItemsPanel() {

        recentItemsPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        recentItemsPanel.setOpaque(false);
        recentItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JScrollPane scrollPane = new JScrollPane(recentItemsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);


        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);


        JLabel recentItemsTitle = new JLabel("Recent Items");
        recentItemsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        recentItemsTitle.setForeground(PRIMARY_COLOR);
        recentItemsTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

        containerPanel.add(recentItemsTitle, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }


    private void addEventHandlers() {
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.logout();
            }
        });

        lostItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showLostItemScreen();
            }
        });

        foundItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showFoundItemScreen();
            }
        });

        reportItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showReportItemScreen();
            }
        });
    }


    private void setupSearchHandler() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText().trim();
                if (!keyword.isEmpty()) {


                    JOptionPane.showMessageDialog(parent,
                            "Searching for: " + keyword,
                            "Search", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });
    }


    public void refreshData() {
        if (parent.getCurrentUser() != null) {
            welcomeLabel.setText("Welcome, " + parent.getCurrentUser().getName() + "!");
        }

        recentItemsPanel.removeAll();

        LostItemDAO lostItemDAO = new LostItemDAO();
        List<LostItem> recentLostItems = lostItemDAO.getAllLostItems();

        FoundItemDAO foundItemDAO = new FoundItemDAO();
        List<FoundItem> recentFoundItems = foundItemDAO.getAllFoundItems();

        for (int i = 0; i < Math.min(3, recentLostItems.size()); i++) {
            LostItem item = recentLostItems.get(i);
            ImageIcon icon = getItemImageFromBytes(item.getImage());
            recentItemsPanel.add(createItemCard(item.getTitle(), item.getLocation(),
                    "Lost on " + item.getFormattedDate(), true, icon));
        }

        for (int i = 0; i < Math.min(3, recentFoundItems.size()); i++) {
            FoundItem item = recentFoundItems.get(i);
            ImageIcon icon = getItemImageFromBytes(item.getImage());
            recentItemsPanel.add(createItemCard(item.getTitle(), item.getLocation(),
                    "Found on " + item.getFormattedDate(), false, icon));
        }

        if (recentLostItems.isEmpty() && recentFoundItems.isEmpty()) {
            JLabel noItemsLabel = new JLabel("No recent items to display");
            noItemsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noItemsLabel.setHorizontalAlignment(JLabel.CENTER);
            recentItemsPanel.add(noItemsLabel);
        }

        recentItemsPanel.revalidate();
        recentItemsPanel.repaint();

        setupSearchHandler();
    }


    private JPanel createItemCard(String title, String location, String dateInfo, boolean isLost, ImageIcon imageIcon) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);

        Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);
        card.setBorder(compoundBorder);

        // 🔹 Image section
        if (imageIcon != null) {
            Image img = imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(imageLabel);
            card.add(Box.createVerticalStrut(10));
        }

        JLabel statusLabel = new JLabel(isLost ? "LOST" : "FOUND");
        statusLabel.setForeground(isLost ? LOST_COLOR : FOUND_COLOR);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        locationPanel.setOpaque(false);
        JLabel locationIcon = new JLabel(" ");
        JLabel locationLabel = new JLabel(location);
        locationPanel.add(locationIcon);
        locationPanel.add(locationLabel);
        locationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel(dateInfo);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(5));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(locationPanel);
        card.add(Box.createVerticalStrut(5));
        card.add(dateLabel);
        card.add(Box.createVerticalStrut(5));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isLost) {
                    parent.showLostItemScreen();
                } else {
                    parent.showFoundItemScreen();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 255));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
            }
        });

        return card;
    }



    public void addEnhancements() {

    }

    private ImageIcon getItemImageFromBytes(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            try {
                Image image = ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));
                return new ImageIcon(image);
            } catch (IOException e) {
                System.err.println("Could not read image bytes.");
            }
        }
        return null;
    }
}
