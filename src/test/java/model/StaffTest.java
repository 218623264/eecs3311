package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {

    @Test
    void testEmailStored() {
        Staff s = new Staff("staff@test.com", "pass", "S001");
        assertEquals("staff@test.com", s.getEmail());
    }

    @Test
    void testPasswordStored() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertEquals("pw123", s.getPassword());
    }

    @Test
    void testIDStored() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertEquals("S001", s.getID());
    }

    @Test
    void testDefaultVerificationFalse() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertFalse(s.isVerified());
    }

    @Test
    void testSetVerifiedTrue() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        s.setVerified(true);
        assertTrue(s.isVerified());
    }

    @Test
    void testAccountTypeStaff() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertEquals("staff", s.getAccountType());
    }

    @Test
    void testHourlyRateIs40() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertEquals(40.0, s.getHourlyRate());
    }

    @Test
    void testLoginDoesNotThrow() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertDoesNotThrow(() -> s.login("staff@test.com", "pw123"));
    }

    @Test
    void testLogoutDoesNotThrow() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertDoesNotThrow(s::logout);
    }

    @Test
    void testViewProfileDoesNotThrow() {
        Staff s = new Staff("staff@test.com", "pw123", "S001");
        assertDoesNotThrow(s::viewProfile);
    }
}
