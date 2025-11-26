package payment;

import model.User;

// Handles debit card payments (Strategy pattern)
public class DebitCardPayment implements PaymentStrategy {

    // Simulate charging a debit card
    @Override
    public boolean pay(double amount, User user) {
        System.out.println("Debit card charged: $" + amount);
        return true;
    }
}