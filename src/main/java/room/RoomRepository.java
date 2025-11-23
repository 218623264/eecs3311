package room;

import java.util.*;
//import java.util.ArrayList;
//import java.util.List;

public class RoomRepository {

    private static List<Room> rooms = new ArrayList<>();

    public static void addRoom(Room room) {
        rooms.add(room);
    }

    public static List<Room> getAllRooms() {
        return rooms;
    }

    public static Room getRoomByID(String roomID) {
        for (Room r : rooms) {
            if (r.getRoomID().equals(roomID)) {
                return r;
            }
        }
        return null;
    }

    public static boolean updateRoomStatus(String roomID, boolean occupied) {
        for (Room room : rooms) {
            if (room.getRoomID().equals(roomID)) {
                room.setOccupied(occupied);
                return true;
            }
        }
        return false;
    }
}