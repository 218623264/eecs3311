package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacultyTest {

    @Test
    void testEmailStored() {
        Faculty f = new Faculty("f@test.com", "pass", "F001");
        assertEquals("f@test.com", f.getEmail());
    }

    @Test
    void testPasswordStored() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertEquals("pw123", f.getPassword());
    }

    @Test
    void testIDStored() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertEquals("F001", f.getID());
    }

    @Test
    void testDefaultVerificationFalse() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertFalse(f.isVerified());
    }

    @Test
    void testSetVerifiedTrue() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        f.setVerified(true);
        assertTrue(f.isVerified());
    }

    @Test
    void testAccountTypeFaculty() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertEquals("faculty", f.getAccountType());
    }

    @Test
    void testHourlyRateIs30() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertEquals(30.0, f.getHourlyRate());
    }

    @Test
    void testLoginDoesNotThrow() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertDoesNotThrow(() -> f.login("f@test.com", "pw123"));
    }

    @Test
    void testLogoutDoesNotThrow() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertDoesNotThrow(f::logout);
    }

    @Test
    void testViewProfileDoesNotThrow() {
        Faculty f = new Faculty("f@test.com", "pw123", "F001");
        assertDoesNotThrow(f::viewProfile);
    }
}
