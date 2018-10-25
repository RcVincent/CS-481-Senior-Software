package edu.ycp.cs481.model;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class TrainingHistory {
	private User user; 
	private ArrayList<SOP> completedSOPs;
	private PriorityQueue<SOP> sopsToDo; 
	
	public TrainingHistory() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<SOP> getCompletedSOPs() {
		return completedSOPs;
	}

	public void setCompletedSOPs(ArrayList<SOP> completedSOPs) {
		this.completedSOPs = completedSOPs;
	}

	public PriorityQueue<SOP> getSopsToDo() {
		return sopsToDo;
	}

	public void setSopsToDo(PriorityQueue<SOP> sopsToDo) {
		this.sopsToDo = sopsToDo;
	}
	
	public void addToCompleted(SOP s) {
		completedSOPs.add(s);
	}
	
	public void addToDoList(SOP s) {
		sopsToDo.add(s);
	}

	public int getTrainingHistorySize() {
		return completedSOPs.size() + sopsToDo.size();
	}
}
