package room;

import java.util.ArrayList;
import java.util.List;

public class Room implements SensorSubject {

    private String roomID;
    private int capacity;
    private String buildingName;
    private String roomNumber;

    private boolean occupied = false;
    private String scannedID = null;

    private List<SensorObserver> observers = new ArrayList<>();

    public Room(String roomID, int capacity, String buildingName, String roomNumber) {
        this.roomID = roomID;
        this.capacity = capacity;
        this.buildingName = buildingName;
        this.roomNumber = roomNumber;
    }

    @Override
    public void addObserver(SensorObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(SensorObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (SensorObserver o : observers) {
            o.onSensorUpdate(this);
        }
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
        notifyObservers();
    }

    public void setScannedID(String scannedID) {
        this.scannedID = scannedID;
        notifyObservers();
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getScannedID() {
        return scannedID;
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
