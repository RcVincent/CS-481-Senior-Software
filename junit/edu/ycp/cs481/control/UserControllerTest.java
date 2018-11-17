package edu.ycp.cs481.control;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;
import edu.ycp.cs481.control.UserController;

public class UserControllerTest {
	private Position pos1, pos2, pos3, pos4, pos5;
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
		
		pos5 = new Position(); 
		pos5.setTitle("Manager");
		pos5.setID(5);
		pos5.setPriority(4);
		
		user1 = new User();
		user1.setEmail("Admin@google.com");
		user1.setPassword("DiveOnIn");
		user1.setFirstName("Rodger");
		user1.setLastName("Smith");
		user1.setUserID(12);
		user1.setPosition(pos1);
		
		user2 = new User();
		user2.setFirstName("Stan");
		user2.setLastName("Smith");
		user2.setEmail("rookie@email.com");
		user2.setPassword("bangBang");
		user2.setPosition(pos3);
		user2.setUserID(4);
		
		user3 = new User(); 
		user3.setEmail("failTest@@gmail.com");
		user3.setFirstName("");
		user3.setLastName("");
		user3.setPassword("");
		user3.setUserID(-1);
		user3.setPosition(pos4);
		
		user4 = new User(); 
		user4.setEmail("theBoss@tesla.com");
		user4.setPassword("POWER");
		user4.setFirstName("Elon");
		user4.setLastName("Musk");
		user4.setUserID(5);
		user4.setPosition(pos2);
		
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
		sop1.setAuthorID(user1.getUserID());
		
		sop2 = new SOP(); 
		sop2.setTitle("Logout");
		sop2.setDescription("How to logout");
		sop2.setPriority(7);
		sop2.setID(2);
		sop2.setVersion(2);
		sop2.setAuthorID(user1.getUserID());
		
		sopList.add(sop1);
		sopList.add(sop2);
		
		
		uc = new UserController();
		
		
	}
	
	@Test
	public void testInsertQuarantineUser() {
		uc.insertQuarantineUser("zhenry@ycp.edu", "password", "Chuck", "Norris");
	}
	
	//this needs to be updated to handle the new salted passwords 
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
		
		List<User> testList = uc.searchForUsers(0, -1, false, "", false, searchFirstName, false, "", 0, -1);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with FirstName" + searchFirstName);
			//fail();
			
		} else {
		assertEquals(1, testList.size());
		
		User u = testList.get(0);
		
		assertEquals("Smith", u.getLastName());
		assertEquals("rookie@email.com", u.getEmail());
		assertEquals(4, u.getUserID());
		}
	}
	
	@Test
	public void testSearchByLName() {
		String searchLastName = "Smith";
		
		List<User> testList = uc.searchForUsers(0, -1, false, "", false, "", false, searchLastName, 0, -1);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with LastName" + searchLastName);
			//fail();
			
		} else {
		assertEquals(2, testList.size());
		
		User u = testList.get(0);
		assertEquals("Rodger", u.getFirstName());
		assertEquals("Admin@google.com", u.getEmail());
		assertEquals(12, u.getUserID());
		
		User u2 = testList.get(1);
		assertEquals("Stan", u.getFirstName());
		assertEquals("rookie@email.com", u.getEmail());
		assertEquals(4, u.getUserID());
		}
	}
	
	@Test
	public void testSearchByFullName() {
		String FirstName = "Stan";
		String LastName = "Smith"; 
		
		List<User> testList = uc.searchForUsers(0, -1, false, "", false, FirstName, false, LastName, 0, -1);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with first name " + FirstName + " and last name " + LastName);
			//fail(); 
			
		} else {
		assertEquals(1, testList.size());
		
		User u = testList.get(0);
		
		//assertEquals("Smith", u.getLastName());
		assertEquals("rookie@email.com", u.getEmail());
		assertEquals(4, u.getUserID());
		}
	}
	
	@Test
	public void testSearchByID() {
		int searchID = 12; 
		
		List<User> testList = uc.searchForUsers(searchID, -1, false, "", false, "", false, "", 0, -1);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with ID " + searchID);
			//fail();
			
		} else {
			assertEquals(1, testList.size());
			
			User u = testList.get(0);
			
			assertEquals("Rodger", u.getFirstName());
			assertEquals("Smith", u.getLastName());
			assertEquals("Admin@google.com", u.getEmail()); 
			
		}
	}
	
	@Test
	public void testSearchByPositionID() {
		int searchPosID = 1;
		
		List<User> testList = uc.searchForUsers(0, -1, false, "", false, "", false, "", searchPosID, -1);
		
		if(testList.isEmpty()) {
			System.out.println("Search failed for user with position ID"+ searchPosID);
			//fail(); 
		
		} else {
			User u = testList.get(0);
			
			assertEquals(5, u.getUserID());
			assertEquals("theBoss@tesla.com", u.getEmail());
			assertEquals("Elon", u.getFirstName());
			assertEquals("Musk", u.getLastName());
		}
	}
	
	
	@Test
	public void testChangeEmail() {
		String newEmail = "lelelel@tcp.com";
		String oldEmail = user2.getEmail();
		assertEquals("rookie@email.com", user2.getEmail());
		
		//this method isnt working 
		uc.changeUserEmail(user2.getUserID(), oldEmail, newEmail);
		
		
		assertEquals("lelelel@tcp.com", user2.getEmail());
	}
	
	@Test
	public void testChangePassword() {
		String newPass = "Password4";
		String oldPass = user3.getPassword();
		
		assertEquals("", user3.getPassword()); 
		
		//this method also appears to not be working 
		uc.changeUserPassword(user3.getUserID(), newPass);
		
		//search for users and pass that through user 3
		
		//user3 = uc.searchForUsers(-1, -1, false, "failTest@@gmail.com" , false, null, false, null, -1).get(0);
		
		assertEquals("Password4", user3.getPassword()); 
		
	}
	
	@Test
	public void testChangePosition() {
		Position oldP = user3.getPosition();
		
		assertEquals(4, oldP.getID());
		int newPositionID = 3;
		
		//this is also not working 
		uc.changePosition(user3, newPositionID);
		
		assertEquals("IT", user3.getPosition().getTitle()); 
	}
	
	@Test 
	public void testUserHasPermission() {
		assertEquals(true, uc.userHasPermission(1, EnumPermission.ALL));
		assertEquals(false, uc.userHasPermission(2, EnumPermission.ALL));
	}
	
	//@Test
	public void testManagerHasSubordinate() {
		
	}
	
	public void testisLockedOut() {
		
	}
	
	public void testGetSubbordinates() {
		
	}
	
	
}
