package edu.ycp.cs481.control;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;
import edu.ycp.cs481.control.UserController;

public class UserControllerTest {
	private Position pos1, pos2, pos3, pos4;
	private SOP sop1, sop2;
	private User user1, user2, user3, user4; 
	private List<Position> positionList; 
	private List<SOP> sopList;
	private List<User> userList;
	private UserController uc;
	
	@Before
	public void setUp(){
		positionList = new ArrayList<Position>(); 
		sopList = new ArrayList<SOP>(); 
		userList = new ArrayList<User>(); 
		
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
		user1.setPassword("DiveOnIn");
		user1.setFirstname("Rodger");
		user1.setLastname("Smith");
		user1.setUserID(12);
		user1.setPosition(pos1);
		user1.setAdminFlag(true);
		
		user2 = new User();
		user2.setFirstname("Stan");
		user2.setLastname("Smith");
		user2.setEmail("rookie@email.com");
		user2.setAdminFlag(false);
		user2.setPassword("bangBang");
		user2.setPosition(pos3);
		user2.setUserID(4);
		
		user3 = new User(); 
		user3.setEmail("failTest@@gmail.com");
		user3.setFirstname("");
		user3.setLastname("");
		user3.setAdminFlag(false);
		user3.setPassword("");
		user3.setUserID(-1);
		user3.setPosition(pos4);
		
		user4 = new User(); 
		user4.setAdminFlag(true);
		user4.setEmail("theBoss@tesla.com");
		user4.setPassword("POWER");
		user4.setFirstname("Elon");
		user4.setLastname("Musk");
		user4.setUserID(5);
		user4.setPosition(pos2);
		
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
		
		
		uc = new UserController();
		
		
	}
	
	@Test
	public void testValidateEmail() {
		assertEquals(true, uc.validateEmail(user1.getEmail()));
		assertEquals(true, uc.validateEmail(user2.getEmail()));
		assertEquals(false, uc.validateEmail(user3.getEmail()));
	}
	
	@Test
	public void testvalidateLogin() {
		String pass1 = "DiveOnIn";
		String pass2 = "bangBang";
		
		boolean firstTest = uc.authenticate(user2, pass1);
		assertFalse(firstTest);
		
		boolean secondTest = uc.authenticate(user1, pass2);
		assertFalse(secondTest); 
		
		//set a login setup 
		//if authenticate login = true
		
	}
	
	// TODO: Rework searches to test searchForUsers
	
	@Test
	public void testSearchByFName() {
		String searchFirstName = "Stan";
		
		List<User> testList = uc.searchForUsers(0, "", searchFirstName, "", 0);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with firstname" + searchFirstName);
			fail();
			
		} else {
		assertEquals(1, testList.size());
		
		User u = testList.get(0);
		
		assertEquals("Smith", u.getLastname());
		assertEquals("rookie@email.com", u.getEmail());
		assertEquals(4, u.getUserID());
		}
	}
	
	@Test
	public void testSearchByLName() {
		String searchLastName = "Smith";
		
		List<User> testList = uc.searchForUsers(0, "", "", searchLastName, 0);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with lastname" + searchLastName);
			fail();
			
		} else {
		assertEquals(2, testList.size());
		
		User u = testList.get(0);
		assertEquals("Rodger", u.getFirstname());
		assertEquals("Admin@google.com", u.getEmail());
		assertEquals(12, u.getUserID());
		
		User u2 = testList.get(1);
		assertEquals("Stan", u.getFirstname());
		assertEquals("rookie@email.com", u.getEmail());
		assertEquals(4, u.getUserID());
		}
	}
	
	@Test
	public void testSearchByFUllName() {
		String firstname = "Stan";
		String lastname = "Smith"; 
		
		List<User> testList = uc.searchForUsers(0, "", firstname, lastname, 0);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with first name " + firstname + " and last name " + lastname);
			fail(); 
			
		} else {
		assertEquals(1, testList.size());
		
		User u = testList.get(0);
		
		//assertEquals("Smith", u.getLastname());
		assertEquals("rookie@email.com", u.getEmail());
		assertEquals(4, u.getUserID());
		}
	}
	
	@Test
	public void testSearchByID() {
		int searchID = 12; 
		
		List<User> testList = uc.searchForUsers(searchID, "", "", "", 0);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with ID " + searchID);
			fail();
			
		} else {
			assertEquals(1, testList.size());
			
			User u = testList.get(0);
			
			assertEquals("Rodger", u.getFirstname());
			assertEquals("Smith", u.getLastname());
			assertEquals("Admin@google.com", u.getEmail()); 
			
		}
	}
	
	@Test
	public void testSearchByPositionID() {
		int searchPosID = 1;
		
		List<User> testList = uc.searchForUsers(0, "", "", "", searchPosID);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with position ID"+ searchPosID);
			fail(); 
		
		} else {
			User u = testList.get(0);
			
			assertEquals(5, u.getUserID());
			assertEquals("theBoss@tesla.com", u.getEmail());
			assertEquals("Elon", u.getFirstname());
			assertEquals("Musk", u.getLastname());
		}
	}
	
	// TODO: Rework these (potentially) for searchForUsers?
	/*
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
	 */
	
	@Test
	public void testChangeEmail() {
		String newEmail = "lelelel@tcp.com";
		String oldEmail = user2.getEmail();
		assertEquals("rookie@email.com", user2.getEmail());
		
		if(uc.validateEmail(newEmail)) {
			uc.changeUserEmail(user2.getUserID(), oldEmail, newEmail);
		}
		
		assertEquals("lelelel@tcp.com", user2.getEmail());
	}
	
	@Test
	public void testChangePassword() {
		String newPass = "Password4";
		String oldPass = user3.getPassword();
		
		assertEquals("", user3.getPassword()); 
		uc.changeUserPassword(user3.getUserID(), newPass);
		
		assertEquals("Password4", user3.getPassword()); 
		
	}
	
	@Test
	public void testChangePosition() {
		Position oldP = user3.getPosition();
		
		assertEquals(4, oldP.getID());
		int newPositionID = 3;
		uc.changePosition(user3, newPositionID);
		
		assertEquals("IT", user3.getPosition().getTitle()); 
	}
	
	/* TODO: Rework to use here
	@Test
	public void testInsertUser() {
		for(User u: uList) {
			db.insertUser(u);
			queryUser = db.findAllUsers();
			assertEquals(u.getEmail(), queryUser.get(queryUser.size()-1).getEmail());
			assertEquals(u.getPassword(), queryUser.get(queryUser.size()-1).getPassword());
			assertEquals(u.getFirstname(), queryUser.get(queryUser.size()-1).getFirstname());
			assertEquals(u.getLastname(), queryUser.get(queryUser.size()-1).getLastname());
			assertEquals(u.isAdminFlag(), queryUser.get(queryUser.size()-1).isAdminFlag());
			assertEquals(u.isArchiveFlag(), queryUser.get(queryUser.size()-1).isArchiveFlag());
			assertEquals(2, queryUser.get(queryUser.size()-1).getPosition().getID());
		}
	}*/
	
	/* TODO: Implement these here?
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
	public void testChangePosition() {
		
	}
	 */
}
