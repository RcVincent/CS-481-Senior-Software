package edu.ycp.cs481.control;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class PositionControllerTest {
	private PositionController poscontrol;
	private Position pos1, pos2, pos3, pos4;
	private SOP sop1, sop2;
	private User user1, user2; 
	private List<Position> positionList; 
	private List<SOP> sopList;
	
	@Before
	public void setUp(){
		poscontrol = new PositionController(); 
		
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
	
	
	@Test
	public void testGetApplicantsSOP() {
		List<SOP> testList = new ArrayList<SOP>(); 
		
		testList = positionList.get(0).getRequirements();
		assertEquals(2, testList.size()); 
		
	}
	
	// TODO: Potentially rework these for searchForPositions?
	@Test
	public void testGetPositionByID() {
		List<Position> testList = new ArrayList<Position>(); 
		int searchID = 2;
		testList = poscontrol.searchForPositions(searchID, false, "", false, "", 0);
		
		assertEquals(1, testList.size());
		Position p = testList.get(0);
		assertEquals(1, p.getPriority());
		assertEquals("Admin", p.getTitle());
		
	}
	
	@Test
	public void testSearchByName() {
		List<Position> testList = new ArrayList<Position>();
		String searchName = "CEO";
		testList = poscontrol.searchForPositions(-1, false, searchName, false, "", -1);
		
		assertEquals(1, testList.size());
		Position p = testList.get(0);
		assertEquals(1, p.getPriority());
		assertEquals(1, p.getID()); 
	}
	
	//should probably re work this test to fit our new search scheme
	@Test
	public void testSearchByPriority() {
		List<Position> testList = new ArrayList<Position>();
		int priority = 1; 
		testList = poscontrol.searchForPositions(0, false, "", false, "", priority);
		assertEquals(2, testList.size());
		
		Position p1 = testList.get(0);
		Position p2 = testList.get(1);
		
		assertEquals(2, p1.getID());
		assertEquals(1, p2.getID()); 
		assertEquals(1, p1.getPriority());
		assertEquals(1, p2.getPriority()); 
	}
	
	
	@Test
	public void testChangePriority() {
		int newPrio = 3; 
		Position testPos = positionList.get(2);
		assertEquals(5, testPos.getPriority()); 
		poscontrol.changePositionPriority(testPos, newPrio);
		
		assertEquals(3, testPos.getPriority()); 
	}
	
	/*
	@Test
	public void testAddSOPtoPosition() {
		
	}
	 */
}
