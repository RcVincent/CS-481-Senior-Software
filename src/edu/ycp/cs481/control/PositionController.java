package edu.ycp.cs481.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;

public class PositionController{
	private Database db = new Database();

	public PositionController(){

	}

	public boolean validPosition(Position p){
		if(p.getDescription() == "" || p.getDescription() == " " || p.getTitle() == "" | p.getTitle() == " "
				|| p.getID() <= 0 || p.getPriority() <= 0){
			return false;
		}else{
			return true;
		}
	}

	// ********************
	// Implementing the database methods
	// ********************

	public Integer insertPosition(Position p){
		return db.insertAndGetID("Position", "position_id", new String[]{"title", "description", "priority"},
				new String[]{p.getTitle(), p.getDescription(), String.valueOf(p.getPriority())});
	}

	public ArrayList<Position> searchForPositions(int positionID, String title, String description, int priority){
		try{
			String name = "";
			String sql = "select * from Position";
			if(positionID == -1 && (title == null || title.equalsIgnoreCase("")) && 
					(description == null || description.equalsIgnoreCase("")) && priority == -1){
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
				
				// TODO: Likely edit description (and possibly title) to search for partial? Not sure if this does that.
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
		return db.getPositionOfUser(userID);
	}

	public List<Position> getPositionBySOPId(int sopID){
		return db.getPositionBySOPID(sopID);
	}

	public Position changePositionPriority(Position p, int priority){
		return db.changePositionPriority(p, priority);
	}

	public void removePosition(int positionID){
		db.deletePosition(positionID);
	}

	public ArrayList<SOP> getApplicantSOPs(int position_id){

		return db.findSOPsByPosition(position_id);
	}

}
