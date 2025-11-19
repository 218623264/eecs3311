package room;
import java.util.*;
//import java.util.ArrayList;
//import java.util.List;

public class RoomRepository {

    private static List<Room> rooms = new ArrayList<>();

    // Add a room to the repository
    public static void addRoom(Room room) {
        rooms.add(room);
    }

    // Return all rooms
    public static List<Room> getAllRooms() {
        return rooms;
    }

    // Return one room by ID
    public static Room getRoomByID(String roomID) {
        for (Room r : rooms) {
            if (r.getRoomID().equals(roomID)) {
                return r;
            }
        }
        return null; // if no match
    }
}
