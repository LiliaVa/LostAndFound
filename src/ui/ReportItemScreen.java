
package ui;

import dao.LostItemDAO;
import dao.FoundItemDAO;
import dao.UserDAO;
import model.LostItem;
import model.FoundItem;
import model.User;
import util.DateUtils;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.ParseException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class ReportItemScreen extends JPanel {
    private MainFrame parent;


    private JRadioButton lostRadio;
    private JRadioButton foundRadio;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField locationField;
    private JFormattedTextField dateField;
    private JButton uploadImageButton;
    private JLabel imageLabel;
    private JButton submitButton;
    private JButton cancelButton;
    private JLabel statusLabel;

    private byte[] selectedImage;
    private BufferedImage previewImage;
    private JPanel imagePreviewPanel;


    public ReportItemScreen(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));


        JPanel titlePanel = new JPanel();
        JLabel screenTitle = new JLabel("Report an Item");
        screenTitle.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(screenTitle);


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));


        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBorder(BorderFactory.createTitledBorder("Item Type"));

        lostRadio = new JRadioButton("Lost Item");
        foundRadio = new JRadioButton("Found Item");
        lostRadio.setSelected(true);

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(lostRadio);
        typeGroup.add(foundRadio);

        typePanel.add(lostRadio);
        typePanel.add(foundRadio);


        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));

        JLabel titleLabel = new JLabel("Item Title:");
        titleField = new JTextField(20);

        JLabel locationLabel = new JLabel("Location:");
        locationField = new JTextField(20);

        JLabel dateLabel = new JLabel("Date (dd/mm/yyyy):");
        dateField = new JFormattedTextField();
        dateField.setColumns(10);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);

        detailsPanel.add(titleLabel);
        detailsPanel.add(titleField);
        detailsPanel.add(locationLabel);
        detailsPanel.add(locationField);
        detailsPanel.add(dateLabel);
        detailsPanel.add(dateField);
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(descScrollPane);


        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(BorderFactory.createTitledBorder("Item Image (Optional)"));
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        uploadImageButton = new JButton("Upload Image");
        uploadImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageLabel = new JLabel("No image selected");
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        imagePreviewPanel = new JPanel();
        imagePreviewPanel.setPreferredSize(new Dimension(200, 200));
        imagePreviewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePreviewPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imagePanel.add(uploadImageButton);
        imagePanel.add(Box.createVerticalStrut(10));
        imagePanel.add(imageLabel);
        imagePanel.add(Box.createVerticalStrut(10));
        imagePanel.add(imagePreviewPanel);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButton = new JButton("Submit Report");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);


        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusPanel.add(statusLabel);


        formPanel.add(typePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(detailsPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(imagePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buttonPanel);


        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);


        addEventHandlers();
    }


    private void addEventHandlers() {

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
                parent.showHomeScreen();
            }
        });


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    submitReport();
                }
            }
        });


        uploadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });
    }


    private boolean validateForm() {

        if (titleField.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter an item title");
            titleField.requestFocus();
            return false;
        }


        if (locationField.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter a location");
            locationField.requestFocus();
            return false;
        }


        if (dateField.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter a date");
            dateField.requestFocus();
            return false;
        }


        if (!DateUtils.isValidDate(dateField.getText().trim())) {
            statusLabel.setText("Please enter a valid date (dd/mm/yyyy)");
            dateField.requestFocus();
            return false;
        }

        return true;
    }


    private void submitReport() {
        try {

            User currentUser = parent.getCurrentUser();
            if (currentUser == null) {
                statusLabel.setText("You must be logged in to report an item");
                return;
            }


            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String location = locationField.getText().trim();


            Date itemDate = DateUtils.parseDate(dateField.getText().trim());


            int itemId = (int)(Math.random() * 10000);

            if (lostRadio.isSelected()) {

                LostItem item = new LostItem(itemId, title, description, itemDate, location);
                if (selectedImage != null) {
                    item.setImage(selectedImage);
                }


                LostItemDAO dao = new LostItemDAO();
                boolean success = dao.reportLostItem(item);

                if (success) {
                    JOptionPane.showMessageDialog(parent,
                            "Lost item reported successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    resetForm();
                    parent.showHomeScreen();
                } else {
                    statusLabel.setText("Error reporting lost item. Please try again.");
                }

            } else {

                FoundItem item = new FoundItem(itemId, title, description, itemDate, location);
                if (selectedImage != null) {
                    item.setImage(selectedImage);
                }


                FoundItemDAO dao = new FoundItemDAO();
                boolean success = dao.reportFoundItem(item);

                if (success) {
                    JOptionPane.showMessageDialog(parent,
                            "Found item reported successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    resetForm();
                    parent.showHomeScreen();
                } else {
                    statusLabel.setText("Error reporting found item. Please try again.");
                }
            }

        } catch (ParseException e) {
            statusLabel.setText("Error with date format. Please use dd/mm/yyyy.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            imageLabel.setText(selectedFile.getName());

            try {

                FileInputStream fis = new FileInputStream(selectedFile);
                selectedImage = new byte[(int) selectedFile.length()];
                fis.read(selectedImage);
                fis.close();


                updateImagePreview(selectedFile);
            } catch (IOException e) {
                statusLabel.setText("Error loading image: " + e.getMessage());
                selectedImage = null;
                imageLabel.setText("No image selected");
                clearImagePreview();
            }
        }
    }


    private void resetForm() {
        titleField.setText("");
        descriptionArea.setText("");
        locationField.setText("");
        dateField.setText("");
        lostRadio.setSelected(true);
        selectedImage = null;
        imageLabel.setText("No image selected");
        statusLabel.setText("");
        clearImagePreview();
    }


    private void updateImagePreview(File imageFile) {
        try {

            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                throw new IOException("Failed to read image file");
            }


            int maxWidth = 180;
            int maxHeight = 180;

            double widthRatio = (double) maxWidth / originalImage.getWidth();
            double heightRatio = (double) maxHeight / originalImage.getHeight();
            double ratio = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (originalImage.getWidth() * ratio);
            int newHeight = (int) (originalImage.getHeight() * ratio);


            previewImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = previewImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();


            imagePreviewPanel.removeAll();
            imagePreviewPanel.setLayout(new BorderLayout());
            imagePreviewPanel.add(new JLabel(new ImageIcon(previewImage)), BorderLayout.CENTER);
            imagePreviewPanel.revalidate();
            imagePreviewPanel.repaint();

        } catch (IOException e) {
            clearImagePreview();
            statusLabel.setText("Error creating image preview: " + e.getMessage());
        }
    }

    private void clearImagePreview() {
        previewImage = null;
        imagePreviewPanel.removeAll();
        imagePreviewPanel.revalidate();
        imagePreviewPanel.repaint();
    }
}
