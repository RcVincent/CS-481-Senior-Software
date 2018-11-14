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
			StringBuilder name = new StringBuilder("");
			StringBuilder sql = new StringBuilder("select " + db.getUserPieces() + " from User");
			if(userID == -1 && employeeID == -1 && (email == null || email.equalsIgnoreCase(""))
					&& (firstName == null || firstName.equalsIgnoreCase(""))
					&& (lastName == null || lastName.equalsIgnoreCase("")) && positionID == -1){
				name.append("Get All Users");
			}else{
				name.append("Get User with ");
				sql.append(" where ");
				boolean first = true;

				if(userID != -1){
					db.addIntSearchToSelect(first, name, sql, "user_id", userID);
					first = false;
				}
				
				if(employeeID != -1){
					db.addIntSearchToSelect(first, name, sql, "employee_id", employeeID);
					first = false;
				}

				if(email != null && !email.equalsIgnoreCase("")){
					db.addStringSearchToSelect(first, name, sql, emailPartial, "email", email);
					first = false;
				}

				if(firstName != null && !firstName.equalsIgnoreCase("")){
					db.addStringSearchToSelect(first, name, sql, firstNamePartial, "first_name", firstName);
					first = false;
				}

				if(lastName != null && !lastName.equalsIgnoreCase("")){
					db.addStringSearchToSelect(first, name, sql, lastNamePartial, "last_name", lastName);
					first = false;
				}

				if(positionID != -1){
					db.addIntSearchToSelect(first, name, sql, "position_id", positionID);
					first = false;
				}
			}
			System.out.println("Name: " + name.toString());
			System.out.println("SQL: " + sql);
			ArrayList<User> results = db.executeQuery(name.toString(), sql.toString(), db.getUserResFormat());
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
	
	public boolean isClockedIn(int userID) {
		try{
			String name = "";
			String sql = "select * from Clock where user_id = " + userID + 
												 " and clock_in <> 0";			// <> is equivalent to != in VB and SQL
			boolean results = db.executeCheck(name, sql);
			if(results == false){
				System.out.println("This employee is not clocked in");
				return false;
			}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public void clockIn(int userID, int time) {
		if(!isClockedIn(userID)) {
			db.executeUpdate("Clock in user_id " + userID, 
					"update Clock set clock_in = " + time + " where user_id = " + userID);
		}
		else
			System.out.println("This employee is already clocked in");
	}
	
	public void clockOut(int userID, int time) {
		int inTime = 0;
		
		if(isClockedIn(userID)) {
			try {
				inTime = db.getIntField("User clock in time", "select clock_in from Clock where user_id = " + userID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// TODO: LOGIC WITH 3RD SHIFT//
			//		   D E V I N		 //
			//							 //
			///////////////////////////////
			
			db.executeUpdate("Update hours", "update Clock set hours = " + (time - inTime) + " where user_id = " + userID);
			db.executeUpdate("Reset clock in time", "update Clock set clock_in = 0 where user_id = " + userID);
		}
		else
			System.out.println("This employee is not clocked in yet");
	}
}