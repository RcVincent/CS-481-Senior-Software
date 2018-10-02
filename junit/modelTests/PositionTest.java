package modelTests;
 
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Position;
import model.SOP;

public class PositionTest {
	private Position p1, p2, p3, p4, p5, p6, p7, p8;
	private SOP req1, req2, req3, req4, req5, req6;
	private ArrayList<Position> positionList, inactiveList; 
	
	@Before
	public void setUp() {
		
		p1 = new Position();
		p2 = new Position();
		p3 = new Position();
		p4 = new Position();
		p5 = new Position(); 
		p6 = new Position(); 
		p7 = new Position(); 
		p8 = new Position(); 
		
		positionList = new ArrayList<Position>();
		inactiveList = new ArrayList<Position>(); 
		
		List<SOP> p1Reqs = new ArrayList<SOP>(); 
		List<SOP> p2Reqs = new ArrayList<SOP>();
		List<SOP> p3Reqs = new ArrayList<SOP>(); 
		List<SOP> p4Reqs = new ArrayList<SOP>();
		List<SOP> p5Reqs = new ArrayList<SOP>();
		List<SOP> p6Reqs = new ArrayList<SOP>();
		List<SOP> p7Reqs = new ArrayList<SOP>();
		List<SOP> p8Reqs = new ArrayList<SOP>();
		
		req1 = new SOP("Test 1", "For testing", 1, 1, 12, 7); 
		req2 = new SOP("Test 2", "For testing", 2, 2, 12, 6); 
		req3 = new SOP("Test 3", "For testing", 3, 3, 0, 1);
		req4 = new SOP(null, null, 0, 0, 0, 0);
		req5 = new SOP("", "testing", 1, 5, 23, 4); 
		req6 = new SOP("Test 6", " ", 4, 6, 0, 0);
		
		p1Reqs.add(req1);
		p1Reqs.add(req2);
		p2Reqs.add(req2);
		p2Reqs.add(req3);
		p3Reqs.add(req3);
		p3Reqs.add(req1);
		
		p4Reqs.add(req4);
		p5Reqs.add(req5);
		p6Reqs.add(req6);
		
		p8Reqs.add(req2);
		
		p1.setID(1);
		p1.setPriority(1);
		p1.setRequirements(p1Reqs);
		p1.setTitle("CEO");
		p1.setValid(true);
		p1.setRequirements(p1Reqs);
		
		p2.setID(10);
		p2.setPriority(4);
		p2.setRequirements(p2Reqs);
		p2.setTitle("Manager");
		p2.setValid(true);
		p2.setRequirements(p2Reqs);
		
		p3.setID(0);
		p3.setPriority(2);
		p3.setRequirements(p3Reqs);
		p3.setTitle("Admin");
		p3.setValid(true);
		p3.setRequirements(p3Reqs);
		
		p4.setID(999);
		p4.setPriority(8);
		p4.setRequirements(p4Reqs);
		p4.setTitle("Maintenance");
		p4.setValid(false);
		p4.setRequirements(p4Reqs);
		
		p5.setID(7);
		p5.setTitle("");
		p5.setPriority(2);
		p5.setValid(true);
		p5.setRequirements(p5Reqs);
		
		p6.setID(19); 
		p6.setPriority(0);
		p6.setTitle("Observer");
		p6.setValid(true);
		p6.setRequirements(p6Reqs);
		
		p7.setID(20);
		p7.setPriority(1);
		p7.setTitle(" ");
		p7.setValid(true);
		p7.setRequirements(p7Reqs);
		
		p8.setID(-2);
		p8.setTitle("Lol");
		p8.setPriority(6);
		p8.setValid(true);
		p8.setRequirements(p8Reqs);
		
		positionList.add(p1);
		positionList.add(p2);
		positionList.add(p3);
		positionList.add(p4);
		positionList.add(p5);
		positionList.add(p6);
		positionList.add(p7);
		positionList.add(p8);
	}
	
	@Test
	public void testID() {
		assertEquals(1, p1.getID());
		assertEquals(10, p2.getID());
		assertEquals(0, positionList.get(2).getID());
		assertEquals(999, positionList.get(3).getID());
	}
	
	@Test
	public void testPriority() {
		assertEquals(1, p1.getPriority());
		assertEquals(4, p2.getPriority());
		assertEquals(2, positionList.get(2).getPriority());
		assertEquals(8, positionList.get(3).getPriority());
	}
	
	@Test 
	public void testRequirements() {
		assertEquals(req1, p1.getRequirements().get(0));
		assertEquals(req2, p2.getRequirements().get(0));
		assertEquals(req3, positionList.get(2).getRequirements().get(0));
		assertEquals(req4, positionList.get(3).getRequirements().get(0));
	}
	
	@Test
	public void testTitle() {
		assertEquals("CEO", p1.getTitle());
		assertEquals("Manager", p2.getTitle());
		assertEquals("Admin", positionList.get(2).getTitle());
		assertEquals("Maintenance", positionList.get(3).getTitle());
	}
	
	@Test
	public void testValid() {
		assertEquals(true, p1.isValid());
		assertEquals(true, p2.isValid());
		assertEquals(true, p3.isValid());
		assertEquals(false, p4.isValid());
	}
	
	@Test
	public void testValidSOP() {
		List<SOP> SOPtestList; 
		List<Position> testList = new ArrayList<Position>(); 
		List<Position> failList = new ArrayList<Position>(); 
		
		testList.addAll(positionList);
		for(Position p: positionList) {
			if(p.isValid() == false) {
				System.out.println("This position is not valid, removing it from the test list");
				testList.remove(p);
				
				if(!inactiveList.contains(p)) {
					inactiveList.add(p);
				}
			
			}
			else {
				SOPtestList = new ArrayList<SOP>(); 
				SOPtestList.addAll(p.getRequirements());
				
				for(SOP s : SOPtestList) {
					if(s.getID() <=0) {
						System.out.println("The position with id" + p.getID() + "has an invalid SOP in its requirements. Removing it from queue");
						testList.remove(p);
						failList.add(p);
					}
					else if(s.getName() == "" || s.getName() == " " || s.getName() == null ) {
						System.out.println("The position with id" + p.getID() + "has an invalid SOP in its requirements. Removing it from queue");
						testList.remove(p);
						failList.add(p);
					}
					else if(s.getDescription() == "" || s.getDescription() == " " || s.getDescription() == null) {
						System.out.println("The position with id" + p.getID() + "has an invalid SOP in its requirements. Removing it from queue");
						testList.remove(p);
						failList.add(p);
					}
					else if(s.getPriority() <= 0) {
						System.out.println("The position with id" + p.getID() + "has an invalid SOP in its requirements. Removing it from queue");
						testList.remove(p);
						failList.add(p);
					}
					else if(s.getRevision() <= 0) {
						System.out.println("The position with id" + p.getID() + "has an invalid SOP in its requirements. Removing it from queue");
						testList.remove(p);
						failList.add(p);
					}
				}
			}
		}
		
		assertEquals(1, inactiveList.size());
		assertEquals(2, failList.size());
		assertEquals(5, testList.size());
	}
	
	@Test
	public void testHasRequirements() {
		List<Position> testList = new ArrayList<Position>(); 
		List<Position> failList = new ArrayList<Position>(); 
		List<SOP> SOPList;
		
		testList.addAll(positionList);
		for(Position p: positionList) {
			if(p.isValid() == false) {
				System.out.println("This position is not valid, removing it from the test list");
				testList.remove(p);
				
				if(!inactiveList.contains(p)) {
					inactiveList.add(p);
				}
			}
			else {
				SOPList = new ArrayList<SOP>(); 
				SOPList.addAll(p.getRequirements());
				
				if(SOPList.size() <= 0) {
					System.out.println("Position woth id "+ p.getID() + " has no SOPs, removing it from active queue.");
					testList.remove(p);
					failList.add(p);
				}
			}
		}
		
		assertEquals(1, failList.size());
		assertEquals(6, testList.size());
		assertEquals(1, inactiveList.size());
	}
	
	@Test
	public void testValidPositionTitle() {
		List<Position> testList = new ArrayList<Position>(); 
		List<Position> failList = new ArrayList<Position>(); 
		
		testList.addAll(positionList);
		
		for(Position p: positionList) {
			if(p.isValid() == false) {
				System.out.println("This position is not valid, removing it from the test list");
				testList.remove(p);
				
				if(!inactiveList.contains(p)) {
					inactiveList.add(p);
				}
			}
			else {
				if(p.getTitle() == "" || p.getTitle() == " ") {
					System.out.println("This position with id:" + p.getID() + "has an invalid title. Removing it from active queue");
					testList.remove(p); 
					failList.add(p); 
					p.setValid(false);
				}
			}
		}
		
		assertEquals(5, testList.size());
		assertEquals(1, inactiveList.size());
		assertEquals(2, failList.size());
	}
	
	@Test
	public void testValidPositionID() {
		List<Position> testList = new ArrayList<Position>(); 
		List<Position> failList = new ArrayList<Position>(); 
		
		testList.addAll(positionList);
		
		for(Position p: positionList) {
			if(p.isValid() == false) {
				System.out.println("This position is not valid, removing it from the test list");
				testList.remove(p);
				
				if(!inactiveList.contains(p)) {
					inactiveList.add(p);
				}
			}
			else {
				if(p.getID() <= 0) {
					System.out.println("This position with title:" + p.getTitle() + "has an invalid ID. Removing it from active queue");
					testList.remove(p); 
					failList.add(p); 
					p.setValid(false);
				}
			}
		}
		
		assertEquals(5, testList.size());
		assertEquals(1, inactiveList.size());
		assertEquals(2, failList.size());
	}
	
	
}