package model;

import java.time.LocalDateTime;
import java.util.Random;

import room.*;
import model.*;

public class Booking {

    public enum UserType{
        student,
        faculty,
        staff,
        partner
    }
    private int bookingID;

    private User user;
    private Room room;
    private String roomID;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private double depositAmount;
    private double hourlyRate;
    private double totalPrice;

    private BookingStatus status;

    public Booking(User user, Room room,
                   LocalDateTime checkInTime, LocalDateTime checkOutTime) {

        Random random = new Random();
        this.bookingID = random.nextInt(900) + 100;
        this.user = user;
        this.room = room;
        this.roomID = room.getRoomID();
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = BookingStatus.Pending;

        this.hourlyRate = switch (user.getAccountType().toLowerCase()) {
            case "student" -> 20.0;
            case "faculty" -> 30.0;
            case "staff" -> 40.0;
            default -> 50.0;
        };

        this.depositAmount = this.hourlyRate;
        long hours = java.time.Duration.between(this.checkInTime, this.checkOutTime).toHours();
        this.totalPrice = this.hourlyRate * hours;
    }

    public void create() {
        this.status = BookingStatus.Pending;
        System.out.println("Booking " + bookingID + " created for " + user.getEmail());
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

    // -----------------------------
    // Check-in (Req5)
    // -----------------------------
    public void checkIn(String scannedBadgeID) {
        if (!user.getID().equals(scannedBadgeID)) {
            throw new SecurityException("Badge ID does not match booking!");
        }
        this.status = BookingStatus.CheckedIn;
        applyDeposit();
    }

    public void checkOut() {
        this.status = BookingStatus.Completed;
    }

    public int getBookingID() {
        return this.bookingID;
    }

    public User getUser() {
        return this.user;
    }

    public Room getRoom() {
        return this.room;
    }

    public LocalDateTime getCheckInTime() {
        return this.checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return this.checkOutTime;
    }

    public double getDepositAmount() {
        return this.depositAmount;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}