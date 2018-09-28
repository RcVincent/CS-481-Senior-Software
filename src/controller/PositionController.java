package controller;

import model.User;
import model.Position;
import model.SOP;

public class PositionController {
	
	public PositionController() {
		
	}
	
	public SOP getApplicantSOPs(User u) {
		
		// TODO: Return database query 
		return new SOP(null, null, -1, -1, -1); 
	}
	
	public Position change(int positionID) {
		
		// Name is up for debate, just a method to change a user's position
		// TODO: Database query for Position at specified ID
		return new Position();
	}
}
