package payment;

public class DebitCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Payment of $" + amount + " made via Debit Card.");
    }
}