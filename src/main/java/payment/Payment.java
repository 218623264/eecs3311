package payment;

import model.User;

public class Payment {

    private long paymentID;
    private double paymentAmount;
    private String paymentDate;
    private String bookingID;
    private User user;

    // Selected payment method (Strategy pattern)
    private PaymentStrategy paymentStrategy;

    // Basic payment record details
    public Payment(long paymentID, double paymentAmount, String paymentDate, String bookingID) {
        this.paymentID = paymentID;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.bookingID = bookingID;
    }

    // Set which payment method to use
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    // Charge the deposit using the chosen strategy
    public void payDeposit() {
        if (paymentStrategy != null) {
            paymentStrategy.pay(paymentAmount, user);
        } else {
            System.out.println("Error: No payment strategy selected!");
        }
    }

    // Charge the remaining balance
    public void payBalance() {
        if (paymentStrategy != null) {
            paymentStrategy.pay(paymentAmount, user);
        } else {
            System.out.println("Error: No payment strategy selected!");
        }
    }

    // Simple placeholder verification
    public void verifyPaymentDetails() {
        System.out.println("Verifying payment details for booking " + bookingID + "...");
    }

    // Mark the payment as completed
    public boolean paymentCompleted() {
        System.out.println("Payment completed for booking " + bookingID);
        return true;
    }
}