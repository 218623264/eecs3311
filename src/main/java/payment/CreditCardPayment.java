package payment;

import model.User;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount, User user) {
        System.out.println("Credit card charged: $" + amount);
        return true;
    }
}