package GUI;

import command.BookingCommand;
import command.BookingInvoker;
import model.Booking;
import model.User;
import room.Room;
import room.RoomRepository;
import service.AccountManagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExistingBooking extends JPanel {

    private static final String BOOKINGS_CSV_PATH = System.getProperty("user.dir") + "/src/main/data/bookings.csv";

    private JFrame parentFrame;
    private JPanel previousPanel;
    private JTable bookingTable;

    private User user;

    public ExistingBooking(JFrame parentFrame, JPanel previousPanel, User user) {
        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;
        this.user = user;

        setLayout(new BorderLayout());
        setBackground(new Color(0xF8F9FA));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel header = new JLabel("Your Bookings", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(new Color(0x007BFF));
        add(header, BorderLayout.NORTH);

        // Load and display bookings from CSV
        List<BookingRow> bookings = loadBookingsFromCsv();
        populateTable(bookings);

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        add(scrollPane, BorderLayout.CENTER);

        // Cancel button
        JButton cancelButton = createStyledButton("Cancel", new Color(0xD9534F), 150);
        cancelButton.addActionListener(e -> cancelSelectedBooking());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setBackground(new Color(0xF8F9FA));
        actionPanel.add(cancelButton);

        add(actionPanel, BorderLayout.NORTH);

        // Back button
        JButton backButton = createStyledButton("Back", new Color(0x6C757D), 150);
        backButton.addActionListener(ev -> {
            parentFrame.setContentPane(previousPanel);  // previousPanel is RoomsUI
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(new Color(0xF8F9FA));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Edit button
        JButton editButton = createStyledButton("Edit Booking", new Color(0x17A2B8), 150);
        editButton.addActionListener(e -> openEditDates());
        backPanel.add(editButton);
    }

    private void populateTable(List<BookingRow> bookings) {

        removeAll();
        setLayout(new BorderLayout());
        setBackground(new Color(0xF8F9FA));

        JLabel header = new JLabel("Your Bookings", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(new Color(0x007BFF));
        add(header, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Room ID", "Check-in Time", "Check-out Time", "Deposit", "User", "Total Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        for (BookingRow booking : bookings) {
            model.addRow(new Object[]{
                    booking.bookingID,
                    booking.roomID,
                    booking.checkInTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    booking.checkOutTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    "$" + booking.deposit,
                    booking.user,
                    "$" + booking.totalPrice,
                    booking.status
            });
        }

        bookingTable = new JTable(model);
        bookingTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bookingTable.setRowHeight(25);
        bookingTable.setGridColor(new Color(0xE9ECEF));
        bookingTable.setShowGrid(true);
        bookingTable.getTableHeader().setBackground(new Color(0x007BFF));
        bookingTable.getTableHeader().setForeground(Color.WHITE);
        bookingTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        // Color rows by status
        bookingTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) model.getValueAt(row, 7);
                    switch (status) {
                        case "Pending" -> c.setBackground(new Color(0xFFF9C4));
                        case "Completed" -> c.setBackground(new Color(0xD4EDDA));
                        default -> c.setBackground(new Color(0xF8D7DA));
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        add(scrollPane, BorderLayout.CENTER);

        // BACK BUTTON
        JButton backButton = createStyledButton("Back", new Color(0x6C757D), 150);
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(previousPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        JPanel backPanel = new JPanel();
        backPanel.setBackground(new Color(0xF8F9FA));
        backPanel.add(backButton);

        add(backPanel, BorderLayout.SOUTH);

        // Final UI refresh
        revalidate();
        repaint();
    }

    // Load bookings from CSV
    private List<BookingRow> loadBookingsFromCsv() {
        List<BookingRow> bookings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_CSV_PATH))) {
            String line = br.readLine(); // Skip header
            if (line == null) {
                return bookings;
            }

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 8) continue; // Skip invalid rows

                try {
                    String bookingID = parts[0].trim();
                    String roomID = parts[1].trim();
                    LocalDateTime checkIn = LocalDateTime.parse(parts[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    LocalDateTime checkOut = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    double deposit = Double.parseDouble(parts[4].trim());
                    String user = parts[5].trim();
                    double totalPrice = Double.parseDouble(parts[6].trim());
                    String status = parts[7].trim();

                    bookings.add(new BookingRow(bookingID, roomID, checkIn, checkOut, deposit, user, totalPrice, status));
                } catch (Exception ex) {
                    System.err.println("Invalid booking row: " + line);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Cannot load bookings.csv\nError: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return bookings;
    }

    // Save a new booking to CSV
    public static void saveBookingToCsv(String bookingID, String roomID, LocalDateTime checkIn, LocalDateTime checkOut, double deposit, String user, double totalPrice, String status) {
        try (FileWriter writer = new FileWriter(BOOKINGS_CSV_PATH, true)) {
            // Add header if file is empty
            File file = new File(BOOKINGS_CSV_PATH);
            if (file.length() == 0) {
                writer.write("bookingID,roomID,checkInTime,checkOutTime,deposit,user,totalPrice,status\n");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            writer.write(String.format("%s,%s,%s,%s,%.2f,%s,%.2f,%s\n",
                    bookingID, roomID, checkIn.format(formatter), checkOut.format(formatter),
                    deposit, user, totalPrice, status));
            System.out.println("Booking saved to CSV: " + bookingID);
        } catch (IOException ex) {
            System.err.println("Failed to save booking to CSV: " + ex.getMessage());
        }
    }

    // Helper class for CSV rows
    private static class BookingRow {
        String bookingID, roomID, user, status;
        LocalDateTime checkInTime, checkOutTime;
        double deposit, totalPrice;

        BookingRow(String bookingID, String roomID, LocalDateTime checkInTime, LocalDateTime checkOutTime,
                   double deposit, String user, double totalPrice, String status) {
            this.bookingID = bookingID;
            this.roomID = roomID;
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
            this.deposit = deposit;
            this.user = user;
            this.totalPrice = totalPrice;
            this.status = status;
        }
    }

    private JButton createStyledButton(String text, Color color, int width) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isEnabled() ? color : new Color(180, 180, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(color.darker());
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(width, 50));
        return button;
    }

    private void cancelSelectedBooking() {
        int selectedRow = bookingTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel!");
            return;
        }

        String bookingID = bookingTable.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel booking " + bookingID + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = deleteBookingFromCsv(bookingID);

        if (success) {
            JOptionPane.showMessageDialog(this, "Booking cancelled successfully!");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Cancellation failed. Booking not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean deleteBookingFromCsv(String bookingID) {
        File file = new File(BOOKINGS_CSV_PATH);
        List<String> lines = new ArrayList<>();
        boolean removed = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header != null) lines.add(header);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(bookingID + ",")) {
                    removed = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error reading CSV during cancel: " + ex.getMessage());
            return false;
        }

        if (!removed) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String l : lines) {
                writer.write(l + "\n");
            }
        } catch (Exception ex) {
            System.err.println("Error writing CSV during cancel: " + ex.getMessage());
            return false;
        }

        return true;
    }

    void refreshTable() {
        List<BookingRow> bookings = loadBookingsFromCsv();
        populateTable(bookings);
        revalidate();
        repaint();
    }

    private void openEditDates() {
        int row = bookingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to edit.");
            return;
        }

        // Extract booking data from table
        String bookingID = bookingTable.getValueAt(row, 0).toString();
        String roomID = bookingTable.getValueAt(row, 1).toString();
        String checkInStr = bookingTable.getValueAt(row, 2).toString();
        String checkOutStr = bookingTable.getValueAt(row, 3).toString();
        String userEmail = bookingTable.getValueAt(row, 5).toString();

        Room room = RoomRepository.getRoomByID(roomID);
        User user = AccountManagement.getInstance().findUserByEmail(userEmail);

        EditDates editUI = new EditDates(parentFrame, this, bookingID, room, this.user, checkInStr, checkOutStr);
        parentFrame.setContentPane(editUI);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}