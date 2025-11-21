package payment;

public class InstitutionalPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Payment of $" + amount + " made via Institutional Billing.");
    }
}