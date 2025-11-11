package payment;

public class DebitCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        // Simulate payment processing using a debit card
        System.out.println("Payment of $" + amount + " made via Debit Card.");
    }
}
