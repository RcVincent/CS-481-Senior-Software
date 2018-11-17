package edu.ycp.cs481.model;

public class SOP{
	private int ID;
	private String title, description;
	private int priority, version,  authorID;
	private boolean archived;
	private boolean isComplete;
	
	public int getID(){
		return ID;
	}

	public void setID(int ID){
		this.ID = ID;
	}
	
	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
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
	
	public int getVersion(){
		return version;
	}

	public void setVersion(int version){
		this.version = version;
	}

	public int getAuthorID(){
		return authorID;
	}

	public void setAuthorID(int authorID){
		this.authorID = authorID;
	}

	public boolean isArchived(){
		return archived;
	}

	public void setArchived(boolean archived){
		this.archived = archived;
	}

	public boolean isComplete(){
		return isComplete;
	}

	public void setComplete(boolean isComplete){
		this.isComplete = isComplete;
	}
}
