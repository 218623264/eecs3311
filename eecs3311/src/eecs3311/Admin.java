package eecs3311;

public class Admin implements User {
	 private String email;
	  private String password;
   private boolean verified;
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
        this.verified= true;
    }
    
    @Override
    public void login(String e, String p){
    	System.out.println("Admin logged in: " + e); 
    	}
    
    @Override
    public void logout(){ 
    	System.out.println("Admin logged out."); 
    	}
    
    @Override
    public void viewProfile(){
    	System.out.println("Admin profile: " + email);
    	}
    
    @Override
    public boolean isVerified(){ 
    	return verified; 
    	}
    
    @Override
    public void setVerified(boolean v) 
    { verified = v; }
    
    @Override
    public String getAccountType(){
    	return "Admin"; 
    	}
    
    public void addRoom(Room room) {
        System.out.println("Admin added room " + room.getRoomID());
    }
    public void enableRoom(Room room) {
        room.setEnabled(true);
    }
    public void disableRoom(Room room) {
        room.setEnabled(false);
    }
    
    
    @Override public String getEmail() { return email; }
}


