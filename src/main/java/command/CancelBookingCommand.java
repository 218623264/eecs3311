package command;

import model.Booking;

public class CancelBookingCommand implements BookingCommand {
    private Booking booking;
    public CancelBookingCommand(Booking booking){ this.booking = booking; }
    @Override
    public void execute() { booking.cancel(); }
}