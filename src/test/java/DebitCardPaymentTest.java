import payment.DebitCardPayment;
import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardPaymentTest {

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

    @Test
    void testBasicPay() {
        assertTrue(new DebitCardPayment().pay(100, new DummyUser()));
    }

    @Test
    void testZeroAmount() {
        assertTrue(new DebitCardPayment().pay(0, new DummyUser()));
    }

    @Test
    void testNegativeAmount() {
        assertTrue(new DebitCardPayment().pay(-10, new DummyUser()));
    }

    @Test
    void testNullUser() {
        assertTrue(new DebitCardPayment().pay(10, null));
    }

    @Test
    void testMultiplePayments() {
        DebitCardPayment d = new DebitCardPayment();
        assertTrue(d.pay(5, new DummyUser()));
        assertTrue(d.pay(15, new DummyUser()));
        assertTrue(d.pay(25, new DummyUser()));
    }

    @Test
    void testLargeAmount() {
        assertTrue(new DebitCardPayment().pay(999999, new DummyUser()));
    }

    @Test
    void testSmallPayment() {
        assertTrue(new DebitCardPayment().pay(1, new DummyUser()));
    }

    @Test
    void testRepeatUser() {
        DummyUser u = new DummyUser();
        DebitCardPayment d = new DebitCardPayment();
        assertTrue(d.pay(10, u));
        assertTrue(d.pay(20, u));
    }

    @Test
    void testAlwaysTrue() {
        assertTrue(new DebitCardPayment().pay(123, new DummyUser()));
    }

    @Test
    void testBoundaryAmount() {
        assertTrue(new DebitCardPayment().pay(Double.MIN_VALUE, new DummyUser()));
    }
}
