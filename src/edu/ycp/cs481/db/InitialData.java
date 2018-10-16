package edu.ycp.cs481.db;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

//import javafx.util.Pair;



public class InitialData {


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
	
	/*public static List<Pair<Position, SOP>> getPosSOPs() throws IOException {
		// TODO:
		return null;
	}*/
}