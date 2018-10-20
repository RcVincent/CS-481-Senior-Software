package edu.ycp.cs481.control;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.User;

public class UserController {
	User U = new User();
	Database db = new Database(); 
	
	public UserController() {
		
	}
    
	/*public ArrayList<User> matchUserNameWithPassword(String username) {

		List<User> userList = db.matchUsernameWithPassword(username);

		ArrayList<User> users = new ArrayList<User>();

		for(User user : userList) {
			users.add(user);
		}

		return users;
	}*/
	
	public static boolean authenticate(User u, String pswd) {
		boolean real = false;
		if(u.getPassword().equals(pswd)){

			real = true;
		}

		return real;
	}
	
    public void login(){
        U.setLoginStatus(true);
    }
    
    public void logout(){
    	U.setLoginStatus(false);
    }
    
    public boolean Authenticate(){
        return false;
    }
    
    public void addPosition(Position p){
        
    }

    public void ChangeEmail(){

    }
    
    public boolean validateEmail(String entry) { // TODO: Make sure there's only one @
		int valid = 0;
		int atloc = 100;
		
		
		// Check and keep track of the location of @ in a string
		for (int x = 0; x < entry.length(); x++) {
			if (entry.charAt(x) == '@' && x != 0 && atloc == 100) {
				atloc = x;
				valid ++;
			}
		}
		
		for (int x = 0; x < entry.length(); x++) {
			if (entry.charAt(x) == '.' && x > atloc && entry.charAt(x-1) != '@' && x != entry.length()-1) {
				valid ++;
			}
		}
		
		if (valid == 2) 
			return true;
		
		else 
			return false;
}
    
    public void Archive(){
        U.setArchiveFlag(true);
    } 
    
    //************************************
  	//Implementing DB calls 
  	//************************************
    
    public Integer insertUserAndGetID(User u) {
    	return db.insertUser(u);
    }
    
    public User insertUser(User u) {
    	int id = db.insertUser(u);
    	return db.getUserByID(id);
    }
    
    public User FindUserByID(int userID) {
    	return db.getUserByID(userID);
    }
    
    public ArrayList<User> findUsersByFirstname(String fname) {
    	return db.getUsersByFirstName(fname);
    }
    
    public ArrayList<User> findUsersByLastname(String lname) {
    	return db.getUsersByLastName(lname);
    }
    
    public User findUserByEmail(String email) {
    	return db.getUserByEmail(email);
    }
    
    //will implement these later
    public void changeUserEmail(String oldEmail, String newEmail, String pass) {
    	db.changeUserEmail(oldEmail, newEmail, pass);
    }
    
    public void changeUserPassword(String email, String oldPass, String newPass) {
    	db.changeUserPassword(email, oldPass, newPass);
    }
    
    public List<User> findAllUsers() {
    	return db.findAllUsers(); 
    }
    
    public void ArchiveUser(int userID){
    	db.archiveUser(userID);
    }
    
    public void unarchiveUser(int userID){
    	db.unarchiveUser(userID);
    }
    
    public User changePosition(int userID, int positionID) {
    	return db.changePosition(userID, positionID);
    }
    
    public ArrayList<User> findUsersWithPosition(int positionID) {
    	return db.findUsersWithPosition(positionID);
    }
    
    
    
}
