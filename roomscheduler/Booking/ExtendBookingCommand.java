package Booking;

import java.time.LocalDateTime;

public class ExtendBookingCommand implements BookingCommand {
    private Booking booking;
    private LocalDateTime newCheckOut;
    public ExtendBookingCommand(Booking booking, LocalDateTime newCheckOut){ this.booking = booking; this.newCheckOut = newCheckOut; }
    @Override
    public void execute() { booking.extend(newCheckOut); }
}