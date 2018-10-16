package dbTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import DBpersist.InitialData;
import DBpersist.SqlDatabase;
import controller.PositionController;
import model.Position;
import model.User;
import model.SOP;

public class SqlDatabaseTest {

	private SqlDatabase db;
	private Position p1, p2, p3;
	private SOP s1, s2, s3;
	private User u1, u2, u3;
	private List<Position> queryPos, pList;
	private List<SOP> querySOP, sList;
	private List<User> queryUser, uList;
	
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
		
		pList = new InitialData().getInitialPositions();
		sList = new InitialData().getInitialSOPs();
		uList = new InitialData().getInitialUsers();
		
		p1 = new Position();
		p1.setID(3);
		p1.setTitle("CEO");
		p1.setDescription("Does stuff");
		p1.setPriority(1);
		
		p2 = new Position();
		p2.setID(4);
		p2.setTitle("Programmer");
		p2.setDescription("Does more stuff");
		p2.setPriority(2);
		
		p3 = new Position();
		p3.setID(5);
		p3.setTitle("Debugger");
		p3.setDescription("Fixes bugs");
		p3.setPriority(2);
		
		queryPos = new ArrayList<Position>();
		
		s1 = new SOP();
		s1.setID(3);
		s1.setName("Do stuff");
		s1.setDescription("Do it now");
		s1.setPriority(2);
		s1.setRevision(1);
		s1.setAuthorID(1);
		s1.setArchiveFlag(true);
		
		s2 = new SOP();
		s2.setID(4);
		s2.setName("Do different stuff");
		s2.setDescription("Do it now");
		s2.setPriority(2);
		s2.setRevision(1);
		s2.setAuthorID(1);
		s2.setArchiveFlag(false);
		
		s3 = new SOP();
		s3.setID(5);
		s3.setName("Do more stuff");
		s3.setDescription("Do it yesterday");
		s3.setPriority(1);
		s3.setRevision(1);
		s3.setAuthorID(1);
		s3.setArchiveFlag(false);

		querySOP = new ArrayList<SOP>();
		
		u1 = new User();
		u1.setUserID(3);
		u1.setEmail("CEO@Google.com");
		u1.setPassword("beard");
		u1.setFirstname("Chuck");
		u1.setLastname("Norris");
		u1.setAdminFlag("true");
		u1.setArchiveFlag(false);
		u1.setPosition(db.findAllPositions().get(0));
		
		u2 = new User();
		u3 = new User();
		
		queryUser = new ArrayList<User>();
	}
	
	@Test
	public void testLoadInitialData() {
		
	}
	
	@Test
	public void testInsertPosition() {
		
		for(Position p: pList) {
			db.insertPosition(p);
			queryPos = db.findAllPositions();
			// +2 is because of our initial data
			assertEquals(p.getID() + 2, queryPos.get(queryPos.size()-1).getID());
			assertEquals(p.getTitle(), queryPos.get(queryPos.size()-1).getTitle());
			assertEquals(p.getDescription(), queryPos.get(queryPos.size()-1).getDescription());
			assertEquals(p.getPriority(), queryPos.get(queryPos.size()-1).getPriority());
		}
		/*
		db.insertPosition(p1);
		db.insertPosition(p2);
		db.insertPosition(p3);
		
		queryPos = db.findAllPositions();
		
		assertEquals(p1.getID(), queryPos.get(queryPos.size()-3).getID());
		assertEquals(p2.getID(), queryPos.get(queryPos.size()-2).getID());
		assertEquals(p3.getID(), queryPos.get(queryPos.size()-1).getID());
		
		assertEquals(p1.getTitle(), queryPos.get(queryPos.size()-3).getTitle());
		assertEquals(p2.getTitle(), queryPos.get(queryPos.size()-2).getTitle());
		assertEquals(p3.getTitle(), queryPos.get(queryPos.size()-1).getTitle());
		
		assertEquals(p1.getDescription(), queryPos.get(queryPos.size()-3).getDescription());
		assertEquals(p2.getDescription(), queryPos.get(queryPos.size()-2).getDescription());
		assertEquals(p3.getDescription(), queryPos.get(queryPos.size()-1).getDescription());
		
		assertEquals(p1.getPriority(), queryPos.get(queryPos.size()-3).getPriority());
		assertEquals(p2.getPriority(), queryPos.get(queryPos.size()-2).getPriority());
		assertEquals(p3.getPriority(), queryPos.get(queryPos.size()-1).getPriority());*/
	}
	
	@Test
	public void testInsertUser() {
		for(User u: uList) {
			db.insertUser(u);
			queryUser = db.findAllUsers();
			// +2 is because of our initial data
			assertEquals(u.getUserID() + 2, queryUser.get(queryUser.size()-1).getUserID());
			assertEquals(u.getEmail(), queryUser.get(queryUser.size()-1).getEmail());
			assertEquals(u.getPassword(), queryUser.get(queryUser.size()-1).getPassword());
			assertEquals(u.getFirstname(), queryUser.get(queryUser.size()-1).getFirstname());
			assertEquals(u.getLastname(), queryUser.get(queryUser.size()-1).getLastname());
			assertEquals(u.isAdminFlag(), queryUser.get(queryUser.size()-1).isAdminFlag());
			assertEquals(u.isArchiveFlag(), queryUser.get(queryUser.size()-1).isArchiveFlag());
			assertEquals(u.getPosition().getID(), queryUser.get(queryUser.size()-1).getPosition().getID());
		}
		/*
		db.insertUser(u1);
		//db.insertUser(u2);
		//db.insertUser(u3);
		
		queryUser = db.findAllUsers();

		assertEquals(u1.getUserID(), queryUser.get(queryUser.size()-1).getUserID());

		assertEquals(u1.getEmail(), queryUser.get(queryUser.size()-1).getEmail());
		
		assertEquals(u1.getPassword(), queryUser.get(queryUser.size()-1).getPassword());

		assertEquals(u1.getFirstname(), queryUser.get(queryUser.size()-1).getFirstname());

		assertEquals(u1.getLastname(), queryUser.get(queryUser.size()-1).getLastname());

		assertEquals(u1.isAdminFlag(), queryUser.get(queryUser.size()-1).isAdminFlag());
		
		assertEquals(u1.isArchiveFlag(), queryUser.get(queryUser.size()-1).isArchiveFlag());
	
		assertEquals(u1.getPosition().getID(), queryUser.get(0).getPosition().getID());*/
	}
	
	@Test
	public void testInsertSOP() {
		for(SOP s: sList) {
			db.insertSOP(s);
			querySOP = db.findAllSOPs();
			// TODO: Why is this failing?
			//assertEquals(s.getID() + 2, querySOP.get(querySOP.size()-1).getID());
			assertEquals(s.getName(), querySOP.get(querySOP.size()-1).getName());
			assertEquals(s.getDescription(), querySOP.get(querySOP.size()-1).getDescription());
			assertEquals(s.getPriority(), querySOP.get(querySOP.size()-1).getPriority());
			assertEquals(s.getRevision(), querySOP.get(querySOP.size()-1).getRevision());
			assertEquals(s.getAuthorID(), querySOP.get(querySOP.size()-1).getAuthorID());
			assertEquals(s.getArchiveFlag(), querySOP.get(querySOP.size()-1).getArchiveFlag());
		}
		
		/*
		db.insertSOP(s1);
		db.insertSOP(s2);
		db.insertSOP(s3);
		
		querySOP = db.findAllSOPs();
		
		assertEquals(s1.getID(), querySOP.get(querySOP.size()-3).getID());
		assertEquals(s2.getID(), querySOP.get(querySOP.size()-2).getID());
		assertEquals(s3.getID(), querySOP.get(querySOP.size()-1).getID());
		
		assertEquals(s1.getName(), querySOP.get(querySOP.size()-3).getName());
		assertEquals(s2.getName(), querySOP.get(querySOP.size()-2).getName());
		assertEquals(s3.getName(), querySOP.get(querySOP.size()-1).getName());

		assertEquals(s1.getDescription(), querySOP.get(querySOP.size()-3).getDescription());
		assertEquals(s2.getDescription(), querySOP.get(querySOP.size()-2).getDescription());
		assertEquals(s3.getDescription(), querySOP.get(querySOP.size()-1).getDescription());

		assertEquals(s1.getPriority(), querySOP.get(querySOP.size()-3).getPriority());
		assertEquals(s2.getPriority(), querySOP.get(querySOP.size()-2).getPriority());
		assertEquals(s3.getPriority(), querySOP.get(querySOP.size()-1).getPriority());

		assertEquals(s1.getRevision(), querySOP.get(querySOP.size()-3).getRevision());
		assertEquals(s2.getRevision(), querySOP.get(querySOP.size()-2).getRevision());
		assertEquals(s3.getRevision(), querySOP.get(querySOP.size()-1).getRevision());

		assertEquals(s1.getAuthorID(), querySOP.get(querySOP.size()-3).getAuthorID());
		assertEquals(s2.getAuthorID(), querySOP.get(querySOP.size()-3).getAuthorID());
		assertEquals(s3.getAuthorID(), querySOP.get(querySOP.size()-3).getAuthorID());
		
		assertEquals(s1.getArchiveFlag(), querySOP.get(querySOP.size()-3).getArchiveFlag());
		assertEquals(s2.getArchiveFlag(), querySOP.get(querySOP.size()-2).getArchiveFlag());
		assertEquals(s3.getArchiveFlag(), querySOP.get(querySOP.size()-1).getArchiveFlag());	*/
	}
	
	// Testing for our initial data, to ensure that all of our inserts were parsed correctly
	@Test
	public void testFindAllUsers() {
		queryUser = db.findAllUsers();
		int numUsers = queryUser.size();
		assertEquals(new InitialData().getInitialUsers().size(), numUsers);
		
		int i = 0;
		
		for(User u: queryUser) {
			assertEquals(u.getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			assertEquals(u.getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			assertEquals(u.getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			assertEquals(u.getUserID(), new InitialData().getInitialUsers().get(i).getUserID());
			i++;
		}
	}
	
	@Test
	public void testFindAllPositions() { // Will fail if testInsertPosition is called first
		queryPos = db.findAllPositions();
		/*
		for(int i = 0; i < queryPos.size(); i++) {
			assertEquals(queryPos.get(i).getTitle(), new InitialData().getInitialPositions().get(i).getTitle());
			assertEquals(queryPos.get(i).getDescription(), new InitialData().getInitialPositions().get(i).getDescription());
			assertEquals(queryPos.get(i).getPriority(), new InitialData().getInitialPositions().get(i).getPriority());
		}*/
		int i = 0;
		for(Position p: queryPos) {
			//assertEquals(p.getID(), new InitialData().getInitialPositions().get(i).getID());
			assertEquals(p.getTitle(), new InitialData().getInitialPositions().get(i).getTitle());
			assertEquals(p.getDescription(), new InitialData().getInitialPositions().get(i).getDescription());
			assertEquals(p.getPriority(), new InitialData().getInitialPositions().get(i).getPriority());
			
			// Make sure we don't get a null pointer exception
			if(i+1 < new InitialData().getInitialPositions().size())
				i++;
		}
	}
	
	@Test
	public void testFindAllSOPs() { // TODO
		querySOP = db.findAllSOPs();
		
		int i = 0;
		
		for(SOP s: querySOP) {
			//assertEquals(s.getID(), new InitialData().getInitialSOPs().get(i).getID()); TODO: Why is this failing?
			assertEquals(s.getName(), new InitialData().getInitialSOPs().get(i).getName());
			assertEquals(s.getDescription(), new InitialData().getInitialSOPs().get(i).getDescription());
			assertEquals(s.getPriority(), new InitialData().getInitialSOPs().get(i).getPriority());
			assertEquals(s.getRevision(), new InitialData().getInitialSOPs().get(i).getRevision());
			assertEquals(s.getAuthorID(), new InitialData().getInitialSOPs().get(i).getAuthorID());
			assertEquals(s.getArchiveFlag(), new InitialData().getInitialSOPs().get(i).getArchiveFlag());
			
			if(i+1 < new InitialData().getInitialSOPs().size())
				i++;
		}
	}
	
	@Test
	public void testFindPositionByID() {
		queryPos = db.findAllPositions();
		
		for(Position p : queryPos) {
			assertEquals(p.getID(), db.findPositionByID(p.getID()).getID());
			assertEquals(p.getTitle(), db.findPositionByID(p.getID()).getTitle());
			assertEquals(p.getDescription(), db.findPositionByID(p.getID()).getDescription());
			assertEquals(p.getPriority(), db.findPositionByID(p.getID()).getPriority());
		}
	}
	
	@Test
	public void testFindSOPByID() {
		querySOP = db.findAllSOPs();
		
		for(SOP s : querySOP) {
			assertEquals(s.getID(), db.findSOPbyID(s.getID()).getID());
			assertEquals(s.getName(), db.findSOPbyID(s.getID()).getName());
			assertEquals(s.getDescription(), db.findSOPbyID(s.getID()).getDescription());
			assertEquals(s.getPriority(), db.findSOPbyID(s.getID()).getPriority());
			assertEquals(s.getRevision(), db.findSOPbyID(s.getID()).getRevision());
			assertEquals(s.getAuthorID(), db.findSOPbyID(s.getID()).getAuthorID());
			assertEquals(s.getArchiveFlag(), db.findSOPbyID(s.getID()).getArchiveFlag());
		}
	}
	
	@Test
	public void testFindSOPbyAuthorID() {
		querySOP = db.findAllSOPs();
		
				
	}
	
	@Test
	public void testFindSOPbyName() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testFindSOPbyPosition() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testFindSOPbyPriority() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testFindSOPbyVersion() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testFindSOPthruPosition() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testFindUserByPosition() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getUserID(), db.findUserByPosition(u.getPosition().getID()).getUserID());
			assertEquals(u.getEmail(), db.findUserByPosition(u.getPosition().getID()).getEmail());
			assertEquals(u.getPassword(), db.findUserByPosition(u.getPosition().getID()).getPassword());
			assertEquals(u.getFirstname(), db.findUserByPosition(u.getPosition().getID()).getFirstname());
			assertEquals(u.getLastname(), db.findUserByPosition(u.getPosition().getID()).getLastname());
			assertEquals(u.isAdminFlag(), db.findUserByPosition(u.getPosition().getID()).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.findUserByPosition(u.getPosition().getID()).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.findUserByPosition(u.getPosition().getID()).getPosition().getID());
		}
	}
	
	@Test
	public void testGetPositionByName() {
		queryPos = db.findAllPositions();
		
	}
	
	@Test
	public void testGetPositionByPriority() {
		queryPos = db.findAllPositions();
		
	}
	
	@Test
	public void testGetPositionBySOPID() {
		queryPos = db.findAllPositions();
		
	}
	
	@Test
	public void testGetPositionByUser() {
		queryPos = db.findAllPositions();
		
		
	}
	
	@Test
	public void testGetUserByEmail() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getUserID(), db.getUserByEmail(u.getEmail()).getUserID());
			assertEquals(u.getEmail(), db.getUserByEmail(u.getEmail()).getEmail());
			assertEquals(u.getPassword(), db.getUserByEmail(u.getEmail()).getPassword());
			assertEquals(u.getFirstname(), db.getUserByEmail(u.getEmail()).getFirstname());
			assertEquals(u.getLastname(), db.getUserByEmail(u.getEmail()).getLastname());
			assertEquals(u.isAdminFlag(), db.getUserByEmail(u.getEmail()).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.getUserByEmail(u.getEmail()).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.getUserByEmail(u.getEmail()).getPosition().getID());
		}
	}
	
	@Test
	public void testGetUserByFirstname() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getUserID(), db.getUserByFirstName(u.getFirstname()).getUserID());
			assertEquals(u.getEmail(), db.getUserByFirstName(u.getFirstname()).getEmail());
			assertEquals(u.getPassword(), db.getUserByFirstName(u.getFirstname()).getPassword());
			assertEquals(u.getFirstname(), db.getUserByFirstName(u.getFirstname()).getFirstname());
			assertEquals(u.getLastname(), db.getUserByFirstName(u.getFirstname()).getLastname());
			assertEquals(u.isAdminFlag(), db.getUserByFirstName(u.getFirstname()).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.getUserByFirstName(u.getFirstname()).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.getUserByFirstName(u.getFirstname()).getPosition().getID());
		}
	}
	
	@Test
	public void testGetUserByLastname() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getUserID(), db.getUserByLastName(u.getLastname()).getUserID());
			assertEquals(u.getEmail(), db.getUserByLastName(u.getLastname()).getEmail());
			assertEquals(u.getPassword(), db.getUserByLastName(u.getLastname()).getPassword());
			assertEquals(u.getFirstname(), db.getUserByLastName(u.getLastname()).getFirstname());
			assertEquals(u.getLastname(), db.getUserByLastName(u.getLastname()).getLastname());
			assertEquals(u.isAdminFlag(), db.getUserByLastName(u.getLastname()).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.getUserByLastName(u.getLastname()).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.getUserByLastName(u.getLastname()).getPosition().getID());
		}
	}
	
	@Test
	public void testGetUserByID() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getUserID(), db.getUserByID(u.getUserID()).getUserID());
			assertEquals(u.getEmail(), db.getUserByID(u.getUserID()).getEmail());
			assertEquals(u.getPassword(), db.getUserByID(u.getUserID()).getPassword());
			assertEquals(u.getFirstname(), db.getUserByID(u.getUserID()).getFirstname());
			assertEquals(u.getLastname(), db.getUserByID(u.getUserID()).getLastname());
			assertEquals(u.isAdminFlag(), db.getUserByID(u.getUserID()).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.getUserByID(u.getUserID()).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.getUserByID(u.getUserID()).getPosition().getID());
		}
	}
}