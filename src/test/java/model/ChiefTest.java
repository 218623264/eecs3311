package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChiefTest {

    @Test
    void testEmailStored() {
        Chief c = new Chief("chief@test.com", "pass");
        assertEquals("chief@test.com", c.getEmail());
    }

    @Test
    void testPasswordStored() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertEquals("pw123", c.getPassword());
    }

    @Test
    void testAccountTypeChief() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertEquals("chief", c.getAccountType());
    }

    @Test
    void testDefaultVerificationFalse() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertFalse(c.isVerified());
    }

    @Test
    void testSetVerifiedTrue() {
        Chief c = new Chief("chief@test.com", "pw123");
        c.setVerified(true);
        assertTrue(c.isVerified());
    }

    @Test
    void testGetIDIsChief() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertEquals("chief", c.getID());
    }

    @Test
    void testHourlyRateZero() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertEquals(0.0, c.getHourlyRate());
    }

    @Test
    void testLoginDoesNotThrow() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertDoesNotThrow(() -> c.login("chief@test.com", "pw123"));
    }

    @Test
    void testLogoutDoesNotThrow() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertDoesNotThrow(c::logout);
    }

    @Test
    void testViewProfileDoesNotThrow() {
        Chief c = new Chief("chief@test.com", "pw123");
        assertDoesNotThrow(c::viewProfile);
    }
}
