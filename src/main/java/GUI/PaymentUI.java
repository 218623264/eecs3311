package GUI;

import model.User;
import payment.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentUI extends JPanel {

    private JFrame parentFrame;
    private JPanel previousPanel;

    private int bookingID;
    private double deposit;
    private double totalPrice;
    private User user;

    private JLabel amountLabel;
    private JButton creditButton, debitButton, institutionalButton, backButton;

    public PaymentUI(JFrame parentFrame,
                     JPanel previousPanel,
                     int bookingID,
                     double deposit,
                     double totalPrice,
                     User user) {

        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;
        this.bookingID = bookingID;
        this.deposit = deposit;
        this.totalPrice = totalPrice;
        this.user = user;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(0xF8F9FA));

        JLabel title = new JLabel("Payment Required", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(0x007BFF));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBackground(new Color(0xF8F9FA));

        amountLabel = new JLabel(
                "<html><center>"
                        + "Total Price: <b>$" + String.format("%.2f", totalPrice) + "</b><br>"
                        + "Deposit Charged Now: <b>$" + String.format("%.2f", deposit) + "</b>"
                        + "</center></html>",
                SwingConstants.CENTER
        );
        amountLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        centerPanel.add(amountLabel);

        creditButton = createStyledButton("Pay with Credit Card", new Color(0x007BFF));
        debitButton = createStyledButton("Pay with Debit Card", new Color(0x28A745));
        institutionalButton = createStyledButton("Institutional Payment", new Color(0x6F42C1));

        centerPanel.add(creditButton);
        centerPanel.add(debitButton);
        centerPanel.add(institutionalButton);

        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        creditButton.addActionListener(new PaymentAction(new CreditCardPayment()));
        debitButton.addActionListener(new PaymentAction(new DebitCardPayment()));
        institutionalButton.addActionListener(new PaymentAction(new InstitutionalPayment()));

        // Back button
        backButton = createStyledButton("Back", new Color(0x6C757D));
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(previousPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        JPanel south = new JPanel();
        south.setBackground(new Color(0xF8F9FA));
        south.add(backButton);

        add(south, BorderLayout.SOUTH);
    }


    private class PaymentAction implements ActionListener {

        private PaymentStrategy strategy;

        public PaymentAction(PaymentStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            boolean success = strategy.pay(deposit, user);

            if (success) {
                JOptionPane.showMessageDialog(PaymentUI.this,
                        "Payment successful!\nDeposit: $" + deposit,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Back to rooms UI
                RoomsUI roomsUI = new RoomsUI(parentFrame, previousPanel, user);
                parentFrame.setContentPane(roomsUI);
                parentFrame.revalidate();
                parentFrame.repaint();

            } else {
                JOptionPane.showMessageDialog(PaymentUI.this,
                        "Payment failed. Try another method.",
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 45));
        return button;
    }
}