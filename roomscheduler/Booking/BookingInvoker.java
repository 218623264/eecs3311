package Booking;

public class BookingInvoker {
    private BookingCommand command;
    public void setCommand(BookingCommand command) { this.command = command; }
    public void executeCommand() {
        if(command != null) command.execute();
    }
}