
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import ui.MainFrame;

public class Main {
    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


            customizeUIDefaults();

        } catch (Exception e) {
            e.printStackTrace();
        }


        SwingUtilities.invokeLater(() -> {

            MainFrame mainFrame = new MainFrame();


            mainFrame.initializeEventHandlers();


            showSplashScreen(mainFrame);
        });
    }


    private static void customizeUIDefaults() {

        Color primaryColor = new Color(70, 130, 180);
        Color accentColor = new Color(255, 165, 0);
        Color lightBackground = new Color(240, 248, 255);


        UIManager.put("Button.background", primaryColor);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));


        UIManager.put("Panel.background", lightBackground);
        UIManager.put("OptionPane.background", lightBackground);


        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TextArea.font", new Font("Arial", Font.PLAIN, 12));
    }


    private static void showSplashScreen(MainFrame mainFrame) {


        mainFrame.setVisible(true);
    }
}