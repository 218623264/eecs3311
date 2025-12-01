package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import room.Room;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private User user;
    private Room room;
    private LocalDateTime now;
    private LocalDateTime later;

    @BeforeEach
    void setup() {
        user = new Faculty("test@example.com", "123", "FAC001");
        room = new Room("R101", 20, "Engineering", "E201");
        now = LocalDateTime.now().plusHours(1);
        later = now.plusHours(2);
    }

    // 1. Constructor assigns correct status
    @Test
    void testStatusAfterCreation() {
        Booking b = new Booking(user, room, now, later);
        assertEquals(BookingStatus.Pending, b.getStatus());
    }

    // 2. Hourly rate for faculty is correct
    @Test
    void testHourlyRate() {
        Booking b = new Booking(user, room, now, later);
        assertEquals(30.0, b.getTotalPrice() / 2);
    }

    // 3. Deposit equals hourly rate at creation
    @Test
    void testDeposit() {
        Booking b = new Booking(user, room, now, later);
        assertEquals(30.0, b.getDepositAmount());
    }

    // 4. Booking ID range 100–999
    @Test
    void testBookingIdRange() {
        Booking b = new Booking(user, room, now, later);
        assertTrue(b.getBookingID() >= 100 && b.getBookingID() <= 999);
    }

    // 5. Total price calculation: 2 hours * 30
    @Test
    void testTotalPrice() {
        Booking b = new Booking(user, room, now, later);
        assertEquals(60.0, b.getTotalPrice());
    }

    // 6. Editing: valid case
    @Test
    void testEditValid() {
        Booking b = new Booking(user, room, now, later);
        LocalDateTime newStart = now.plusMinutes(10);
        LocalDateTime newEnd = later.plusHours(1);

        b.edit(newStart, newEnd);
        assertEquals(newStart, b.getCheckInTime());
        assertEquals(newEnd, b.getCheckOutTime());
    }

    // 7. Editing after status != Pending throws
    @Test
    void testEditInvalidStatus() {
        Booking b = new Booking(user, room, now, later);
        b.setStatus(BookingStatus.CheckedIn);

        assertThrows(IllegalStateException.class,
                () -> b.edit(now.plusMinutes(10), later.plusHours(1)));
    }

    // 8. Cancel before check-in works
    @Test
    void testCancelValid() {
        Booking b = new Booking(user, room, now, later);
        b.cancel();
        assertEquals(BookingStatus.Cancelled, b.getStatus());
    }

    // 9. Cancel after check-in time throws
    @Test
    void testCancelLateFails() {
        Booking b = new Booking(user, room, LocalDateTime.now().minusMinutes(5), later);
        assertThrows(IllegalStateException.class, b::cancel);
    }

    // 10. Extend valid case
    @Test
    void testExtendValid() {
        Booking b = new Booking(user, room, now, later);
        b.setStatus(BookingStatus.CheckedIn);

        LocalDateTime newEnd = later.plusHours(1);
        b.extend(newEnd);

        assertEquals(newEnd, b.getCheckOutTime());
    }

    @Test
void testApplyDeposit_PendingLate_DepositBecomesZero() {
    // Check-in was 1 hour ago → late by more than 30 minutes
    LocalDateTime checkIn = LocalDateTime.now().minusHours(1);
    LocalDateTime checkOut = LocalDateTime.now().plusHours(1);

    User user = new Faculty("a@a.com", "123", "FAC001");
    Room room = new Room("R1", 5, "ENG", "101");

    Booking booking = new Booking(user, room, checkIn, checkOut);

    // Booking is Pending by default
    booking.applyDeposit();

    assertEquals(0.0, booking.getDepositAmount(),
            "Deposit should become 0 for late pending bookings.");
}
@Test
void testApplyDeposit_PendingNotLate_ThrowsException() {
    // Check-in is still in future → not late
    LocalDateTime checkIn = LocalDateTime.now().plusHours(1);
    LocalDateTime checkOut = LocalDateTime.now().plusHours(2);

    User user = new Faculty("a@a.com", "123", "FAC001");
    Room room = new Room("R1", 5, "ENG", "101");

    Booking booking = new Booking(user, room, checkIn, checkOut);

    // Should throw because: status = PENDING and NOT late
    assertThrows(IllegalStateException.class, booking::applyDeposit,
            "Should throw because booking is pending and not late.");
}
@Test
void testApplyDeposit_CompletedAndDepositPositive_SetsDepositToHourlyRate() {
    LocalDateTime checkIn = LocalDateTime.now().plusHours(1);
    LocalDateTime checkOut = LocalDateTime.now().plusHours(2);

    User user = new Student("1", "a@a.com", "student"); // hourlyRate = 20
    Room room = new Room("R1", 5, "ENG", "101");

    Booking booking = new Booking(user, room, checkIn, checkOut);

    // Force booking to completed
    booking.setStatus(BookingStatus.Completed);

    double hourlyRate = booking.getTotalPrice() / 1; // or user type → expected 20

    booking.applyDeposit();

    assertEquals(20.0, booking.getDepositAmount(),
            "Deposit should be set to hourly rate (20) after completion.");
}
@Test
void testApplyDeposit_CompletedDepositZero_ThrowsException() {
    LocalDateTime checkIn = LocalDateTime.now().plusHours(1);
    LocalDateTime checkOut = LocalDateTime.now().plusHours(2);

    User user = new Student("1", "a@a.com", "student");
    Room room = new Room("R1", 5, "ENG", "101");

    Booking booking = new Booking(user, room, checkIn, checkOut);

    // Manually zero out deposit using reflection (since no setter)
    try {
        var field = Booking.class.getDeclaredField("depositAmount");
        field.setAccessible(true);
        field.set(booking, 0.0);
    } catch (Exception e) {
        fail("Reflection failed: " + e.getMessage());
    }

    booking.setStatus(BookingStatus.Completed);

    assertThrows(IllegalStateException.class, booking::applyDeposit,
            "Should throw because deposit is 0.");
}
@Test
public void testCheckIn_WrongBadge_ThrowsSecurityException() {
    User user = new Faculty("U1", "Test User", "FAC001");
    Room room = new Room("R1", 6);

    LocalDateTime checkIn = LocalDateTime.now().plusHours(1);
    LocalDateTime checkOut = checkIn.plusHours(2);

    Booking booking = new Booking(user, room, checkIn, checkOut);

    // Wrong badge ID
    assertThrows(SecurityException.class, () -> booking.checkIn("WRONG-ID"));
}
}