package command;

import model.Booking;
import model.Faculty;
import model.User;
import room.Room;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingInvokerTest {

    private BookingInvoker invoker;
    private User user;
    private Room room;
    private LocalDateTime now;
    private LocalDateTime later;

    @BeforeEach
    void setup() {
        invoker = new BookingInvoker();
        user = new Faculty("test@example.com", "123", "FAC001");
        room = new Room("R101", 20, "ENG", "201");
        now = LocalDateTime.now().plusHours(1);
        later = now.plusHours(2);
    }

    // 1. Ensure command executes successfully
    @Test
    void testExecuteCommand() {
        CreateBookingCommand cmd = new CreateBookingCommand(user, room, now, later);
        invoker.setCommand(cmd);

        Booking result = invoker.executeCommand();

        assertNotNull(result);
        assertEquals("R101", result.getRoom().getRoomID());
        assertEquals("FAC001", result.getUser().getID());
    }

    // 2. Ensure invoker holds command
    @Test
    void testSetCommandStoresCommand() {
        CreateBookingCommand cmd = new CreateBookingCommand(user, room, now, later);
        invoker.setCommand(cmd);

        // Execute to confirm
        Booking result = invoker.executeCommand();
        assertNotNull(result);
    }

    // 3. Test using a mock-like dummy command
    @Test
    void testInvokerWithFakeCommand() {
        BookingCommand fake = () -> null;

        invoker.setCommand(fake);

        Booking result = invoker.executeCommand(); // should return null
        assertNull(result);
    }

    // 4. Ensure executeCommand throws if no command is set (optional)
    @Test
    void testNoCommandThrows() {
        assertThrows(NullPointerException.class, () -> invoker.executeCommand());
    }
}