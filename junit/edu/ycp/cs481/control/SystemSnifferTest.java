package edu.ycp.cs481.control;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.ycp.cs481.model.User;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.control.SystemSnifferController;

public class SystemSnifferTest {
	
	private SystemSnifferController s; 
	private User admin, user, manager, failUser;
	
	private SOP s1, s2, s3, s4, s5, s6, s7, s8, s9, s10; 
	private Position adminP, userP, managerP; 
	private ArrayList<SOP> adminReqs, userReqs, managerReqs; 
	private ArrayList<User> userList;  
	
	@Before 
	public void setUp() {
		
		s = new SystemSnifferController(); 
		
		userList = new ArrayList<User>(); 
		 
		adminReqs = new ArrayList<SOP>(); 
		userReqs = new ArrayList<SOP>(); 
		managerReqs = new ArrayList<SOP>();
		
		//create 3 new users for testing two valid one non valid 
		admin = new User(); 
		admin.setArchived(false);
		admin.setEmail("oscarWinner@gmail.com");
		admin.setPassword("legendary");
		admin.setID(14);
		admin.setFirstName("Guillermo");
		admin.setLastName("Del-Toro");
		admin.setPosition(adminP);
		
		user = new User();
		user.setArchived(true);
		user.setID(33);
		user.setEmail("zod@krypton.com");
		user.setFirstName("General");
		user.setLastName("Zod");
		user.setPassword("imAPrick");
		user.setPosition(userP);
		
		manager = new User(); 
		
		manager.setArchived(false);
		manager.setEmail("headOfTheClub@SOA.com");
		manager.setPassword("weRunGuns");
		manager.setFirstName("Clay");
		manager.setLastName("Marrow");
		manager.setID(89);
		manager.setPosition(managerP);
		
		failUser = new User(); 
		failUser.setEmail("rvincent@ycp.edu");
		failUser.setFirstName("Ryan");
		failUser.setLastName("Vincent");
		failUser.setArchived(false);
		failUser.setPassword("ppSH");
		failUser.setLockedOut(false);
		
		
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
		failUser.setPosition(userP);
		
		//create a set of bare-bones SOPs for basic population testing 
		s1 = new SOP();
		s1.setID(1);
		s1.setComplete(false);
		s1.setPriority(2);
		s1.setVersion(1);
		
		s2 = new SOP();
		s2.setID(2);
		s2.setComplete(false);
		s2.setPriority(8);
		s2.setVersion(2);
		
		s3 = new SOP();
		s3.setID(3);
		s3.setComplete(false);
		s3.setPriority(7);
		s3.setVersion(1);
		s4 = new SOP();
		
		s4.setID(4);
		s4.setComplete(false);
		s4.setPriority(3);
		s4.setVersion(5);
		
		s5 = new SOP();
		s5.setID(5);
		s5.setComplete(false);
		s5.setPriority(5);
		s5.setVersion(1);
		
		s6 = new SOP();
		s6.setID(6);
		s6.setComplete(false);
		s6.setPriority(10);
		s6.setVersion(2);
		
		s7 = new SOP();
		s7.setID(7);
		s7.setComplete(false);
		s7.setPriority(9); 
		s7.setVersion(2);
		
		s8 = new SOP();
		s8.setID(8);
		s8.setComplete(false);
		s8.setPriority(4);
		s8.setVersion(1);
		
		s9 = new SOP();
		s9.setID(9);
		s9.setComplete(false);
		s9.setPriority(6);
		s9.setVersion(3);
		
		s10 = new SOP();
		s10.setID(10);
		s10.setComplete(false);
		s10.setPriority(1);
		s10.setVersion(8);
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
	
	
	@Test
	public void testDisplayDoneList() {
		s3.setComplete(true);
		s4.setComplete(true);
		s10.setComplete(true);
		
		System.out.println("Printing the completed SOP list");
		s.setAndshowDoneList(adminP);
	}
	
	@Test
	public void testDisplayToDoList() {
		
		System.out.println();
		System.out.println("Printing out the to do SOP list");
		s.setAndShowToDoList(userP);
	}
	
	@Test
	public void testAreGaps() {
		
		boolean AreGapstest = s.checkIfToDoIsEmpty(failUser.getPosition());
		assertTrue(AreGapstest);
		System.out.println();
		s1.setComplete(true);
		s3.setComplete(true);
		s4.setComplete(true);
		s5.setComplete(true);
		s10.setComplete(true);
		
		boolean AreGapsTest2 = s.checkIfToDoIsEmpty(adminP);
		assertFalse(AreGapsTest2);
		System.out.println();
	}
	
	@Test
	public void testSniffDeeply() {
		s.SniffDeeply();
	}
}
