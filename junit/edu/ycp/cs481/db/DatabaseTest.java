package edu.ycp.cs481.db;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class DatabaseTest {

	private Database db;
	private List<Position> queryPos, pList;
	private List<SOP> querySOP, sList;
	private List<User> queryUser, uList;
	private int p_size, u_size, s_size;
	
	@BeforeClass
	public static void cleanBefore(){
		// Clean the database in case someone/something else messed with it
		Database.cleanDB();
	}
	
	@AfterClass
	public static void cleanAfter(){
		// Clean the database for the next person
		Database.cleanDB();
	}
	
	@Before
	public void setUp(){
		db = new Database();
		
		pList = new InitialData().getInitialPositions();
		sList = new InitialData().getInitialSOPs();
		uList = new InitialData().getInitialUsers();
		
		queryPos = new ArrayList<Position>();
		querySOP = new ArrayList<SOP>();
		queryUser = new ArrayList<User>();

		p_size = new InitialData().getInitialPositions().size();
		u_size = new InitialData().getInitialUsers().size();
		s_size = new InitialData().getInitialSOPs().size();
	}
	
	@Test
	public void testLoadInitialData() {
		
	}
	
	/// Testing for our initial data, to ensure that all of our inserts were parsed correctly \\\
   ///																						   \\\
	// TODO: Test initial data is in correctly? (Commented out stuff as moved to controllers)
	/*@Test
	public void testFindAllUsers() {
		queryUser = db.findAllUsers();
		
		for(int i = 0; i < u_size; i++) {
			assertEquals(queryUser.get(i).getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			assertEquals(queryUser.get(i).getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			assertEquals(queryUser.get(i).getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			assertEquals(queryUser.get(i).getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
		}
	}
	
	@Test
	public void testFindAllPositions() {
		queryPos = db.findAllPositions();
		
		for(int i = 0; i < p_size; i++)
		{
			assertEquals(queryPos.get(i).getTitle(), new InitialData().getInitialPositions().get(i).getTitle());
			assertEquals(queryPos.get(i).getDescription(), new InitialData().getInitialPositions().get(i).getDescription());
			assertEquals(queryPos.get(i).getPriority(), new InitialData().getInitialPositions().get(i).getPriority());
		}
	}
	
	@Test
	public void testFindAllSOPs() { 
		querySOP = db.findAllSOPs();
		
		for(int i = 0; i < s_size; i++) {
			assertEquals(querySOP.get(i).getName(), new InitialData().getInitialSOPs().get(i).getName());
			assertEquals(querySOP.get(i).getDescription(), new InitialData().getInitialSOPs().get(i).getDescription());
			assertEquals(querySOP.get(i).getPriority(), new InitialData().getInitialSOPs().get(i).getPriority());
			assertEquals(querySOP.get(i).getRevision(), new InitialData().getInitialSOPs().get(i).getRevision());
			assertEquals(querySOP.get(i).getAuthorID(), new InitialData().getInitialSOPs().get(i).getAuthorID());
			assertEquals(querySOP.get(i).getArchiveFlag(), new InitialData().getInitialSOPs().get(i).getArchiveFlag());
		}
	}*/
	
	
	
	/*@Test // TODO: Implement after PositionSOP
	public void testFindSOPthruPosition() {
		querySOP = db.findAllSOPs();
	}*/
	
	
	
	/*@Test //TODO: After PositionSOP is implemented
	public void testGetPositionBySOPID() {
		queryPos = db.findAllPositions();
		querySOP = db.findAllSOPs();
		assertEquals(queryPos.get(0).getID(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getID());
		assertEquals(queryPos.get(0).getTitle(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getTitle());
		assertEquals(queryPos.get(0).getDescription(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getDescription());
		assertEquals(queryPos.get(0).getPriority(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getPriority());
	}*/
	
	// TODO: Move this, does it go in PositionController or UserController Test?
	/*
	@Test
	public void testGetPositionByUser() {
		queryPos = db.findAllPositions();
		queryUser = db.findAllUsers();
		
		for(int i = 0; i < u_size; i++) {
			assertEquals(queryPos.get(i).getTitle(), db.getPositionOfUser(queryUser.get(i).getUserID()).getTitle());
			assertEquals(queryPos.get(i).getDescription(), db.getPositionOfUser(queryUser.get(i).getUserID()).getDescription());
			assertEquals(queryPos.get(i).getPriority(), db.getPositionOfUser(queryUser.get(i).getUserID()).getPriority());
		}
	}*/
}