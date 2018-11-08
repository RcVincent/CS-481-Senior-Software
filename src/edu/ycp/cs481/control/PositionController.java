package edu.ycp.cs481.control;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;

public class PositionController{
	private Database db = new Database();

	public Integer insertPosition(String positionTitle, String description, int priority){
		return db.insertAndGetID("Position", "position_id", new String[]{"title", "description", "priority"},
				new String[]{positionTitle, description, String.valueOf(priority)});
	}

	public ArrayList<Position> searchForPositions(int positionID, String title, String description, int priority){
		try{
			String name = "";
			String sql = "select * from Position";
			if(positionID == -1 && (title == null || title.equalsIgnoreCase(""))
					&& (description == null || description.equalsIgnoreCase("")) && priority == -1){
				name = "Get All Positions";
			}else{
				name = "Get Position with ";
				sql += " where ";
				boolean prevSet = false;

				if(positionID != -1){
					name += "id of " + positionID;
					sql += "position_id = " + positionID;
					prevSet = true;
				}

				if(title != null && !title.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "title of " + title;
					sql += "title = '" + title + "'";
					prevSet = true;
				}

				// TODO: Likely edit description (and possibly title) to search for partial? Not
				// sure if this does that.
				if(description != null && !description.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "description of " + description;
					sql += "description = '" + description + "'";
					prevSet = true;
				}

				if(priority != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "priority " + priority;
					sql += "priority = " + priority;
					prevSet = true;
				}
			}
			ArrayList<Position> results = db.executeQuery(name, sql, db.getPosResFormat());
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
			ArrayList<Position> results = db.executeQuery("Get Position By User", "select " + db.getPositionPieces()
					+ " from Position, User where user_id = " + userID + " and Position.position_id = User.position_id",
					db.getPosResFormat());
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
					"select " + db.getPositionPieces() + " from Position, PositionSOP " + "where PositionSOP.sop_id = "
							+ SOPID + " and Position.position_id = PositionSOP.position_id",
					db.getPosResFormat());
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

	public void removePosition(int positionID){
		db.executeUpdate("Delete Position with ID " + positionID,
				"delete from Position where position_id = " + positionID);
	}

	public ArrayList<SOP> findSOPsOfPosition(int positionID){
		try{
			return db.executeQuery("Get SOPs By Position",
					"select " + db.getSopPieces() + " from PositionSOP, SOP " + "where position_id = " + positionID,
					db.getSopResFormat());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public Integer changePositionPermission(int position_id, int perm_id){
		return db.changePositionPermission(position_id, perm_id);
	}
	
	public boolean hasRequirement(int positionID) {
		try{
			String name = "";
			String sql = "select * from PositionSOP where position_id = " + positionID;
			boolean results = db.executeCheck(name, sql);
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
