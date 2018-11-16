package edu.ycp.cs481.control;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import edu.ycp.cs481.db.DBFormat;
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
			boolean firstNamePartial, String firstName, boolean lastNamePartial, String lastName, int positionID,
			int managerID){
		try{
			ArrayList<String> otherTables = new ArrayList<String>();
			ArrayList<String> junctions = new ArrayList<String>();
			if(managerID != -1){
				otherTables.add("Subordinate");
				junctions.add("Subordinate.subordinate_id = User.user_id");
			}
			ArrayList<User> results = db.doSearch(DBFormat.getUserResFormat(), "User", otherTables, junctions, 
					new String[]{"user_id", "employee_id", "position_id", "manager_id"}, 
					new int[]{userID, employeeID, positionID, managerID}, 
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
			ArrayList<User> u = searchForUsers(userID, -1, false, null, false, null, false, null, -1, -1);
			String name = "";
			String sql = "select * from PositionPermission where position_id = " + u.get(0).getPosition().getID() + 
															" and perm_id = " + perm.getID();
			return db.executeQuery(name, sql, DBFormat.getCheckResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean managerHasSubordinate(int managerID, int userID){
		try{
			String name = "";
			String sql = "select * from Subordinate where manager_id = " + managerID + 
												 " and subordinate_id = " + userID;
			return db.executeQuery(name, sql, DBFormat.getCheckResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}

	public boolean SOPisCompleted(int userID, int sopID){
		try{
			String name = "";
			String sql = "select * from CompletedSOP where user_id = " + userID + 
												 " and sop_id = " + sopID;
			return db.executeQuery(name, sql, DBFormat.getCheckResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean isLockedOut(int userID){
		try{
			String name = "";
			String sql = "select * from User where user_id = " + userID + 
												 " and locked_out = 1";
			return db.executeQuery(name, sql, DBFormat.getCheckResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public ArrayList<User> getManagersOfUser(int userID){
		try{
			return db.executeQuery("Get Managers of User",
				"select " + DBFormat.getUserPieces() + " from Subordinate, User " + "where manager_id = " + userID,
				DBFormat.getUserResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

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
	
	public boolean isClockedIn(int userID){
		// TODO: Rework for new stuff
		/*try{
			String name = "Is user clocked in";
			String sql = "select * from Clock where user_id = " + userID + " order by time desc";
			ClockTime result = db.executeQuery(name, sql, db.getDateResFormat()).get(0);
			return result.getIn();
		}catch(SQLException e){
			e.printStackTrace();
		}*/
		return false;
	}
	
	// TODO: Rework for new tables
	public void clockIn(int userID){
		if(!isClockedIn(userID)){
			db.insert("Clock",
					new String[]{"user_id", "in"},
					new String[]{String.valueOf(userID), String.valueOf(true)});
		}else
			System.out.println("This employee is already clocked in");
	}
	
	// TODO: Rework for new tables
	public void clockOut(int userID){
		if(isClockedIn(userID)){
			db.insert("Clock",
					new String[]{"user_id", "in"},
					new String[]{String.valueOf(userID), String.valueOf(false)});
		}else
			System.out.println("This employee is not clocked in yet");
	}
	
	public void updateHours(){
		// TODO
	}
	
	public void assignSOP(int userID, int sopID) {
		db.insert("UserSOP",
				new String[] {"user_id", "sop_id"},
				new String[] {String.valueOf(userID), String.valueOf(sopID)});
	}
}