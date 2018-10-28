package edu.ycp.cs481.control;

import java.util.ArrayList;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.User;
import java.math.BigInteger;
import java.security.MessageDigest;

public class UserController {
	User U = new User();
	Database db = new Database(); 
	String sha1;
	
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
	
	public boolean authenticate(User u, String pswd) {
		boolean real = false;
		
		pswd = hashPassword(pswd);
		
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
    
    public String hashPassword(String password) {
    	try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(password.getBytes("utf8"));
	        sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e){
			e.printStackTrace();
		}
    	return sha1;
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
    
    public Integer insertUser(String email, String password, String firstName, String lastName, boolean isAdmin, 
			boolean isArchived, int positionID){
		return db.insertAndGetID("User", "user_id", 
				new String[]{"email", "password", "first_name", "last_name", "admin_flag", "archive_flag", "position_id"}, 
				new String[]{email, "SHA('"+ password + "')", firstName, lastName, String.valueOf(isAdmin), 
						String.valueOf(isArchived), String.valueOf(positionID)});
	}
    
    public ArrayList<User> searchForUsers(int id, String email, String fname, String lname, int positionID) {
    	return db.searchForUsers(id, email, fname, lname, positionID);
    }
    
    //will implement these later
    public void changeUserEmail(String oldEmail, String newEmail, String pass) {
    	db.changeUserEmail(oldEmail, newEmail, pass);
    }
    
    public void changeUserPassword(String email, String oldPass, String newPass) {
    	db.changeUserPassword(email, oldPass, newPass);
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
}
