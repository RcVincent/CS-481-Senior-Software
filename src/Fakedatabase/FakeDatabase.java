package Fakedatabase;

import java.util.ArrayList;

import model.User;
import model.Position;
import model.SOP;
import model.Messenger;

public class FakeDatabase {
	private User u1;
	private Position p1;
	private SOP s1;
	
	private ArrayList<User> userList = new ArrayList<User>();
	private ArrayList<Position> posList = new ArrayList<Position>();
	private ArrayList<SOP> sopList = new ArrayList<SOP>();

	public FakeDatabase() {
		u1 = new User(0, "Pass", "test@ycp.edu", p1);
		p1 = new Position("Admin", 1, 1);

		userList.add(u1);
	}
}