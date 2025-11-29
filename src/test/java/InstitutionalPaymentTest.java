import payment.InstitutionalPayment;
import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionalPaymentTest {

    private static class DummyUser implements User {
        public void login(String e, String p) {}
        public void logout() {}
        public void viewProfile() {}
        public String getAccountType() { return "institution"; }
        public String getEmail() { return null; }
        public boolean isVerified() { return false; }
        public void setVerified(boolean v) {}
        public String getID() { return "U"; }
        public String getPassword() { return null; }
        public double getHourlyRate() { return 0; }
    }

    @Test
    void testBasicPay() {
        assertTrue(new InstitutionalPayment().pay(100, new DummyUser()));
    }

    @Test
    void testZeroAmount() {
        assertTrue(new InstitutionalPayment().pay(0, new DummyUser()));
    }

    @Test
    void testNegativeAmount() {
        assertTrue(new InstitutionalPayment().pay(-10, new DummyUser()));
    }

    @Test
    void testNullUser() {
        assertTrue(new InstitutionalPayment().pay(10, null));
    }

    @Test
    void testMultiplePayments() {
        InstitutionalPayment i = new InstitutionalPayment();
        assertTrue(i.pay(5, new DummyUser()));
        assertTrue(i.pay(15, new DummyUser()));
        assertTrue(i.pay(25, new DummyUser()));
    }

    @Test
    void testLargeAmount() {
        assertTrue(new InstitutionalPayment().pay(999999, new DummyUser()));
    }

    @Test
    void testSmallPayment() {
        assertTrue(new InstitutionalPayment().pay(1, new DummyUser()));
    }

    @Test
    void testRepeatUser() {
        DummyUser u = new DummyUser();
        InstitutionalPayment i = new InstitutionalPayment();
        assertTrue(i.pay(10, u));
        assertTrue(i.pay(20, u));
    }

    @Test
    void testAlwaysTrue() {
        assertTrue(new InstitutionalPayment().pay(123, new DummyUser()));
    }

    @Test
    void testBoundaryAmount() {
        assertTrue(new InstitutionalPayment().pay(Double.MIN_VALUE, new DummyUser()));
    }
}
