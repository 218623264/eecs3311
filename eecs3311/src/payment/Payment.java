package payment;

public class Payment {

    private long paymentID;
    private double paymentAmount;
    private String paymentDate;
    private String bookingID;

    // reference to the Strategy interface
    private PaymentStrategy paymentStrategy;

    // constructor (optional)
    public Payment(long paymentID, double paymentAmount, String paymentDate, String bookingID) {
        this.paymentID = paymentID;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.bookingID = bookingID;
    }

    // allows changing the payment method at runtime
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    // delegates the algorithm to the strategy
    public void payDeposit() {
        //System.out.println("\n[Deposit Payment Started]");
        if (paymentStrategy != null) {
            paymentStrategy.pay(paymentAmount);
        } else {
            System.out.println("Error: No payment strategy selected!");
        }
    }

    public void payBalance() {
       // System.out.println("\n[Balance Payment Started]");
        if (paymentStrategy != null) {
            paymentStrategy.pay(paymentAmount);
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
