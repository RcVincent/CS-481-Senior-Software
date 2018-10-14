package edu.ycp.cs481.control;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.User;

public class TransitionWatcher {
	private User user;
	private Position oldPos, newPos;
	
	public TransitionWatcher(User user, Position oldPos, Position newPos) {
		// Likely called whenever a new user is created?
		this.user = user;
		this.oldPos = oldPos;
		this.newPos = newPos;
	}
	
	public void updateToDoList() {
		
	}
	
	public User assignNewPosition(User u, Position p) {
		this.oldPos = this.newPos;
		this.newPos = p;
		u.setPosition(p);
		
		return u;
	}
}
