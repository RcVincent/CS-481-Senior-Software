package model;

public class Position {
	private String Title;
	private int ID, Priority;
	private SOP Requirements;
	
	public Position() {
		
	}
	
	// Getters/Setters
	public void setTitle(String Title) {
		this.Title = Title;	
	}
	
	public String getTitle() {
		return this.Title;	
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
	
	public void setRequirements(SOP Requirements) {
		this.Requirements = Requirements;	
	}
	
	public SOP getRequirements() {
		return this.Requirements;	
	}
}
