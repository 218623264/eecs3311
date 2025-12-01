package command;

import model.Booking;
import model.BookingStatus;
import model.Faculty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import room.Room;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendBookingCommandTest {

    private Booking booking;
    private Room room;
    private Faculty user;

    @BeforeEach
    void setup() {
        user = new Faculty("test@uni.ca", "pass123", "FAC001");
        room = new Room("R100", 10, "BuildingA", "201");

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        booking = new Booking(user, room, start, end);
        booking.setStatus(BookingStatus.CheckedIn);
    }

    // 1 — Successful extension
    @Test
    void testSuccessfulExtend() {
        LocalDateTime newEnd = booking.getCheckOutTime().plusHours(2);

        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, newEnd);
        Booking result = cmd.execute();

        assertEquals(newEnd, result.getCheckOutTime());
        assertTrue(result.getTotalPrice() > 0);
    }

    // 2 — Cannot extend if NOT checked-in
    @Test
    void testExtendFailsIfNotCheckedIn() {
        booking.setStatus(BookingStatus.Pending);
        LocalDateTime newEnd = booking.getCheckOutTime().plusHours(1);

        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, newEnd);

        assertThrows(IllegalStateException.class, cmd::execute);
    }

    // 3 — New checkout must be after old checkout
    @Test
    void testExtendFailsIfNewCheckoutBeforeOld() {
        LocalDateTime earlier = booking.getCheckOutTime().minusHours(1);
        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, earlier);

        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    // 4 — New checkout cannot be equal to old checkout
    @Test
    void testExtendFailsIfCheckoutSame() {
        LocalDateTime same = booking.getCheckOutTime();
        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, same);

        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    // 5 — Extending changes total price
    @Test
    void testTotalPriceRecalculated() {
        double oldPrice = booking.getTotalPrice();
        LocalDateTime newEnd = booking.getCheckOutTime().plusHours(3);

        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, newEnd);
        cmd.execute();

        assertNotEquals(oldPrice, booking.getTotalPrice());
    }

    // 6 — Extension updates internal checkOutTime field
    @Test
    void testCheckoutTimeUpdated() {
        LocalDateTime newEnd = booking.getCheckOutTime().plusHours(5);
        new ExtendBookingCommand(booking, newEnd).execute();

        assertEquals(newEnd, booking.getCheckOutTime());
    }

    // 7 — Large extension (e.g., +24 hours)
    @Test
    void testLargeExtension() {
        LocalDateTime longEnd = booking.getCheckOutTime().plusHours(24);

        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, longEnd);
        cmd.execute();

        assertEquals(longEnd, booking.getCheckOutTime());
    }

    // 8 — Extend multiple times
    @Test
    void testMultipleExtensions() {
        LocalDateTime newEnd1 = booking.getCheckOutTime().plusHours(2);
        new ExtendBookingCommand(booking, newEnd1).execute();

        LocalDateTime newEnd2 = newEnd1.plusHours(3);
        new ExtendBookingCommand(booking, newEnd2).execute();

        assertEquals(newEnd2, booking.getCheckOutTime());
    }

    // 9 — New end time far in future
    @Test
    void testExtremeFutureExtension() {
        LocalDateTime future = booking.getCheckOutTime().plusDays(7);

        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, future);
        cmd.execute();

        assertEquals(future, booking.getCheckOutTime());
    }

    // 10 — Extension command returns same booking instance
    @Test
    void testSameBookingReturned() {
        LocalDateTime newEnd = booking.getCheckOutTime().plusHours(2);
        ExtendBookingCommand cmd = new ExtendBookingCommand(booking, newEnd);

        Booking result = cmd.execute();

        assertSame(booking, result);
    }
}