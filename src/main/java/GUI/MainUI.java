package GUI;

import model.User;
import service.AccountManagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainUI extends JFrame {

    private static final String USERS_PATH = "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\Users.csv";

    // Constants for styling
    private static final Color PRIMARY_COLOR = new Color(0x007BFF); // Blue theme
    private static final Color SECONDARY_COLOR = new Color(0x6C757D); // Gray
    private static final Color BACKGROUND_COLOR = new Color(0xF8F9FA); // Light background
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int BORDER_RADIUS = 10;

    // Components
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField emailField, passwordField, roomIdField;
    private JComboBox<String> userTypeCombo;
    private JButton signupButton, loginButton;
    //private JButton bookButton;

    private User user;

    public MainUI() {
        initializeLookAndFeel();
        setTitle("York University Conference Room Scheduler");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Main panel with padding
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Add title with gradient
        titleLabel = createStyledLabel("Room Booking Dashboard", TITLE_FONT, PRIMARY_COLOR);
        addComponent(mainPanel, titleLabel, 0, 0, 2, 1, GridBagConstraints.CENTER);

        // User type selection
        JLabel userTypeLabel = createStyledLabel("User Type:", LABEL_FONT, SECONDARY_COLOR);
        addComponent(mainPanel, userTypeLabel, 0, 1, 1, 1, GridBagConstraints.WEST);
        userTypeCombo = new JComboBox<>(new String[]{"Student", "Faculty", "Staff", "Partner"});
        styleComboBox(userTypeCombo);
        addComponent(mainPanel, userTypeCombo, 1, 1, 1, 1, GridBagConstraints.EAST);

        // Email field
        JLabel emailLabel = createStyledLabel("Email:", LABEL_FONT, SECONDARY_COLOR);
        addComponent(mainPanel, emailLabel, 0, 2, 1, 1, GridBagConstraints.WEST);
        emailField = createStyledTextField();
        addComponent(mainPanel, emailField, 1, 2, 1, 1, GridBagConstraints.EAST);

        // Password field
        JLabel passwordLabel = createStyledLabel("Password:", LABEL_FONT, SECONDARY_COLOR);
        addComponent(mainPanel, passwordLabel, 0, 3, 1, 1, GridBagConstraints.WEST);
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        addComponent(mainPanel, passwordField, 1, 3, 1, 1, GridBagConstraints.EAST);

        /*
        // Room ID field
        JLabel roomIdLabel = createStyledLabel("Room ID:", LABEL_FONT, SECONDARY_COLOR);
        addComponent(mainPanel, roomIdLabel, 0, 4, 1, 1, GridBagConstraints.WEST);
        roomIdField = createStyledTextField();
        addComponent(mainPanel, roomIdField, 1, 4, 1, 1, GridBagConstraints.EAST);
         */

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton signupButton = createStyledButton("Signup", PRIMARY_COLOR);
        signupButton.addActionListener(e -> {
            // Switch to SignupPanel, passing mainFrame and mainPanel for back navigation
            Signup signupPanel = new Signup(this, mainPanel);
            setContentPane(signupPanel);
            revalidate();
            repaint();
        });
        buttonPanel.add(signupButton);

        loginButton = createStyledButton("Login", PRIMARY_COLOR);
        loginButton.addActionListener(new LoginAction());
        buttonPanel.add(loginButton);

        /*
        bookButton = createStyledButton("Book Room", PRIMARY_COLOR);
        bookButton.addActionListener(new BookAction());
        buttonPanel.add(bookButton);
        */

        addComponent(mainPanel, buttonPanel, 0, 5, 2, 1, GridBagConstraints.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    // Helper method to initialize Nimbus Look and Feel
    private void initializeLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not available, using default.");
        }
    }

    // Helper to create styled label
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    // Helper to create styled text field with rounded border
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        styleTextField(field);
        return field;
    }

    // Style text field or password field
    private void styleTextField(JTextField field) {
        field.setBorder(new LineBorder(SECONDARY_COLOR, 1, true)); // Rounded border
        field.setFont(LABEL_FONT);
        field.setBackground(Color.WHITE);
    }

    // Style combo box
    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(LABEL_FONT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(SECONDARY_COLOR, 1, true));
    }

    // Helper to create styled button with gradient
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, color.brighter(), 0, getHeight(), color);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                super.paintComponent(g);
            }
        };
        button.setFont(LABEL_FONT);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false); // For custom paint
        button.setBorder(new LineBorder(color.darker(), 1, true));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    // Helper to add component to GridBagLayout
    private void addComponent(JPanel panel, Component comp, int x, int y, int width, int height, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.anchor = anchor;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comp, gbc);
    }


    // Action Listener for Signup (Placeholder)
    private class SignupAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement signup logic (Req1)
            JOptionPane.showMessageDialog(null, "Signup successful!");
        }
    }

    // Action Listener for Login
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(((JPasswordField) passwordField).getPassword());
            String type = (String) userTypeCombo.getSelectedItem();

            // (Correct code, greyed out for development phase)
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in both email and password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (validateLogin(email, password, type)) {

                User user = AccountManagement.getInstance().findUserByEmail(email);
                setUser(user);

                JOptionPane.showMessageDialog(null, "Login successful!");

                // Switch to RoomsUI
                RoomsUI roomsUI = new RoomsUI(MainUI.this, mainPanel, getUser());
                setContentPane(roomsUI);
                revalidate();
                repaint();

            /*
            // For development phase only. Delete after
            if (validateLogin()) {

                JOptionPane.showMessageDialog(null, "Login successful!");

                // To RoomsUI
                RoomsUI roomsUI = new RoomsUI(MainUI.this, mainPanel);
                setContentPane(roomsUI);
                revalidate();
                repaint();
            */
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email, password, or user type!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    // (Correct code, greyed out for development phase)
    private boolean validateLogin(String email, String password, String type) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_PATH))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String storedEmail = parts[0].trim();
                String storedPassword = parts[1].trim();
                String storedType = parts[2].trim();

                if (storedEmail.equalsIgnoreCase(email) &&
                        storedPassword.equals(password) &&
                        storedType.equalsIgnoreCase(type)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            System.err.println("Login CSV read failed: " + ex.getMessage());
        }
        return false;
    }

    // For development phase only, delete after
    //private boolean  validateLogin() {return true;}

    /*
    // Action Listener for Book Room (Placeholder)
    private class BookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement booking logic (Req3)
            JOptionPane.showMessageDialog(null, "Room booked successfully!");
        }
    }
    */

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}