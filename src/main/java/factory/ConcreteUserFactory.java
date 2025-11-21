package factory;

import model.*;

public class ConcreteUserFactory extends UserFactory {
    @Override
    public User createUser(String type, String email, String password, String organizationID) {
        return switch (type.toLowerCase()) {
            case "student" -> new Student(email, password, organizationID);
            case "faculty" -> new Faculty(email, password, organizationID);
            case "staff"   -> new Staff(email, password, organizationID);
            case "partner" -> new Partner(email, password, organizationID);
            case "admin"   -> new Admin(email, password);
            default -> throw new IllegalArgumentException("Unknown user type: " + type);
        };
    }
}