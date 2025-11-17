package Booking;

import java.time.LocalDateTime;

/**
 * Initial Booking class skeleton.
 * No external dependencies yet.
 * You can integrate User, Room, and Payment later.
 */
public class Booking {

    // -----------------------------
    // Fields (Basic)
    // -----------------------------
    public enum TempUserType{
        student,
        faculty,
        staff,
        partner
    }
    private String bookingID;

    // Temporary placeholders (replace later)
    private TempUserType userID; 
    private String roomID;     // later convert to Room object

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private double depositAmount;
    private double hourlyRate;
    private double totalPrice;

    private BookingStatus status;

    // -----------------------------
    // Constructor
    // -----------------------------
    public Booking(String bookingID, TempUserType userID, String roomID,
                   LocalDateTime checkInTime, LocalDateTime checkOutTime) {

        this.bookingID = bookingID;
        this.userID = userID;
        this.roomID = roomID;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = BookingStatus.Pending;
    }

    // -----------------------------
    // Methods to Implement Later
    // -----------------------------

    public void create() {
        // TODO: Req3 - pricing logic (based on user type)
        // TODO: Req8 - validate ID format (just basic check)
        // TODO: Req8 - enforce timing rules
        // TODO: calculate deposit (1-hour fee)
        // TODO: set status to Pending
        switch(userID){
            case student:
                this.depositAmount = 20.0; 
                this.hourlyRate = 20.0;    
                break;
            case faculty:
                this.depositAmount = 30.0; 
                this.hourlyRate = 30.0;    
                break;
            case staff:
                this.depositAmount = 40.0;  
                this.hourlyRate = 40.0;    
                break;
            case partner:
                this.depositAmount = 50.0; 
                this.hourlyRate = 50.0;    
                break;
        }
        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        this.totalPrice = hours * this.hourlyRate;
        this.status = BookingStatus.Pending;
    }

    public void edit(LocalDateTime newCheckIn, LocalDateTime newCheckOut) {
        // Req8 
        if (this.status != BookingStatus.Pending) {
            throw new IllegalStateException("Only pending bookings can be edited.");
        }
        else if (LocalDateTime.now().isAfter(this.checkInTime)) {
            throw new IllegalStateException("Cannot edit booking after check-in time.");
        }
        else if (newCheckOut.isBefore(newCheckIn) || newCheckIn.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid check-in/check-out times.");
        }
        else if (newCheckIn.equals(this.checkInTime) && newCheckOut.equals(this.checkOutTime)) {
            throw new IllegalArgumentException("New check-in/check-out times are the same as the current ones.");
        }
        else if (newCheckIn.isAfter(newCheckOut)) {
            throw new IllegalArgumentException("Check-in time must be before check-out time.");
        }
        else if (newCheckIn.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Check-in time cannot be in the past.");
        }
        this.checkInTime = newCheckIn;
        this.checkOutTime = newCheckOut;

        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        this.totalPrice = hours * this.hourlyRate;
        this.status = BookingStatus.Pending;

    }

    public void cancel() {
        // TODO: Req8 - only before start time
        if (this.status != BookingStatus.Pending) {
            throw new IllegalStateException("Only pending bookings can be cancelled.");
        }
        else if (LocalDateTime.now().isAfter(this.checkInTime)) {
            throw new IllegalStateException("Cannot cancel booking after check-in time.");
        }
        this.status = BookingStatus.Cancelled;
    }

    public void extend(LocalDateTime newCheckOutTime) {
        // Req9 
        // adjust pricing
        if (this.status != BookingStatus.CheckedIn) {
            throw new IllegalStateException("Only checked-in bookings can be extended.");
        }
        else if (newCheckOutTime.isBefore(this.checkOutTime) || newCheckOutTime.equals(this.checkOutTime)) {
            throw new IllegalArgumentException("New check-out time must be after current check-out time.");
        }
        this.checkOutTime = newCheckOutTime;

        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        this.totalPrice = hours * this.hourlyRate;
    }

    public void applyDeposit() {
        // TODO: apply deposit rules
        if (this.status == BookingStatus.Pending && LocalDateTime.now().isAfter(this.checkInTime.plusMinutes(30))) {
            this.depositAmount=0.0;        }

        else  if (this.status != BookingStatus.Completed) {
            throw new IllegalStateException("Deposit can only be applied after booking is completed.");
        }
        else if (this.depositAmount <= 0) {
            throw new IllegalStateException("No deposit to apply.");
        }
        else {
            this.depositAmount= this.hourlyRate;
     }
    }

    public String checkStatus() {
        return status.toString();
    }

    public void checkIn(String userID) {
        // TODO: badge/ID validation mock
        this.status = BookingStatus.CheckedIn;
    }

    public void checkOut() {
        this.status = BookingStatus.Completed;
    }

    // -----------------------------
    // Getters
    // -----------------------------
    public String getBookingID() {
        return bookingID;
    }

    public TempUserType getUserID() {
        return userID;
    }

    public String getRoomID() {
        return roomID;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }
}