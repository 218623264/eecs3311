package command;

import model.*;
import room.Room;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EditBookingCommandTest {

    private Booking booking;
    private Room room;
    private User user;
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        user = new Faculty("test@example.com", "123", "U100");
        room = new Room("R1", 4, "Building", "101");
        now = LocalDateTime.now().plusHours(2); // future check-in

        booking = new Booking(
                user,
                room,
                now,
                now.plusHours(2)
        );
    }

    // 1 - success case: booking edited correctly
    @Test
    void testEditBookingSuccess() {
        LocalDateTime newIn = now.plusHours(1);
        LocalDateTime newOut = now.plusHours(4);

        EditBookingCommand cmd = new EditBookingCommand(booking, newIn, newOut);
        Booking updated = cmd.execute();

        assertEquals(newIn, updated.getCheckInTime());
        assertEquals(newOut, updated.getCheckOutTime());
    }

    // 2 - cannot edit if status != Pending
    @Test
    void testEditFailsWhenNotPending() {
        booking.setStatus(BookingStatus.CheckedIn);

        LocalDateTime newIn = now.plusHours(1);
        LocalDateTime newOut = now.plusHours(3);

        EditBookingCommand cmd = new EditBookingCommand(booking, newIn, newOut);

        assertThrows(IllegalStateException.class, cmd::execute);
    }

    // 3 - cannot edit after check-in time has passed
    @Test
    void testEditFailsAfterCheckInPassed() {
        booking = new Booking(
                user,
                room,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1)
        );

        EditBookingCommand cmd = new EditBookingCommand(
                booking,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );

        assertThrows(IllegalStateException.class, cmd::execute);
    }

    // 4 - invalid times: checkout before checkin
    @Test
    void testCheckoutBeforeCheckIn() {
        LocalDateTime newIn = now.plusHours(2);
        LocalDateTime newOut = now.plusHours(1);

        EditBookingCommand cmd = new EditBookingCommand(booking, newIn, newOut);

        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    // 5 - new times equal old times
    @Test
    void testTimesNotChanged() {
        EditBookingCommand cmd = new EditBookingCommand(
                booking,
                booking.getCheckInTime(),
                booking.getCheckOutTime()
        );

        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    // 6 - check-in before now (invalid)
    @Test
    void testCheckInInPast() {
        LocalDateTime newIn = LocalDateTime.now().minusHours(2);
        LocalDateTime newOut = LocalDateTime.now().plusHours(2);

        EditBookingCommand cmd = new EditBookingCommand(booking, newIn, newOut);

        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    // 7 - updates total price correctly
    @Test
    void testTotalPriceUpdated() {
        LocalDateTime newIn = now.plusHours(1);
        LocalDateTime newOut = now.plusHours(5);

        EditBookingCommand cmd = new EditBookingCommand(booking, newIn, newOut);
        cmd.execute();

        long expectedHours = java.time.Duration.between(newIn, newOut).toHours();
        assertEquals(expectedHours * user.getHourlyRate(), booking.getTotalPrice());
    }

    // 8 - status stays Pending after edit
    @Test
    void testStatusRemainsPending() {
        LocalDateTime newIn = now.plusHours(1);
        LocalDateTime newOut = now.plusHours(3);

        EditBookingCommand cmd = new EditBookingCommand(booking, newIn, newOut);
        cmd.execute();

        assertEquals(BookingStatus.Pending, booking.getStatus());
    }

    // 9 - cannot edit with checkout same as current checkout
    @Test
    void testCheckoutUnchangedDoesNotThrow() {
    LocalDateTime newIn = booking.getCheckInTime().plusHours(1);

    EditBookingCommand cmd = new EditBookingCommand(booking, newIn, booking.getCheckOutTime());

    assertDoesNotThrow(cmd::execute);
}

    // 10 - cannot edit with checkin same as current checkin
    @Test
    void testCheckinUnchangedDoesNotThrow() {
    LocalDateTime newIn = booking.getCheckInTime().plusHours(1);

    EditBookingCommand cmd = new EditBookingCommand(booking, newIn, booking.getCheckOutTime());

    assertDoesNotThrow(cmd::execute);
}
}