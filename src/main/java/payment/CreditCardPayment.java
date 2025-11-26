package payment;

import model.User;

// Handles credit card payments (Strategy pattern)
public class CreditCardPayment implements PaymentStrategy {

    // Simulate charging a credit card
    @Override
    public boolean pay(double amount, User user) {
        System.out.println("Credit card charged: $" + amount);
        return true;
    }
}