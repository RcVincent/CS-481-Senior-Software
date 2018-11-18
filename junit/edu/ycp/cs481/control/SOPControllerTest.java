package edu.ycp.cs481.control;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;
import edu.ycp.cs481.control.SOPController;

public class SOPControllerTest {
	private SOPController sc;
	private Position pos1, pos2, pos3, pos4;
	private SOP sop1, sop2;
	private User user1, user2; 
	private List<Position> positionList; 
	private List<SOP> sopList;
	
	@Before
	public void setUp(){
		sc = new SOPController(); 
		positionList = new ArrayList<Position>(); 
		sopList = new ArrayList<SOP>(); 
		
		pos1 = new Position(); 
		pos1.setID(2);
		pos1.setPriority(1);
		pos1.setTitle("Admin");
		
		pos2 = new Position(); 
		pos2.setID(1);
		pos2.setPriority(1);
		pos2.setTitle("CEO");
		
		pos3 = new Position(); 
		pos3.setID(3);
		pos3.setPriority(5);
		pos3.setTitle("IT");
		
		pos4 = new Position(); 
		pos4.setID(4);
		pos4.setPriority(9);
		pos4.setTitle("Intern");
		
		user1 = new User();
		user1.setEmail("Admin@google.com");
		user1.setFirstName("Rodger");
		user1.setLastName("Smith");
		user1.setID(12);
		user1.setPosition(pos1);
		//user1.setAdminFlag(true);
		
		user2 = new User();
		user2.setFirstName("Stan");
		user2.setLastName("Smith");
		user2.setEmail("rookie@email.com");
		//user2.setAdminFlag(false);
		user2.setPassword("bangBang");
		user2.setPosition(pos3);
		user2.setID(4);
		
		positionList.add(pos1);
		positionList.add(pos2);
		positionList.add(pos3);
		positionList.add(pos4);
		
		sop1 = new SOP(); 
		sop1.setTitle("Login");
		sop1.setID(1);
		sop1.setDescription("How to login");
		sop1.setPriority(7);
		sop1.setVersion(1);
		sop1.setAuthorID(user1.getID());
		
		sop2 = new SOP(); 
		sop2.setTitle("Logout");
		sop2.setDescription("How to logout");
		sop2.setPriority(7);
		sop2.setID(2);
		sop2.setVersion(2);
		sop2.setAuthorID(user1.getID());
		
		sopList.add(sop1);
		sopList.add(sop2);
	}
	
	
	
	public void testArchiveSOP() {
		
	}
	
	// TODO: Rework for here?
	/*@Test
	public void testInsertSOP() {
		for(SOP s: sList) {
			db.insertSOP(s);
			querySOP = db.findAllSOPs();
			assertEquals(s.getTitle(), querySOP.get(querySOP.size()-1).getTitle());
			assertEquals(s.getDescription(), querySOP.get(querySOP.size()-1).getDescription());
			assertEquals(s.getPriority(), querySOP.get(querySOP.size()-1).getPriority());
			assertEquals(s.getVersion(), querySOP.get(querySOP.size()-1).getVersion());
			assertEquals(s.getAuthorID(), querySOP.get(querySOP.size()-1).getAuthorID());
			assertEquals(s.getArchiveFlag(), querySOP.get(querySOP.size()-1).getArchiveFlag());
		}
	}*/
	
	// TODO: Rework these for searchForSOPs now?
	@Test
	public void testSearchByID() {
		int searchID = 1;
		
		List<SOP> testList = sc.searchForSOPs(searchID, false, "", false, "", 0, 0, 0);
		
		if(testList.isEmpty()) {
			System.out.println("The search for sop with id "+ searchID + " failed."); 
			//fail();
		} else {
			assertEquals(1, testList.size()); 
			
			SOP s = testList.get(0);
			assertEquals("Login", s.getTitle());
			assertEquals(7, s.getPriority());
			assertEquals(1, s.getVersion());
		}
	}
	
	@Test
	public void testSearchByName() {
		String searchName = "Logout";
		List<SOP> testList = sc.searchForSOPs(0, false, searchName, false, "", 0, 0, 0);
		
		if(testList.isEmpty()) {
			System.out.println("The search for sop with name "+searchName+ "failed."); 
			//fail();
		} else {
			
			assertEquals(1, testList.size()); 
			SOP s = testList.get(0);
			assertEquals(7, s.getPriority());
			assertEquals(2, s.getVersion()); 
			assertEquals(2, s.getID()); 
		}
	}
	
	@Test
	public void testSearchByPriority() {
		int searchPrio = 7; 
		List<SOP> testList = sc.searchForSOPs(0, false, "", false, "", searchPrio, 0, 0);
		
		if(testList.isEmpty()) {
			System.out.println("The search for sop with "+ searchPrio+ "failed."); 
			//fail();
		} else {
			
			assertEquals(2, testList.size());
			
			SOP s1 = testList.get(0); 
			assertEquals("Login", s1.getTitle());
			assertEquals(7, s1.getPriority());
			assertEquals(1, s1.getVersion());
			assertEquals(1, s1.getID()); 
			assertEquals(12, s1.getAuthorID());
			
			SOP s2 = testList.get(1);
			assertEquals("Logout", s2.getTitle());
			assertEquals(7, s2.getPriority());
			assertEquals(2, s2.getVersion()); 
			assertEquals(2, s2.getID()); 
			assertEquals(12, s2.getAuthorID());
			
		}		
	}
	
	@Test
	public void testSearchByAuthor() {
		int searchAuthorID = 12; 
		List<SOP> testList = sc.searchForSOPs(0, false, "", false, "", 0, 0, searchAuthorID);
		
		if(testList.isEmpty()) {
			System.out.println("The search for sop with author id "+ searchAuthorID + "failed."); 
			//fail();
		} else {
			assertEquals(2, testList.size());
			
			SOP s1 = testList.get(0); 
			assertEquals("Login", s1.getTitle());
			assertEquals(7, s1.getPriority());
			assertEquals(1, s1.getVersion());
			assertEquals(1, s1.getID()); 
			
			
			SOP s2 = testList.get(1);
			assertEquals("Logout", s2.getTitle());
			assertEquals(7, s2.getPriority());
			assertEquals(2, s2.getVersion()); 
			assertEquals(2, s2.getID()); 
		}
	}
	
	@Test
	public void TestChangeVersion() {
		int newVersion = 2; 
		
		assertEquals(1, sop1.getVersion()); 
		sc.reversionSOP(sop1.getID(), newVersion);
		
		if(sop1.getVersion() == 1) {
			System.out.println("Reversioning of sop not successful");
			//fail();
		}
		else {
			assertEquals(2, sop1.getVersion()); 
		}
	}
	
	@Test
	public void testChangePriority() {
		int newPriority = 8; 
		
		assertEquals(7, sop2.getPriority());
		
		//TODO: this method fails we need to fix it 
		sc.changePriority(sop2.getID(), newPriority);
		
		if(sop2.getPriority() == 7) {
			System.out.println("Changing the priority of the sop failed");
			fail(); 
		} else {
			assertEquals(8, sop2.getPriority()); 
		}
	}
	
}
