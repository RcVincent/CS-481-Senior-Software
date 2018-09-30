package modelTests;
//import org.junit.before;

//import org.junit.*; 
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Position;
import model.User;
import model.SOP;

public class UserTest {
	private User user1, user2, user3, user4, user5, user6, user7;
	private User admin1, admin2; 
	private Position p1, p2, p3, a1, a2;
	private ArrayList<User> userlist, archivelist; 
	private ArrayList<Position> positionList; 
	
	@Before
	public void setUp() {

		userlist = new ArrayList<User>(); 


		user1 = new User(); 
		user1.setUserID(902685223);
		user1.setEmail("gmailsucks@bing.com");
		user1.setPassword("Pass");
		user1.setAdminFlag("user"); 
		user1.setArchiveFlag(false);

		p1 = new Position(); 
		user1.setPosition(p1);
		userlist.add(user1);

		user2 = new User(); 
		user2.setUserID(901678905);
		user2.setEmail("bingSucks@gmail.com");
		user2.setPassword("Yeet");
		user2.setAdminFlag("user"); 
		user2.setArchiveFlag(false);

		p2 = new Position(); 
		user2.setPosition(p3);
		userlist.add(user2); 

		user3 = new User(); 
		user3.setUserID(901678509);
		user3.setEmail("IamADegenerate@ycp.edu");
		user3.setPassword("No");
		user3.setAdminFlag("user");
		user3.setArchiveFlag(true);

		p3 = new Position(); 
		user3.setPosition(p3);
		userlist.add(user3); 

		user4 = new User(); 
		user4.setUserID(905678509);
		user4.setEmail(""); 
		user4.setPassword("lelelele");
		user4.setArchiveFlag(false);
		user4.setAdminFlag("user");

		user4.setPosition(p2);
		userlist.add(user4); 

		user5 = new User(); 
		user5.setUserID(900174502);
		user5.setEmail("yayayaya@mrc.org");
		user5.setPassword("");
		user5.setAdminFlag("user");
		user5.setArchiveFlag(false);

		user5.setPosition(p2);
		userlist.add(user5);

		user6 = new User(); 
		user6.setUserID(0);
		user6.setEmail("Yesnomaybeso@gmail.com");
		user6.setPassword("NNOOOOOOOOOO");

		user6.setPosition(p1);
		userlist.add(user6);

		user7 = new User(); 
		user7.setUserID(899245663);
		user7.setEmail("kbdack@hot.com");
		user7.setAdminFlag("user");
		user7.setArchiveFlag(false);

		user7.setPosition(null);
		userlist.add(user7);

		admin1 = new User(); 
		admin1.setUserID(124);
		admin1.setEmail("admin@email.com");
		admin1.setPassword("LetmeIn");
		admin1.setArchiveFlag(false);
		admin1.setAdminFlag("admin");

		a1 = new Position(); 
		admin1.setPosition(a1);

		userlist.add(admin1);

		admin2 = new User(); 
		admin2.setUserID(125);
		admin2.setEmail("admin2@email.com");
		admin2.setPassword("Now");
		admin2.setArchiveFlag(false);
		admin2.setAdminFlag("admin");

		a2 = new Position();
		admin2.setPosition(a2);

		userlist.add(admin2); 
		

	}
	
	@Test
	public void testValidEmail() {
		//prepare the failure lists
		List<User> failedTests = new ArrayList<User>(); 
		
		List<User> testlist = new ArrayList<User>(); 
		testlist.addAll(userlist);
		
		//sort through the userlist 
		for (User u : testlist) {
			//if archived immediately stop, there is no reason to search the list anymore 
			if(u.isArchiveFlag() == false) {
				
				//if the email is empty its invalid, invalidate that user and remove them from the user list. Also send their manager an email. 
				if(u.getEmail() == "" || u.getEmail() == " ") {
					u.setValidUser(false);
					System.out.println("There is an invalid user in the list: " + u.getEmail());
					userlist.remove(u);
					failedTests.add(u);
				}
			}
			else {
				//if the user was invalid, notify and remove them from the user list 
				System.out.println("This user is marked as archived. Please search the archive list for this information");
				userlist.remove(u);
				
				//if the user does not already exist in the archive, add them to it 
				/*if(!archivelist.contains(u)) {
					archivelist.add(u);
					
				}*/
			}
			
		}

		assertEquals(failedTests.size(), 1);  
		assertEquals(userlist.size(), 7);
		//assertEquals(archivelist.size(), 1); 


	}
	
	@Test
	public void testValidPassword() {
		//prepare the failure lists
				List<User> failedTests = new ArrayList<User>(); 
				List<User> testlist = new ArrayList<User>(); 
				
				testlist.addAll(userlist);
				
				//sort through the userlist 
				for (User u : testlist) {
					//if archived immediately stop, there is no reason to search the list anymore 
					if(u.isArchiveFlag() == false) {
						
						//if the email is empty its invalid, invalidate that user and remove them from the user list. Also send their manager an email. 
						if(u.getPassword() == "" || u.getPassword() == " ") {
							u.setValidUser(false);
							System.out.println("There is an invalid user in the list: " + u.getEmail());
							userlist.remove(u);
							failedTests.add(u);
						}
					}
					else {
						//if the user was invalid, notify and remove them from the user list 
						System.out.println("This user is marked as archived. Please search the archive list for this information");
						userlist.remove(u);
						
						//if the user does not already exist in the archive, add them to it 
						/*if(!archivelist.contains(u)) {
							archivelist.add(u);
							
						}*/
					}
					
				}
				
				assertEquals(failedTests.size(), 1);  
				assertEquals(userlist.size(), 7);
				//assertEquals(archivelist.size(), 1); 
	}

	@Test
	public void testValidID() {
		//prepare the failure lists
				List<User> failedTests = new ArrayList<User>(); 
				
				List<User> testlist = new ArrayList<User>(); 
				testlist.addAll(userlist);
				
				//sort through the userlist 
				for (User u : testlist) {
					//if archived immediately stop, there is no reason to search the list anymore 
					if(u.isArchiveFlag() == false) {
						
						//if the email is empty its invalid, invalidate that user and remove them from the user list. Also send their manager an email. 
						if(u.getUserID() <= 0) {
							u.setValidUser(false);
							System.out.println("There is an invalid user in the list: " + u.getEmail());
							userlist.remove(u);
							failedTests.add(u);
						}
					}
					else {
						//if the user was invalid, notify and remove them from the user list 
						System.out.println("This user is marked as archived. Please search the archive list for this information");
						userlist.remove(u);
						
						//if the user does not already exist in the archive, add them to it 
						/*if(!archivelist.contains(u)) {
							archivelist.add(u);
							
						}*/
					}
					
				}

				assertEquals(failedTests.size(), 1);  
				assertEquals(userlist.size(), 7);
				//assertEquals(archivelist.size(), 1); 


	}

	public void testArchive() {
		
	}
	
	public void testPositionValidity() {
		
	}
	
	public void viewPositionSOPListTest() {
		
	}
	
	public void testFindAdmins() {
		
	}
}


