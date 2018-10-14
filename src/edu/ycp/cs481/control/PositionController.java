package edu.ycp.cs481.control;

import java.util.List;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class PositionController {
	private Position p;
	Database db = new Database(); 
	
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

	public SOP getApplicantSOPs(User u) {
		
		// TODO: Return database query 
		return new SOP(null, null, -1, -1, -1, -1, false);
	}
	
	public Position getPositionByID(int positionID) {
		return db.findPositionByID(positionID);
	}
	
	public List<Position> getAllPositions() {
		return db.findAllPositions();
	}
	
	//this will be populated later 
	public Position getPositionByPriority(int priority) {
		return null; 
	}
	
	
}


