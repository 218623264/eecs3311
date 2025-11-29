import room.Room;
import room.SensorObserver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testConstructorValues() {
        Room r = new Room("R1", 30, "Building", "101");
        assertEquals("R1", r.getRoomID());
        assertEquals(30, r.getCapacity());
        assertEquals("Building", r.getBuildingName());
        assertEquals("101", r.getRoomNumber());
    }

    @Test
    void testSetOccupied() {
        Room r = new Room("R1", 30, "B", "101");
        r.setOccupied(true);
        assertTrue(r.isOccupied());
    }

    @Test
    void testSetScannedID() {
        Room r = new Room("R1", 30, "B", "101");
        r.setScannedID("User123");
        assertEquals("User123", r.getScannedID());
    }

    @Test
    void testEnableDisableRoom() {
        Room r = new Room("R1", 30, "B", "101");
        r.setEnabled(false);
        assertFalse(r.isEnabled());
        r.setEnabled(true);
        assertTrue(r.isEnabled());
    }

    @Test
    void testMaintenanceFlag() {
        Room r = new Room("R1", 30, "B", "101");
        r.setUnderMaintenance(true);
        assertTrue(r.isUnderMaintenance());
    }

    @Test
    void testGetFullLocation() {
        Room r = new Room("R1", 30, "Bergeron", "101");
        assertEquals("Bergeron - 101", r.getFullLocation());
    }

    @Test
    void testToString() {
        Room r = new Room("R1", 30, "B", "101");
        assertTrue(r.toString().contains("R1"));
    }

    @Test
    void testObserverAdded() {
        Room r = new Room("R1", 30, "B", "101");
        SensorObserver o = rm -> {};
        assertDoesNotThrow(() -> r.addObserver(o));
    }

    @Test
    void testObserverRemoved() {
        Room r = new Room("R1", 30, "B", "101");
        SensorObserver o = rm -> {};
        r.addObserver(o);
        assertDoesNotThrow(() -> r.removeObserver(o));
    }

    @Test
    void testNotifyObserversDoesNotCrash() {
        Room r = new Room("R1", 30, "B", "101");
        r.addObserver(room -> {});
        assertDoesNotThrow(r::notifyObservers);
    }
}
