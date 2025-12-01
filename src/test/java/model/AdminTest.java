package model;

import org.junit.jupiter.api.Test;
import room.Room;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    void testEmailStored() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertEquals("admin@test.com", a.getEmail());
    }

    @Test
    void testPasswordStored() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertEquals("pw123", a.getPassword());
    }

    @Test
    void testAccountTypeAdmin() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertEquals("admin", a.getAccountType());
    }

    @Test
    void testAdminIsVerifiedByDefault() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertTrue(a.isVerified());
    }

    @Test
    void testSetVerifiedFalse() {
        Admin a = new Admin("admin@test.com", "pw123");
        a.setVerified(false);
        assertFalse(a.isVerified());
    }

    @Test
    void testGetIDIsAdmin() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertEquals("admin", a.getID());
    }

    @Test
    void testHourlyRateZero() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertEquals(0.0, a.getHourlyRate());
    }

    @Test
    void testRoomEnableDisable() {
        Admin a = new Admin("admin@test.com", "pw123");
        Room r = new Room("R101", 50, "Building A", "101");

        a.disableRoom(r);
        assertFalse(r.isEnabled());

        a.enableRoom(r);
        assertTrue(r.isEnabled());
    }

    @Test
    void testLoginDoesNotThrow() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertDoesNotThrow(() -> a.login("admin@test.com", "pw123"));
    }

    @Test
    void testLogoutAndViewProfileDoNotThrow() {
        Admin a = new Admin("admin@test.com", "pw123");
        assertDoesNotThrow(a::logout);
        assertDoesNotThrow(a::viewProfile);
    }
}
