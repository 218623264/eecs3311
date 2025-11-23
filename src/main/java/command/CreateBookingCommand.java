package command;

import model.Booking;
import model.User;
import room.Room;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateBookingCommand implements BookingCommand {

    private static final String BOOKINGS_PATH = "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\bookings.csv";

    private Booking booking;
    private User user;
    private Room room;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;

    public CreateBookingCommand(User user, Room room, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        this.user = user;
        this.room = room;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    @Override
    public Booking execute() {

        long hours = (long) Math.ceil(Duration.between(this.checkInTime, this.checkOutTime).toMinutes() / 60.0);

        this.booking = new Booking(this.user, this.room, this.checkInTime, this.checkOutTime);

        //long hours = (long) Math.ceil(Duration.between(checkIn, checkOut).toMinutes() / 60.0);
        //double rate = getHourlyRate(user.getAccountType());
        //double totalPrice = hours * rate;
        //booking.setTotalPrice(totalPrice);

        saveBookingToCsv(this.booking);
        room.setOccupied(true);

        return this.booking;
    }

    private void saveBookingToCsv(Booking booking) {
        File file = new File(BOOKINGS_PATH);
        boolean fileExists = file.exists();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // write header if new file or empty
            if (!fileExists || file.length() == 0) {
                out.println("bookingID, roomID, checkInTime, checkOutTime, deposit, user, totalPrice, status");
            }

            out.printf("%s,%s,%s,%s,%.2f,%s,%.2f,%s%n",
                    booking.getBookingID(),
                    booking.getRoom().getRoomID(),
                    booking.getCheckInTime().format(fmt),
                    booking.getCheckOutTime().format(fmt),
                    booking.getDepositAmount(),
                    // "user" column: per your choice B, this should be organizationID
                    booking.getUser().getID(),  // or whatever getter you use
                    booking.getTotalPrice(),
                    booking.getStatus()
            );
        } catch (IOException e) {
            System.err.println("Failed to write booking to CSV: " + e.getMessage());
        }
    }
}