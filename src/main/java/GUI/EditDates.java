package GUI;

import model.User;
import room.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class EditDates extends JPanel {

    private JFrame parentFrame;
    private JPanel previousPanel;
    private Room selectedRoom;
    private User currentUser;
    private String bookingID;

    // Components
    private JTextField checkInField, checkOutField;
    private JButton confirmButton, backButton;

    public EditDates(JFrame parentFrame, JPanel previousPanel,
                     String bookingID,
                     Room selectedRoom,
                     User loggedInUser,
                     String currentCheckIn,
                     String currentCheckOut) {

        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;
        this.selectedRoom = selectedRoom;
        this.currentUser = loggedInUser;
        this.bookingID = bookingID;

        setLayout(new GridBagLayout());
        setBackground(new Color(0xF8F9FA));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Edit Booking Dates", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(0x007BFF));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // Check-in
        JLabel checkInLabel = new JLabel("New Check-in Time (YYYY-MM-DD HH:MM):");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(checkInLabel, gbc);

        checkInField = new JTextField(20);
        checkInField.setBorder(new LineBorder(new Color(0xCED4DA)));
        checkInField.setText(currentCheckIn);   // Pre-populate
        gbc.gridx = 1;
        add(checkInField, gbc);

        // Check-out
        JLabel checkOutLabel = new JLabel("New Check-out Time (YYYY-MM-DD HH:MM):");
        gbc.gridx = 0; gbc.gridy = 2;
        add(checkOutLabel, gbc);

        checkOutField = new JTextField(20);
        checkOutField.setBorder(new LineBorder(new Color(0xCED4DA)));
        checkOutField.setText(currentCheckOut); // Pre-populate
        gbc.gridx = 1;
        add(checkOutField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton("Confirm New Dates");
        backButton = new JButton("Back");

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        confirmButton.addActionListener(new ConfirmEditAction());
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(previousPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
    }

    private class ConfirmEditAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                LocalDateTime newCheckIn = LocalDateTime.parse(checkInField.getText().trim(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                LocalDateTime newCheckOut = LocalDateTime.parse(checkOutField.getText().trim(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                if (newCheckOut.isBefore(newCheckIn)) {
                    JOptionPane.showMessageDialog(EditDates.this,
                            "Check-out must be AFTER check-in.", "Invalid", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                updateBookingInCSV(bookingID, newCheckIn, newCheckOut);

                JOptionPane.showMessageDialog(EditDates.this,
                        "Booking updated successfully!");

                if (previousPanel instanceof ExistingBooking eb) {
                    eb.refreshTable();     // ← reload table
                }

                parentFrame.setContentPane(previousPanel);
                parentFrame.revalidate();
                parentFrame.repaint();


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(EditDates.this,
                        "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateBookingInCSV(String bookingID, LocalDateTime newCheckIn, LocalDateTime newCheckOut) {

        File inputFile = new File("E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\bookings.csv");
        File tempFile = new File("booking_temp.csv");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

            String header = br.readLine();
            pw.println(header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (!parts[0].equals(bookingID)) {
                    pw.println(line);
                    continue;
                }

                // Recalculate total price
                long minutes = java.time.Duration.between(newCheckIn, newCheckOut).toMinutes();
                int hours = (int) Math.ceil(minutes / 60.0);

                double hourlyRate = switch (currentUser.getAccountType().toLowerCase()) {
                    case "student" -> 20.0;
                    case "faculty" -> 30.0;
                    case "staff"   -> 40.0;
                    default -> 50.0;
                };
                double newTotalPrice = hours * hourlyRate;

                // Write updated row
                pw.printf("%s,%s,%s,%s,%.2f,%s,%.2f,%s%n",
                        bookingID,
                        parts[1],                                   // roomID
                        newCheckIn.format(fmt),
                        newCheckOut.format(fmt),
                        Double.parseDouble(parts[4]),               // deposit unchanged
                        parts[5],                                   // user
                        newTotalPrice,
                        "Pending"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
}