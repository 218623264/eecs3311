package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    void testEmailStored() {
        Student s = new Student("a@b.com", "123", "100");
        assertEquals("a@b.com", s.getEmail());
    }

    @Test
    void testPasswordStored() {
        Student s = new Student("a@b.com", "pass", "100");
        assertEquals("pass", s.getPassword());
    }

    @Test
    void testIDStored() {
        Student s = new Student("a@b.com", "pass", "100");
        assertEquals("100", s.getID());
    }

    @Test
    void testDefaultVerificationFalse() {
        Student s = new Student("a@b.com", "pass", "100");
        assertFalse(s.isVerified());
    }

    @Test
    void testSetVerified() {
        Student s = new Student("a@b.com", "pass", "100");
        s.setVerified(true);
        assertTrue(s.isVerified());
    }

    @Test
    void testAccountType() {
        Student s = new Student("a@b.com", "pass", "100");
        assertEquals("student", s.getAccountType());
    }

    @Test
    void testHourlyRate() {
        Student s = new Student("a@b.com", "pass", "100");
        assertEquals(20.0, s.getHourlyRate());
    }

    @Test
    void testLoginPrintsSomething() {
        Student s = new Student("a@b.com", "pass", "100");
        assertDoesNotThrow(() -> s.login("a", "b"));
    }

    @Test
    void testLogout() {
        Student s = new Student("a@b.com", "pass", "100");
        assertDoesNotThrow(s::logout);
    }

    @Test
    void testViewProfile() {
        Student s = new Student("a@b.com", "pass", "100");
        assertDoesNotThrow(s::viewProfile);
    }
}
