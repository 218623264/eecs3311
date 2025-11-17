package Booking;

import java.time.LocalDateTime;

public class EditBookingCommand implements BookingCommand {
    private Booking booking;
    private LocalDateTime newCheckIn, newCheckOut;
    public EditBookingCommand(Booking booking, LocalDateTime newCheckIn, LocalDateTime newCheckOut){
        this.booking = booking;
        this.newCheckIn = newCheckIn;
        this.newCheckOut = newCheckOut;
    }
    @Override
    public void execute() { booking.edit(newCheckIn, newCheckOut); }
}