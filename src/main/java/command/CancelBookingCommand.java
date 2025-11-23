package command;

import model.Booking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static model.BookingStatus.Cancelled;

public class CancelBookingCommand implements BookingCommand {
    private Booking booking;
    public CancelBookingCommand(Booking booking){ this.booking = booking; }

    @Override
    public Booking execute() {

        String csvPath = System.getProperty("user.dir") + "/eecs3311/src/main/data/bookings.csv";
        //String csvPath = "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\bookings.csv";
        boolean removed = false;
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String header = reader.readLine(); // keep header
            updatedLines.add(header);

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(booking.getBookingID() + ",")) {
                    updatedLines.add(line);
                } else {
                    removed = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading CSV while canceling: " + e.getMessage());
        }

        if (removed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath))) {
                for (String l : updatedLines) {
                    writer.write(l + "\n");
                }
            } catch (Exception e) {
                System.out.println("Error writing CSV while canceling: " + e.getMessage());
            }
        }

        this.booking.setStatus(Cancelled);

        return this.booking;

        // Update internal repository (or CSV) to mark as Cancelled
        //BookingRepository.cancelBooking(this.booking.getBookingID());

        // Update in-memory object state
        //this.booking.setStatus(Cancelled);

        // Return the same booking object for UI confirmation
        //return this.booking;
    }
}