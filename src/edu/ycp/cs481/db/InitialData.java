package edu.ycp.cs481.db;


import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

//import javafx.util.Pair;



public class InitialData {
	private List<User> uList;
	private User u1, u2;
	private List<Position> pList;
	private Position p1, p2, p3;
	private List<SOP> sList, p1reqs;	
	private SOP s1, s2, p1req1, p1req2;
	private String[] perms, permNames;
	private int[] permIds;
	
	public InitialData() {
		uList = new ArrayList<User>();
		pList = new ArrayList<Position>();
		sList = new ArrayList<SOP>();
		p1reqs = new ArrayList<SOP>();
		perms = new String[5];
		permNames = new String[5];			// start with 2, work our way up. We won't need that many.
		
		
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
		u1.setUserID(1);
		u1.setEmail("CEO@Google.com");
		u1.setPassword("yes");
		u1.setFirstname("Carl");
		u1.setLastname("Sagan");
		u1.setLockedOut(false);
		u1.setArchiveFlag(false);
		u1.setPosition(p1);
		
		u2 = new User();
		u2.setUserID(2);
		u2.setEmail("Worker@Google.com");
		u2.setPassword("no");
		u2.setFirstname("Billiam");
		u2.setLastname("Nye");
		u2.setLockedOut(false);
		u2.setArchiveFlag(false);
		u2.setPosition(p2);
		
		s1 = new SOP();
		s1.setID(1);
		s1.setName("Admin");
		s1.setDescription("IsAdmin");
		s1.setPriority(1);
		s1.setRevision(1);
		s1.setAuthorID(p1.getID());
		s1.setArchiveFlag(false);
		
		s2 = new SOP();
		s2.setID(2);
		s2.setName("User");
		s2.setDescription("IsUser");
		s2.setPriority(1);
		s2.setRevision(1);
		s2.setAuthorID(p1.getID());
		s2.setArchiveFlag(false);
		
		p1req1 = new SOP();
		p1req1.setID(3);
		p1req1.setName("Requirement 1");
		p1req1.setDescription("This is the first requirement");
		p1req1.setPriority(1);
		p1req1.setRevision(1);
		p1req1.setAuthorID(p1.getID());
		p1req1.setArchiveFlag(false);
		
		p1req2 = new SOP();
		p1req2.setID(4);
		p1req2.setName("Requirement 2");
		p1req2.setDescription("This is the second requirement");
		p1req2.setPriority(1);
		p1req2.setRevision(1);
		p1req2.setAuthorID(p1.getID());
		p1req2.setArchiveFlag(false);
		
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
		
		permNames[0] = "Admin";
		perms[0] = "User can view and modify any Position, SOP, or User";
		
		permNames[1] = "Create Position/SOP";
		perms[1] = "User can create new Positions and SOPs";

		permNames[2] = "Search Position/SOP/User";
		perms[2] = "User can search for other Positions, SOPs, Users";
		
		permNames[3] = "View Position/SOP";
		perms[3] = "User can view their Position and SOPs";
		
		permNames[4] = "Locked out";
		perms[4] = "User is unable to access the system";
		
		
		permIds = new int[pList.size()];
		
		permIds[0] = 1;
		permIds[1] = 4;
		permIds[2] = 4;
	}


	public List<User> getInitialUsers() {
		return uList;
	}
	
	public List<Position> getInitialPositions() {
		return pList;
	}
	
	public List<SOP> getInitialSOPs() {
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