/*
package facade;

import model.*;
import service.*;
import command.*;
import room.*;
import payment.*;
import factory.*;

import java.time.LocalDateTime;
import java.util.List;

public class Facade {

    private final AccountManagement accountManagement;
    private final BookingService bookingService;
    private final RoomService roomService;

    // Current logged-in user (set after login/signup)
    private User currentUser;

    public SystemFacade() {
        UserFactory factory = new ConcreteUserFactory();
        this.accountManagement = new AccountManagement(factory);
        this.bookingService = new BookingService();
        this.roomService = new RoomService();  // Loads rooms.csv automatically
    }

    // ==================== USER & AUTH ====================
    public User signup(String type, String email, String password, String id) {
        User user = accountManagement.registerUser(type, email, password, id);
        if (user != null) {
            this.currentUser = user;
        }
        return user;
    }

    public boolean login(String email, String password) {
        // Simple mock login — in real project, check password hash
        // For demo, just find any user with that email
        // In future: add proper password check
        this.currentUser = findUserByEmail(email);
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ==================== BOOKING ====================
    public boolean bookRoom(String roomId, LocalDateTime start, int hours, PaymentStrategy paymentStrategy) {
        if (currentUser == null) return false;
        return bookingService.createBooking(currentUser, roomId, start, hours, paymentStrategy);
    }

    public void cancelBooking(String bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    public void extendBooking(String bookingId, LocalDateTime newEndTime) {
        bookingService.extendBooking(bookingId, newEndTime);
    }

    // ==================== ROOMS ====================
    public List<Room> getAllRooms() {
        return RoomRepository.getAllRooms();
    }

    public List<Room> getAvailableRooms() {
        return RoomRepository.getAllRooms().stream()
                .filter(r -> r.isEnabled() && !r.isOccupied())
                .toList();
    }

    // Admin only (Req6) — add check in real app
    public void adminAddRoom(String id, int capacity, String building, String number) {
        roomService.addRoom(id, capacity, building, number);
    }

    // Helper (mock for now)
    private User findUserByEmail(String email) {
        // In real app: search repository
        // For now, return a dummy — just to let login "work"
        return new Student(email, "temp", "S000000");
    }
}
*/