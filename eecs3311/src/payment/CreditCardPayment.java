package payment;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Payment of $" + amount + " made via Credit Card.");
    }
}

