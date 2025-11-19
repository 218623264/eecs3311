package room;

public class Room {
    private String roomID;
    private int capacity;
    private String buildingName;
    private String roomNumber;

    public Room(String roomID, int capacity, String buildingName, String roomNumber) {
        this.roomID = roomID;
        this.capacity = capacity;
        this.buildingName = buildingName;
        this.roomNumber = roomNumber;
    }
    public String getRoomID() {
        return roomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getFullLocation() {
        return buildingName + " - " + roomNumber;
    }

    @Override
    public String toString() {
        return "Room " + roomID + " | " + getFullLocation() + " | Capacity: " + capacity;
    }
}
