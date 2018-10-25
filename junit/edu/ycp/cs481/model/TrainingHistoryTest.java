package edu.ycp.cs481.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import edu.ycp.cs481.model.TrainingHistory;

public class TrainingHistoryTest {
	private User admin, user, manager;
	private TrainingHistory AdminHist, userHist, managerHist;
	private SOP s1, s2, s3, s4, s5, s6, s7, s8, s9, s10; 
	private Position adminP, userP, managerP; 
	private List<SOP> adminReqs, userReqs, managerReqs; 
	private List<User> userList; 
	
	public TrainingHistoryTest() {
		userList = new ArrayList<User>(); 
		
		AdminHist = new TrainingHistory(); 
		userHist = new TrainingHistory();
		managerHist = new TrainingHistory(); 
		
		adminReqs = new ArrayList<SOP>(); 
		userReqs = new ArrayList<SOP>(); 
		managerReqs = new ArrayList<SOP>();
		
		//create 3 new users for testing two valid one non valid 
		admin = new User(); 
		admin.setAdminFlag(true);
		admin.setArchiveFlag(false);
		admin.setEmail("oscarWinner@gmail.com");
		admin.setPassword("legendary");
		admin.setUserID(14);
		admin.setFirstname("Guillermo");
		admin.setLastname("Del-Toro");
		admin.setPosition(adminP);
		
		user = new User();
		user.setAdminFlag(false);
		user.setArchiveFlag(true);
		user.setUserID(33);
		user.setEmail("zod@krypton.com");
		user.setFirstname("General");
		user.setLastname("Zod");
		user.setPassword("imAPrick");
		user.setPosition(userP);
		
		manager = new User(); 
		manager.setAdminFlag(false);
		manager.setArchiveFlag(false);
		manager.setEmail("headOfTheClub@SOA.com");
		manager.setPassword("weRunGuns");
		manager.setFirstname("Clay");
		manager.setLastname("Marrow");
		manager.setUserID(89);
		manager.setPosition(managerP);
		
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
		
		//add sops to the user position list
		userReqs.add(s2);
		userReqs.add(s5);
		userReqs.add(s6);
		userReqs.add(s7);
		userReqs.add(s9);
		
		//add sops to the admin position list
		adminReqs.add(s1);
		adminReqs.add(s3);
		adminReqs.add(s4);
		adminReqs.add(s5);
		adminReqs.add(s10);
		
		//add sops top to the manager position list 
		managerReqs.add(s3);
		managerReqs.add(s6);
		managerReqs.add(s7);
		managerReqs.add(s8);
		managerReqs.add(s9);
		
		//add the sop requitement lists to their positions 
		adminP.setRequirements(adminReqs);
		managerP.setRequirements(managerReqs);
		userP.setRequirements(userReqs);
		
		//set the training history users 
		AdminHist.setUser(admin);
		userHist.setUser(user);
		managerHist.setUser(manager);
		
		//set the users training histories 
		admin.setHistory(AdminHist);
		user.setHistory(userHist);
		manager.setHistory(managerHist); 
		
		
	}
	
	@Test
	public void testisPopulated() {
		assertEquals(5, AdminHist.getTrainingHistorySize());
		assertEquals(5, userHist.getTrainingHistorySize());
		assertEquals(5, managerHist.getTrainingHistorySize());
		
	}
	
	@Test
	public void testPopulateThroughPosition() {
		List<SOP> reqs1 = admin.getPosition().getRequirements();
		List<SOP> reqs2 = user.getPosition().getRequirements();
		List<SOP> reqs3 = manager.getPosition().getRequirements();
		
		 assertEquals(5, reqs1.size());
		 assertEquals(5, reqs2.size());
		 assertEquals(5, reqs3.size());
		 
		 AdminHist.addAndSortCollection(reqs1);
		 assertEquals(5, AdminHist.getTrainingHistorySize());
		 assertEquals(5, AdminHist.getSopsToDo().size());
		 
		 userHist.addAndSortCollection(reqs2);
		 assertEquals(5, userHist.getTrainingHistorySize());
		 assertEquals(5, userHist.getSopsToDo().size());
		 
		 managerHist.addAndSortCollection(reqs3);
		 assertEquals(5, managerHist.getTrainingHistorySize());
		 assertEquals(5, managerHist.getSopsToDo().size());
		 
	}
	
	@Test
	public void testPopulateThroughTheUser() {
		TrainingHistory h1 = admin.getHistory(); 
		TrainingHistory h2 = user.getHistory();
		TrainingHistory h3 = manager.getHistory(); 
		
		assertEquals(5, h1.getTrainingHistorySize());
		assertEquals(5, h2.getTrainingHistorySize());
		assertEquals(5, h3.getTrainingHistorySize());
	}
	
	@Test
	public void testPopulateManuallyThroughList() {
		AdminHist.addAndSortCollection(adminReqs);
		assertEquals(5, AdminHist.getTrainingHistorySize());
		assertEquals(5, AdminHist.getSopsToDo().size());
		
		userHist.addAndSortCollection(userReqs);
		assertEquals(5, userHist.getTrainingHistorySize());
		assertEquals(5, userHist.getSopsToDo().size());
		
		managerHist.addAndSortCollection(managerReqs);
		assertEquals(5, managerHist.getTrainingHistorySize());
		assertEquals(5, managerHist.getSopsToDo().size());
		
		
	}
	
	@Test
	public void TestPopulateBySOP() {
		SOP s1 = new SOP();
		SOP s2 = new SOP(); 
		SOP s3 = new SOP(); 
		
		AdminHist.addToDoList(s1);
		AdminHist.addToCompleted(s2);
		
		userHist.addToDoList(s2);
		userHist.addToCompleted(s3);
		
		managerHist.addToDoList(s3);
		managerHist.addToCompleted(s1);
		
		assertEquals(2, AdminHist.getTrainingHistorySize()); 
		assertEquals(1, AdminHist.getSopsToDo().size());
		assertEquals(1, AdminHist.getCompletedSOPs().size());
		
		assertEquals(2, userHist.getTrainingHistorySize()); 
		assertEquals(1, userHist.getSopsToDo().size());
		assertEquals(1, userHist.getCompletedSOPs().size());
		
		assertEquals(2, managerHist.getTrainingHistorySize()); 
		assertEquals(1, managerHist.getSopsToDo().size());
		assertEquals(1, managerHist.getCompletedSOPs().size());
	}
	
	public void TestViewHistory(); 

}
