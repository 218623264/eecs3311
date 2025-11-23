package payment;

import model.User;

public class Payment {

    private long paymentID;
    private double paymentAmount;
    private String paymentDate;
    private String bookingID;
    private User user;

    //  Strategy interface
    private PaymentStrategy paymentStrategy;

    // constructor
    public Payment(long paymentID, double paymentAmount, String paymentDate, String bookingID) {
        this.paymentID = paymentID;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.bookingID = bookingID;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void payDeposit() {
        if (paymentStrategy != null) {
            paymentStrategy.pay(paymentAmount, user);
        } else {
            System.out.println("Error: No payment strategy selected!");
        }
    }

    public void payBalance() {
        if (paymentStrategy != null) {
            paymentStrategy.pay(paymentAmount, user);
        } else {
            System.out.println("Error: No payment strategy selected!");
        }
    }

    public void verifyPaymentDetails() {
        System.out.println("Verifying payment details for booking " + bookingID + "...");
    }

    public boolean paymentCompleted() {
        System.out.println("Payment completed for booking " + bookingID);
        return true;
    }
}