package payment;

import model.User;

public interface PaymentStrategy {
    boolean pay(double amount, User user);
}