package controller;

import model.User;
import model.Position;

import model.SOP;

public class PositionController {
	private Position p;
	
	public PositionController() {
		
	}

   

	public SOP getApplicantSOPs(User u) {
		
		// TODO: Return database query 
		return new SOP(null, null, -1, -1, -1, -1, false); 
	}
	
	public Position getPositionByID(int positionID) {
		
		// Just a method to pull the position associated with ID
		// this.p = 
		// TODO: Database query for Position at specified ID
		return this.p;
	}
}


