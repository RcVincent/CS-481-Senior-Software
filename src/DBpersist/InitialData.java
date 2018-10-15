package DBpersist;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import model.Position;
import model.SOP;
import model.User;

//import javafx.util.Pair;



public class InitialData {
	static List<User> uList;
	User u1, u2, u3, u4;
	static List<Position> pList;
	Position p1, p2, p3, p4;
	static List<SOP> sList;	
	SOP s1, s2, s3, s4;
	
	public InitialData() {
		p1 = new Position();
		p1.setID(1);
		p1.setTitle("Administrator");
		p1.setDescription("Can view/modify all aspects of the system.");
		p1.setPriority(1);
		
		p2 = new Position();
		p2.setID(2);
		p2.setTitle("User");
		p2.setDescription("This is used as a temporary position for those without one.");
		p2.setPriority(3);
		
		u1 = new User();
		u1.setUserID(1);
		u1.setEmail("CEO@Google.com");
		u1.setPassword("yes");
		u1.setFirstname("Carl");
		u1.setLastname("Sagan");
		u1.setAdminFlag("true");
		u1.setArchiveFlag(false);
		u1.setPosition(p1);
		
		u2 = new User();
		u2.setUserID(2);
		u2.setEmail("Worker@Google.com");
		u2.setPassword("no");
		u2.setFirstname("Billiam");
		u2.setLastname("Nye");
		u2.setAdminFlag("false");
		u2.setArchiveFlag(false);
		u2.setPosition(p2);
		
		s1 = new SOP();
		s1.setID(1);
		s1.setName("Admin");
		s1.setDescription("IsAdmin");
		s1.setPriority(1);
		s1.setRevision(1);
		s1.setAuthorID(p1.getID());
		s1.setArchiveFlag(false);
		
		s2 = new SOP();
		s2.setID(2);
		s2.setName("User");
		s2.setDescription("IsUser");
		s2.setPriority(1);
		s2.setRevision(1);
		s2.setAuthorID(p1.getID());
		s2.setArchiveFlag(false);
		
		sList.add(s2);
		p2.setRequirements(sList);
		
		sList.add(s1);
		p1.setRequirements(sList);		
		
		uList.add(u1);
		uList.add(u2);
		pList.add(p1);
		pList.add(p2);
	}


	public static List<User> getInitialUsers() {
		return uList;
	}
	
	public static List<Position> getInitialPositions() {
		return pList;
	}
	
	public static List<SOP> getInitialSOPs() {
		return sList;
	}
	
	//Position db will be for authentication and hold primary key for positions
	public static List<Position> getPositions() throws IOException {

		//read the Position file
		List<Position> positionList = new ArrayList<Position>();
		ReadCSV readPosition = new ReadCSV("Positions.csv");
		System.out.println("about to loop position csv");
		try {
			//set this id, and auto generate them: they are not unique to the table anymore
			Integer positionId = 1;
			while(true) {
				List<String> tuple = readPosition.next();
				if(tuple == null) {
					System.out.println("breaking for empty");
					break;

				}
				System.out.println("Looping Position");
				Iterator<String> i = tuple.iterator();
				Position position = new Position();
				Integer.parseInt(i.next());

				position.setID(positionId++);
				position.setTitle(i.next());
				position.setDescription(i.next());
				position.setPriority(Integer.parseInt(i.next()));
				
				/*position_id, title, description, priority*/
				
				positionList.add(position);

			}
			System.out.println("position List loaded from the CSV file");
			return positionList;
		}
		finally {
			readPosition.close();
		}

	}
	//user db will be for authentication and hold a primary key for users and admins
	public static List<User> getUsers() throws IOException {
		//read the users file
		List<User> userList = new ArrayList<User>();
		ReadCSV readUser = new ReadCSV("Users.csv");

		try {
			//set this id, and auto generate them: they are not unique to the table anymore
			Integer userId = 1;
			while (true) {
				List<String> tuple = readUser.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				User user = new User();
				Integer.parseInt(i.next());

				// read User ID from CSV file, but don't use it
				// auto-generate User ID, instead
				user.setUserID(userId++);	
				user.setEmail(i.next());			
				user.setPassword(i.next());
				user.setFirstname(i.next());
				user.setLastname(i.next());
				user.setAdminFlag(i.next());
				user.setArchiveFlag(Boolean.parseBoolean(i.next()));
				// Figure out the proper way to do position, use position by id query?
				userList.add(user);
				/*user_id, email, password, first_name, last_name
				 * admin_flag, archive_flag, create_time, position_id*/
			}

			System.out.println("UserList loaded from CSV file");	
			return userList;
		} finally {
			readUser.close();
		}
	}

	//Sop db will be for authentication and hold primary key for users and admins 
	public static List<SOP> getSOPs() throws IOException {
		System.out.println("in getSOPs");
		//read the SOPs file
		List<SOP> sopList = new ArrayList<SOP>();
		ReadCSV readSOP = new ReadCSV("SOPs.csv");

		try {
			//set this id, and auto generate them: they are not unique to the table anymore
			Integer sopId = 1;
			while(true) {
				List<String> tuple = readSOP.next();
				if(tuple == null) {
					break;
				}

				System.out.println("taking the data from ReadCSV and adding it");

				Iterator<String> i = tuple.iterator();
				SOP sop = new SOP();
				Integer.parseInt(i.next());

				sop.setID(sopId++);
				sop.setName(i.next());
				sop.setDescription((i.next()));
				sop.setPriority(Integer.parseInt(i.next()));
				sop.setRevision(Integer.parseInt(i.next()));
				sop.setAuthorID(Integer.parseInt(i.next()));
				sop.setArchiveFlag(Boolean.parseBoolean(i.next()));
				
				/*sop_id, title, description, priority, version, author_id, archive_flag*/

			}
			System.out.println("SOPList loaded from the CSV file");
			return sopList;
		}
		finally {
			readSOP.close();
		}
	}
}