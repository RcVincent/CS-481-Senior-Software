package edu.ycp.cs481.model;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

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
		user1.setID(902685223);
		user1.setEmail("gmailsucks@bing.com");
		user1.setPassword("Pass");
		//user1.setAdminFlag(false); 
		user1.setArchived(false);

		p1 = new Position(); 
		p1.setTitle("Intern");
		p1.setID(21);
		p1.setPriority(9);


		user1.setPosition(p1);
		userlist.add(user1);

		user2 = new User(); 
		user2.setID(901678905);
		user2.setEmail("bingSucks@gmail.com");
		user2.setPassword("Yeet");
		//user2.setAdminFlag(false); 
		user2.setArchived(false);

		p2 = new Position(); 
		p2.setID(19);
		p2.setTitle("Manager");
		p2.setPriority(5);

		user2.setPosition(p3);
		userlist.add(user2); 

		user3 = new User(); 
		user3.setID(901678509);
		user3.setEmail("IamADegenerate@ycp.edu");
		user3.setPassword("No");
		//user3.setAdminFlag(false);
		user3.setArchived(true);

		p3 = new Position(); 
		p3.setID(14);
		p3.setTitle("Janitor");
		p3.setPriority(10);


		user3.setPosition(p3);
		userlist.add(user3); 

		user4 = new User(); 
		user4.setID(905678509);
		user4.setEmail(""); 
		user4.setPassword("lelelele");
		user4.setArchived(false);
		//user4.setAdminFlag(false);

		user4.setPosition(p2);
		userlist.add(user4); 

		user5 = new User(); 
		user5.setID(900174502);
		user5.setEmail("yayayaya@mrc.org");
		user5.setPassword("");
		//user5.setAdminFlag(false);
		user5.setArchived(false);

		user5.setPosition(p2);
		userlist.add(user5);

		user6 = new User(); 
		user6.setID(0);
		user6.setEmail("Yesnomaybeso@gmail.com");
		user6.setPassword("NNOOOOOOOOOO");

		user6.setPosition(p1);
		userlist.add(user6);

		user7 = new User(); 
		user7.setID(899245663);
		user7.setEmail("kbdack@hot.com");
		//user7.setAdminFlag(false);
		user7.setArchived(false);

		user7.setPosition(null);
		userlist.add(user7);
		
		//this user is for testing valid position requirements only
		user8 = new User();
		user8.setPosition(p4);
		
		admin1 = new User(); 
		admin1.setID(124);
		admin1.setEmail("admin@email.com");
		admin1.setPassword("LetmeIn");
		admin1.setArchived(false);
		//admin1.setAdminFlag(true);

		a1 = new Position(); 
		a1.setID(1);
		a1.setPriority(1);
		a1.setTitle("Auditor");
		admin1.setPosition(a1);

		userlist.add(admin1);

		admin2 = new User(); 
		admin2.setID(125);
		admin2.setEmail("admin2@email.com");
		admin2.setPassword("Now");
		admin2.setArchived(false);
		//admin2.setAdminFlag(true);

		a2 = new Position();
		a2.setID(2);
		a2.setPriority(1);
		a2.setTitle("Assistant Auditor");
		admin2.setPosition(a2);

		userlist.add(admin2); 

		admin3 = new User(); 
		admin3.setID(126); 
		admin3.setEmail("");
		admin3.setPassword("OpenUP");
		admin3.setArchived(false);
		//admin3.setAdminFlag(true);

		admin3.setPosition(null);
		userlist.add(admin3); 

		p4 = new Position(); 
		p4.setTitle("");
		p4.setPriority(5);
		p4.setID(10);

		//get the SOPs ready 
		s1 = new SOP(); 
		s1.setTitle("How to sign your name");
		s1.setDescription("Lets be honest here");
		s1.setPriority(1);
		s1.setID(1);
		s1.setAuthorID(124);
		s1.setVersion(3);
		s1.setArchived(false);
		
		s2 = new SOP(); 
		s2.setTitle("How to sign into your machine");
		s2.setDescription("Yes we are serious");
		s2.setPriority(2);
		s2.setID(2);
		s2.setAuthorID(125);
		s2.setVersion(5);
		s2.setArchived(false);
		
		s3 = new SOP(); 
		s3.setTitle("How to flush a toilet");
		s3.setDescription("Some of ya'll nasty");
		s3.setPriority(6);
		s3.setID(33);
		s3.setAuthorID(124);
		s3.setVersion(1);
		s3.setArchived(false);
		
		s4 = new SOP(); 
		s4.setTitle("How to sigh out");
		s4.setDescription("Can't have you lot logged in all the time");
		s4.setPriority(3);
		s4.setID(55);
		s4.setAuthorID(124);
		s4.setVersion(2);
		s4.setArchived(false);
		
		s5 = new SOP(); 
		s5.setTitle("Lelelele");
		s5.setDescription("To annoy the flying Duchman");
		s5.setPriority(8);
		s5.setID(68);
		s5.setAuthorID(125);
		s5.setVersion(4);
		s5.setArchived(false);
		
		s6 = new SOP(); 
		s6.setTitle(null);
		s6.setDescription(null);
		s6.setPriority(-1);
		s6.setID(0);
		s6.setAuthorID(0);
		s6.setVersion(0);
		s6.setArchived(false);
		
		//add them to the sop list
		sopList = new ArrayList<SOP>(); 
		sopList.add(s1);
		sopList.add(s2);
		sopList.add(s3);
		sopList.add(s4);
		sopList.add(s5);
		sopList.add(s6);


		//set position to SOP requirements 
		ArrayList<SOP> req1 = new ArrayList<SOP>();
		ArrayList<SOP> req2 = new ArrayList<SOP>();
		ArrayList<SOP> req3 = new ArrayList<SOP>();
		ArrayList<SOP> fail = new ArrayList<SOP>(); 

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
		int id = user3.getID();
		assertEquals(901678509, id); 
	}

	@Test
	public void TestGetPosition() {
		Position p = admin1.getPosition(); 
		assertEquals(p.getID(), 1);
	}

	@Test
	public void viewPositionSOPListTest() {
		ArrayList<SOP> testlist = admin1.getPosition().getRequirements();
		
		assertEquals(3, testlist.size());

		SOP test = admin1.getPosition().getRequirements().get(0);
		
		assertEquals(1, test.getID());

	}
}


