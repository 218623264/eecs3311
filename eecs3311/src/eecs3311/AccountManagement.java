package eecs3311;
import java.util.ArrayList;
import java.util.List;

public class AccountManagement {

    private List<User> accounts = new ArrayList<>();
    private UserFactory factory;

    public AccountManagement(UserFactory factory) {
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
        accounts.add(newUser);

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
        accounts.add(admin);

        System.out.println("Chief " + chief.getEmail() + " created admin: " + adminEmail);
        return admin;
    }

 

    private boolean isEmailTaken(String email) {
        for (User u : accounts) {
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
}
