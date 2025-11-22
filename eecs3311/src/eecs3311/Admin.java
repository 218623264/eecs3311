package eecs3311;
import java.util.List;
import java.util.ArrayList;

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
    
  
    
    @Override public String getEmail() { return email; }
    
    
    
    
    
    private List<Room> managedRooms = new ArrayList<>();

    public void addRoom(Room room) {
        managedRooms.add(room);
        System.out.println("Admin added room: " + room.getRoomID());
    }

    public List<Room> getManagedRooms() {
        return managedRooms;
    }

 
    public void enableRoom(Room room) {
        if (room.isUnderMaintenance()) {
            System.out.println("Cannot enable room " + room.getRoomID() +
                    " because it is under maintenance.");
            return;
        }
        room.setEnabled(true);
        System.out.println("Room " + room.getRoomID() + " enabled.");
    }

 
    public void disableRoom(Room room) {
        room.setEnabled(false);
        System.out.println("Room " + room.getRoomID() + " disabled.");
    }


    public void putRoomUnderMaintenance(Room room) {
        room.setUnderMaintenance(true);
        room.setEnabled(false); 
        System.out.println("Room " + room.getRoomID() + " placed under maintenance.");
    }

   
    public void removeMaintenance(Room room) {
        room.setUnderMaintenance(false);
        System.out.println("Maintenance cleared for room " + room.getRoomID());
    }

    
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}


