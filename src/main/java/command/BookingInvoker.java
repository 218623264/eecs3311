package command;

import model.Booking;

public class BookingInvoker {

    private BookingCommand command;

    public void setCommand(BookingCommand command) { this.command = command; }

    public Booking executeCommand() {
            return command.execute();
    }
}