package edu.ycp.cs481.control;

import edu.ycp.cs481.model.TrainingHistory;
import edu.ycp.cs481.model.User;

import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import edu.ycp.cs481.model.Messenger;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;

public class SystemSnifferController {
	private List<SOP> reqs;

	//private User u; 
	//private Position p;
	private PositionController pc = new PositionController(); 
	private TrainingHistory t; 
	
	public SystemSnifferController() {
	}
	
	public void initHistory(User u) {
		t = u.getHistory();
	}
	
	
	public boolean validateSOPs() {
		//List<SOP> testList = new ArrayList<SOP>(); 
		
		Iterator<SOP> i = t.getSopsToDo().iterator();
		while(i.hasNext()) {
			if(!i.next().isComplete()) {
				System.out.println("There is an incomplete SOP. SOP id is" + i.next().getID());
				//send a message to user and their manager 
				return false;  
			}
			else {
				System.out.println("There is nothing in the priority queue that is incomplete");
				Messenger m = new Messenger(); 
				//i.remove();
				t.getCompletedSOPs().add(i.next());
			}
		}
		
		//if the method has run this far, its successful
		return true;
		
		
	}
	//the DB calls the training histories will need
	public void PopulateHistoryThroughUser(User u) {
		t.addAndSortCollection(pc.findSOPsOfPosition(u.getPosition().getID()));
	}
	
	public void PopulateHistoryThroughPosition(int pos_id) {
		t.addAndSortCollection(pc.findSOPsOfPosition(pos_id));
	}
	
}
