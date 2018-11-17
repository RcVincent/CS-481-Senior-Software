package edu.ycp.cs481.control;

import edu.ycp.cs481.model.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.ArrayList;
import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.control.SOPController;

import edu.ycp.cs481.model.Messenger;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;

public class SystemSnifferController {
	private Database db = new Database();
	private List<SOP> reqs; 
	private static User u; 
	private static Position p;
	private static PositionController pc = new PositionController(); 
	private static UserController uc = new UserController();
	private static SOPController sc = new SOPController(); 
	
	public SystemSnifferController() {
	
	}
	
	//rework into the manager or admin pushes a button and it searches through the requisite user positoins 
	
	//use session data to get if a user has certain permissions 
	public static void getPermissions(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String id = (String) session.getAttribute("user_id");
		int user_id = Integer.parseInt(id);
		
		u = uc.searchForUsers(user_id, -1, false, "", false, "", false, "", 0, -1).get(0); 
		p = u.getPosition();
		
		//do a permissions check here
	}
	
	public void setAndShowToDoList() {
		List<SOP> displayList = p.getIncompleteSOPs(p);
		for(SOP s: displayList) {
			System.out.println(s.getID() + " | " + s.getTitle() + " | " + s.getDescription());
		}
	}
	
	public void setAndshowDoneList() {
		List<SOP> displayList = p.getCompletedSOPs(p);
		
		for(SOP s: displayList) {
			System.out.println(s.getID() + " | " + s.getTitle() + " | " + s.getDescription());
		}
	}
	
	public void sendMessage() {
		//call the messenger
	}
}
