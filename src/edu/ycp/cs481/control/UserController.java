package edu.ycp.cs481.control;

import java.util.ArrayList;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.User;
import java.sql.SQLException;

import org.mindrot.jbcrypt.*;

public class UserController {
	private User U = new User();
	private Database db = new Database();
	
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
        return BCrypt.checkpw(pswd, u.getPassword());
	}
	
    public void login(){
        U.setLoginStatus(true);
    }
    
    public void logout(){
    	U.setLoginStatus(false);
    }
    
    public void addPosition(Position p){
        
    }

    public void ChangeEmail(){

    }
    
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
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
    	password = hashPassword(password);
		return db.insertAndGetID("User", "user_id", 
				new String[]{"email", "password", "first_name", "last_name", "admin_flag", "archive_flag", "position_id"}, 
				new String[]{email, password, firstName, lastName, String.valueOf(isAdmin), 
						String.valueOf(isArchived), String.valueOf(positionID)});
	}
    
    public ArrayList<User> searchForUsers(int userID, String email, String firstName, String lastName, int positionID){
		try{
			String name = "";
			String sql = "select * from User";
			if(userID == -1 && (email == null || email.equalsIgnoreCase("")) && 
					(firstName == null || firstName.equalsIgnoreCase("")) && 
					(lastName == null || lastName.equalsIgnoreCase("")) && positionID == -1){
				name = "Get All Users";
			}else{
				name = "Get User with ";
				sql += " where ";
				boolean prevSet = false;
				
				if(userID != -1){
					name += "id of " + userID;
					sql += "user_id = " + userID;
					prevSet = true;
				}
				
				// TODO: Likely change strings for searching by partial?
				if(email != null && !email.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "email of " + email;
					sql += "email = '" + email + "'";
					prevSet = true;
				}
				
				if(firstName != null && !firstName.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "first name of " + firstName;
					sql += "first_name = '" + firstName + "'";
					prevSet = true;
				}
				
				if(lastName != null && !lastName.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "last name of " + lastName;
					sql += "last_name = '" + lastName + "'";
					prevSet = true;
				}
				
				if(positionID != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "position ID of " + positionID;
					sql += "position_id = " + positionID;
					prevSet = true;
				}
			}
			ArrayList<User> results = db.executeQuery(name, sql, db.getUserResFormat());
			if(results.size() == 0 && userID != -1){
				System.out.println("No User found with ID " + userID);
			}else if(results.size() > 1){
				if(userID != -1){
					System.out.println("Multiple Users found with ID " + userID + "! Returning null");
					return null;
				}else if(email != null && !email.equalsIgnoreCase("")){
					System.out.println("Multiple Users found with email " + email + "! Returning null");
					return null;
				}
			}
			return results;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
    
    //will implement these later
    public void changeUserEmail(int userID, String oldEmail, String newEmail){
    	db.executeUpdate("Change User Email", "update User set email = '" + newEmail + "' where "
				+ "email = '" + oldEmail + "' and user_id = " + userID);
    }
    
    public void changeUserPassword(int userID, String newPass){
    	db.executeUpdate("Change User Password", "update User set password = SHA('" + newPass + "') where "
    			+ "user_id = " + userID);
    }
    
    public void ArchiveUser(int userID){
    	db.archiveUser(userID);
    }
    
    public void unarchiveUser(int userID){
    	db.unarchiveUser(userID);
    }
    
    public void changePosition(User user, int positionID) {
    	db.executeUpdate("Change User " + user.getFirstname() + " " + user.getLastname() + " Position to id " + 
    			positionID, "update User set position_id = " + positionID + " where user_id = " + user.getUserID());
    	PositionController pc = new PositionController();
    	user.setPosition(pc.getPositionByUser(user.getUserID()));
    }
}
