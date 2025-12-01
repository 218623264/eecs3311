package command;

import model.Booking;
import model.BookingStatus;
import model.Faculty;
import model.User;
import org.junit.jupiter.api.*;
import room.Room;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CancelBookingCommandTest {

    private Path originalUserDir;
    private Path testRoot; // temporary root to act as user.dir
    private Path csvPath;
    private User user;
    private Room room;
    private LocalDateTime now;

    @BeforeEach
    void setUp() throws IOException {
        // Save original user.dir and switch to a temp one
        originalUserDir = Paths.get(System.getProperty("user.dir"));
        testRoot = Files.createTempDirectory("test-user-dir-");
        System.setProperty("user.dir", testRoot.toString());

        // Ensure data folder exists: eecs3311/src/main/data
        Path dataDir = testRoot.resolve("eecs3311/src/main/data");
        Files.createDirectories(dataDir);

        csvPath = dataDir.resolve("bookings.csv");

        // Basic user/room/time used in tests
        user = new Faculty("test@example.com", "pw", "FAC100");
        room = new Room("R100", 5, "Eng", "101");
        now = LocalDateTime.now().plusHours(2);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore original user.dir
        System.setProperty("user.dir", originalUserDir.toString());
        // Delete temp test root recursively
        if (Files.exists(testRoot)) {
            Files.walk(testRoot)
                    .sorted((a, b) -> b.compareTo(a)) // delete children first
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {}
                    });
        }
    }

    // helper to write csv with header + lines
    private void writeCsvLines(List<String> lines) throws IOException {
        Files.write(csvPath, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private List<String> readCsvLines() throws IOException {
        if (!Files.exists(csvPath)) return List.of();
        return Files.readAllLines(csvPath, StandardCharsets.UTF_8);
    }

    // 1) Successful cancel removes the exact booking line and sets status -> Cancelled
    @Test
    void testCancelRemovesLineAndSetsStatus() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        // header + one booking line (we'll format simply with bookingID at start)
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";
        String line = String.format("%d,R100,%s,%s,%.2f,%s,%.2f,%s",
                b.getBookingID(),
                b.getCheckInTime().toString(),
                b.getCheckOutTime().toString(),
                b.getDepositAmount(),
                b.getUser().getID(),
                b.getTotalPrice(),
                b.getStatus().name()
        );
        writeCsvLines(List.of(header, line));

        CancelBookingCommand cmd = new CancelBookingCommand(b);
        Booking result = cmd.execute();

        assertEquals(BookingStatus.Cancelled, result.getStatus());

        // file should only contain header now
        List<String> remaining = readCsvLines();
        assertEquals(1, remaining.size());
        assertEquals(header, remaining.get(0));
    }

    // 2) If CSV has no matching booking line, booking status is still set to Cancelled (command sets status anyway)
    @Test
    void testCancelWhenNoLineExistsSetsStatusButLeavesFile() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";
        // write a CSV that does NOT contain this booking
        String other = "999,R9,2025-01-01T10:00,2025-01-01T11:00,30.0,OTHER,30.0,Pending";
        writeCsvLines(List.of(header, other));

        CancelBookingCommand cmd = new CancelBookingCommand(b);
        Booking res = cmd.execute();

        assertEquals(BookingStatus.Cancelled, res.getStatus());

        // file should remain unchanged (header + other line)
        List<String> lines = readCsvLines();
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("999"));
    }

    // 3) Command is robust when CSV file does not exist (still sets status)
    @Test
    void testBookingStatusChangedEvenIfFileMissing() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        // Ensure CSV does not exist
        Files.deleteIfExists(csvPath);

        CancelBookingCommand cmd = new CancelBookingCommand(b);
        Booking res = cmd.execute();

        assertEquals(BookingStatus.Cancelled, res.getStatus());
        assertFalse(Files.exists(csvPath)); // no file created by cancel
    }

    // 4) Multiple lines: ensure only exact matching bookingID line is removed
    @Test
    void testMultipleLinesOnlyRemoveExactMatch() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";

        // include a line that starts with bookingID + "4..." to ensure startsWith uses comma delimiter matters
        String similar = String.format("%d4,RX,2025-01-01T10:00,2025-01-01T11:00,30.0,OTHER,30.0,Pending", b.getBookingID());
        String exact = String.format("%d,R100,%s,%s,%.2f,%s,%.2f,%s",
                b.getBookingID(),
                b.getCheckInTime().toString(),
                b.getCheckOutTime().toString(),
                b.getDepositAmount(),
                b.getUser().getID(),
                b.getTotalPrice(),
                b.getStatus().name()
        );
        writeCsvLines(List.of(header, similar, exact));

        CancelBookingCommand cmd = new CancelBookingCommand(b);
        cmd.execute();

        List<String> remaining = readCsvLines();
        // header + similar should remain (exact removed)
        assertEquals(2, remaining.size());
        assertEquals(header, remaining.get(0));
        assertTrue(remaining.get(1).startsWith(String.valueOf(b.getBookingID()) + "4"));
    }

    // 5) Running cancel twice: second run shouldn't find the line but should not crash and status remains Cancelled
    @Test
    void testSecondCancelIdempotent() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";
        String exact = String.format("%d,R100,%s,%s,%.2f,%s,%.2f,%s",
                b.getBookingID(),
                b.getCheckInTime().toString(),
                b.getCheckOutTime().toString(),
                b.getDepositAmount(),
                b.getUser().getID(),
                b.getTotalPrice(),
                b.getStatus().name()
        );
        writeCsvLines(List.of(header, exact));

        CancelBookingCommand cmd = new CancelBookingCommand(b);
        cmd.execute();

        // second run: file no longer has the line
        assertDoesNotThrow(cmd::execute);
        assertEquals(BookingStatus.Cancelled, b.getStatus());
        List<String> remaining = readCsvLines();
        assertEquals(1, remaining.size());
    }

    // 6) Null booking passed into command -> executing will throw NullPointerException (booking used directly)
    @Test
    void testNullBookingThrowsNPE() {
        CancelBookingCommand cmd = new CancelBookingCommand(null);
        assertThrows(NullPointerException.class, cmd::execute);
    }

    // 7) Header preservation: header must remain when removing single line
    @Test
    void testCsvHeaderPreserved() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";
        String exact = String.format("%d,R100,%s,%s,%.2f,%s,%.2f,%s",
                b.getBookingID(),
                b.getCheckInTime().toString(),
                b.getCheckOutTime().toString(),
                b.getDepositAmount(),
                b.getUser().getID(),
                b.getTotalPrice(),
                b.getStatus().name()
        );
        writeCsvLines(List.of(header, exact));

        new CancelBookingCommand(b).execute();

        List<String> lines = readCsvLines();
        assertFalse(lines.isEmpty());
        assertEquals(header, lines.get(0));
    }

    // 8) Ensure booking status is EXACT BookingStatus.Cancelled constant after execute
    @Test
    void testBookingStatusSetToCancelledConstant() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        writeCsvLines(List.of("bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status",
                String.valueOf(b.getBookingID()) + ",..."));
        new CancelBookingCommand(b).execute();
        assertSame(BookingStatus.Cancelled, b.getStatus());
    }

    // 9) Cancel leaves other bookings intact
    @Test
    void testCancelDoesNotRemoveOtherBookings() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";
        String other = "777,R200,2025-02-01T10:00,2025-02-01T11:00,30.0,AAA,30.0,Pending";
        String exact = String.valueOf(b.getBookingID()) + ",R100," + b.getCheckInTime() + "," + b.getCheckOutTime() + ",30.0,FAC100,60.0,Pending";
        writeCsvLines(List.of(header, other, exact));

        new CancelBookingCommand(b).execute();

        List<String> remaining = readCsvLines();
        // header and other should remain (exact removed)
        assertEquals(2, remaining.size());
        assertEquals(header, remaining.get(0));
        assertEquals(other, remaining.get(1));
    }

    // 10) If line format has leading spaces, ensure match still requires starting with "id," so not removed erroneously
    @Test
    void testLineWithLeadingSpacesNotRemovedUnlessExactPrefix() throws IOException {
        Booking b = new Booking(user, room, now, now.plusHours(2));
        String header = "bookingID,roomID,checkIn,checkOut,deposit,user,totalPrice,status";
        String spaced = "   " + b.getBookingID() + ",R100,2025-01-01T10:00,2025-01-01T11:00,30.0,FAC100,30.0,Pending";
        String exact = String.valueOf(b.getBookingID()) + ",R100," + b.getCheckInTime() + "," + b.getCheckOutTime() + ",30.0,FAC100,60.0,Pending";
        writeCsvLines(List.of(header, spaced, exact));

        new CancelBookingCommand(b).execute();

        List<String> remaining = readCsvLines();
        // header + spaced should remain (exact removed)
        assertEquals(2, remaining.size());
        assertEquals(header, remaining.get(0));
        assertTrue(remaining.get(1).startsWith("   "));
    }
}