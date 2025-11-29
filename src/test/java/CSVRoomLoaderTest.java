import room.CSVRoomLoader;
import room.RoomRepository;
import room.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVRoomLoaderTest {

    private final String csvPath = "src/main/data/rooms.csv";

    @BeforeEach
    void setup() {
        RoomRepository.getAllRooms().clear();
    }

    @Test
    void testLoadCSVNoError() {
        assertDoesNotThrow(() -> CSVRoomLoader.loadRooms(csvPath));
    }

    @Test
    void testRoomsLoaded() {
        CSVRoomLoader.loadRooms(csvPath);
        assertTrue(RoomRepository.getAllRooms().size() > 0);
    }

    @Test
    void testFirstRoomExists() {
        CSVRoomLoader.loadRooms(csvPath);
        assertNotNull(RoomRepository.getRoomByID("R101"));
    }

    @Test
    void testCapacityLoadedCorrectly() {
        CSVRoomLoader.loadRooms(csvPath);
        Room r = RoomRepository.getRoomByID("R101");
        assertEquals(30, r.getCapacity());
    }

    @Test
    void testBuildingNameLoaded() {
        CSVRoomLoader.loadRooms(csvPath);
        Room r = RoomRepository.getRoomByID("R101");
        assertEquals("Bergeron Centre", r.getBuildingName());
    }

    @Test
    void testRoomNumberLoaded() {
        CSVRoomLoader.loadRooms(csvPath);
        Room r = RoomRepository.getRoomByID("R101");
        assertEquals("101", r.getRoomNumber());
    }

    @Test
    void testMultipleRoomsPresent() {
        CSVRoomLoader.loadRooms(csvPath);
        assertTrue(RoomRepository.getAllRooms().size() >= 5);
    }

    @Test
    void testSecondRoomExists() {
        CSVRoomLoader.loadRooms(csvPath);
        assertNotNull(RoomRepository.getRoomByID("R102"));
    }

    @Test
    void testInvalidFileDoesNotCrash() {
        assertDoesNotThrow(() -> CSVRoomLoader.loadRooms("src/main/data/fakefile.csv"));
    }

    @Test
    void testLoadDoesNotReturnAnything() {
        CSVRoomLoader.loadRooms(csvPath);
        assertTrue(true);
    }
}
