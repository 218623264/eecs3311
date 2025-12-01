package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PartnerTest {

    @Test
    void testEmailStored() {
        Partner p = new Partner("p@test.com", "pass", "P001");
        assertEquals("p@test.com", p.getEmail());
    }

    @Test
    void testPasswordStored() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertEquals("pw123", p.getPassword());
    }

    @Test
    void testIDStored() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertEquals("P001", p.getID());
    }

    @Test
    void testDefaultVerificationFalse() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertFalse(p.isVerified());
    }

    @Test
    void testSetVerifiedTrue() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        p.setVerified(true);
        assertTrue(p.isVerified());
    }

    @Test
    void testAccountTypePartner() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertEquals("partner", p.getAccountType());
    }

    @Test
    void testHourlyRateIs50() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertEquals(50.0, p.getHourlyRate());
    }

    @Test
    void testLoginDoesNotThrow() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertDoesNotThrow(() -> p.login("p@test.com", "pw123"));
    }

    @Test
    void testLogoutDoesNotThrow() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertDoesNotThrow(p::logout);
    }

    @Test
    void testViewProfileDoesNotThrow() {
        Partner p = new Partner("p@test.com", "pw123", "P001");
        assertDoesNotThrow(p::viewProfile);
    }
}
