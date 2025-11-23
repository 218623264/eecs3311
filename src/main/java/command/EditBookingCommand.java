package command;

import java.time.LocalDateTime;
import model.Booking;

public class EditBookingCommand implements BookingCommand {
    private Booking booking;
    private LocalDateTime newCheckIn, newCheckOut;
    public EditBookingCommand(Booking booking, LocalDateTime newCheckIn, LocalDateTime newCheckOut){
        this.booking = booking;
        this.newCheckIn = newCheckIn;
        this.newCheckOut = newCheckOut;
    }
    @Override
    public Booking execute() {
        booking.edit(newCheckIn, newCheckOut);
        return booking;
    }
}