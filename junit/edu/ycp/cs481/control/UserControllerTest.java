package edu.ycp.cs481.control;

import org.junit.*;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.User;

public class UserControllerTest {
	private User u1, u2, u3;
	private UserController controller;
	
	@Before
	public void setUp(){
		u1 = new User();
		u2 = new User();
		u3 = new User();
		
		u1.setEmail("CEO@Google.com");
		u2.setEmail("BadEmail@@ycp.edu");
		u3.setEmail("email");
		
		
		controller = new UserController();
	}
	
	@Test
	public void testValidateEmail() {
		// TODO:assertEquals(false, controller.validateEmail(u1.getEmail()));
	}
}
