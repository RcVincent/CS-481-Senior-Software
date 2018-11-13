package edu.ycp.cs481.model;
 
import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;

public class PositionTest {
	private Position p1, p2, p3, p4, p5, p6, p7, p8;
	private SOP req1, req2, req3, req4, req5, req6;
	private ArrayList<Position> positionList; 
	
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
		
		ArrayList<SOP> p1Reqs = new ArrayList<SOP>(); 
		ArrayList<SOP> p2Reqs = new ArrayList<SOP>();
		ArrayList<SOP> p3Reqs = new ArrayList<SOP>(); 
		ArrayList<SOP> p4Reqs = new ArrayList<SOP>();
		ArrayList<SOP> p5Reqs = new ArrayList<SOP>();
		ArrayList<SOP> p6Reqs = new ArrayList<SOP>();
		ArrayList<SOP> p7Reqs = new ArrayList<SOP>();
		ArrayList<SOP> p8Reqs = new ArrayList<SOP>();
		
		req1 = new SOP(); 
		req1.setTitle("Test 1");
		req1.setDescription("For testing");
		req1.setPriority(1);
		req1.setID(1);
		req1.setAuthorID(12);
		req1.setVersion(7);
		req1.setArchived(false);
		
		req2 = new SOP(); 
		req2.setTitle("Test 2");
		req2.setDescription("For testing");
		req2.setPriority(2);
		req2.setID(2);
		req2.setAuthorID(12);
		req2.setVersion(6);
		req2.setArchived(false);
		
		req3 = new SOP(); 
		req3.setTitle("Test 3");
		req3.setDescription("For testing");
		req3.setPriority(3);
		req3.setID(3);
		req3.setAuthorID(0);
		req3.setVersion(1);
		req3.setArchived(false);
		
		req4 = new SOP(); 
		req4.setTitle(null);
		req4.setDescription(null);
		req4.setPriority(0);
		req4.setID(0);
		req4.setAuthorID(0);
		req4.setVersion(0);
		req4.setArchived(false);
		
		req5 = new SOP(); 
		req5.setTitle("");
		req5.setDescription("testing");
		req5.setPriority(1);
		req5.setID(5);
		req5.setAuthorID(23);
		req5.setVersion(4);
		req5.setArchived(false);
		
		req6 = new SOP(); 
		req6.setTitle("Test 6");
		req6.setDescription(" ");
		req6.setPriority(4);
		req6.setID(6);
		req6.setAuthorID(0);
		req6.setVersion(0);
		req6.setArchived(false);
		
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
		p1.setRequirements(p1Reqs);
		
		p2.setID(10);
		p2.setPriority(4);
		p2.setRequirements(p2Reqs);
		p2.setTitle("Manager");
		p2.setRequirements(p2Reqs);
		
		p3.setID(0);
		p3.setPriority(2);
		p3.setRequirements(p3Reqs);
		p3.setTitle("Admin");
		p3.setRequirements(p3Reqs);
		
		p4.setID(999);
		p4.setPriority(8);
		p4.setRequirements(p4Reqs);
		p4.setTitle("Maintenance");
		p4.setRequirements(p4Reqs);
		
		p5.setID(7);
		p5.setTitle("");
		p5.setPriority(2);
		p5.setRequirements(p5Reqs);
		
		p6.setID(19); 
		p6.setPriority(0);
		p6.setTitle("Observer");
		p6.setRequirements(p6Reqs);
		
		p7.setID(20);
		p7.setPriority(1);
		p7.setTitle(" ");
		p7.setRequirements(p7Reqs);
		
		p8.setID(-2);
		p8.setTitle("Lol");
		p8.setPriority(6);
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
	public void testHasRequirements() {
		ArrayList<Position> testList = new ArrayList<Position>(); 
		ArrayList<Position> failList = new ArrayList<Position>(); 
		ArrayList<SOP> SOPList;
		
		testList.addAll(positionList);
		for(Position p: positionList) {
			SOPList = new ArrayList<SOP>(); 
			SOPList.addAll(p.getRequirements());
			
			if(SOPList.size() <= 0) {
				System.out.println("Position woth id "+ p.getID() + " has no SOPs, removing it from active queue.");
				testList.remove(p);
				failList.add(p);
			}
		}
		
		assertEquals(1, failList.size());
		assertEquals(6, testList.size());
	}
}