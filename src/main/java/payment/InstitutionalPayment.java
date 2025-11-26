package payment;

import model.User;

public class InstitutionalPayment implements PaymentStrategy {

    // Simulate approving an institutional charge
    @Override
    public boolean pay(double amount, User user) {
        System.out.println("Institutional payment processed: $" + amount);
        return true;
    }
}