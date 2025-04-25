
package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;


public class UIUtils {


    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    public static final Color ACCENT_COLOR = new Color(255, 165, 0);
    public static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    public static final Color CARD_COLOR = new Color(255, 255, 255);
    public static final Color LOST_COLOR = new Color(255, 99, 71);
    public static final Color FOUND_COLOR = new Color(60, 179, 113);


    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));

        return button;
    }


    public static JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }


    public static ImageIcon createPlaceholderIcon(int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setColor(PRIMARY_COLOR);
        g2d.fillOval(10, 10, width - 40, height - 40);


        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(width - 25, height - 25, width - 10, height - 10);


        g2d.setColor(Color.WHITE);
        g2d.fillOval(20, 20, width - 60, height - 60);

        g2d.dispose();
        return new ImageIcon(image);
    }


    public static ImageIcon byteArrayToImageIcon(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(bis);
            return new ImageIcon(image);
        } catch (IOException e) {
            System.err.println("Error converting byte array to ImageIcon: " + e.getMessage());
            return null;
        }
    }


    public static byte[] imageIconToByteArray(ImageIcon icon) {
        if (icon == null) {
            return null;
        }

        try {
            BufferedImage image = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB
            );

            Graphics g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Error converting ImageIcon to byte array: " + e.getMessage());
            return null;
        }
    }


    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return null;
        }

        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }


    public static JPanel createTitledPanel(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }


    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }


    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
