package edu.ycp.cs481.model;

import java.util.ArrayList;

public class Position{
	private String title, description;
	private int ID, priority;
	private ArrayList<SOP> requirements;

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
	
	public int getID(){
		return this.ID;
	}

	public void setID(int ID){
		this.ID = ID;
	}
	
	public int getPriority(){
		return priority;
	}

	public void setPriority(int priority){
		this.priority = priority;
	}
	
	public ArrayList<SOP> getRequirements(){
		return requirements;
	}

	public void setRequirements(ArrayList<SOP> requirements){
		this.requirements = requirements;
	}

	public ArrayList<SOP> getCompletedSOPs(Position p){
		ArrayList<SOP> completedSOPs = new ArrayList<SOP>();
		for(SOP s: p.getRequirements()){
			if(s.isComplete()){
				completedSOPs.add(s);
			}
		}

		return completedSOPs;
	}

	public ArrayList<SOP> getIncompleteSOPs(Position p){
		ArrayList<SOP> IncompleteSOPs = new ArrayList<SOP>();
		for(SOP s: p.getRequirements()){
			if(!s.isComplete()){
				IncompleteSOPs.add(s);
			}
		}

		return IncompleteSOPs;
	}

}
