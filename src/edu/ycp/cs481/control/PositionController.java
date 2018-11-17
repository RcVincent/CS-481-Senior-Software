package edu.ycp.cs481.control;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ycp.cs481.db.DBFormat;
import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;

public class PositionController{
	private Database db = new Database();

	public Integer insertPosition(String positionTitle, String description, int priority){
		return db.insertAndGetID("Position", "position_id", new String[]{"title", "description", "priority"},
				new String[]{positionTitle, description, String.valueOf(priority)});
	}

	public ArrayList<Position> searchForPositions(int positionID, boolean titlePartial, String title, 
			boolean descPartial, String description, int priority){
		try{
			ArrayList<Position> results = db.doSearch(DBFormat.getPosResFormat(), "Position", null, null, 
					new String[]{"position_id", "priority"}, 
					new int[]{positionID, priority}, 
					new boolean[]{titlePartial, descPartial}, 
					new String[]{"title", "description"}, 
					new String[]{title, description});
			if(positionID != -1){
				if(results.size() == 0){
					System.out.println("No Position found with ID " + positionID);
				}else if(results.size() > 1){
					System.out.println("Multiple Positions found with ID " + positionID + "! Returning null");
					return null;
				}
			}
			return results;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public Position getPositionByUser(int userID){
		try{
			ArrayList<Position> results = db.executeQuery("Get Position By User", "select " + DBFormat.getPositionPieces()
					+ " from Position, User where user_id = " + userID + " and Position.position_id = User.position_id",
					DBFormat.getPosResFormat());
			if(results.size() == 0){
				System.out.println("No positions found for User_id " + userID + "!");
			}else if(results.size() > 1){
				System.out.println("More than one position found for User_id " + userID + "! Returning null");
			}else{
				return results.get(0);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Position> getPositionBySOPID(int SOPID){
		try{
			return db.executeQuery("Get Position by SOP ID",
					"select " + DBFormat.getPositionPieces() + " from Position, PositionSOP " + "where PositionSOP.sop_id = "
							+ SOPID + " and Position.position_id = PositionSOP.position_id",
					DBFormat.getPosResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public void changePositionPriority(Position pos, int priority){
		db.executeUpdate("Change Position " + pos.getTitle() + " to priority " + priority,
				"update Position set priority = " + priority + " where position_id = " + pos.getID());
		pos.setPriority(priority);
	}

	
	public void changePositionTitle(Position p, String newTitle) {
		db.executeUpdate("Change Position" + p.getID() + "to title" + newTitle,
				"update Position set title = " + newTitle + "where position_id = " + p.getID());
		p.setTitle(newTitle);
	}
	
	
	public void changePositionDescription(Position p, String newDesc) {
		db.executeUpdate("Change Position" + p.getTitle() + "to description " + newDesc,
				"update Position set description = " + newDesc + "where position_id = " + p.getID());
		p.setDescription(newDesc);
	}
	
	public void removePosition(int positionID){
		db.executeUpdate("Delete Position with ID " + positionID,
				"delete from Position where position_id = " + positionID);
	}
	
	public void addPositionPermission(Position pos, EnumPermission perm){
		db.insert("PositionPermission", new String[]{"position_id", "permission_id"}, 
				new String[]{String.valueOf(pos.getID()), String.valueOf(perm.getID())});
	}
	
	public void removePositionPermission(Position pos, EnumPermission perm){
		db.executeUpdate("Remove Permission " + perm.getPerm() + " from Position " + pos.getTitle(),
				"delete from PositionPermission where position_id = " + pos.getID() + " and perm_id = " + perm.getID());
	}
	
	public void insertPositionSOP(int position_id, int sop_id){		
		db.insert("PositionSOP", new String[]{"position_id" ,"sop_id"}, 
				new String[] {String.valueOf(position_id) , String.valueOf(sop_id)});
	}
	
	public ArrayList<SOP> findSOPsOfPosition(int positionID){
		try{
			return db.executeQuery("Get SOPs By Position",
					"select " + DBFormat.getSopPieces() + " from PositionSOP, SOP " + "where position_id = " + positionID,
					DBFormat.getSopResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO: Wouldn't this make more sense to check a particular requirement?
	public boolean hasRequirement(int positionID) {
		try{
			String name = "";
			String sql = "select * from PositionSOP where position_id = " + positionID;
			boolean results = db.executeQuery(name, sql, DBFormat.getCheckResFormat());
			if(results == false){
				System.out.println("This Position has no requirements");
				return false;
			}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
		} 
		return false;
	}
}
