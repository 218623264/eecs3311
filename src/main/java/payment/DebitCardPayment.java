package payment;

import model.User;

public class DebitCardPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount, User user) {
        System.out.println("Debit card charged: $" + amount);
        return true;
    }
}