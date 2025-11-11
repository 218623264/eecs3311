package payment;

public class InstitutionalPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        // Simulate institutional billing or internal university payment
        System.out.println("Payment of $" + amount + " made via Institutional Billing.");
    }
}
