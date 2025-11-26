package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import factory.*;

public class AccountManagement {

    private static List<User> users = new ArrayList<>();
    private static UserFactory factory;
    private static AccountManagement instance = new AccountManagement(new ConcreteUserFactory());

    public static AccountManagement getInstance() {
        return instance;
    }

    public AccountManagement(ConcreteUserFactory factory) {
        this.factory = factory;
    }



    public User registerUser(String type,
                             String email,
                             String password,
                             String organizationID) {


        if (isEmailTaken(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }


        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }


        if (!isStrongPassword(password)) {
            throw new IllegalArgumentException("Weak password.");
        }


        if (!type.equalsIgnoreCase("student") &&
                !type.equalsIgnoreCase("faculty") &&
                !type.equalsIgnoreCase("staff") &&
                !type.equalsIgnoreCase("partner")) {
            throw new IllegalArgumentException(
                    "Only student/faculty/staff/partner accounts can self-register.");
        }


        if (organizationID == null || organizationID.isEmpty()) {
            throw new IllegalArgumentException("Organization ID / student number required.");
        }


        User newUser = factory.createUser(type, email, password, organizationID);
        users.add(newUser);

        System.out.println(type + " account created successfully for " + email);
        return newUser;
    }



    public Admin createAdminAccount(Chief chief,
                                    String adminEmail,
                                    String adminPassword) {

        if (chief == null) {
            throw new IllegalArgumentException("Only the Chief Event Coordinator can create admin accounts.");
        }

        if (isEmailTaken(adminEmail)) {
            throw new IllegalArgumentException("Admin email already in use.");
        }

        if (!isStrongPassword(adminPassword)) {
            throw new IllegalArgumentException("Weak admin password.");
        }


        User user = factory.createUser("admin", adminEmail, adminPassword, null);
        if (!(user instanceof Admin)) {
            throw new IllegalStateException("Factory did not return an Admin for type 'admin'");
        }

        Admin admin = (Admin) user;
        users.add(admin);

        System.out.println("Chief " + chief.getEmail() + " created admin: " + adminEmail);
        return admin;
    }



    private boolean isEmailTaken(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }


    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[^@]+@[^@]+\\.[^@]+$");
    }

    private boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSymbol = password.matches(".*[^A-Za-z0-9].*");
        return hasUpper && hasLower && hasDigit && hasSymbol;
    }

    public static User findUserByEmail(String email) {
        String path = System.getProperty("user.dir") + "/eecs3311/src/main/data/Users.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String storedEmail = parts[0].trim();
                if (storedEmail.equalsIgnoreCase(email)) {
                    String password = parts[1].trim();
                    String type = parts[2].trim();
                    String orgID = parts[3].trim();
                    boolean verified = Boolean.parseBoolean(parts[4].trim());

                    // Create the correct User subtype from backend
                    User user = factory.createUser(type, storedEmail, password, orgID);
                    user.setVerified(verified);

                    // sync memory list
                    if (!users.contains(user)) {
                        users.add(user);
                    }

                    return user;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading Users.csv: " + e.getMessage());
        }

        return null;
    }

    public static void addUser(User user) {
        users.add(user);
    }
}