// package Booking;

// public class CreateBookingCommand implements BookingCommand {
//     private Booking booking;

//     public CreateBookingCommand(Booking booking) {
//         this.booking = booking;
//     }

//     @Override
//     public void execute() {
//         booking.create();
//     }
    
// }
package Booking;

import java.time.LocalDateTime;

public class CreateBookingCommand implements BookingCommand {
    private String bookingID;
    private Booking.TempUserType userType;
    private String roomID;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private Booking createdBooking; // store the new booking

    public CreateBookingCommand(String bookingID,
                                Booking.TempUserType userType,
                                String roomID,
                                LocalDateTime checkIn,
                                LocalDateTime checkOut) {
        this.bookingID = bookingID;
        this.userType = userType;
        this.roomID = roomID;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    @Override
    public void execute() {
        createdBooking = new Booking(
                bookingID,
                userType,
                roomID,
                checkIn,
                checkOut
        );
        createdBooking.create(); // run pricing + deposit logic
    }

    public Booking getCreatedBooking() {
        return createdBooking;
    }
}