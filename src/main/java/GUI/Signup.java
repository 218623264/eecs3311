package GUI;

import factory.ConcreteUserFactory;
import factory.UserFactory;
import model.User;
import service.AccountManagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Set;

/**
 * A separate class for the Signup panel, handling all signup-related logic and UI.
 * This panel can be added to the main frame's content pane when the Signup button is clicked.
 *
 * Features:
 * - Email validation (unique and valid format).
 * - Password strength check (uppercase, lowercase, number, symbol).
 * - Account type selection (student, faculty, staff, partner; extensible via combo box).
 * - Verification for university accounts (student, faculty, staff) via an additional ID field.
 * - Simulated in-memory user storage for uniqueness check.
 * - Back button to return to the main login panel.
 */
public class Signup extends JPanel {

    private static final String UACCOUNTS_PATH = "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\UAccounts.csv";
    private static final String USERS_PATH = "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\Users.csv";

    // In-memory user storage for simulation (email uniqueness)
    private static List<String> existingEmails = new ArrayList<>();

    // Components
    private JTextField emailField, idField;
    private JPasswordField passwordField;
    private JComboBox<String> accountTypeCombo;
    private JLabel verificationLabel;
    private JButton signupButton, backButton;

    // Reference to the main frame to switch panels
    private JFrame mainFrame;
    private JPanel originalPanel;  // To switch back

    public Signup(JFrame mainFrame, JPanel originalPanel) {
        this.mainFrame = mainFrame;
        this.originalPanel = originalPanel;

        setLayout(new GridBagLayout());
        setBackground(new Color(0xF8F9FA));  // Light background
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Sign Up");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(0x007BFF));
        addComponent(this, title, 0, 0, 2, 1, GridBagConstraints.CENTER);

        // Account Type
        JLabel typeLabel = createStyledLabel("Account Type:");
        addComponent(this, typeLabel, 0, 1, 1, 1, GridBagConstraints.WEST);
        accountTypeCombo = new JComboBox<>(new String[]{"Student", "Faculty", "Staff", "Partner"});
        styleComboBox(accountTypeCombo);
        accountTypeCombo.addActionListener(e -> toggleVerificationField());  // Show/hide verification based on type
        addComponent(this, accountTypeCombo, 1, 1, 1, 1, GridBagConstraints.EAST);

        // Email
        JLabel emailLabel = createStyledLabel("Email:");
        addComponent(this, emailLabel, 0, 2, 1, 1, GridBagConstraints.WEST);
        emailField = createStyledTextField();
        addComponent(this, emailField, 1, 2, 1, 1, GridBagConstraints.EAST);

        // Password
        JLabel passwordLabel = createStyledLabel("Password:");
        addComponent(this, passwordLabel, 0, 3, 1, 1, GridBagConstraints.WEST);
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        addComponent(this, passwordField, 1, 3, 1, 1, GridBagConstraints.EAST);

        // Verification ID (hidden initially)
        verificationLabel = createStyledLabel("Student/Faculty/Staff ID:");
        idField = createStyledTextField();
        toggleVerificationField();  // Initial toggle

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(0xF8F9FA));

        signupButton = createStyledButton("Sign Up", new Color(0x007BFF));
        signupButton.addActionListener(new SignupAction());
        buttonPanel.add(signupButton);

        backButton = createStyledButton("Back", new Color(0x6C757D));
        backButton.addActionListener(e -> switchToOriginalPanel());
        buttonPanel.add(backButton);

        addComponent(this, buttonPanel, 0, 6, 2, 1, GridBagConstraints.CENTER);
    }

    // Toggle visibility of verification field based on account type
    private void toggleVerificationField() {
        String selectedType = (String) accountTypeCombo.getSelectedItem();
        boolean isUniversityType = selectedType.equals("Student") || selectedType.equals("Faculty") || selectedType.equals("Staff");

        if (isUniversityType) {
            addComponent(this, verificationLabel, 0, 4, 1, 1, GridBagConstraints.WEST);
            addComponent(this, idField, 1, 4, 1, 1, GridBagConstraints.EAST);
        } else {
            remove(verificationLabel);
            remove(idField);
        }
        revalidate();
        repaint();
    }

    // ==================== NEW: CSV VERIFICATION & PERSISTENCE ====================
    private boolean verifyUniversityId(String type, String id) {
        if (type.equals("Partner")) return true; // no verification needed

        try (BufferedReader br = new BufferedReader(new FileReader(UACCOUNTS_PATH))) {
            String line = br.readLine(); // Read header line
            if (line == null) return false;

            // Find which column contains "userID" or "id" (case-insensitive)
            String[] headers = line.split(",");
            int idColumnIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("userID") ||
                        headers[i].trim().equalsIgnoreCase("id")) {
                    idColumnIndex = i;
                    break;
                }
            }

            if (idColumnIndex == -1) {
                JOptionPane.showMessageDialog(this, "UAccounts.csv format error: 'userID' column not found",
                        "CSV Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Now read the actual data rows
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length <= Math.max(1, idColumnIndex)) continue;

                String rowType = parts[0].trim();           // First column = type (student/faculty/staff)
                String rowId = parts[idColumnIndex].trim(); // Column with header "userID"

                if (rowType.equalsIgnoreCase(type) && rowId.equalsIgnoreCase(id.trim())) {
                    return true;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Cannot read UAccounts.csv\nError: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return false;
    }

    private void saveUserToCsv(String email, String password, String type, String id) {
        boolean verified = !type.equals("Partner"); // university accounts are verified if ID matched

        try (FileWriter writer = new FileWriter(USERS_PATH, true)) {
            // Add header if file is new
            File file = new File(USERS_PATH);
            if (file.length() == 0) {
                writer.write("email,password,type,id,verified\n");
            }

            writer.write(String.format("%s,%s,%s,%s,%b\n", email, password, type, id, verified));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save user data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // =============================================================================

    // Action for Signup button
    private class SignupAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String type = (String) accountTypeCombo.getSelectedItem();
            String id = idField.getText();  // For verification

            // Validate email uniqueness and format
            if (!isValidEmail(email) || existingEmails.contains(email)) {
                JOptionPane.showMessageDialog(null, "Invalid or duplicate email!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate password strength
            if (!isStrongPassword(password)) {
                JOptionPane.showMessageDialog(null, "Password must include uppercase, lowercase, number, and symbol!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verify university accounts
            boolean isUniversityType = type.equals("Student") || type.equals("Faculty") || type.equals("Staff");
            if (isUniversityType && !verifyUniversityId(type, id)) {
                JOptionPane.showMessageDialog(null, "Invalid " + type + " ID!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Account creation
            existingEmails.add(email);
            saveUserToCsv(email, password, type, id);

            // Create new User object based on entered information
            UserFactory factory = new ConcreteUserFactory();
            factory.createUser(type, email, password, id);

            JOptionPane.showMessageDialog(null, "Account created successfully for " + type + "!");
            switchToOriginalPanel();  // Return to main panel
        }
    }

    // Switch back to original login panel
    private void switchToOriginalPanel() {
        mainFrame.setContentPane(originalPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Validation methods
    private boolean isValidEmail(String email) {
        // Simple regex for valid email
        return Pattern.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+[.])+[\\w]+[\\w]$", email);
    }

    private boolean isStrongPassword(String password) {
        // Check for uppercase, lowercase, number, symbol
        if (password == null || password.isEmpty()) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSymbol = password.matches(".*[@$!%*?&].*");

        return hasUpper && hasLower && hasDigit && hasSymbol;
    }

    /*
    private boolean verifyUniversityId(String userID, String type) {
        // Simulate verification (e.g., check format)
        // For student: starts with 'S', etc. In real system, query database/API
        return !userID.isEmpty();  // Placeholder: assume valid if not empty
    }
     */

    // Styling helpers (reused from original GUI for consistency)
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(0x6C757D));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBorder(new LineBorder(new Color(0x6C757D), 1, true));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        return field;
    }

    private void styleTextField(JComponent field) {
        field.setBorder(new LineBorder(new Color(0x6C757D), 1, true));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(new Color(0x6C757D), 1, true));
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, color.brighter(), 0, getHeight(), color);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(new LineBorder(color.darker(), 1, true));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    // GridBagLayout helper
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
}