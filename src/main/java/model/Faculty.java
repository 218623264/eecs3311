package model;

public class Faculty implements User {

    private String email;
    private String password;
    private String userID;
    private boolean verified;
    private double hourlyRate = 30.0;
    private String userType = "faculty";

    public Faculty(String email, String password, String userID) {
        this.email = email;
        this.password = password;
        this.userID= userID;
        this.verified= false;
    }

    @Override
    public void login(String email, String password) {
        System.out.println("Faculty logged in: " + email);
    }

    @Override
    public void logout() {
        System.out.println("Faculty logged out.");
    }

    @Override
    public void viewProfile() {
        System.out.println("Faculty profile: " + email);
    }

    @Override
    public boolean isVerified()
    { return verified; }

    @Override
    public void setVerified(boolean v)
    { verified = v; }

    @Override
    public String getAccountType()
    { return userType; }

    @Override public String getEmail() { return email; }

    @Override
    public String getID() { return this.userID; }
}