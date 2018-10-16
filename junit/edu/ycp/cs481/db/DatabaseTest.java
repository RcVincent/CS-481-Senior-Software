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
	private Position p1, p2, p3;
	private SOP s1, s2, s3;
	private User u1, u2, u3;
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
		u1.setAdminFlag(true);
		u1.setArchiveFlag(false);
		u1.setPosition(db.findAllPositions().get(0));
		
		u2 = new User();
		u3 = new User();
		
		queryUser = new ArrayList<User>();

		p_size = new InitialData().getInitialPositions().size();
		u_size = new InitialData().getInitialUsers().size();
		s_size = new InitialData().getInitialSOPs().size();
	}
	
	@Test
	public void testLoadInitialData() {
		
	}
	
	@Test
	public void testInsertPosition() {
		
		for(Position p: pList) {
			db.insertPosition(p);
			queryPos = db.findAllPositions();
			assertEquals(p.getID() + p_size, queryPos.get(queryPos.size()-1).getID());
			assertEquals(p.getTitle(), queryPos.get(queryPos.size()-1).getTitle());
			assertEquals(p.getDescription(), queryPos.get(queryPos.size()-1).getDescription());
			assertEquals(p.getPriority(), queryPos.get(queryPos.size()-1).getPriority());
		}
	}
	
	@Test
	public void testInsertUser() {
		for(User u: uList) {
			db.insertUser(u);
			queryUser = db.findAllUsers();
			assertEquals(u.getUserID() + u_size, queryUser.get(queryUser.size()-1).getUserID());
			assertEquals(u.getEmail(), queryUser.get(queryUser.size()-1).getEmail());
			assertEquals(u.getPassword(), queryUser.get(queryUser.size()-1).getPassword());
			assertEquals(u.getFirstname(), queryUser.get(queryUser.size()-1).getFirstname());
			assertEquals(u.getLastname(), queryUser.get(queryUser.size()-1).getLastname());
			assertEquals(u.isAdminFlag(), queryUser.get(queryUser.size()-1).isAdminFlag());
			assertEquals(u.isArchiveFlag(), queryUser.get(queryUser.size()-1).isArchiveFlag());
			assertEquals(u.getPosition().getID(), queryUser.get(queryUser.size()-1).getPosition().getID());
		}
	}
	
	@Test
	public void testInsertSOP() {
		for(SOP s: sList) {
			db.insertSOP(s);
			querySOP = db.findAllSOPs();
			assertEquals(s.getID() + s_size, querySOP.get(querySOP.size()-1).getID());
			assertEquals(s.getName(), querySOP.get(querySOP.size()-1).getName());
			assertEquals(s.getDescription(), querySOP.get(querySOP.size()-1).getDescription());
			assertEquals(s.getPriority(), querySOP.get(querySOP.size()-1).getPriority());
			assertEquals(s.getRevision(), querySOP.get(querySOP.size()-1).getRevision());
			assertEquals(s.getAuthorID(), querySOP.get(querySOP.size()-1).getAuthorID());
			assertEquals(s.getArchiveFlag(), querySOP.get(querySOP.size()-1).getArchiveFlag());
		}
	}
	
	/// Testing for our initial data, to ensure that all of our inserts were parsed correctly \\\
   ///																						   \\\
	@Test
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
			assertEquals(queryPos.get(i).getID(), new InitialData().getInitialPositions().get(i).getID());
			assertEquals(queryPos.get(i).getTitle(), new InitialData().getInitialPositions().get(i).getTitle());
			assertEquals(queryPos.get(i).getDescription(), new InitialData().getInitialPositions().get(i).getDescription());
			assertEquals(queryPos.get(i).getPriority(), new InitialData().getInitialPositions().get(i).getPriority());
		}
	}
	
	@Test
	public void testFindAllSOPs() { 
		querySOP = db.findAllSOPs();
		
		for(int i = 0; i < s_size; i++) {
			assertEquals(querySOP.get(i).getID(), new InitialData().getInitialSOPs().get(i).getID());
			assertEquals(querySOP.get(i).getName(), new InitialData().getInitialSOPs().get(i).getName());
			assertEquals(querySOP.get(i).getDescription(), new InitialData().getInitialSOPs().get(i).getDescription());
			assertEquals(querySOP.get(i).getPriority(), new InitialData().getInitialSOPs().get(i).getPriority());
			assertEquals(querySOP.get(i).getRevision(), new InitialData().getInitialSOPs().get(i).getRevision());
			assertEquals(querySOP.get(i).getAuthorID(), new InitialData().getInitialSOPs().get(i).getAuthorID());
			assertEquals(querySOP.get(i).getArchiveFlag(), new InitialData().getInitialSOPs().get(i).getArchiveFlag());
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
		
		assertEquals(querySOP.get(0).getID(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getID());
		assertEquals(querySOP.get(0).getName(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPbyAuthorID(querySOP.get(0).getID()).get(0).getArchiveFlag());
	}
	
	@Test
	public void testFindSOPbyName() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getID(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getID());
		assertEquals(querySOP.get(0).getName(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPbyName(querySOP.get(0).getName()).get(0).getArchiveFlag());
	}
	
	@Test // TODO: Implement after PositionSOP
	public void testFindSOPbyPosition() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testFindSOPbyPriority() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getID(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getID());
		assertEquals(querySOP.get(0).getName(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPbyPriority(querySOP.get(0).getPriority()).get(0).getArchiveFlag());
	}
	
	@Test
	public void testFindSOPbyVersion() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getID(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getID());
		assertEquals(querySOP.get(0).getName(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPbyVersion(querySOP.get(0).getRevision()).get(0).getArchiveFlag());
	}
	
	@Test // TODO: Implement after PositionSOP
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
		
		for(int i = 0; i < p_size; i++) {
			assertEquals(queryPos.get(i).getID(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getID());
			assertEquals(queryPos.get(i).getTitle(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getTitle());
			assertEquals(queryPos.get(i).getDescription(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getDescription());
			assertEquals(queryPos.get(i).getPriority(), db.getPositionByName(queryPos.get(i).getTitle()).get(0).getPriority());
		}
	}
	
	@Test
	public void testGetPositionByPriority() {
		queryPos = db.findAllPositions();
		
		assertEquals(queryPos.get(0).getID(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getID());
		assertEquals(queryPos.get(0).getTitle(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getTitle());
		assertEquals(queryPos.get(0).getDescription(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getDescription());
		assertEquals(queryPos.get(0).getPriority(), db.getPositionByPriority(queryPos.get(0).getPriority()).get(0).getPriority());

	}
	
	@Test //TODO: After PositionSOP is implemented
	public void testGetPositionBySOPID() {
		queryPos = db.findAllPositions();
		querySOP = db.findAllSOPs();
		/*
		assertEquals(queryPos.get(0).getID(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getID());
		assertEquals(queryPos.get(0).getTitle(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getTitle());
		assertEquals(queryPos.get(0).getDescription(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getDescription());
		assertEquals(queryPos.get(0).getPriority(), db.getPositionBySOPID(querySOP.get(0).getID()).get(0).getPriority());*/
	}
	
	@Test
	public void testGetPositionByUser() {
		queryPos = db.findAllPositions();
		queryUser = db.findAllUsers();
		
		for(int i = 0; i < u_size; i++) {
			assertEquals(queryPos.get(i).getID(), db.getPositionOfUser(queryUser.get(i).getUserID()).getID());
			assertEquals(queryPos.get(i).getTitle(), db.getPositionOfUser(queryUser.get(i).getUserID()).getTitle());
			assertEquals(queryPos.get(i).getDescription(), db.getPositionOfUser(queryUser.get(i).getUserID()).getDescription());
			assertEquals(queryPos.get(i).getPriority(), db.getPositionOfUser(queryUser.get(i).getUserID()).getPriority());
		}
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
	
	@Test
	public void testDeletePosition() {
		assertEquals(false, db.deletePosition(10));
		// TODO: Foreign key constraints 
		// assertEquals(true, db.deletePosition(1));
	}
	
	@Test // TODO: this breaks testInsertPosition and testFindAllPositions //
	public void testChangePositionPriority() {
		/*queryPos = db.findAllPositions();
		int insert_id = db.insertPosition(queryPos.get(0));
		
		Position changedPriority = db.changePositionPriority(queryPos.get(insert_id - 1).getID(), 2);
		assertEquals(2, changedPriority.getPriority());
		*/
	}
}