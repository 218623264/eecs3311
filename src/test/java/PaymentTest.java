import payment.Payment;
import payment.CreditCardPayment;
import payment.DebitCardPayment;
import payment.InstitutionalPayment;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private static class DummyUser implements User {
        public void login(String e, String p) {}
        public void logout() {}
        public void viewProfile() {}
        public String getAccountType() { return null; }
        public String getEmail() { return null; }
        public boolean isVerified() { return false; }
        public void setVerified(boolean v) {}
        public String getID() { return "U"; }
        public String getPassword() { return null; }
        public double getHourlyRate() { return 0; }
    }

    private Payment payment;

    @BeforeEach
    void setup() {
        payment = new Payment(1, 150.0, "2025-01-01", "B123");
    }

    @Test
    void testSetPaymentStrategy() {
        payment.setPaymentStrategy(new CreditCardPayment());
        assertNotNull(payment);
    }

    @Test
    void testPayDepositCreditCard() {
        payment.setPaymentStrategy(new CreditCardPayment());
        assertDoesNotThrow(payment::payDeposit);
    }

    @Test
    void testPayBalanceCreditCard() {
        payment.setPaymentStrategy(new CreditCardPayment());
        assertDoesNotThrow(payment::payBalance);
    }

    @Test
    void testPayDepositDebitCard() {
        payment.setPaymentStrategy(new DebitCardPayment());
        assertDoesNotThrow(payment::payDeposit);
    }

    @Test
    void testPayBalanceDebitCard() {
        payment.setPaymentStrategy(new DebitCardPayment());
        assertDoesNotThrow(payment::payBalance);
    }

    @Test
    void testVerifyPaymentDetails() {
        assertDoesNotThrow(payment::verifyPaymentDetails);
    }

    @Test
    void testPaymentCompleted() {
        assertTrue(payment.paymentCompleted());
    }

    @Test
    void testPayDepositNoStrategy() {
        Payment p = new Payment(2, 50, "2025-01-02", "B2");
        assertDoesNotThrow(p::payDeposit);
    }

    @Test
    void testPayBalanceNoStrategy() {
        Payment p = new Payment(3, 50, "2025-01-03", "B3");
        assertDoesNotThrow(p::payBalance);
    }

    @Test
    void testSwitchStrategy() {
        payment.setPaymentStrategy(new CreditCardPayment());
        payment.payDeposit();
        payment.setPaymentStrategy(new DebitCardPayment());
        assertDoesNotThrow(payment::payBalance);
    }
}
