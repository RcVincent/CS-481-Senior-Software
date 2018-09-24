package modelTests;
//import org.junit.before;

import org.junit.*; 
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import model.Position;
import model.User;

public class UserTest {
	private User user1, user2, user3, user4;
	private User admin1, admin2; 
	private Position p1, p2, p3, a1, a2;
	 
	
	@Before
	public void setUp() {
	
		
		user1 = new User(); 
		user1.setEmail("gmailsucks@bing.com");
		user1.setPassword("Pass");
		user1.setAdminFlag(false); 
		user1.setArchiveFlag(false);
		
		p1 = new Position(); 
		user1.setP(p1);
		
		user2 = new User(); 
		user2.setEmail("bingSucks@gmail.com");
		user2.setPassword("Yeet");
		user2.setAdminFlag(false); 
		user2.setArchiveFlag(false);
		
		p2 = new Position(); 
		user2.setP(p2);
		
		user3 = new User(); 
		user3.setEmail("IamADegenerate@ycp.edu");
		user3.setPassword("No");
		user3.setAdminFlag(false);
		user3.setArchiveFlag(true);
		
		p3 = new Position(); 
		user3.setP(p3);
		
		
		user4 = new User(); 
		user4.setEmail(""); 
		user4.setPassword("lelelele");
		user4.setArchiveFlag(false);
		user4.setAdminFlag(false);
		
		admin1 = new User(); 
		admin2 = new User(); 
	}
}


