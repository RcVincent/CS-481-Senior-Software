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
	private User user1, user2, user3; 
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
	
	public void testChangeEmail() {
		
	}
	
	public void testChangePassword() {
		
	}
	
	public void testChangePosition() {
		
	}
	
	public void testSearchByFName() {
		
	}
	
	public void testSearchByLName() {
		
	}
	
	public void testSearchByFUllName() {
		
	}
	
	public void testSearchByID() {
		
	}
	
	
}
