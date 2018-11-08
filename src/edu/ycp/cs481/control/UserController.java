package edu.ycp.cs481.control;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.User;
import java.sql.SQLException;

import org.mindrot.jbcrypt.*;

public class UserController{
	private Database db = new Database();

	public boolean authenticate(User u, String pswd){
		return BCrypt.checkpw(pswd, u.getPassword());
	}

	public static String hashPassword(String password){
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	


	public Integer insertUser(String email, String password, String firstName, String lastName, boolean isAdmin,
			boolean isArchived, int positionID){
		password = hashPassword(password);
		return db.insertAndGetID("User", "user_id",
				new String[]{"email", "password", "first_name", "last_name", "admin_flag", "archive_flag",
						"position_id"},
				new String[]{email, password, firstName, lastName, String.valueOf(isAdmin), String.valueOf(isArchived),
						String.valueOf(positionID)});
	}

	public ArrayList<User> searchForUsers(int userID, String email, String firstName, String lastName, int positionID){
		try{
			String name = "";
			String sql = "select * from User";
			if(userID == -1 && (email == null || email.equalsIgnoreCase(""))
					&& (firstName == null || firstName.equalsIgnoreCase(""))
					&& (lastName == null || lastName.equalsIgnoreCase("")) && positionID == -1){
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

	public void changeUserEmail(int userID, String oldEmail, String newEmail){
		db.executeUpdate("Change User Email", "update User set email = '" + newEmail + "' where " + "email = '"
				+ oldEmail + "' and user_id = " + userID);
	}

	public void changeUserPassword(int userID, String newPass){
		db.executeUpdate("Change User Password",
				"update User set password = '" + hashPassword(newPass) + "' where " + "user_id = " + userID);
	}
	
	public boolean userHasPermission(int userID, int permissionID) {
		try{
			ArrayList<User> u = searchForUsers(userID, null, null, null, -1);
			String name = "";
			String sql = "select * from PositionPermission where position_id = " + u.get(0).getPosition().getID() + 
															" and perm_id = " + permissionID;
			boolean results = db.executeCheck(name, sql);
			if(results == false){
				System.out.println("This user doesn't have this permission");
				return false;
			}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean managerHasSubordinate(int managerID, int userID) {
		try{
			String name = "";
			String sql = "select * from Subordinate where manager_id = " + managerID + 
												 " and subordinate_id = " + userID;
			boolean results = db.executeCheck(name, sql);
			if(results == false){
				System.out.println("This employee doesn't report to this manager");
				return false;
			}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}

	public boolean SOPisCompleted(int userID, int sopID) {
		try{
			String name = "";
			String sql = "select * from CompletedSOP where user_id = " + userID + 
												 " and sop_id = " + sopID;
			boolean results = db.executeCheck(name, sql);
			if(results == false){
				System.out.println("This employee hasn't finished this SOP");
				return false;
			}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean isLockedOut(int userID) {
		try{
			String name = "";
			String sql = "select * from User where user_id = " + userID + 
												 " and locked_out = 1";
			boolean results = db.executeCheck(name, sql);
			if(results == false){
				System.out.println("This employee is not locked out");
				return false;
			}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public void archiveUser(int userID){
		db.executeUpdate("Archive User with ID " + userID, "update User set archive_flag = true where user_id = " + userID);
	}

	public void unarchiveUser(int userID){
		db.executeUpdate("Unarchive User with ID " + userID, "update User set archive_flag = false where user_id = " + userID);
	}

	public void changePosition(User user, int positionID){
		db.executeUpdate(
				"Change User " + user.getFirstname() + " " + user.getLastname() + " Position to id " + positionID,
				"update User set position_id = " + positionID + " where user_id = " + user.getUserID());
		PositionController pc = new PositionController();
		user.setPosition(pc.getPositionByUser(user.getUserID()));
	}
	
	public static void logout(HttpServletRequest req){
		req.getSession().removeAttribute("user_id");
	}
	
	public void clockIn(Integer hours,Integer min){
		//db call to put a clock in time in the database
		String h = hours.toString();
		String m = min.toString();
		if(hours<10) {h="0"+h;}
		if(min<10) {m="0"+m;}
		System.out.println("CLOCKED IN: "+h+":"+m);
	}
	
	public String clockOut(){
		int shifthour=0;
		int shiftmin=0;
		int totalhour=0;
		int totalmin=0;
		//db method pulling 
		return "Your last shift was "+ shifthour +" hours and "+ shiftmin + "minutes. "
				+ "Your total hours are"+ totalhour +" hours and "+ totalmin + "minutes.";
	}
	
	public String RemoveHours(int userID){
		return"";
	}
}
