package model;

public interface User {

    String userID = "";
    String email = "";
    String password = "";
    String userType = "";
    boolean verified = false;
    double hourlyRate = 0;

    void login(String email, String password);
    void logout();
    void viewProfile();
    String getAccountType();
    String getEmail();
    boolean isVerified();
    void setVerified(boolean verified);
    String getID();
    String getPassword();
    double getHourlyRate();
}