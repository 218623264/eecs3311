package eecs3311;

public class Student implements User {
	  private String email;
	  private String password;
    private String organizationID;
    private boolean verified;
    private double hourlyRate = 20.0;

    public Student(String email, String password, String organizationID) {
        this.email = email;
        this.password = password;
        this.organizationID= organizationID;
        this.verified= false;
    }

    @Override
    public void login(String email, String password) {
        System.out.println("Student logged in: " + email);
    }

    @Override
    public void logout() {
        System.out.println("Student logged out.");
    }

    @Override
    public void viewProfile() {
        System.out.println("Student profile: " + email);
    }
    
    @Override 
    public boolean isVerified() 
    { return verified; }
    
    @Override 
    public void setVerified(boolean v) 
    { verified = v; }
    
    @Override 
    public String getAccountType() { 
    	return "student"; }
    
    @Override public String getEmail() { return email; }
}

