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
	private User user1, user2, user3, user4, user5, user6, user7, user8;
	private User admin1, admin2, admin3; 
	private Position p1, p2, p3, a1, a2, p4;
	private ArrayList<User> userlist, archivelist; 
	private ArrayList<Position> positionList;
	private SOP s1, s2, s3, s4, s5, s6; 
	private ArrayList<SOP> sopList; 

	@Before
	public void setUp() {

		userlist = new ArrayList<User>(); 
		archivelist = new ArrayList<User>(); 
		positionList = new ArrayList<Position>();

		user1 = new User(); 
		user1.setUserID(902685223);
		user1.setEmail("gmailsucks@bing.com");
		user1.setPassword("Pass");
		user1.setAdminFlag("user"); 
		user1.setArchiveFlag(false);

		p1 = new Position(); 
		p1.setTitle("Intern");
		p1.setID(21);
		p1.setPriority(9);


		user1.setPosition(p1);
		userlist.add(user1);

		user2 = new User(); 
		user2.setUserID(901678905);
		user2.setEmail("bingSucks@gmail.com");
		user2.setPassword("Yeet");
		user2.setAdminFlag("user"); 
		user2.setArchiveFlag(false);

		p2 = new Position(); 
		p2.setID(19);
		p2.setTitle("Manager");
		p2.setPriority(5);

		user2.setPosition(p3);
		userlist.add(user2); 

		user3 = new User(); 
		user3.setUserID(901678509);
		user3.setEmail("IamADegenerate@ycp.edu");
		user3.setPassword("No");
		user3.setAdminFlag("user");
		user3.setArchiveFlag(true);

		p3 = new Position(); 
		p3.setID(14);
		p3.setTitle("Janitor");
		p3.setPriority(10);


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
		
		//this user is for testing valid position requirements only
		user8 = new User();
		user8.setPosition(p4);
		
		admin1 = new User(); 
		admin1.setUserID(124);
		admin1.setEmail("admin@email.com");
		admin1.setPassword("LetmeIn");
		admin1.setArchiveFlag(false);
		admin1.setAdminFlag("admin");

		a1 = new Position(); 
		a1.setID(1);
		a1.setPriority(1);
		a1.setTitle("Auditor");
		admin1.setPosition(a1);

		userlist.add(admin1);

		admin2 = new User(); 
		admin2.setUserID(125);
		admin2.setEmail("admin2@email.com");
		admin2.setPassword("Now");
		admin2.setArchiveFlag(false);
		admin2.setAdminFlag("admin");

		a2 = new Position();
		a2.setID(2);
		a2.setPriority(1);
		a2.setTitle("Assistant Auditor");
		admin2.setPosition(a2);

		userlist.add(admin2); 

		admin3 = new User(); 
		admin3.setUserID(126); 
		admin3.setEmail("");
		admin3.setPassword("OpenUP");
		admin3.setArchiveFlag(false);
		admin3.setAdminFlag("Admin");

		admin3.setPosition(null);
		userlist.add(admin3); 

		p4 = new Position(); 
		p4.setTitle("");
		p4.setPriority(5);
		p4.setID(10);

		//get the SOPs ready 
		s1 = new SOP("How to sign your name", "Lets be honest here", 1, 1, 124, 3, false); 
		s2 = new SOP("How to sign into your machine", "Yes we are serious", 2, 2, 125, 5, false);
		s3 = new SOP("How to flush a toilet", "Some of ya'll nasty", 6, 33, 124, 1, false); 
		s4 = new SOP("How to sigh out", "Can't have you lot logged in all the time", 3, 55, 124, 2, false); 
		s5 = new SOP("Lelelele", "To annoy the flying Duchman", 8, 68, 125, 4, false); 
		s6 = new SOP(null, null, -1, 0, 0, 0, false); 

		//add them to the sop list
		sopList = new ArrayList<SOP>(); 
		sopList.add(s1);
		sopList.add(s2);
		sopList.add(s3);
		sopList.add(s4);
		sopList.add(s5);
		sopList.add(s6);


		//set position to SOP requirements 
		List<SOP> req1 = new ArrayList<SOP>();
		List<SOP> req2 = new ArrayList<SOP>();
		List<SOP> req3 = new ArrayList<SOP>();
		List<SOP> fail = new ArrayList<SOP>(); 

		req1.add(s1);
		req1.add(s2);
		req1.add(s3);

		req2.add(s1);
		req2.add(s3);
		req2.add(s4);

		req3.add(s1);
		req3.add(s4);
		req3.add(s5);

		fail.add(s6);

		p1.setRequirements(req1);
		p2.setRequirements(req2);
		p3.setRequirements(req3);

		p4 = new Position(); 
		p4.setRequirements(fail);

		a1.setRequirements(req1);
		a2.setRequirements(fail);

		positionList.add(p1);
		positionList.add(p2);
		positionList.add(p3);
		positionList.add(p4);
		positionList.add(a1);
		positionList.add(a2);
	}

	@Test
	public void testEmailGet() {
		String email = user1.getEmail();
		assertEquals("gmailsucks@bing.com", email); 
	}

	@Test
	public void testGetPassword() {
		String pass = user2.getPassword(); 
		assertEquals("Yeet", pass); 
	}

	@Test
	public void testGetID() {
		int id = user3.getUserID();
		assertEquals(901678509, id); 
	}

	@Test
	public void TestGetPosition() {
		Position p = admin1.getPosition(); 
		assertEquals(p.getID(), 1);
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
				if(u.getEmail() == "" || u.getEmail() == " " && (u.isAdminFlag() == "user" || u.isAdminFlag() == "User")) {
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
				if(!archivelist.contains(u)) {
					archivelist.add(u);

				}
			}

		}

		assertEquals(failedTests.size(), 2);  
		assertEquals(userlist.size(), 7);
		assertEquals(archivelist.size(), 1); 


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
				if(!archivelist.contains(u)) {
					archivelist.add(u);

				}
			}

		}

		assertEquals(failedTests.size(), 1);  
		assertEquals(userlist.size(), 8);
		assertEquals(archivelist.size(), 1); 
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

				//if the user id is empty its invalid, invalidate that user and remove them from the user list. Also send their manager an email. 
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
				if(!archivelist.contains(u)) {
					archivelist.add(u);

				}
			}

		}

		assertEquals(failedTests.size(), 1);  
		assertEquals(userlist.size(), 8);
		assertEquals(archivelist.size(), 1); 


	}

	public void testArchive() {

	}

	//@Test
	public void testPositionValidity() {
		List<User> testList = new ArrayList<User>(); 
		List<Position> failList = new ArrayList<Position>(); 

		testList.addAll(userlist);

		for(User u: testList) {
			if(u.isArchiveFlag() == false) {

				Position p = u.getPosition(); 
				if(p.getTitle() == "" || p.getTitle()== " ") {
					System.out.println("Position with id " +p.getID() + "has an invalid name, removing it from the active system until the error is fixed");
					positionList.remove(p);
					failList.add(p);
				}
				else if(p.getID() == 0 || p.getID() < 0) {
					System.out.println("Position with title " +p.getTitle() + "has an invalid ID, removing it from the active system until the error is fixed");
					positionList.remove(p);
					failList.add(p);
				}
				else if(p.getPriority() == 0) {
					System.out.println("Position with title " +p.getTitle() + "has an invalid priority, removing it from the active system until the error is fixed");
					positionList.remove(p);
					failList.add(p);
				}
					
			} else {
				//if the user was invalid, notify and remove them from the user list 
				System.out.println("This user is marked as archived. Please search the archive list for this information");
				userlist.remove(u);

				//if the user does not already exist in the archive, add them to it 
				if(!archivelist.contains(u)) {
					archivelist.add(u);

				}
			
		}
		}
		assertEquals(1, failList.size());
		assertEquals(5, positionList.size()); 
		
	}


	@Test
	public void viewPositionSOPListTest() {
		List<SOP> testlist = admin1.getPosition().getRequirements();
		
		assertEquals(3, testlist.size());

		SOP test = admin1.getPosition().getRequirements().get(0);
		
		assertEquals(1, test.getID());

	}
	
	//@Test
	public void testValidRequirements() {
		Position test = user7.getPosition();  
		
		assertNull(test);
		
		Position test2 = user8.getPosition();
		
		List<SOP> testlist = test2.getRequirements();
		
		for(SOP s: testlist) {
			if(s.getID()<= 0) {
				System.out.println("This position han an invalid SOP, marking this position as inactive until that error is resolved");
				test2.setValid(false);
				
			}
			else if(s.getName() == "" || s.getName() == " ") {
				System.out.println("This position han an invalid SOP, marking this position as inactive until that error is resolved");
				test2.setValid(false);
			}
			else if(s.getPriority() <= 0) {
				System.out.println("This position han an invalid SOP, marking this position as inactive until that error is resolved");
				test2.setValid(false);
			}
			else if(s.getRevision() <= 0) {
				System.out.println("This position han an invalid SOP, marking this position as inactive until that error is resolved");
				test2.setValid(false);
				
			}
			
		}
		
		assertFalse(test2.isValid());
	}
	@Test
	public void testFindAdmins() {
		List<User> adminList = new ArrayList<User>(); 
		List<User> failedAdminList = new ArrayList<User>(); 
		List<User> testlist = new ArrayList<User>(); 

		testlist.addAll(userlist);

		//sort through the user list to find the admins 
		for(User u: testlist) {
			//if archived immediately stop, there is no reason to search the list anymore
			if(u.isArchiveFlag() == false) {
				//if the user is an admin 
				if(u.isAdminFlag() == "admin" || u.isAdminFlag() == "Admin") { 
					//make sure the email is 
					if(u.getEmail()== "" || u.getEmail() == " ") {
						failedAdminList.add(u); 
						adminList.remove(u);
						System.out.println("There was an invalid admin and they have been removed");
					}
					else {
						adminList.add(u);
					}
				}
				else {
					//if the user was invalid, notify and remove them from the user list 
					System.out.println("This user is marked as archived. Please search the archive list for this information");
					userlist.remove(u);

					//if the user does not already exist in the archive, add them to it 
					if(!archivelist.contains(u)) {
						archivelist.add(u);

					}
				}
			}
		}

		assertEquals(1, failedAdminList.size());
		assertEquals(2, adminList.size()); 
		assertEquals(6, archivelist.size());
	}
}


