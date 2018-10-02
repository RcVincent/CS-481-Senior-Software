package controllerTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.PositionController;
import model.Position;
import model.User;
import model.SOP;

public class PositionControllerTest {
	private PositionController poscontrol;
	private Position pos1, pos2;
	private SOP sop1, sop2;
	
	@Before
	public void setUp(){
		pos1.setID(0);
		pos1.setPriority(0);
		pos1.setTitle("Admin");
		
		pos2.setID(1);
		pos2.setPriority(1);
		pos2.setTitle("CEO");
	}
	
	@Test
	public void testGetApplicantsSOP() {
		
	}
	
	@Test
	public void testGetPositionByID() {
		// These will be commented out until we get our database tables up
		//assertEquals(poscontrol.getPositionByID(pos1.getID()).getTitle(), "Admin");
		//assertEquals(poscontrol.getPositionByID(pos2.getID()).getTitle(), "CEO");

		//assertEquals(poscontrol.getPositionByID(pos1.getID()).getPriority(), 0);
		//assertEquals(poscontrol.getPositionByID(pos2.getID()).getPriority(), 1);

		//assertEquals(poscontrol.getPositionByID(pos1.getID()).getID(), 0);
		//assertEquals(poscontrol.getPositionByID(pos2.getID()).getID(), 1);
		
	}
}
