package edu.ycp.cs481.model;


import java.util.ArrayList;
import java.util.List;

public class SOP implements Comparable<SOP> {
	private String name;
	private String description;
	private int priority;
	private int ID;
	// Assuming Author refers to a User, authorID would be their userID then.
	private int authorID;
	private int revision; 
	private boolean archiveFlag;
	private List<Position> positionsAffected = new ArrayList<Position>(); 
	private boolean isComplete;
	
	public SOP() {
		//empty constructor incase we want one 
		
	}
	public SOP(String name, String description, int priority, int ID, int authorID, int revision, boolean archiveFlag){
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.ID = ID;
		this.authorID = authorID;
		this.revision = revision;
		this.archiveFlag = archiveFlag;
	}
	
  public ArrayList<Position> showPositionsAffected(int id) {
		ArrayList<Position> positions = new ArrayList<Position>();
		
		for(int i = 0; i < positionsAffected.size(); i++) {
			if(positionsAffected.get(i).getRequirements().get(i).getID() == id) {
				positions.add(positionsAffected.get(i));
			}
		}
		return positions;
	}
  
  public List<Position> getPositionsAffected() {
		return positionsAffected;
	}

  public void setPositionsAffected(List<Position> positionsAffected) {
		this.positionsAffected = positionsAffected;
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
	
	public boolean getArchiveFlag() {
		return archiveFlag;
	}
	
	public void setArchiveFlag(boolean archiveFlag) {
		this.archiveFlag = archiveFlag;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	@Override
	public int compareTo(SOP o) {
		// TODO Auto-generated method stub
		return 0;
	}
}

