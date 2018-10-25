package edu.ycp.cs481.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class TrainingHistory {
	private User user; 
	private List<SOP> completedSOPs;
	private PriorityQueue<SOP> sopsToDo; 
	
	public TrainingHistory() {
		completedSOPs = new ArrayList<SOP>(); 
		sopsToDo = new PriorityQueue<SOP>(); 
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<SOP> getCompletedSOPs() {
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
	
	public void addAndSortCollection(List<SOP> s) {
		sopsToDo.addAll(s);
		/*for(SOP S: sopsToDo) {
			if(S.isComplete()) {
				completedSOPs.add(S);
			}
		}*/
		/*for (int i = 0; i < sopsToDo.size(); i++) {
			if(sopsToDo.element().isComplete()) {
				SOP S = sopsToDo.remove();
				completedSOPs.add(S);
				//i--;
			}
		}*/
		
		int count = 0; 
		while(!sopsToDo.isEmpty() && count != sopsToDo.size()) {
			SOP x = sopsToDo.iterator().next();
			if(x.isComplete()) {
				completedSOPs.add(x);
				sopsToDo.remove(x);
			}
			count++; 
	
		}
	}
}
