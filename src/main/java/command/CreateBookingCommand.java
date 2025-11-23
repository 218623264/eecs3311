package command;

import model.Booking;

public class CreateBookingCommand implements BookingCommand {

    private Booking booking;

    public CreateBookingCommand(Booking booking) {
        this.booking = booking;
    }

    @Override
    public void execute() {
        this.booking.create();

    }

}