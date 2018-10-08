package dbTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.PositionController;
import model.Position;
import model.User;
import model.SOP;
import sqlDB.SqlDatabase;

public class SqlDatabaseTest {

	private SqlDatabase db;
	private Position p1, p2, p3;
	private SOP s1, s2, s3;
	private User u1, u2, u3;
	private List<Position> queryPos;
	private List<SOP> querySOP;
	private List<User> queryUser;
	
	@Before
	public void setUp(){
    // Clean the database in case someone/something else messed with it
		SqlDatabase.cleanDB();
    
		db = new SqlDatabase();
		
		p1 = new Position();
		p1.setID(1);
		p1.setTitle("CEO");
		p1.setDescription("Does stuff");
		p1.setPriority(1);
		
		p2 = new Position();
		p2.setID(2);
		p2.setTitle("Programmer");
		p2.setDescription("Does more stuff");
		p2.setPriority(2);
		
		p3 = new Position();
		p3.setID(3);
		p3.setTitle("Debugger");
		p3.setDescription("Fixes bugs");
		p3.setPriority(2);
		
		queryPos = new ArrayList<Position>();
		
		s1 = new SOP();
		s2 = new SOP();
		s3 = new SOP();

		querySOP = new ArrayList<SOP>();
		
		u1 = new User();
		u2 = new User();
		u3 = new User();
		
		queryUser = new ArrayList<User>();
	}
  
  @After
	public void cleanUp(){
		// Clean the database for the next person
		SqlDatabase.cleanDB();
	}
	
	@Test
	public void testInsertUser() {
		
	}
	
	@Test
	public void testInsertPosition() {
		db.insertPosition(p1);
		db.insertPosition(p2);
		db.insertPosition(p3);
		
		queryPos = db.findAllPositions();
		
		assertEquals(p1.getID(), queryPos.get(0).getID());
		assertEquals(p2.getID(), queryPos.get(1).getID());
		assertEquals(p3.getID(), queryPos.get(2).getID());
		
		assertEquals(p1.getTitle(), queryPos.get(0).getTitle());
		assertEquals(p2.getTitle(), queryPos.get(1).getTitle());
		assertEquals(p3.getTitle(), queryPos.get(2).getTitle());
		
		assertEquals(p1.getDescription(), queryPos.get(0).getDescription());
		assertEquals(p2.getDescription(), queryPos.get(1).getDescription());
		assertEquals(p3.getDescription(), queryPos.get(2).getDescription());
		
		assertEquals(p1.getPriority(), queryPos.get(0).getPriority());
		assertEquals(p2.getPriority(), queryPos.get(1).getPriority());
		assertEquals(p3.getPriority(), queryPos.get(2).getPriority());
	}
	
	@Test
	public void testInsertSOP() {
		
	}
	
	@Test
	public void testGetAllUsers() {
		
	}
	
	@Test
	public void testGetAllPositions() {
		
	}
	
	@Test
	public void testGetAllSOPs() {
		
	}
	
	@Test
	public void testGetPositionByID() {
		
	}
}