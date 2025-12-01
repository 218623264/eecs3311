package command;

import model.Booking;
import model.User;
import room.Room;
import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CreateBookingCommandTest {

    private User mockUser;
    private Room mockRoom;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Path tempCsv;

    @BeforeEach
    public void setup() throws Exception {
        mockUser = new User("U1", "John Doe");
        mockRoom = new Room("R1", 100.0);
        mockRoom.setOccupied(false);

        checkIn = LocalDateTime.of(2025, 1, 1, 10, 0);
        checkOut = LocalDateTime.of(2025, 1, 1, 14, 30); // 4.5 hours

        // Create temp csv to avoid writing into real project directory
        tempCsv = Files.createTempFile("booking_test", ".csv");

        // Override BOOKINGS_PATH using reflection
        Field pathField = CreateBookingCommand.class.getDeclaredField("BOOKINGS_PATH");
        pathField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(pathField, pathField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        pathField.set(null, tempCsv.toString());
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(tempCsv);
    }

    // ------------------------------ TESTS ----------------------------------

    @Test
    public void testExecuteReturnsBooking() {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        Booking b = cmd.execute();

        assertNotNull(b);
        assertEquals(mockUser, b.getUser());
        assertEquals(mockRoom, b.getRoom());
    }

    @Test
    public void testRoomBecomesOccupied() {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        cmd.execute();

        assertTrue(mockRoom.isOccupied());
    }

    @Test
    public void testBookingTimesSetCorrectly() {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        Booking b = cmd.execute();

        assertEquals(checkIn, b.getCheckInTime());
        assertEquals(checkOut, b.getCheckOutTime());
    }

    @Test
    public void testCsvCreatedAndHeaderWritten() throws Exception {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        cmd.execute();

        String content = Files.readString(tempCsv);
        assertTrue(content.startsWith("bookingID"));
    }

    @Test
    public void testCsvAppendsNewLine() throws Exception {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        cmd.execute(); // first line
        cmd.execute(); // second line

        long lineCount = Files.lines(tempCsv).count();
        assertEquals(3, lineCount); // header + 2 bookings
    }

    @Test
    public void testBookingIDWrittenToCsv() throws Exception {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        Booking b = cmd.execute();

        String content = Files.readString(tempCsv);
        assertTrue(content.contains(b.getBookingID()));
    }

    @Test
    public void testRoomIDWrittenToCsv() throws Exception {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        cmd.execute();

        String content = Files.readString(tempCsv);
        assertTrue(content.contains(mockRoom.getRoomID()));
    }

    @Test
    public void testUserIDWrittenToCsv() throws Exception {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);
        cmd.execute();

        String content = Files.readString(tempCsv);
        assertTrue(content.contains(mockUser.getID()));
    }

    @Test
    public void testExecuteMultipleTimesCreatesMultipleBookings() {
        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);

        Booking b1 = cmd.execute();
        Booking b2 = cmd.execute();

        assertNotEquals(b1.getBookingID(), b2.getBookingID());
    }

    @Test
    public void testFileIOExceptionHandledGracefully() throws Exception {

        // Make CSV read-only → triggers IOException
        Files.writeString(tempCsv, "");
        tempCsv.toFile().setReadOnly();

        CreateBookingCommand cmd = new CreateBookingCommand(mockUser, mockRoom, checkIn, checkOut);

        assertDoesNotThrow(cmd::execute);

        // Reset permissions for cleanup
        tempCsv.toFile().setWritable(true);
    }
}