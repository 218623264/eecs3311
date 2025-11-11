package payment;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        // minimal demo behavior (no real card processing)
        System.out.println("Payment of $" + amount + " made via Credit Card.");
    }
}

