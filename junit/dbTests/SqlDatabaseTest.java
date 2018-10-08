package dbTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.PositionController;
import model.Position;
import model.User;
import model.SOP;
import sqlDB.SqlDatabase;

public class SqlDatabaseTest{

	private SqlDatabase db;
	private Position p;
	private SOP s;
	private User u;

	@Before
	public void setUp(){
		// Clean the database in case someone/something else messed with it
		SqlDatabase.cleanDB();
		db = new SqlDatabase();

		p = new Position();
		// p.setID(ID);

		s = new SOP();

		u = new User();
	}

	@After
	public void cleanUp(){
		// Clean the database for the next person
		SqlDatabase.cleanDB();
	}

	@Test
	public void testInsertUser(){

	}

	@Test
	public void testInsertPosition(){

	}

	@Test
	public void testInsertSOP(){

	}

	@Test
	public void testGetAllUsers(){

	}

	@Test
	public void testGetAllPositions(){

	}

	@Test
	public void testGetAllSOPs(){

	}

	@Test
	public void testGetPositionByID(){

	}
}