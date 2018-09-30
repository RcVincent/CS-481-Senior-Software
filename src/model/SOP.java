package model;

public class SOP{
	private String name;
	private String description;
	private int priority;
	private int ID;
	// Assuming Author refers to a User, authorID would be their userID then.
	private int authorID;
	private int revision; 
	
	public SOP(String name, String description, int priority, int ID, int authorID){
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.ID = ID;
		this.authorID = authorID;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public void setPriority(int priority){
		this.priority = priority;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public int getAuthorID(){
		return authorID;
	}
	
	public void setAuthorID(int authorID){
		this.authorID = authorID;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
}
