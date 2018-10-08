package controllerTests;

import org.junit.*;
import model.User;
import controller.UserController;

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
