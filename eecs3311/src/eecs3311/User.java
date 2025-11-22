package eecs3311;

public interface User {
	

    void login(String email, String password);
    void logout();
    void viewProfile();
    String getAccountType();
    String getEmail();
    boolean isVerified();
    void setVerified(boolean verified);
    
    void setEmail(String email);
    void setPassword(String password);

}

