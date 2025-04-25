package ui;

import dao.FoundItemDAO;
import model.FoundItem;
import util.DateUtils;
import util.UIUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Date;


public class FoundItemScreen extends JPanel {
    private MainFrame parent;


    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton reportButton;
    private JTextField searchField;
    private JButton searchButton;


    private JButton deleteButton;
    private JButton editButton;
    private JButton refreshButton;


    private JComboBox<String> locationFilter;
    private JComboBox<String> dateFilter;
    private JButton clearFiltersButton;

    private List<FoundItem> currentItems;
    private List<FoundItem> allItems;

    private boolean updatingFilters = false;


    public FoundItemScreen(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));


        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Found Items");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel);


        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(240, 240, 245));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));


        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtons.setBackground(new Color(240, 240, 245));
        backButton = UIUtils.createStyledButton("Back to Home", new Color(70, 130, 180));
        leftButtons.add(backButton);


        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtons.setBackground(new Color(240, 240, 245));
        reportButton = UIUtils.createStyledButton("Report New Found Item", new Color(60, 179, 113));
        rightButtons.add(reportButton);

        navPanel.add(leftButtons, BorderLayout.WEST);
        navPanel.add(rightButtons, BorderLayout.EAST);


        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(240, 240, 245));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        searchButton = UIUtils.createStyledButton("Search", new Color(70, 130, 180));

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);


        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterPanel.setBackground(new Color(240, 240, 245));
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true),
                "Filters",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(70, 130, 180)));


        filterPanel.add(new JLabel("Location:"));
        locationFilter = new JComboBox<>();
        locationFilter.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(locationFilter);


        filterPanel.add(new JLabel("Date:"));
        dateFilter = new JComboBox<>();
        dateFilter.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(dateFilter);


        clearFiltersButton = UIUtils.createStyledButton("Clear Filters", new Color(169, 169, 169));
        filterPanel.add(clearFiltersButton);


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 245));
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(navPanel, BorderLayout.CENTER);

        JPanel searchFilterPanel = new JPanel(new BorderLayout());
        searchFilterPanel.setBackground(new Color(240, 240, 245));
        searchFilterPanel.add(searchPanel, BorderLayout.NORTH);
        searchFilterPanel.add(filterPanel, BorderLayout.SOUTH);

        topPanel.add(searchFilterPanel, BorderLayout.SOUTH);


        createItemTable();
        JScrollPane tableScrollPane = new JScrollPane(itemTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel itemManagementPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        itemManagementPanel.setBackground(new Color(240, 240, 245));
        itemManagementPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        deleteButton = UIUtils.createStyledButton("Delete Item", new Color(220, 20, 60));
        editButton = UIUtils.createStyledButton("Edit Item", new Color(255, 140, 0));
        refreshButton = UIUtils.createStyledButton("Refresh", new Color(70, 130, 180));


        deleteButton.setEnabled(false);
        editButton.setEnabled(false);

        itemManagementPanel.add(editButton);
        itemManagementPanel.add(deleteButton);
        itemManagementPanel.add(refreshButton);


        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(itemManagementPanel, BorderLayout.SOUTH);


        addEventHandlers();
    }


    private void createItemTable() {

        String[] columnNames = {"ID", "Item Name", "Location", "Date Found", "Description"};


        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        itemTable = new JTable(tableModel);
        itemTable.setRowHeight(30);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 13));
        itemTable.setSelectionBackground(new Color(173, 216, 230));
        itemTable.setSelectionForeground(Color.BLACK);


        TableColumnModel columnModel = itemTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(500);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }


        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        itemTable.setShowGrid(true);
        itemTable.setGridColor(new Color(200, 200, 220));


        JTableHeader header = itemTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
    }


    private void addEventHandlers() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showHomeScreen();
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showReportItemScreen();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText().trim();
                if (!keyword.isEmpty()) {
                    searchItems(keyword);
                } else {
                    refreshData();
                }
            }
        });


        itemTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = itemTable.getSelectedRow();
                    if (row >= 0 && row < currentItems.size()) {
                        FoundItem selectedItem = currentItems.get(row);
                        showItemDetails(selectedItem);
                    }
                }
            }
        });


        locationFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updatingFilters) {
                    applyFilters();
                }
            }
        });

        dateFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updatingFilters) {
                    applyFilters();
                }
            }
        });

        clearFiltersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedItem();
            }
        });


        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
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


        addTableSelectionListener();


        if (itemTable.getRowCount() > 0) {
            itemTable.setRowSelectionInterval(0, 0);
            updateButtonStates();
        }
    }


    private void updateButtonStates() {
        boolean hasSelection = (itemTable.getSelectedRow() != -1);
        deleteButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
    }


    private void addTableSelectionListener() {
        itemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
    }


    private void searchItems(String keyword) {
        FoundItemDAO dao = new FoundItemDAO();
        currentItems = dao.searchFoundItems(keyword);
        updateTableData();
    }


    private void showItemDetails(FoundItem item) {
        StringBuilder details = new StringBuilder();
        details.append("Item: ").append(item.getTitle()).append("\n\n");
        details.append("Date Found: ").append(DateUtils.formatDate(item.getDateFound())).append("\n\n");
        details.append("Location: ").append(item.getLocation()).append("\n\n");
        details.append("Description: ").append(item.getDescription()).append("\n");

        // Text area with item details
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(250, 250, 255));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Buttons
        JButton editButton = UIUtils.createStyledButton("Edit", new Color(255, 140, 0));
        JButton deleteButton = UIUtils.createStyledButton("Delete", new Color(220, 20, 60));
        JButton closeButton = UIUtils.createStyledButton("Close", new Color(70, 130, 180));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(250, 250, 255));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Image section
        JLabel imageLabel = new JLabel();
        byte[] imageBytes = item.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            try {
                Image image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                Image scaled = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            } catch (IOException e) {
                System.err.println("Could not read image bytes.");
                imageLabel.setText("Image not available");
            }
        } else {
            imageLabel.setText("No image available");
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // Assemble dialog content
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(new Color(250, 250, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(imageLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Found Item Details");
        dialog.setModal(true);
        dialog.setContentPane(contentPanel);

        closeButton.addActionListener(e -> dialog.dispose());

        editButton.addActionListener(e -> {
            dialog.dispose();
            editItem(item);
        });

        deleteButton.addActionListener(e -> {
            dialog.dispose();
            deleteItem(item);
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    public void refreshData() {
        FoundItemDAO dao = new FoundItemDAO();
        allItems = dao.getAllFoundItems();
        currentItems = new ArrayList<>(allItems);
        updateTableData();
        updateFilterOptions();


        if (itemTable.getRowCount() > 0) {
            itemTable.setRowSelectionInterval(0, 0);
            updateButtonStates();
        }
    }


    private void updateTableData() {

        tableModel.setRowCount(0);


        for (FoundItem item : currentItems) {
            Object[] rowData = {
                item.getFoundItemID(),
                item.getTitle(),
                item.getLocation(),
                DateUtils.formatDate(item.getDateFound()),
                item.getDescription()
            };
            tableModel.addRow(rowData);
        }


        itemTable.revalidate();
        itemTable.repaint();


        updateButtonStates();
    }


    private void updateFilterOptions() {

        updatingFilters = true;

        try {

            String selectedLocation = (locationFilter.getSelectedItem() != null) ?
                                    locationFilter.getSelectedItem().toString() : null;
            String selectedDate = (dateFilter.getSelectedItem() != null) ?
                                dateFilter.getSelectedItem().toString() : null;


            locationFilter.removeAllItems();
            dateFilter.removeAllItems();


            locationFilter.addItem("All Locations");
            dateFilter.addItem("All Dates");


            java.util.Set<String> locations = new java.util.HashSet<>();
            java.util.Set<String> dates = new java.util.HashSet<>();


            for (FoundItem item : allItems) {
                if (item.getLocation() != null && !item.getLocation().isEmpty()) {
                    locations.add(item.getLocation());
                }

                if (item.getDateFound() != null) {
                    dates.add(DateUtils.formatDate(item.getDateFound()));
                }
            }


            for (String location : locations) {
                locationFilter.addItem(location);
            }

            for (String date : dates) {
                dateFilter.addItem(date);
            }


            if (selectedLocation != null) {
                for (int i = 0; i < locationFilter.getItemCount(); i++) {
                    if (locationFilter.getItemAt(i).equals(selectedLocation)) {
                        locationFilter.setSelectedIndex(i);
                        break;
                    }
                }
            }

            if (selectedDate != null) {
                for (int i = 0; i < dateFilter.getItemCount(); i++) {
                    if (dateFilter.getItemAt(i).equals(selectedDate)) {
                        dateFilter.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } finally {

            updatingFilters = false;
        }
    }

    private void applyFilters() {
        if (allItems == null || locationFilter.getSelectedItem() == null || dateFilter.getSelectedItem() == null) {
            return;
        }


        String locationValue = locationFilter.getSelectedItem().toString();
        String dateValue = dateFilter.getSelectedItem().toString();


        currentItems = new ArrayList<>();


        for (FoundItem item : allItems) {
            boolean includeItem = true;


            if (!locationValue.equals("All Locations")) {
                if (!item.getLocation().equals(locationValue)) {
                    includeItem = false;
                }
            }


            if (!dateValue.equals("All Dates")) {
                String itemDate = DateUtils.formatDate(item.getDateFound());
                if (!itemDate.equals(dateValue)) {
                    includeItem = false;
                }
            }


            if (includeItem) {
                currentItems.add(item);
            }
        }


        updateTableData();
    }

    private void clearFilters() {
        updatingFilters = true;
        try {
            locationFilter.setSelectedIndex(0);
            dateFilter.setSelectedIndex(0);


            currentItems = new ArrayList<>(allItems);
            updateTableData();
        } finally {
            updatingFilters = false;
            applyFilters();
        }
    }


    private void deleteSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < currentItems.size()) {
            FoundItem selectedItem = currentItems.get(selectedRow);
            deleteItem(selectedItem);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select an item to delete",
                "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteItem(FoundItem item) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this found item?\n" +
            "Item: " + item.getTitle() + "\n" +
            "Date: " + DateUtils.formatDate(item.getDateFound()),
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            FoundItemDAO dao = new FoundItemDAO();
            boolean success = dao.deleteFoundItem(item.getFoundItemID());

            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "Item deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                refreshData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete the item",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void editSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < currentItems.size()) {
            FoundItem selectedItem = currentItems.get(selectedRow);
            editItem(selectedItem);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select an item to edit",
                "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editItem(FoundItem item) {

        JDialog editDialog = new JDialog();
        editDialog.setTitle("Edit Found Item");
        editDialog.setModal(true);
        editDialog.setLayout(new BorderLayout());


        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(250, 250, 255));


        JLabel titleLabel = new JLabel("Item Title:");
        JTextField titleField = new JTextField(item.getTitle(), 20);


        JLabel locationLabel = new JLabel("Location:");
        JTextField locationField = new JTextField(item.getLocation(), 20);


        JLabel dateLabel = new JLabel("Date Found (dd/mm/yyyy):");
        JTextField dateField = new JTextField(DateUtils.formatDate(item.getDateFound()), 10);


        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionArea = new JTextArea(item.getDescription(), 4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);


        formPanel.add(titleLabel);
        formPanel.add(titleField);
        formPanel.add(locationLabel);
        formPanel.add(locationField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(descriptionLabel);
        formPanel.add(descScrollPane);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(250, 250, 255));

        JButton saveButton = UIUtils.createStyledButton("Save Changes", new Color(60, 179, 113));
        JButton cancelButton = UIUtils.createStyledButton("Cancel", new Color(220, 20, 60));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);


        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);


        cancelButton.addActionListener(e -> editDialog.dispose());

        saveButton.addActionListener(e -> {

            String title = titleField.getText().trim();
            String location = locationField.getText().trim();
            String dateStr = dateField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (title.isEmpty() || location.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog,
                    "Please fill in all required fields",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!DateUtils.isValidDate(dateStr)) {
                JOptionPane.showMessageDialog(editDialog,
                    "Please enter a valid date (dd/mm/yyyy)",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {

                item.setTitle(title);
                item.setLocation(location);
                item.setDateFound(DateUtils.parseDate(dateStr));
                item.setDescription(description);


                FoundItemDAO dao = new FoundItemDAO();
                boolean success = dao.updateFoundItem(item);

                if (success) {
                    JOptionPane.showMessageDialog(editDialog,
                        "Item updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    editDialog.dispose();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(editDialog,
                        "Failed to update item",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });


        editDialog.pack();
        editDialog.setSize(500, 350);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }
}
