package command;

import model.Booking;
import model.User;
import room.Room;

public class CreateBookingCommand implements BookingCommand {

    private Booking booking;
    private User user;
    private Room room;
    String checkInTime;
    String checkOutTime;

    public CreateBookingCommand(User user, Room room, String checkInTime, String checkOutTime) {
        this.user = user;
        this.room = room;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    @Override
    public void execute() {
        this.booking.create();
    }

}