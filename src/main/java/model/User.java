package model;

public interface User {

    String userID = "";
    //String userName();
    String userType = "";

    void login(String email, String password);
    void logout();
    void viewProfile();
    String getAccountType();
    String getEmail();
    boolean isVerified();
    void setVerified(boolean verified);
    String getID();

}