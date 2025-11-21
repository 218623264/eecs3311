package model;

public interface User {

    int userID();
    //String userName();

    void login(String email, String password);
    void logout();
    void viewProfile();
    String getAccountType();
    String getEmail();
    boolean isVerified();
    void setVerified(boolean verified);

    }
}