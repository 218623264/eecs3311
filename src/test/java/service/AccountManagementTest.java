package service;

import factory.ConcreteUserFactory;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountManagementTest {

    private AccountManagement am;

    @BeforeEach
    void setup() {
        am = new AccountManagement(new ConcreteUserFactory());
    }

    @Test
    void testRegisterUserSuccess() {
        User u = am.registerUser("student", "test1@test.com",
                "Strong@123", "S001");
        assertNotNull(u);
        assertEquals("student", u.getAccountType());
    }

    @Test
    void testDuplicateEmailRejected() {
        am.registerUser("faculty", "dup@test.com",
                "Strong@123", "F111");

        assertThrows(IllegalArgumentException.class, () ->
                am.registerUser("staff", "dup@test.com",
                        "Strong@123", "S200"));
    }

    @Test
    void testInvalidEmailRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                am.registerUser("student", "invalidEmail",
                        "Strong@123", "S1"));
    }

    @Test
    void testWeakPasswordRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                am.registerUser("student", "weak@test.com",
                        "weak", "S2"));
    }

    @Test
    void testMissingOrganizationID() {
        assertThrows(IllegalArgumentException.class, () ->
                am.registerUser("partner", "p@test.com",
                        "Strong@123", ""));
    }

    @Test
    void testInvalidUserTypeRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                am.registerUser("alien", "x@test.com",
                        "Strong@123", "X1"));
    }

    @Test
    void testAdminCreationRequiresChief() {
        assertThrows(IllegalArgumentException.class, () ->
                am.createAdminAccount(null,
                        "admin@test.com", "Strong@123"));
    }

    @Test
    void testAdminCreationSuccess() {
        Chief chief = new Chief("chief@test.com", "Pass@123");
        Admin admin = am.createAdminAccount(
                chief, "adminX@test.com", "Strong@123");

        assertNotNull(admin);
        assertEquals("admin", admin.getAccountType());
    }

    @Test
    void testAdminCreationDuplicateEmail() {
        Chief chief = new Chief("chief@test.com", "Pass@123");

        am.createAdminAccount(chief,
                "admin@test.com", "Strong@123");

        assertThrows(IllegalArgumentException.class, () ->
                am.createAdminAccount(chief,
                        "admin@test.com", "Strong@123"));
    }




}
