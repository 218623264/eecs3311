package GUI;

import command.BookingCommand;
import command.BookingInvoker;
import command.CreateBookingCommand;
import model.Booking;
import model.User;
import room.Room;
import room.RoomRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SelectDates extends JPanel {

    private JFrame parentFrame;
    private JPanel previousPanel;
    private Room selectedRoom;
    private User user;

    // Components
    private JLabel roomDetailsLabel;
    private JTextField checkInField, checkOutField;
    private JLabel depositLabel;
    private JLabel warningLabel;
    private JButton confirmButton, backButton;

    public SelectDates(JFrame parentFrame, JPanel previousPanel, Room selectedRoom, User user) {
        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;
        this.selectedRoom = selectedRoom;
        this.user = user;

        setLayout(new GridBagLayout());
        setBackground(new Color(0xF8F9FA));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel title = new JLabel("Select Booking Dates", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(0x007BFF));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // Room Details
        roomDetailsLabel = new JLabel(getRoomDetailsText());
        roomDetailsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        roomDetailsLabel.setForeground(new Color(0x007BFF));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        add(roomDetailsLabel, gbc);

        // Check-in Time
        JLabel checkInLabel = new JLabel("Check-in Time (YYYY-MM-DD HH:MM):");
        checkInLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(checkInLabel, gbc);

        checkInField = new JTextField(20);
        checkInField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkInField.setBorder(new LineBorder(new Color(0xCED4DA), 1, true));
        //checkInField.setText(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // Default: 1 hour from now
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(checkInField, gbc);

        // Check-out Time
        JLabel checkOutLabel = new JLabel("Check-out Time (YYYY-MM-DD HH:MM):");
        checkOutLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(checkOutLabel, gbc);

        checkOutField = new JTextField(20);
        checkOutField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkOutField.setBorder(new LineBorder(new Color(0xCED4DA), 1, true));
        //checkOutField.setText(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); // Default: 2 hours from now
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(checkOutField, gbc);

        /*
        // Deposit Amount
        depositLabel = new JLabel(getDepositText());
        depositLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        depositLabel.setForeground(new Color(0x28A745));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(depositLabel, gbc);
         */

        // Warning Message
        warningLabel = new JLabel("⚠️ If you do not check in within 30 minutes of the start time, the deposit is lost. Otherwise, it is applied to the final cost.");
        warningLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        warningLabel.setForeground(new Color(0x856404));
        warningLabel.setBorder(BorderFactory.createLineBorder(new Color(0xFFC107), 1));
        warningLabel.setOpaque(true);
        warningLabel.setBackground(new Color(0xFFF3CD));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(warningLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(0xF8F9FA));

        confirmButton = createStyledButton("Confirm Booking", new Color(0x28A745), 200);
        confirmButton.addActionListener(new ConfirmAction());
        buttonPanel.add(confirmButton);

        backButton = createStyledButton("Back", new Color(0x6C757D), 100);
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(previousPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    private String getRoomDetailsText() {
        return String.format("<html><b>Room Selected:</b><br/>ID: %s<br/>Location: %s<br/>Capacity: %d</html>",
                selectedRoom.getRoomID(),
                selectedRoom.getFullLocation(),
                selectedRoom.getCapacity());
    }

    private String getDepositText() {
        double deposit = switch (this.user.getAccountType().toLowerCase()) {
            case "student" -> 20.0;
            case "faculty" -> 30.0;
            case "staff" -> 40.0;
            default -> 50.0; // partner
        };
        return String.format("<html><b>Required Deposit:</b> $%.2f (1 hour upfront)</html>", deposit);
    }

    private class ConfirmAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String checkInStr = checkInField.getText();
                String checkOutStr = checkOutField.getText();

                LocalDateTime checkIn = LocalDateTime.parse(checkInStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime checkOut = LocalDateTime.parse(checkOutStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                if (checkOut.isBefore(checkIn)) {
                    JOptionPane.showMessageDialog(SelectDates.this, "Check-out time must be after check-in time!", "Invalid Dates", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BookingCommand command = new CreateBookingCommand(SelectDates.this.user, selectedRoom, checkIn, checkOut);
                BookingInvoker invoker = new BookingInvoker();
                invoker.setCommand(command);
                Booking booking = invoker.executeCommand();
                RoomRepository.updateRoomStatus(selectedRoom.getRoomID(), true);

                JOptionPane.showMessageDialog(SelectDates.this, String.format("Booking confirmed!\nRoom: %s\nCheck-in: %s\nCheck-out: %s\nDeposit: $%.2f",
                                selectedRoom.getRoomID(), checkInStr, checkOutStr, getDepositFromUserType()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                long minutes = java.time.Duration.between(checkIn, checkOut).toMinutes();
                int hours = (int) Math.ceil(minutes / 60.0);
                double totalPrice = hours * user.getHourlyRate();
                PaymentUI paymentUI = new PaymentUI(
                        parentFrame,
                        previousPanel,
                        booking.getBookingID(),
                        user.getHourlyRate(),
                        totalPrice,
                        user
                );

                // Navigate to PaymentUI
                parentFrame.setContentPane(paymentUI);
                parentFrame.revalidate();
                parentFrame.repaint();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(SelectDates.this, "Invalid date format! Use YYYY-MM-DD HH:MM", "Date Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private double getDepositFromUserType() {
            return switch (user.getAccountType().toLowerCase()) {
                case "student" -> 20.0;
                case "faculty" -> 30.0;
                case "staff" -> 40.0;
                default -> 50.0;
            };
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
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(width, 45));
        return button;
    }
}