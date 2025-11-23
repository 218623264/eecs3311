package model;

public class Staff implements User {

    private String email;
    private String password;
    private String userID;
    private boolean verified;
    private double hourlyRate = 40.0;
    private String userType = "staff";

    public Staff(String email, String password, String userID) {
        this.email = email;
        this.password = password;
        this.userID= userID;
        this.verified= false;
    }

    @Override
    public void login(String e, String p){
        System.out.println("Staff logged in: " + e);
    }
    @Override
    public void logout(){
        System.out.println("Staff logged out.");
    }
    @Override
    public void viewProfile(){
        System.out.println("Staff profile: " + email);
    }

    @Override
    public boolean isVerified()
    { return verified; }


    @Override
    public void setVerified(boolean v)
    { verified = v; }

    @Override
    public String getAccountType() {
        return userType; }

    @Override public String getEmail() { return this.email; }

    @Override
    public String getID() { return this.userID; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public double getHourlyRate() { return this.hourlyRate; }
}