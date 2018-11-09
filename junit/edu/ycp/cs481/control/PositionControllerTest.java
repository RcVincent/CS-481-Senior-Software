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
		user1.setFirstname("Rodger");
		user1.setLastname("Smith");
		user1.setUserID(12);
		user1.setPosition(pos1);
		//user1.setAdminFlag(true);
		
		user2 = new User();
		user2.setFirstname("Stan");
		user2.setLastname("Smith");
		user2.setEmail("rookie@email.com");
		//user2.setAdminFlag(false);
		user2.setPassword("bangBang");
		user2.setPosition(pos3);
		user2.setUserID(4);
		
		positionList.add(pos1);
		positionList.add(pos2);
		positionList.add(pos3);
		positionList.add(pos4);
		
		sop1 = new SOP(); 
		sop1.setName("Login");
		sop1.setID(1);
		sop1.setDescription("How to login");
		sop1.setPriority(7);
		sop1.setRevision(1);
		sop1.setAuthorID(user1.getUserID());
		sop1.setPositionsAffected(positionList);
		
		sop2 = new SOP(); 
		sop2.setName("Logout");
		sop2.setDescription("How to logout");
		sop2.setPriority(7);
		sop2.setID(2);
		sop2.setRevision(2);
		sop2.setAuthorID(user1.getUserID());
		sop2.setPositionsAffected(positionList);
		
		sopList.add(sop1);
		sopList.add(sop2);
	}
	
	// TODO: Rework this for PositionController
	/*@Test
	public void testInsertPosition() {
		
		for(Position p: pList) {
			db.insertPosition(p);
			queryPos = db.findAllPositions();
			assertEquals(p.getTitle(), queryPos.get(queryPos.size()-1).getTitle());
			assertEquals(p.getDescription(), queryPos.get(queryPos.size()-1).getDescription());
			assertEquals(p.getPriority(), queryPos.get(queryPos.size()-1).getPriority());
		}
	}*/
	
	//@Test
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
		testList = poscontrol.searchForPositions(searchID, "", "", 0);
		
		assertEquals(1, testList.size());
		Position p = testList.get(0);
		assertEquals(1, p.getPriority());
		assertEquals("Admin", p.getTitle());
		
	}
	
	@Test
	public void testSearchByName() {
		List<Position> testList = new ArrayList<Position>();
		String searchName = "CEO";
		testList = poscontrol.searchForPositions(-1, searchName, "", -1);
		
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
		testList = poscontrol.searchForPositions(0, "", "", priority);
		assertEquals(2, testList.size());
		
		Position p1 = testList.get(0);
		Position p2 = testList.get(1);
		
		assertEquals(2, p1.getID());
		assertEquals(1, p2.getID()); 
		assertEquals(1, p1.getPriority());
		assertEquals(1, p2.getPriority()); 
	}
	
	// TODO: Potentially use these in testing searchForPositions?
	/*
	@Test
	public void testFindPositionByID() {
		queryPos = db.findAllPositions();
		
		for(Position p : queryPos) {
			assertEquals(p.getTitle(), db.findPositionByID(p.getID()).getTitle());
			assertEquals(p.getDescription(), db.findPositionByID(p.getID()).getDescription());
			assertEquals(p.getPriority(), db.findPositionByID(p.getID()).getPriority());
		}
	}
	
	@Test
	public void testGetPositionByName() {
		queryPos = db.findAllPositions();
		
		for(int i = 0; i < p_size; i++) {
			assertEquals(queryPos.get(i).getTitle(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getTitle());
			assertEquals(queryPos.get(i).getDescription(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getDescription());
			assertEquals(queryPos.get(i).getPriority(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getPriority());
		}
	}
	
	@Test
	public void testGetPositionByPriority() {
		queryPos = db.findAllPositions();
		
		assertEquals(queryPos.get(0).getTitle(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getTitle());
		assertEquals(queryPos.get(0).getDescription(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getDescription());
		assertEquals(queryPos.get(0).getPriority(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getPriority());

	}
	
	@Test
	public void testDeletePosition() {
		db.deletePosition(3);
		queryPos = db.findAllPositions();

		assertNotEquals(queryPos.get(2).getID(), pList.get(2).getID());
		assertNotEquals(queryPos.get(2).getDescription(), pList.get(2).getDescription());
		assertNotEquals(queryPos.get(2).getTitle(), pList.get(2).getTitle());
	}
	 */
	
	@Test
	public void testChangePriority() {
		int newPrio = 3; 
		Position testPos = positionList.get(2);
		assertEquals(5, testPos.getPriority()); 
		poscontrol.changePositionPriority(testPos, newPrio);
		
		assertEquals(3, testPos.getPriority()); 
	}
	
	/* TODO: Implement these here?
	@Test // TODO: this breaks testInsertPosition and testFindAllPositions
	public void testChangePositionPriority() {
		queryPos = db.findAllPositions();
		int insert_id = db.insertPosition(queryPos.get(0));
		
		Position changedPriority = db.changePositionPriority(queryPos.get(insert_id - 1).getID(), 2);
		assertEquals(2, changedPriority.getPriority());
	}

	@Test
	public void testAddSOPtoPosition() {
		
	}
	 */
}
