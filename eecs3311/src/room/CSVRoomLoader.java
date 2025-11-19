package room;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVRoomLoader {

    public static void loadRooms(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line = br.readLine(); // skip header line

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String roomID = data[0];
                int capacity = Integer.parseInt(data[1]);
                String buildingName = data[2];
                String roomNumber = data[3];

                Room room = new Room(roomID, capacity, buildingName, roomNumber);
                RoomRepository.addRoom(room);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
