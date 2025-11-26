package GUI;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomsUI extends JPanel {

    private JFrame parentFrame;
    private JPanel previousPanel;
    private User user;

    public RoomsUI(JFrame parentFrame, JPanel previousPanel, User user) {
        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;
        this.user = user;

        setLayout(new BorderLayout());
        setBackground(new Color(0xF8F9FA));
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // Title
        JLabel title = new JLabel("Room Booking Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(0x007BFF));
        title.setBorder(new EmptyBorder(0, 0, 40, 0));
        add(title, BorderLayout.NORTH);

        // Main buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0xF8F9FA));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Book a New Room button
        JButton bookNewButton = createStyledButton("Book a New Room", new Color(0x007BFF), 250);
        bookNewButton.addActionListener(new BookNewAction());
        buttonPanel.add(bookNewButton, gbc);

        gbc.gridy++;

        // Check Existing Bookings button
        JButton checkBookingsButton = createStyledButton("Check Existing Bookings", new Color(0x28A745), 250);
        checkBookingsButton.addActionListener(new CheckBookingsAction());
        buttonPanel.add(checkBookingsButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        // Footer with Back button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(0xF8F9FA));
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backButton = createStyledButton("Back", new Color(0x6C757D), 150);
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(previousPanel);  // Switch back to MainUI
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private class BookNewAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BookRoomUI bookRoomUI = new BookRoomUI(parentFrame, RoomsUI.this);
            parentFrame.setContentPane(bookRoomUI);
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    private class CheckBookingsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ExistingBooking existingBooking = new ExistingBooking(parentFrame, RoomsUI.this, user);
            parentFrame.setContentPane(existingBooking);
            parentFrame.revalidate();
            parentFrame.repaint();
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
}