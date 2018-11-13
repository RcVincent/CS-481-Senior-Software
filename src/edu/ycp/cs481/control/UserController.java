package edu.ycp.cs481.control;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.EnumPermission;
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
	
	public Integer insertUser(String email, String password, String firstName, String lastName, boolean lockedOut,
			boolean isArchived, int positionID){
		password = hashPassword(password);
		return db.insertAndGetID("User", "user_id",
				new String[]{"email", "password", "first_name", "last_name", "locked_out", "archive_flag",
						"position_id"},
				new String[]{email, password, firstName, lastName, String.valueOf(lockedOut), String.valueOf(isArchived),
						String.valueOf(positionID)});
	}

	public ArrayList<User> searchForUsers(int userID, int employeeID, boolean emailPartial, String email, 
			boolean firstNamePartial, String firstName, boolean lastNamePartial, String lastName, int positionID){
		try{
			ArrayList<User> results = db.doSearch(db.getUserResFormat(), "User", null, 
					new String[]{"user_id", "employee_id", "position_id"}, 
					new int[]{userID, employeeID, positionID}, 
					new boolean[]{emailPartial, firstNamePartial, lastNamePartial}, 
					new String[]{"email", "first_name", "last_name"}, 
					new String[]{email, firstName, lastName});
			if(results.size() == 0 && userID != -1){
				System.out.println("No User found with ID " + userID);
			}else if(results.size() > 1){
				if(userID != -1){
					System.out.println("Multiple Users found with ID " + userID + "! Returning null");
					return null;
				}else if(!emailPartial && (email != null && !email.equalsIgnoreCase(""))){
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
	
	public boolean userHasPermission(int userID, EnumPermission perm){
		try{
			ArrayList<User> u = searchForUsers(userID, -1, false, null, false, null, false, null, -1);
			String name = "";
			String sql = "select * from PositionPermission where position_id = " + u.get(0).getPosition().getID() + 
															" and perm_id = " + perm.getID();
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
	
	public ArrayList<User> listSubordinates(int managerID) {
		try{
			return db.executeQuery("Get Subordinates of Manager",
					"select " + db.getUserPieces() + " from Subordinate, User " + "where manager_id = " + managerID,
					db.getUserResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
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
	
	/*public ArrayList<User> getManagerOfUser(int userID) {
		try {
			return db.executeQuery("Get Manager of User",
				"select " + db.getUserPieces() + " from Subordinate, User " + "where manager_id = " + userID,
				db.getUserResFormat());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}*/

	public void overturnLockout(int userID) {
		db.executeUpdate("Overturn lockout on User with ID " + userID, "update User set lock_out = false where user_id = " + userID);
	}
	
	public void archiveUser(int userID){
		db.executeUpdate("Archive User with ID " + userID, "update User set archive_flag = true where user_id = " + userID);
	}

	public void unarchiveUser(int userID){
		db.executeUpdate("Unarchive User with ID " + userID, "update User set archive_flag = false where user_id = " + userID);
	}

	public void changePosition(User user, int positionID){
		db.executeUpdate(
				"Change User " + user.getFirstName() + " " + user.getLastName() + " Position to id " + positionID,
				"update User set position_id = " + positionID + " where user_id = " + user.getUserID());
		PositionController pc = new PositionController();
		user.setPosition(pc.getPositionByUser(user.getUserID()));
	}
	
	public void changeEmployeeID(int userID, int employeeID){
		db.executeUpdate(
				"Change User " + userID + "'s employee_id to " + employeeID,
				"update User set employee_id = " + employeeID + " where user_id = " + userID);
	}
	
	public static void logout(HttpServletRequest req){
		req.getSession().removeAttribute("user_id");
	}
	
	// InsertSubordinate
	public void addSubordinate(int manager_id, int subordinate_id){
		db.insert("Subordinate", new String[] {"manager_id", "subordinate_id"},
				new String[] {String.valueOf(manager_id), String.valueOf(subordinate_id)});
	}
	
	// DeleteSubordinate
	public void removeSubordinate(int manager_id, int subordinate_id){
		db.executeUpdate("Remove subordinate with ID " + subordinate_id, "delete from Subordinate where manager_id = " + 
				manager_id + " and subordinate_id = " + subordinate_id);
	}
}
