package model;

import java.util.List;

public class Position {
	private String Title, Description;
	private int ID, Priority;
	private List<SOP> Requirements;
	private boolean isValid; 
	// Constructors
	public Position() {
		
	}
	
	public Position(String Title, int ID, int Priority) {
		this.Title = Title;
		this.Description = Description;
		this.ID = ID;
		this.Priority = Priority;
	}
	
	// Getters/Setters
	public void setTitle(String Title) {
		this.Title = Title;	
	}
	
	public String getTitle() {
		return this.Title;	
	}
	
	public void setDescription(String Description) {
		this.Description = Description;	
	}
	
	public String getDescription() {
		return this.Description;	
	}
	
	public void setID(int ID) {
		this.ID = ID;	
	}
	
	public int getID() {
		return this.ID;	
	}
	
	public void setPriority(int Priority) {
		this.Priority = Priority;	
	}
	
	public int getPriority() {
		return this.Priority;	
	}
	
	public void setRequirements(List<SOP> Requirements) {
		this.Requirements = Requirements;	
	}
	
	public List<SOP> getRequirements() {
		return this.Requirements;	
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
