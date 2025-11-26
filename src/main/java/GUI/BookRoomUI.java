package GUI;

import model.User;
import room.CSVRoomLoader;
import room.Room;
import room.RoomRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class BookRoomUI extends JPanel {

    private static final String ROOMS_CSV_PATH =
            "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\rooms.csv";

    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton bookButton;
    private JButton backButton;
    private JButton toggleRoomButton;   // Admin-only button

    private JFrame parentFrame;
    private JPanel previousPanel;
    private User currentUser;           // Logged-in user from MainUI

    public BookRoomUI(JFrame parentFrame, JPanel previousPanel) {
        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;

        // Get the current user from MainUI
        if (parentFrame instanceof MainUI) {
            this.currentUser = ((MainUI) parentFrame).getUser();
        }

        setLayout(new BorderLayout());
        setBackground(new Color(0xF8F9FA));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Available Rooms", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(0x007BFF));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Load rooms from CSV
        CSVRoomLoader.loadRooms(ROOMS_CSV_PATH);
        List<Room> rooms = RoomRepository.getAllRooms();

        // Table setup
        String[] columns = {"RoomID", "Location", "Room Number", "Capacity", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Populate table
        for (Room room : rooms) {
            String status;
            if (!room.isEnabled()) {
                status = "Unavailable";
            } else if (room.isOccupied()) {
                status = "Occupied";
            } else {
                status = "Available";
            }

            Object[] rowData = {
                    room.getRoomID(),
                    room.getFullLocation(),
                    room.getRoomNumber(),
                    room.getCapacity(),
                    status
            };
            tableModel.addRow(rowData);
        }

        roomTable = new JTable(tableModel);
        roomTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roomTable.setRowHeight(35);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.setGridColor(new Color(0xE9ECEF));
        roomTable.setShowGrid(true);

        // Header
        roomTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        roomTable.getTableHeader().setBackground(new Color(0x007BFF));
        roomTable.getTableHeader().setForeground(Color.WHITE);

        // Custom cell renderer
        roomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String status = table.getValueAt(row, 4).toString();
                    if ("Occupied".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(0xFFE5E5));
                        c.setForeground(new Color(0xC82333));
                    } else if ("Unavailable".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(0xF8D7DA));
                        c.setForeground(new Color(0x721C24));
                    } else { // Available
                        c.setBackground(new Color(0xE8F8E8));
                        c.setForeground(new Color(0x1B4332));
                    }
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(new LineBorder(new Color(0xDEE2E6), 1, true));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(0xF8F9FA));

        // Book button
        bookButton = createStyledButton("Book Selected Room", new Color(0x007BFF));
        bookButton.addActionListener(new BookRoomAction());
        buttonPanel.add(bookButton);

        // Admin-only enable/disable button
        if (currentUser != null &&
                "admin".equalsIgnoreCase(currentUser.getAccountType())) {
            toggleRoomButton = createStyledButton("Enable / Disable Room", new Color(0x8B0000));
            toggleRoomButton.addActionListener(new ToggleRoomAction());
            buttonPanel.add(toggleRoomButton);
        }

        // Back button
        backButton = createStyledButton("Back", new Color(0x6C757D));
        backButton.addActionListener(e -> switchToPreviousPanel());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Booking action
     * Prevents booking Occupied or Unavailable rooms.
     */
    private class BookRoomAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "Please select a room first!",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String status = (String) tableModel.getValueAt(selectedRow, 4);
            if ("Occupied".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "This room is currently occupied!",
                        "Unavailable",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Unavailable".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "This room has been disabled by an admin and cannot be booked.",
                        "Unavailable",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String roomID = (String) tableModel.getValueAt(selectedRow, 0);
            Room selectedRoom = RoomRepository.getRoomByID(roomID);
            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "Could not find the selected room in repository.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the user from the main frame
            User user = null;
            if (parentFrame instanceof MainUI) {
                user = ((MainUI) parentFrame).getUser();
            }

            if (user == null) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "No logged-in user found. Please log in again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Go to SelectDates UI
            SelectDates selectDates = new SelectDates(parentFrame, BookRoomUI.this, selectedRoom, user);
            parentFrame.setContentPane(selectDates);
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    /**
     * Admin-only action to enable/disable a room.
     * Updates both the in-memory Room object and rooms.csv.
     */
    private class ToggleRoomAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "Please select a room to enable/disable.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String roomID = (String) tableModel.getValueAt(selectedRow, 0);
            String status = (String) tableModel.getValueAt(selectedRow, 4);

            // Don't allow disabling an occupied room
            if ("Occupied".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "Cannot disable an occupied room.",
                        "Action Not Allowed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Room room = RoomRepository.getRoomByID(roomID);
            if (room == null) {
                JOptionPane.showMessageDialog(BookRoomUI.this,
                        "Room not found in repository.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean currentlyUnavailable = "Unavailable".equalsIgnoreCase(status);
            boolean newEnabled = currentlyUnavailable;  // If now unavailable → enable; else disable

            // Update room status
            room.setEnabled(newEnabled);

            String newStatus = newEnabled ? "Available" : "Unavailable";
            tableModel.setValueAt(newStatus, selectedRow, 4);

            // Update status to rooms.csv
            updateRoomEnabledInCsv(roomID, newEnabled);

            JOptionPane.showMessageDialog(BookRoomUI.this,
                    "Room " + roomID + " is now " + newStatus + ".",
                    "Room Updated",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Updates the enabled/disabled flag in rooms.csv for the given roomID.
     */
    private void updateRoomEnabledInCsv(String roomID, boolean enabled) {
        File inputFile = new File(ROOMS_CSV_PATH);
        File tempFile = new File(ROOMS_CSV_PATH + ".tmp");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

            String line = br.readLine();
            if (line != null) {
                pw.println(line);
            }

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    pw.println(line);
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length > 0 && parts[0].trim().equals(roomID)) {

                    if (parts.length >= 5) {
                        // Assume 5th column is enabled flag
                        parts[4] = Boolean.toString(enabled);
                    } else {

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < parts.length; i++) {
                            if (i > 0) sb.append(",");
                            sb.append(parts[i]);
                        }
                        sb.append(",").append(Boolean.toString(enabled));
                        line = sb.toString();
                        pw.println(line);
                        continue;
                    }

                    // Rebuild line with updated parts
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        if (i > 0) sb.append(",");
                        sb.append(parts[i]);
                    }
                    line = sb.toString();
                }

                pw.println(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to update rooms.csv: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this,
                    "Failed to replace rooms.csv after updating.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchToPreviousPanel() {
        parentFrame.setContentPane(previousPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isEnabled() ? color.brighter() : new Color(180, 180, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(color.darker());
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
        return button;
    }
}
