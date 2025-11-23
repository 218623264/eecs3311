package model;

public class Partner implements User {

    private String email;
    private String password;
    private String userID;
    private boolean verified;
    private double hourlyRate = 50.0;
    private String userType = "partner";

    public Partner(String email, String password, String userID) {
        this.email = email;
        this.password = password;
        this.userID= userID;
        this.verified= false;
    }

    @Override
    public void login(String e, String p){
        System.out.println("Partner logged in: " + e);
    }
    @Override
    public void logout(){
        System.out.println("Partner logged out.");

    }

    @Override
    public void viewProfile(){
        System.out.println("Partner profile: " + email);
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

    @Override public String getEmail() { return email; }

    @Override
    public String getID() { return this.userID; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public double getHourlyRate() { return this.hourlyRate; }
}