package controller;

import model.User;
import model.Position;

import model.SOP;

import java.util.List;

import DBpersist.SqlDatabase;

public class PositionController {
	private Position p;
	SqlDatabase db = new SqlDatabase(); 
	
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
	
	//********************
	//Implementing the database methods 
	//********************
	
	public Position getPositionByID(int positionID) {
		return db.findPositionByID(positionID);
	}
	
	public List<Position> getAllPositions() {
		return db.findAllPositions();
	}
	
	//this will be populated later 
	public List<Position> getPositionByPriority(int priority) {
		return db.getPositionByPriority(priority); 
	}
	
	public List<Position> getPositionByName(String title) {
		return db.getPositionByName(title);
	}
	
	public Position getPositionByUser(int userID) {
		return db.getPositionByUser(userID);
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
	
	public Boolean removePosition(int positionID) {
		return db.deletePosition(positionID);
	}
	
	
	
	
	
}


