package edu.ycp.cs481.model;

public class User{
	private int userID;
	private String email, password;
	private String firstName, lastName;
	private Position position;
	private boolean lockedOut, archived;
	// TODO: MOVE TO CONTROLLER
	private TrainingHistory history = new TrainingHistory();
	
	// Getters/Setters
	public int getUserID(){
		return userID;	
	}
	
	public void setUserID(int userID){
		this.userID = userID;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}
	
	public String getFirstName(){
		return firstName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public Position getPosition(){
		return position;
	}

	public void setPosition(Position position){
		this.position = position;
	}

	public boolean isArchived(){
		return archived;
	}

	public void setArchived(boolean archived){
		this.archived = archived;
	}

	public boolean isLockedOut(){
		return lockedOut;
	}

	public void setLockedOut(boolean lockedOut){
		this.lockedOut = lockedOut;
	}
	
	// TODO: MOVE THESE TO A CONTROLLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public TrainingHistory getHistory(){
		return history;
	}
	
	public void setHistory(TrainingHistory history){
		this.history = history;
	}
}
