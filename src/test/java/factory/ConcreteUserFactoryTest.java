package factory;

import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConcreteUserFactoryTest {

    private ConcreteUserFactory factory;

    @BeforeEach
    void setup() {
        factory = new ConcreteUserFactory();
    }

    @Test
    void testCreateStudent() {
        User u = factory.createUser("student", "a@b.com", "123", "100");
        assertTrue(u instanceof Student);
    }

    @Test
    void testCreateFaculty() {
        User u = factory.createUser("faculty", "f@b.com", "123", "200");
        assertTrue(u instanceof Faculty);
    }

    @Test
    void testCreateStaff() {
        User u = factory.createUser("staff", "s@b.com", "123", "300");
        assertTrue(u instanceof Staff);
    }

    @Test
    void testCreatePartner() {
        User u = factory.createUser("partner", "p@b.com", "123", "400");
        assertTrue(u instanceof Partner);
    }

    @Test
    void testCreateAdmin() {
        User u = factory.createUser("admin", "adm@b.com", "123", null);
        assertTrue(u instanceof Admin);
    }

    @Test
    void testUnknownTypeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createUser("random", "a@b.com", "123", "100"));
    }

    @Test
    void testEmailStoredCorrectly() {
        User u = factory.createUser("student", "email@test.com", "123", "100");
        assertEquals("email@test.com", u.getEmail());
    }

    @Test
    void testPasswordStoredCorrectly() {
        User u = factory.createUser("faculty", "f@test.com", "pass", "200");
        assertEquals("pass", u.getPassword());
    }

    @Test
    void testOrgIDStoredCorrectly() {
        User u = factory.createUser("student", "s@test.com", "123", "900");
        assertEquals("900", u.getID());
    }

    @Test
    void testAdminHasVerifiedTrue() {
        Admin a = (Admin) factory.createUser("admin", "a@a.com", "pass", null);
        assertTrue(a.isVerified());
    }
}
