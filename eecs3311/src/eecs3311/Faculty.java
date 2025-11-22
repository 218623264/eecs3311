package eecs3311;

public class Faculty implements User {
	  private String email;
	  private String password;
      private String organizationID;
      private boolean verified;
      private double hourlyRate = 30.0;

    public Faculty(String email, String password, String OrganizationID) {
        this.email = email;
        this.password = password;
        this.organizationID= OrganizationID;
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
    { return "faculty"; }
    
    @Override public String getEmail() { return email; }
    
    
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    
}
