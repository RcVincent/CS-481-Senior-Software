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
	
	@Test
	public void testInsertPosition() {
		
		for(Position p: pList) {
			db.insertPosition(p);
			queryPos = db.findAllPositions();
			assertEquals(p.getTitle(), queryPos.get(queryPos.size()-1).getTitle());
			assertEquals(p.getDescription(), queryPos.get(queryPos.size()-1).getDescription());
			assertEquals(p.getPriority(), queryPos.get(queryPos.size()-1).getPriority());
		}
	}
	
	@Test
	public void testInsertSOP() {
		for(SOP s: sList) {
			db.insertSOP(s);
			querySOP = db.findAllSOPs();
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
	}
	
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
	public void testFindSOPByID() {
		querySOP = db.findAllSOPs();
		
		for(SOP s : querySOP) {
			assertEquals(s.getName(), db.findSOPbyID(s.getID()).getName());
			assertEquals(s.getDescription(), db.findSOPbyID(s.getID()).getDescription());
			assertEquals(s.getPriority(), db.findSOPbyID(s.getID()).getPriority());
			assertEquals(s.getRevision(), db.findSOPbyID(s.getID()).getRevision());
			assertEquals(s.getAuthorID(), db.findSOPbyID(s.getID()).getAuthorID());
			assertEquals(s.getArchiveFlag(), db.findSOPbyID(s.getID()).getArchiveFlag());
		}
	}
	
	@Test
	public void testfindSOPsByAuthorID() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getName(), db.findSOPsByAuthorID(querySOP.get(0).getID()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPsByAuthorID(querySOP.get(0).getID()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPsByAuthorID(querySOP.get(0).getID()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPsByAuthorID(querySOP.get(0).getID()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPsByAuthorID(querySOP.get(0).getID()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPsByAuthorID(querySOP.get(0).getID()).get(0).getArchiveFlag());
	}
	
	@Test
	public void testfindSOPsByTitle() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getName(), db.findSOPsByTitle(querySOP.get(0).getName()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPsByTitle(querySOP.get(0).getName()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPsByTitle(querySOP.get(0).getName()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPsByTitle(querySOP.get(0).getName()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPsByTitle(querySOP.get(0).getName()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPsByTitle(querySOP.get(0).getName()).get(0).getArchiveFlag());
	}
	
	@Test // TODO: Implement after PositionSOP
	public void testFindSOPbyPosition() {
		querySOP = db.findAllSOPs();
	}
	
	@Test
	public void testfindSOPsByPriority() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getName(), db.findSOPsByPriority(querySOP.get(0).getPriority()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPsByPriority(querySOP.get(0).getPriority()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPsByPriority(querySOP.get(0).getPriority()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPsByPriority(querySOP.get(0).getPriority()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPsByPriority(querySOP.get(0).getPriority()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPsByPriority(querySOP.get(0).getPriority()).get(0).getArchiveFlag());
	}
	
	@Test
	public void testfindSOPsByVersion() {
		querySOP = db.findAllSOPs();
		
		assertEquals(querySOP.get(0).getName(), db.findSOPsByVersion(querySOP.get(0).getRevision()).get(0).getName());
		assertEquals(querySOP.get(0).getDescription(), db.findSOPsByVersion(querySOP.get(0).getRevision()).get(0).getDescription());
		assertEquals(querySOP.get(0).getPriority(), db.findSOPsByVersion(querySOP.get(0).getRevision()).get(0).getPriority());
		assertEquals(querySOP.get(0).getRevision(), db.findSOPsByVersion(querySOP.get(0).getRevision()).get(0).getRevision());
		assertEquals(querySOP.get(0).getAuthorID(), db.findSOPsByVersion(querySOP.get(0).getRevision()).get(0).getAuthorID());
		assertEquals(querySOP.get(0).getArchiveFlag(), db.findSOPsByVersion(querySOP.get(0).getRevision()).get(0).getArchiveFlag());
	}
	
	/*@Test // TODO: Implement after PositionSOP
	public void testFindSOPthruPosition() {
		querySOP = db.findAllSOPs();
	}*/
	
	@Test
	public void testFindUserByPosition() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getEmail(), db.findUsersWithPosition(u.getPosition().getID()).get(0).getEmail());
			assertEquals(u.getPassword(), db.findUsersWithPosition(u.getPosition().getID()).get(0).getPassword());
			assertEquals(u.getFirstname(), db.findUsersWithPosition(u.getPosition().getID()).get(0).getFirstname());
			assertEquals(u.getLastname(), db.findUsersWithPosition(u.getPosition().getID()).get(0).getLastname());
			assertEquals(u.isAdminFlag(), db.findUsersWithPosition(u.getPosition().getID()).get(0).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.findUsersWithPosition(u.getPosition().getID()).get(0).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.findUsersWithPosition(u.getPosition().getID()).get(0).getPosition().getID());
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
			assertEquals(queryPos.get(i).getTitle(), db.getPositionOfUser(queryUser.get(i).getUserID()).getTitle());
			assertEquals(queryPos.get(i).getDescription(), db.getPositionOfUser(queryUser.get(i).getUserID()).getDescription());
			assertEquals(queryPos.get(i).getPriority(), db.getPositionOfUser(queryUser.get(i).getUserID()).getPriority());
		}
	}
	
	@Test
	public void testGetUserByEmail() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
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
			assertEquals(u.getEmail(), db.getUsersByFirstName(u.getFirstname()).get(0).getEmail());
			assertEquals(u.getPassword(), db.getUsersByFirstName(u.getFirstname()).get(0).getPassword());
			assertEquals(u.getFirstname(), db.getUsersByFirstName(u.getFirstname()).get(0).getFirstname());
			assertEquals(u.getLastname(), db.getUsersByFirstName(u.getFirstname()).get(0).getLastname());
			assertEquals(u.isAdminFlag(), db.getUsersByFirstName(u.getFirstname()).get(0).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.getUsersByFirstName(u.getFirstname()).get(0).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.getUsersByFirstName(u.getFirstname()).get(0).getPosition().getID());
		}
	}
	
	@Test
	public void testGetUserByLastname() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
			assertEquals(u.getEmail(), db.getUsersByLastName(u.getLastname()).get(0).getEmail());
			assertEquals(u.getPassword(), db.getUsersByLastName(u.getLastname()).get(0).getPassword());
			assertEquals(u.getFirstname(), db.getUsersByLastName(u.getLastname()).get(0).getFirstname());
			assertEquals(u.getLastname(), db.getUsersByLastName(u.getLastname()).get(0).getLastname());
			assertEquals(u.isAdminFlag(), db.getUsersByLastName(u.getLastname()).get(0).isAdminFlag());
			assertEquals(u.isArchiveFlag(), db.getUsersByLastName(u.getLastname()).get(0).isArchiveFlag());
			assertEquals(u.getPosition().getID(), db.getUsersByLastName(u.getLastname()).get(0).getPosition().getID());
		}
	}
	
	@Test
	public void testGetUserByID() {
		queryUser = db.findAllUsers();
		
		for(User u: queryUser) {
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
		db.deletePosition(3);
		queryPos = db.findAllPositions();

		assertNotEquals(queryPos.get(2).getID(), pList.get(2).getID());
		assertNotEquals(queryPos.get(2).getDescription(), pList.get(2).getDescription());
		assertNotEquals(queryPos.get(2).getTitle(), pList.get(2).getTitle());
	}
	
	@Test // TODO: this breaks testInsertPosition and testFindAllPositions //
	public void testChangePositionPriority() {
		/*queryPos = db.findAllPositions();
		int insert_id = db.insertPosition(queryPos.get(0));
		
		Position changedPriority = db.changePositionPriority(queryPos.get(insert_id - 1).getID(), 2);
		assertEquals(2, changedPriority.getPriority());
		*/
	}
	
	@Test
	public void testChangeUserPassword() {
		
	}
	
	@Test
	public void testChangeUserEmail() {
		
	}
	
	@Test
	public void testArchiveUser() {
		
	}
	
	@Test
	public void testArchiveSOP() {
		
	}
	
	@Test
	public void testChangePosition() {
		
	}
	
	@Test
	public void testRevertSOP() {
		
	}
	
	@Test
	public void testAddSOPtoPosition() {
		
	}
	
	@Test
	public void testChangeSOPPriority() {
		
	}
}