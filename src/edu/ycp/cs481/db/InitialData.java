package edu.ycp.cs481.db;

import java.util.ArrayList;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class InitialData {
	private ArrayList<User> uList;
	private User u1, u2;
	private ArrayList<Position> pList;
	private Position p1, p2, p3;
	private ArrayList<SOP> sList, p1reqs;	
	private SOP s1, s2, p1req1, p1req2;
	private String[] perms, permNames;
	private int[] permIds;
	
	public InitialData() {
		uList = new ArrayList<User>();
		pList = new ArrayList<Position>();
		sList = new ArrayList<SOP>();
		p1reqs = new ArrayList<SOP>();
		perms = new String[6];
		permNames = new String[6];			// start with 2, work our way up. We won't need that many.
		
		
		p1 = new Position();
		p1.setID(1);
		p1.setTitle("Administrator");
		p1.setDescription("Can view/modify all aspects of the system.");
		p1.setPriority(1);
		
		p2 = new Position();
		p2.setID(2);
		p2.setTitle("User");
		p2.setDescription("This is used as a temporary position for those without one.");
		p2.setPriority(3);
		
		p3 = new Position();
		p3.setID(3);
		p3.setTitle("Delete me!");
		p3.setDescription("Do it now!");
		p3.setPriority(1);
		
		u1 = new User();
		u1.setID(1);
		u1.setEmail("CEO@Google.com");
		u1.setPassword("yes");
		u1.setFirstName("Carl");
		u1.setLastName("Sagan");
		u1.setLockedOut(false);
		u1.setArchived(false);
		u1.setPosition(p1);
		
		u2 = new User();
		u2.setID(2);
		u2.setEmail("Worker@Google.com");
		u2.setPassword("no");
		u2.setFirstName("Billiam");
		u2.setLastName("Nye");
		u2.setLockedOut(false);
		u2.setArchived(false);
		u2.setPosition(p2);
		
		s1 = new SOP();
		s1.setID(1);
		s1.setTitle("Admin");
		s1.setDescription("IsAdmin");
		s1.setPriority(1);
		s1.setVersion(1);
		s1.setAuthorID(p1.getID());
		s1.setArchived(false);
		
		s2 = new SOP();
		s2.setID(2);
		s2.setTitle("User");
		s2.setDescription("IsUser");
		s2.setPriority(1);
		s2.setVersion(1);
		s2.setAuthorID(p1.getID());
		s2.setArchived(false);
		
		p1req1 = new SOP();
		p1req1.setID(3);
		p1req1.setTitle("Requirement 1");
		p1req1.setDescription("This is the first requirement");
		p1req1.setPriority(1);
		p1req1.setVersion(1);
		p1req1.setAuthorID(p1.getID());
		p1req1.setArchived(false);
		
		p1req2 = new SOP();
		p1req2.setID(4);
		p1req2.setTitle("Requirement 2");
		p1req2.setDescription("This is the second requirement");
		p1req2.setPriority(1);
		p1req2.setVersion(1);
		p1req2.setAuthorID(p1.getID());
		p1req2.setArchived(false);
		
		p1reqs.add(p1req1);
		p1reqs.add(p1req2);		
		
		sList.add(s2);
		p2.setRequirements(sList);
		sList.remove(s2);
		
		sList.add(s1);
		sList.add(s2);
		p1.setRequirements(sList);		
		
		uList.add(u1);
		uList.add(u2);
		pList.add(p1);
		pList.add(p2);
		pList.add(p3);
		
		permNames[0] = "All";
		perms[0] = "all";
		
		permNames[1] = "Create Position";
		perms[1] = "createPosition";

		permNames[2] = "Create SOP";
		perms[2] = "createSOP";
		
		permNames[3] = "Create User";
		perms[3] = "createUser";
		
		permNames[4] = "Have Subordinates";
		perms[4] = "haveSubordinates";
		
		permNames[5] = "Search Users";
		perms[5] = "searchUsers";
		
		permIds = new int[pList.size()];
		
		permIds[0] = 1;
		permIds[1] = 4;
		permIds[2] = 4;
	}


	public ArrayList<User> getInitialUsers() {
		return uList;
	}
	
	public ArrayList<Position> getInitialPositions() {
		return pList;
	}
	
	public ArrayList<SOP> getInitialSOPs() {
		return sList;
	}
	
	public String[] getInitialPermissions() {
		return perms;
	}
	
	public String[] getInitialPermissionNames() {
		return permNames;
	}
	
	public int[] getInitialPermissionIDs() {
		return permIds;
	}
}