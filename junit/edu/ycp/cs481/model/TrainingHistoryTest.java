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
	
	public TrainingHistoryTest() {
		AdminHist = new TrainingHistory(); 
		userHist = new TrainingHistory();
		managerHist = new TrainingHistory(); 
		adminReqs = new ArrayList<SOP>(); 
		userReqs = new ArrayList<SOP>(); 
		managerReqs = new ArrayList<SOP>();
		
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
		
		s1 = new SOP();
		s1.setID(1);
		
		
		s2 = new SOP();
		s2.setID(2);
		
		s3 = new SOP();
		s3.setID(3);
		
		s4 = new SOP();
		s4.setID(4);
		
		s5 = new SOP();
		s5.setID(5);
		
		s6 = new SOP();
		s6.setID(6);
		
		s7 = new SOP();
		s7.setID(7);
		
		s8 = new SOP();
		s8.setID(8);
		
		s9 = new SOP();
		s9.setID(9);
		
		s10 = new SOP();
		s10.setID(10);
		
		userReqs.add(s2);
		userReqs.add(s5);
		userReqs.add(s6);
		userReqs.add(s7);
		userReqs.add(s9);
		
		adminReqs.add(s1);
		adminReqs.add(s3);
		adminReqs.add(s4);
		adminReqs.add(s5);
		adminReqs.add(s10);
		
		managerReqs.add(s3);
		managerReqs.add(s6);
		managerReqs.add(s7);
		managerReqs.add(s8);
		managerReqs.add(s9);
		
		AdminHist.setUser(admin);
		userHist.setUser(user);
		managerHist.setUser(manager);
		
		
	}
	
	@Test
	public void testisPopulated() {
		assertEquals(5, AdminHist.getTrainingHistorySize());
		assertEquals(5, userHist.getTrainingHistorySize());
		assertEquals(5, managerHist.getTrainingHistorySize());
		
	}
	
	public void testPopulateThroughPosition() {
		List<SOP> reqs1 = admin.getPosition().getRequirements();
		List<SOP> reqs2 = user.getPosition().getRequirements();
		List<SOP> reqs3 = manager.getPosition().getRequirements();
		
		
	}

}
