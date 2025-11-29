import room.Room;
import room.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomRepositoryTest {

    @BeforeEach
    void setup() {
        RoomRepository.getAllRooms().clear();
    }

    @Test
    void testAddRoom() {
        Room r = new Room("R1", 30, "B", "101");
        RoomRepository.addRoom(r);
        assertEquals(1, RoomRepository.getAllRooms().size());
    }

    @Test
    void testGetRoomByID() {
        Room r = new Room("R1", 30, "B", "101");
        RoomRepository.addRoom(r);
        assertNotNull(RoomRepository.getRoomByID("R1"));
    }

    @Test
    void testGetRoomByIDNotFound() {
        assertNull(RoomRepository.getRoomByID("X"));
    }

    @Test
    void testUpdateRoomStatusTrue() {
        Room r = new Room("R1", 30, "B", "101");
        RoomRepository.addRoom(r);
        assertTrue(RoomRepository.updateRoomStatus("R1", true));
    }

    @Test
    void testUpdateRoomStatusFalse() {
        assertFalse(RoomRepository.updateRoomStatus("NOPE", true));
    }

    @Test
    void testRoomStatusActuallyChanges() {
        Room r = new Room("R1", 30, "B", "101");
        RoomRepository.addRoom(r);
        RoomRepository.updateRoomStatus("R1", true);
        assertTrue(r.isOccupied());
    }

    @Test
    void testMultipleRoomsAdded() {
        RoomRepository.addRoom(new Room("R1", 10, "B", "10"));
        RoomRepository.addRoom(new Room("R2", 20, "C", "20"));
        assertEquals(2, RoomRepository.getAllRooms().size());
    }

    @Test
    void testGetAllRoomsNotNull() {
        assertNotNull(RoomRepository.getAllRooms());
    }

    @Test
    void testGetAllRoomsReturnsList() {
        assertTrue(RoomRepository.getAllRooms() instanceof java.util.List);
    }

    @Test
    void testFindSpecificRoom() {
        RoomRepository.addRoom(new Room("A1", 10, "B", "101"));
        assertEquals("A1", RoomRepository.getRoomByID("A1").getRoomID());
    }
}
