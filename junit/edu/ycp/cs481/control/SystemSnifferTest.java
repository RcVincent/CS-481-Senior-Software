package edu.ycp.cs481.control;

import java.util.ArrayList;

import org.junit.Before;
import edu.ycp.cs481.model.User;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.control.SystemSnifferController;

public class SystemSnifferTest {
	
	private SystemSnifferController s; 
	private User admin, user, manager;
	
	private SOP s1, s2, s3, s4, s5, s6, s7, s8, s9, s10; 
	private Position adminP, userP, managerP; 
	private ArrayList<SOP> adminReqs, userReqs, managerReqs; 
	private ArrayList<User> userList;  
	
	@Before 
	public void setUp() {
		userList = new ArrayList<User>(); 
		 
		
		adminReqs = new ArrayList<SOP>(); 
		userReqs = new ArrayList<SOP>(); 
		managerReqs = new ArrayList<SOP>();
		
		//create 3 new users for testing two valid one non valid 
		admin = new User(); 
		//admin.setAdminFlag(true);
		admin.setArchived(false);
		admin.setEmail("oscarWinner@gmail.com");
		admin.setPassword("legendary");
		admin.setID(14);
		admin.setFirstName("Guillermo");
		admin.setLastName("Del-Toro");
		//admin.setPosition(adminP);
		
		user = new User();
		//user.setAdminFlag(false);
		user.setArchived(true);
		user.setID(33);
		user.setEmail("zod@krypton.com");
		user.setFirstName("General");
		user.setLastName("Zod");
		user.setPassword("imAPrick");
		//user.setPosition(userP);
		
		manager = new User(); 
		//manager.setAdminFlag(false);
		manager.setArchived(false);
		manager.setEmail("headOfTheClub@SOA.com");
		manager.setPassword("weRunGuns");
		manager.setFirstName("Clay");
		manager.setLastName("Marrow");
		manager.setID(89);
		//manager.setPosition(managerP);
		
		userList.add(admin);
		userList.add(user);
		userList.add(manager);
		
		//create three new positions for testing and fully fill them out 
		adminP = new Position(); 
		adminP.setTitle("Conqueror");
		adminP.setDescription("Here to bring new worlds to life through cinema");
		adminP.setPriority(1);
		adminP.setID(2);
		adminP.setRequirements(adminReqs);
		
		managerP = new Position(); 
		managerP.setID(6);
		managerP.setTitle("President of SAMCRO");
		managerP.setDescription("To ensure the survival of the club and its members, plus ruining a decent show");
		managerP.setPriority(5);
		managerP.setRequirements(userReqs);
		
		userP = new Position(); 
		userP.setID(56);
		userP.setTitle("Conqueror of Worlds");
		userP.setDescription("To claim new worlds in the name of krypton");
		userP.setPriority(8);
		userP.setRequirements(userReqs);
		
		admin.setPosition(adminP);
		user.setPosition(userP);
		manager.setPosition(managerP);
		//create a set of bare-bones SOPs for basic population testing 
		s1 = new SOP();
		s1.setID(1);
		s1.setComplete(false);
		
		s2 = new SOP();
		s2.setID(2);
		s2.setComplete(false);
		
		s3 = new SOP();
		s3.setID(3);
		s3.setComplete(false);
		
		s4 = new SOP();
		s4.setID(4);
		s4.setComplete(false);
		
		s5 = new SOP();
		s5.setID(5);
		s5.setComplete(false);
		
		s6 = new SOP();
		s6.setID(6);
		s1.setComplete(false);
		
		s7 = new SOP();
		s7.setID(7);
		s7.setComplete(false);
		
		s8 = new SOP();
		s8.setID(8);
		s8.setComplete(false);
		
		s9 = new SOP();
		s9.setID(9);
		s9.setComplete(false);
		
		
		s10 = new SOP();
		s10.setID(10);
		s10.setComplete(false);
		
		//add sops to the user position ArrayList
		userReqs.add(s2);
		userReqs.add(s5);
		userReqs.add(s6);
		userReqs.add(s7);
		userReqs.add(s9);
		
		//add sops to the admin position ArrayList
		adminReqs.add(s1);
		adminReqs.add(s3);
		adminReqs.add(s4);
		adminReqs.add(s5);
		adminReqs.add(s10);
		
		//add sops top to the manager position ArrayList 
		managerReqs.add(s3);
		managerReqs.add(s6);
		managerReqs.add(s7);
		managerReqs.add(s8);
		managerReqs.add(s9);
	}
}
