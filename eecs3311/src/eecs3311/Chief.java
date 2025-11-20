package eecs3311;

public class Chief implements User {
    private String email;
    private String password;
    private boolean verified;

    public Chief(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override public String getEmail() { return email; }
    @Override public String getAccountType() { return "chief"; }

    @Override public void login(String email, String password) { }
    @Override public void logout() { }
    @Override public void viewProfile() { }
    
    @Override
    public boolean isVerified() 
     { return verified; }
     
     
     @Override
     public void setVerified(boolean v) 
     { verified = v; }


}