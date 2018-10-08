package dbTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
	
	@BeforeClass
	public static void cleanBefore(){
		// Clean the database in case someone/something else messed with it
		SqlDatabase.cleanDB();
	}
	
	@AfterClass
	public static void cleanAfter(){
		// Clean the database for the next person
		SqlDatabase.cleanDB();
	}
	
	@Before
	public void setUp(){
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
		s1.setID(1);
		s1.setName("Do stuff");
		s1.setDescription("Do it now");
		s1.setPriority(2);
		s1.setRevision(1);
		s1.setAuthorID(p1.getID());
		s1.setArchiveFlag(true);
		
		s2 = new SOP();
		s2.setID(2);
		s2.setName("Do different stuff");
		s2.setDescription("Do it now");
		s2.setPriority(2);
		s2.setRevision(2);
		s2.setAuthorID(p1.getID());
		s2.setArchiveFlag(false);
		
		s3 = new SOP();
		s3.setID(3);
		s3.setName("Do more stuff");
		s3.setDescription("Do it yesterday");
		s3.setPriority(1);
		s3.setRevision(1);
		s3.setAuthorID(p2.getID());
		s3.setArchiveFlag(false);

		querySOP = new ArrayList<SOP>();
		
		u1 = new User();
		u1.setUserID(1);
		u1.setEmail("CEO@Google.com");
		u1.setPassword("beard");
		u1.setFirstname("Chuck");
		u1.setLastname("Norris");
		u1.setAdminFlag("true");
		u1.setArchiveFlag(false);
		u1.setPosition(p1);
		
		u2 = new User();
		u3 = new User();
		
		queryUser = new ArrayList<User>();
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
	public void testInsertUser() {
		db.insertUser(u1);
		//db.insertUser(u2);
		//db.insertUser(u3);
		
		queryUser = db.findAllUsers();

		assertEquals(u1.getUserID(), queryUser.get(0).getUserID());

		assertEquals(u1.getEmail(), queryUser.get(0).getEmail());
		
		assertEquals(u1.getPassword(), queryUser.get(0).getPassword());

		assertEquals(u1.getFirstname(), queryUser.get(0).getFirstname());

		assertEquals(u1.getLastname(), queryUser.get(0).getLastname());

		assertEquals(u1.isAdminFlag(), queryUser.get(0).isAdminFlag());
		
		assertEquals(u1.isArchiveFlag(), queryUser.get(0).isArchiveFlag());
	
		assertEquals(u1.getPosition().getID(), queryUser.get(0).getPosition().getID());
	}
	
	@Test
	public void testInsertSOP() {
		db.insertSOP(s1);
		db.insertSOP(s2);
		db.insertSOP(s3);
		
		querySOP = db.findAllSOPs();
		
		assertEquals(s1.getID(), querySOP.get(0).getID());
		assertEquals(s2.getID(), querySOP.get(1).getID());
		assertEquals(s3.getID(), querySOP.get(2).getID());
		
		assertEquals(s1.getName(), querySOP.get(0).getName());
		assertEquals(s2.getName(), querySOP.get(1).getName());
		assertEquals(s3.getName(), querySOP.get(2).getName());

		assertEquals(s1.getDescription(), querySOP.get(0).getDescription());
		assertEquals(s2.getDescription(), querySOP.get(1).getDescription());
		assertEquals(s3.getDescription(), querySOP.get(2).getDescription());

		assertEquals(s1.getPriority(), querySOP.get(0).getPriority());
		assertEquals(s2.getPriority(), querySOP.get(1).getPriority());
		assertEquals(s3.getPriority(), querySOP.get(2).getPriority());

		assertEquals(s1.getRevision(), querySOP.get(0).getRevision());
		assertEquals(s2.getRevision(), querySOP.get(1).getRevision());
		assertEquals(s3.getRevision(), querySOP.get(2).getRevision());

		assertEquals(s1.getAuthorID(), querySOP.get(0).getAuthorID());
		assertEquals(s2.getAuthorID(), querySOP.get(1).getAuthorID());
		assertEquals(s3.getAuthorID(), querySOP.get(2).getAuthorID());
		
		assertEquals(s1.getArchiveFlag(), querySOP.get(0).getArchiveFlag());
		assertEquals(s2.getArchiveFlag(), querySOP.get(1).getArchiveFlag());
		assertEquals(s3.getArchiveFlag(), querySOP.get(2).getArchiveFlag());	
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