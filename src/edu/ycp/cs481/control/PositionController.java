package edu.ycp.cs481.control;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class PositionController {
	private Position p;
	private Database db = new Database(); 
	
	public PositionController() {
		
		
	}

   public boolean validPosition(Position p) {
	   if(p.getDescription() == "" || p.getDescription() == " " ||
		  p.getTitle()	== "" | p.getTitle() == " " ||
		  p.getID() <= 0 || p.getPriority() <= 0) {
		   return false; 
	   }
	   else {
		   return true; 
	   }
   }

	
	//********************
	//Implementing the database methods 
	//********************
	
	public ArrayList<Position> searchForPosition(int id, String title, String desc, int priority) {
		return db.searchForPositions(id, title, desc, priority);
	}
	
	public Position getPositionByUser(int userID) {
		return db.getPositionOfUser(userID);
	}
	
	public List<Position> getPositionBySOPId(int sopID) {
		return db.getPositionBySOPID(sopID);
	}
	
	public Position changePositionPriority(Position p, int priority) {
		return db.changePositionPriority(p, priority);
	}
	
	public Integer insertPosition(Position p) {
		return db.insertPosition(p);
	}
	
	public void removePosition(int positionID) {
		db.deletePosition(positionID);
	}
	
	public ArrayList<SOP> getApplicantSOPs(int position_id) {
		
		return db.findSOPsByPosition(position_id);
	}
	
	
	
}


