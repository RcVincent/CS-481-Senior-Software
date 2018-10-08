package modelTests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.SOP;

public class SOPTest{
	private SOP sop, invalidSOP1, invalidSOP2, invalidSOP3, invalidSOP4, invalidSOP5;
	private List<SOP> invalidList;
	
	@Before
	public void setup(){
		sop = new SOP("How to Avoid Sexual Harassment", "A how-to guide for men on avoiding sexually harassing women in "
				+ "the workplace environment.", 1, 5, 70, 2, false);
		
		invalidSOP1 = new SOP(" ", "A book about nothing", 2, 4, 66, 6, false); 
		invalidSOP2 = new SOP("A test", " ", 3, 5, 89, 7, false);
		invalidSOP3 = new SOP("Another test", "A very silly test to be honest", 0, 2, 99, 1, false);
		invalidSOP4 = new SOP("A third test", "This is getting silly", 4, 0, 98, 3, false);
		invalidSOP5 = new SOP("A final test", "Finally over", 5, 4, 124, 0, false); 
		
		invalidList = new ArrayList<SOP>();
		invalidList.add(invalidSOP1);
		invalidList.add(invalidSOP2);
		invalidList.add(invalidSOP3);
		invalidList.add(invalidSOP4);
		invalidList.add(invalidSOP5);
	}
	
	@Test
	public void testGetName(){
		assertEquals("How to Avoid Sexual Harassment", sop.getName());
	}
	
	@Test
	public void testGetDescription(){
		assertEquals("A how-to guide for men on avoiding sexually harassing women in the workplace environment.", 
				sop.getDescription());
	}
	
	@Test
	public void testGetPriority(){
		assertEquals(1, sop.getPriority());
	}
	
	@Test
	public void testGetID(){
		assertEquals(5, sop.getID());
	}
	
	@Test
	public void testGetAuthorID(){
		assertEquals(70, sop.getAuthorID());
	}
	
	@Test
	public void testSetName(){
		// Ensure that name was set correctly in constructor.
		testGetName();
		
		// Change name
		sop.setName("How to Avoid Sexual Harassment (For Men)");
		
		// Test new name
		assertEquals("How to Avoid Sexual Harassment (For Men)", sop.getName());
	}
	
	@Test
	public void testSetDescription(){
		// Ensure that description was set correctly in constructor.
		testGetDescription();
		
		// Change description
		sop.setDescription("A how-to guide for men on avoiding sexually harassing women in the workplace environment, "
				+ "Sixty-Ninth Edition");
		
		// Test new description
		assertEquals("A how-to guide for men on avoiding sexually harassing women in the workplace environment, "
				+ "Sixty-Ninth Edition", sop.getDescription());
	}
	
	@Test
	public void testSetPriority(){
		// Ensure that priority was set correctly in constructor.
		testGetPriority();
		
		// Change priority
		sop.setPriority(100);
		
		// Test new priority
		assertEquals(100, sop.getPriority());
	}
	
	@Test
	public void testSetID(){
		// Ensure that ID was set correctly in constructor.
		testGetID();
		
		// Change ID
		sop.setID(1);
		
		// Test new ID
		assertEquals(1, sop.getID());
	}
	
	@Test
	public void testSetAuthorID(){
		// Ensure that Author ID was set correctly in constructor.
		testGetAuthorID();
		
		// Change Author ID
		sop.setAuthorID(25);
		
		// Test new Author ID
		assertEquals(25, sop.getAuthorID());
	}
	
	@Test
	public void testSetRevision() {
		//Ensure SOP version was set correctly in the constructor 
		//testSetRevision(); 
		
		//set a new revision
		sop.setRevision(4);
		
		//test the new revision 
		assertEquals(4, sop.getRevision());
		
	}
	
	@Test
	public void TestInvalidSOP() {
		List<SOP> failedList = new ArrayList<SOP>(); 
		List<SOP> testList = new ArrayList<SOP>(); 
		
		testList.addAll(invalidList);
		
		//sort through the failed list 
		for(SOP s: invalidList) {
			if(s.getName() == "" || s.getName() == " ") {
				System.out.println("SOPID: "+ s.getID() + "has an invalid name, removing it from the active system until it is fixed");
				testList.remove(s);
				failedList.add(s);
			}
			else if(s.getDescription() == "" || s.getDescription() == " ") {
				System.out.println("SOP Name:" + s.getName() + "has an invalid description, removing it from active system until fixed");
				testList.remove(s);
				failedList.add(s);
			}
			else if(s.getID() <= 0 ) {
				System.out.println("SOP Name:" + s.getName() + "has an invalid ID, removing it from active system until fixed");
				testList.remove(s);
				failedList.add(s);
			}
			else if(s.getPriority() <= 0) {
				System.out.println("SOP Name:" + s.getName() + "has an invalid description, removing it from active system until fixed");
				testList.remove(s);
				failedList.add(s);
			}
			else if (s.getRevision() <= 0){
				System.out.println("SOP Name:" + s.getName() + "has an invalid revision, removing it from active system until fixed");
				testList.remove(s);
				failedList.add(s);
			}
		}
		
		assertEquals(0, testList.size());
		assertEquals(5, failedList.size());
	}
}

