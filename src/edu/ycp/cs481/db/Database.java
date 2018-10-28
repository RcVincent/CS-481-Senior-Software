package edu.ycp.cs481.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class Database {
	public String positionPieces = "Position.position_id, Position.title, Position.description, Position.priority";
	public String sopPieces = "SOP.sop_id, SOP.title, SOP.description, SOP.priority, SOP.version, SOP.author_id, SOP.archive_flag";
	public String dbName = "cs481db";
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load JDBC driver");
		}
	}

	public interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 100;
		
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}

	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();

		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;

			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						numAttempts++;
					} else {
						throw e;
					}
				}
			}

			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}

			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}
	
	public interface QueryResultFormat<ResultType>{
		public ResultType convertFromResultSet(ResultSet resultSet) throws SQLException;
	}
	
	public QueryResultFormat<ArrayList<Position>> posResFormat = new QueryResultFormat<ArrayList<Position>>(){
		@Override
		public ArrayList<Position> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Position> positions = new ArrayList<Position>();
			while(resultSet.next()){
				Position p = new Position();
				p.setID(resultSet.getInt(1));
				p.setTitle(resultSet.getString(2));
				p.setDescription(resultSet.getString(3));
				p.setPriority(resultSet.getInt(4));
				
				positions.add(p);
			}
			
			return positions;
		}
	};
	
	public QueryResultFormat<ArrayList<User>> userResFormat = new QueryResultFormat<ArrayList<User>>(){
		@Override
		public ArrayList<User> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<User> users = new ArrayList<User>();
			
			while(resultSet.next()){
				User u = new User();
				
				u.setUserID(resultSet.getInt(1));
				u.setEmail(resultSet.getString(2));
				u.setPassword(resultSet.getString(3));
				u.setFirstname(resultSet.getString(4));
				u.setLastname(resultSet.getString(5));
				u.setAdminFlag(resultSet.getBoolean(6));
				u.setArchiveFlag(resultSet.getBoolean(7));
				u.setPosition(searchForPositions(resultSet.getInt(9), null, null, -1).get(0));
				
				users.add(u);
			}
			
			return users;
		}
	};
	
	public QueryResultFormat<ArrayList<SOP>> sopResFormat = new QueryResultFormat<ArrayList<SOP>>(){
		@Override
		public ArrayList<SOP> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<SOP> sops = new ArrayList<SOP>();
			
			while(resultSet.next()){
					SOP s = new SOP();
					
					s.setID(resultSet.getInt(1));
					s.setName(resultSet.getString(2));
					s.setDescription(resultSet.getString(3));
					s.setPriority(resultSet.getInt(4));
					s.setRevision(resultSet.getInt(5));
					s.setAuthorID(resultSet.getInt(6));
					s.setArchiveFlag(resultSet.getBoolean(7));
					
					sops.add(s);
			}
			
			return sops;
		}
	};
	
	public<ResultType> ResultType executeQuery(String name, String sql, QueryResultFormat<ResultType> querExec) throws SQLException{
		return executeTransaction(new Transaction<ResultType>(){
			@Override
			public ResultType execute(Connection conn) throws SQLException{
				Statement stmt = null;
				ResultSet resultSet = null;
				
				try{
					if(name != null){
						System.out.println("Doing query " + name);
					}
					stmt = conn.createStatement();
					resultSet = stmt.executeQuery(sql);
					if(name != null){
						System.out.println("Finished query " + name);
					}
					
					return querExec.convertFromResultSet(resultSet);
				}finally{
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName +"?user=root&password=password");

		conn.setAutoCommit(false);

		return conn;
	}
	
	/*
	 * This method is used in executing a list of updates to the database (e.g. create tables, update data, etc.)
	 */
	public boolean executeUpdates(String[] names, String[] sqls){
		return executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				ArrayList<Statement> stmts = new ArrayList<Statement>();
				try{
					boolean hasNames = names != null;
					if(hasNames && names.length != sqls.length){
						throw new IllegalArgumentException("Must have all sql statements named or pass null names array!");
					}
					for(int i = 0; i < sqls.length; i++){
						if(hasNames){
							System.out.println("Starting " + names[i]);
						}
						stmts.add(conn.createStatement());
						stmts.get(i).executeUpdate(sqls[i]);
						if(hasNames){
							System.out.println("Finished " + names[i]);
						}
					}
					return true;
				}finally{
					for(Statement stmt: stmts){
						DBUtil.closeQuietly(stmt);
					}
				}
			}
		});
	}
	
	public boolean executeUpdate(String name, String sql){
		return executeUpdates(new String[]{name}, new String[]{sql});
	}
	
	public void createDatabase(){
		executeUpdate("Create CS481DB database", "create database if not exists cs481db");
	}
	
	public void createTables(){
		String[] names = new String[4];
		String[] sqls = new String[4];
		
		names[0] = "Create Position table";
		sqls[0] = "CREATE TABLE IF NOT EXISTS Position (" +
				 "position_id INT NOT NULL AUTO_INCREMENT," +
				 "title VARCHAR(80) NOT NULL," +
				 "description VARCHAR(255) NOT NULL," +
				 "priority INT NOT NULL," +
				 "PRIMARY KEY (position_id)," +
				 "UNIQUE INDEX position_id_UNIQUE (position_id ASC) VISIBLE);";
		
		names[1] = "Create Users table";
		sqls[1] = "CREATE TABLE IF NOT EXISTS User (" +
				  "user_id INT NOT NULL AUTO_INCREMENT," +
				  "email VARCHAR(255) NOT NULL," +
				  "password VARCHAR(80) NOT NULL, " +
				  "first_name VARCHAR(80) NOT NULL, " +
				  "last_name VARCHAR(80) NOT NULL, " +
				  "admin_flag TINYINT NOT NULL, " +
				  "archive_flag TINYINT NOT NULL, " +
				  "create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
				  "position_id INT NOT NULL, " +
				  "PRIMARY KEY (user_id), " +
				  "UNIQUE INDEX user_id_UNIQUE (user_id ASC) VISIBLE, " +
				  "INDEX fk_User_Position_idx (position_id ASC) VISIBLE, " +
				  "CONSTRAINT User_Position " +
				  "FOREIGN KEY (position_id) " +
				  "REFERENCES Position (position_id) " +
				  "ON DELETE NO ACTION " +
				  "ON UPDATE NO ACTION);";
		
		names[2] = "Create SOP table";
		sqls[2] = "CREATE TABLE IF NOT EXISTS SOP (" +
				  "sop_id INT NOT NULL AUTO_INCREMENT, " +
				  "title VARCHAR(80) NOT NULL, " +
				  "description VARCHAR(255) NOT NULL, " +
				  "priority INT NOT NULL, " +
				  "version INT NOT NULL, " +
//				  "filepath VARCHAR(255) NOT NULL, " +
				  "author_id INT NOT NULL, " +
				  "archive_flag TINYINT NOT NULL, " +
				  "PRIMARY KEY (sop_id), " +
				  "UNIQUE INDEX sop_id_UNIQUE (sop_id ASC) VISIBLE, " +
				  "INDEX fk_SOP_User1_idx (author_id ASC) VISIBLE, " +
				  "CONSTRAINT fk_SOP_User1 " +
				  "FOREIGN KEY (author_id) " +
				  "REFERENCES User (user_id) " +
				  "ON DELETE NO ACTION " +
				  "ON UPDATE NO ACTION);";
		
		names[3] = "Create PositionSOP table";
		sqls[3] = "CREATE TABLE IF NOT EXISTS PositionSOP (" +
					"position_id INT NOT NULL, " +
					"sop_id INT NOT NULL, " +
					"CONSTRAINT FOREIGN KEY (position_id) REFERENCES Position (position_id), " + 
					"CONSTRAINT FOREIGN KEY (sop_id) REFERENCES SOP (sop_id) " +
					");";
		
		executeUpdates(names, sqls);
	}
	
	public void loadInitialData(){
		InitialData initData = new InitialData();
		
		// They must go in this order
		List<Position> posList = initData.getInitialPositions();
		List<User> userList = initData.getInitialUsers();
		List<SOP> sopList = initData.getInitialSOPs();
		
		int numInserts = posList.size() + userList.size() + sopList.size();
		// TODO: Add PositionSOP connections to size
		
		String[] names = new String[numInserts];
		String[] sqls = new String[numInserts];
		
		int currentInsert = 0;
		for(Position p: posList){
			names[currentInsert] = "Insert Position " + p.getTitle();
			sqls[currentInsert] = "insert into Position (title, description, priority) " +
					"values ('" + p.getTitle() + "', '" + p.getDescription() + "', " + p.getPriority() + ")";
			currentInsert++;
		}
		
		for(User u: userList){
			names[currentInsert] = "Insert User " + u.getFirstname() + " " + u.getLastname();
			sqls[currentInsert] = "insert into User (email, password, first_name, last_name, admin_flag, archive_flag, " +
					"position_id)  values ('" + u.getEmail() + "', SHA('" + u.getPassword() + "'), '" + u.getFirstname() +
					"', '" + u.getLastname() + "', " + u.isAdminFlag() + ", " + u.isArchiveFlag() + ", " + 
					u.getPosition().getID() + ")";
			currentInsert++;
		}
		
		for(SOP s: sopList){
			names[currentInsert] = "Insert SOP " + s.getName();
			sqls[currentInsert] = "insert into SOP (title, description, priority, version, author_id, archive_flag)" +
					" values ('" + s.getName() + "', '" + s.getDescription() + "', " + s.getPriority() + ", " +
					s.getRevision() + ", " + s.getAuthorID() + ", " + s.getArchiveFlag() + ")";
			currentInsert++;
		}
		
		// TODO: Insert PositionSOP connections
		/*
			names[currentInsert] = "Insert SOP " + <sop_name> + " and Position " + <position_name> + " connection";
			sqls[currentInsert] = "insert into PositionSOP (position_id, sop_id) " + 
					" values (" + <pos_id> + ", " + <sop_id> + ")";
			currentInsert++;
		 */
		
		executeUpdates(names, sqls);
	}
	
	public Integer insertAndGetID(String table, String id_str, String[] args, String[] values){
		return executeTransaction(new Transaction<Integer>(){
			@Override
			public Integer execute(Connection conn) throws SQLException{
				Statement insert = null;
				Statement selectID = null;
				ResultSet id = null;
				
				if(args.length != values.length){
					throw new IllegalArgumentException("Args and Values don't match up!");
				}
				
				try{
					insert = conn.createStatement();
					String insertSQL = "insert into " + table + " (";
					for(int i = 0; i < args.length; i++){
						if(i == args.length - 1){
							insertSQL += args[i] + ") select ";
						}else{
							insertSQL += args[i] + ", ";
						}
					}
					for(int i = 0; i < values.length; i++){
						if(values[i].equalsIgnoreCase("true") || values[i].equalsIgnoreCase("false") ||
								values[i].startsWith("SHA")){
							insertSQL += values[i];
						}else{
							insertSQL += "'" + values[i] + "'";
						}
						if(i == values.length - 1){
							insertSQL += ";";
						}else{
							insertSQL += ", ";
						}
					}
					System.out.println(insertSQL);
					insert.executeUpdate(insertSQL);
					System.out.println("Executed Insert of a " + table + "!");
					
					selectID = conn.createStatement();
					String selectSQL = "select " + id_str + " from " + table + " where ";
					for(int i = 0; i < args.length; i++){
						if(!values[i].startsWith("SHA")){
							selectSQL += args[i] + " = ";
							if(values[i].equalsIgnoreCase("true") || values[i].equalsIgnoreCase("false")){
								selectSQL += values[i];
							}else{
								selectSQL += "'" + values[i] + "'";
							}
							if(i != args.length - 1){
								selectSQL += " and ";
							}else{
								selectSQL += ";";
							}
						}
					}
					id = selectID.executeQuery(selectSQL);
					System.out.println("Pulled out " + id_str + " of just inserted " + table + "!");
					
					id.next();
					return id.getInt(1);
				}finally{
					DBUtil.closeQuietly(insert);
					DBUtil.closeQuietly(selectID);
					DBUtil.closeQuietly(id);
				}
			}
		});
	}
	
	public Integer insertPosition(Position p){
		return insertAndGetID("Position", "position_id", new String[]{"title", "description", "priority"}, 
				new String[]{p.getTitle(), p.getDescription(), String.valueOf(p.getPriority())});
	}
	
	public Integer insertSOP(SOP s){
		return insertAndGetID("SOP", "sop_id", 
				new String[]{"title", "description", "priority", "version", "author_id", "archive_flag"}, 
				new String[]{s.getName(), s.getDescription(), String.valueOf(s.getPriority()), String.valueOf(s.getRevision()), 
						String.valueOf(s.getAuthorID()), String.valueOf(s.getArchiveFlag())});
	}
	
	public ArrayList<Position> searchForPositions(int positionID, String title, String description, int priority){
		try{
			String name = "";
			String sql = "select * from Position";
			if(positionID == -1 && (title == null || title.equalsIgnoreCase("")) && 
					(description == null || description.equalsIgnoreCase("")) && priority == -1){
				name = "Get All Positions";
			}else{
				name = "Get Position with ";
				sql += " where ";
				boolean prevSet = false;
				
				if(positionID != -1){
					name += "id of " + positionID;
					sql += "position_id = " + positionID;
					prevSet = true;
				}
				
				if(title != null && !title.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "title of " + title;
					sql += "title = '" + title + "'";
					prevSet = true;
				}
				
				// TODO: Likely edit description (and possibly title) to search for partial? Not sure if this does that.
				if(description != null && !description.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "description of " + description;
					sql += "description = '" + description + "'";
					prevSet = true;
				}
				
				if(priority != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "priority " + priority;
					sql += "priority = " + priority;
					prevSet = true;
				}
			}
			ArrayList<Position> results = executeQuery(name, sql, posResFormat);
			if(positionID != -1){
				if(results.size() == 0){
					System.out.println("No Position found with ID " + positionID);
				}else if(results.size() > 1){
					System.out.println("Multiple Positions found with ID " + positionID + "! Returning null");
					return null;
				}
			}
			return results;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated // TODO: Remove, use searchForPositions instead
	public ArrayList<Position> findAllPositions(){
		return searchForPositions(-1, null, null, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForPositions instead
	public Position findPositionByID(int position_id){
		ArrayList<Position> result = searchForPositions(position_id, null, null, -1);
		if(result != null){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	@Deprecated // TODO: Remove, use searchForPositions instead
	public ArrayList<Position> getPositionByName(String title){
		return searchForPositions(-1, title, null, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForPositions instead
	public ArrayList<Position> getPositionByPriority(int priority){
		return searchForPositions(-1, null, null, priority);
	}
	
	public Position getPositionOfUser(int userID){
		try{
			ArrayList<Position> results = executeQuery("Get Position By User", "select " + positionPieces + 
					" from Position, User where user_id = " + userID + " and Position.position_id = User.position_id", 
					posResFormat);
			if(results.size() == 0){
				System.out.println("No positions found for User_id " + userID + "!");
			}else if(results.size() > 1){
				System.out.println("More than one position found for User_id " + userID + "! Returning null");
			}else{
				return results.get(0);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Position> getPositionBySOPID(int SOPID){
		try{
			return executeQuery("Get Position by SOP ID", "select " + positionPieces + " from Position, PositionSOP where "
					+ "PositionSOP.sop_id = " + SOPID + " and Position.position_id = PositionSOP.position_id", posResFormat);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO: Change this to use Execute Update? Not sure why it gets the Position back again, perhaps to update?
	public Position changePositionPriority(Position pos, int priority) {
		return executeTransaction(new Transaction<Position>() {
			@Override
			public Position execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
	
	
				try{
					stmt = conn.prepareStatement(
							"UPDATE Position SET priority = ? WHERE position_id = ? ");
					
					stmt.setInt(1, priority);
					stmt.setInt(2, pos.getID());
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM Position WHERE position_id = ?");
					
					stmt2.setInt(1, pos.getID());
					
					resultSet = stmt2.executeQuery();
					
					Position position = new Position(); 
					
					Boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						position.setID(resultSet.getInt(1));
						position.setTitle(resultSet.getString(2));
						position.setDescription(resultSet.getString(3));
						position.setPriority(resultSet.getInt(4));
					}
	
					// check if the position exists
					if (!found) {
						System.out.println("Position with ID "+ pos.getID() +" was not found in the Position table");
					}
	
					return position;
	
	
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2); 
				}
			}
		});
	}
	
	public void deletePosition(int position_id){
		executeUpdate("Delete Position with ID " + position_id, "delete from Position where position_id = " + position_id);
	}
	
	public ArrayList<User> searchForUsers(int userID, String email, String firstName, String lastName, int positionID){
		try{
			String name = "";
			String sql = "select * from User";
			if(userID == -1 && (email == null || email.equalsIgnoreCase("")) && 
					(firstName == null || firstName.equalsIgnoreCase("")) && 
					(lastName == null || lastName.equalsIgnoreCase("")) && positionID == -1){
				name = "Get All Users";
			}else{
				name = "Get User with ";
				sql += " where ";
				boolean prevSet = false;
				
				if(userID != -1){
					name += "id of " + userID;
					sql += "user_id = " + userID;
					prevSet = true;
				}
				
				// TODO: Likely change strings for searching by partial?
				if(email != null && !email.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "email of " + email;
					sql += "email = '" + email + "'";
					prevSet = true;
				}
				
				if(firstName != null && !firstName.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "first name of " + firstName;
					sql += "first_name = '" + firstName + "'";
					prevSet = true;
				}
				
				if(lastName != null && !lastName.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "last name of " + lastName;
					sql += "last_name = '" + lastName + "'";
					prevSet = true;
				}
				
				if(positionID != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "position ID of " + positionID;
					sql += "position_id = " + positionID;
					prevSet = true;
				}
			}
			ArrayList<User> results = executeQuery(name, sql, userResFormat);
			if(results.size() == 0 && userID != -1){
				System.out.println("No User found with ID " + userID);
			}else if(results.size() > 1){
				if(userID != -1){
					System.out.println("Multiple Users found with ID " + userID + "! Returning null");
					return null;
				}else if(email != null && !email.equalsIgnoreCase("")){
					System.out.println("Multiple Users found with email " + email + "! Returning null");
					return null;
				}
			}
			return results;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated // TODO: Remove, use searchForUsers instead
	public ArrayList<User> findAllUsers(){
		return searchForUsers(-1, null, null, null, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForUsers instead
	public User getUserByID(int ID){
		ArrayList<User> result = searchForUsers(ID, null, null, null, -1);
		if(result != null){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	@Deprecated // TODO: Remove, use searchForUsers instead
	public User getUserByEmail(String email){
		ArrayList<User> result = searchForUsers(-1, email, null, null, -1);
		if(result != null){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	@Deprecated // TODO: Remove, use searchForUsers instead
	public ArrayList<User> getUsersByFirstName(String firstName){
		return searchForUsers(-1, null, firstName, null, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForUsers instead
	public ArrayList<User> getUsersByLastName(String lastName){
		return searchForUsers(-1, null, null, lastName, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForUsers instead
	public ArrayList<User> findUsersWithPosition(int position_id){
		return searchForUsers(-1, null, null, null, position_id);
	}
	
	public void archiveUser(int userID){
		executeUpdate("Archive User with ID " + userID, "update User set archive_flag = true where user_id = " + userID);
	}
	
	public void unarchiveUser(int userID){
		executeUpdate("Unarchive User with ID " + userID, "update User set archive_flag = false where user_id = " + userID);
	}
	
	// TODO: Change this to use Execute Update? Not sure why it gets the User back again, perhaps to update?
	// Also this currently updates the Position in a weird way (doing a sub-transaction within the greater one)
	public User changePosition(final int user_id, final int position_id) {
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"UPDATE User SET position_id = ? WHERE user_id = ? ");
					
					stmt.setInt(1, position_id);
					stmt.setInt(2, user_id);
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM User WHERE user_id = ?");
					
					stmt2.setInt(1, user_id);
					
					resultSet = stmt2.executeQuery();
					//if anything is found, return it in a list format
					User result = new User(); 
					
					Boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						result.setUserID(resultSet.getInt(1));
						result.setEmail(resultSet.getString(2));
						result.setPassword(resultSet.getString(3));
						result.setFirstname(resultSet.getString(4));
						result.setLastname(resultSet.getString(5));
						result.setAdminFlag(resultSet.getBoolean(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.setPosition(findPositionByID(resultSet.getInt(9)));
					}


					if (!found) {
						System.out.println("User with ID <" + user_id + "> was not found in the User table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2); 
				}
			}
		});
	}
	
	public ArrayList<SOP> searchForSOPss(int sopID, String title, String description, int priority, int version, int authorID){
		try{
			String name = "";
			String sql = "select * from SOP";
			if(sopID == -1 && (title == null || title.equalsIgnoreCase("")) &&
					(description == null || description.equalsIgnoreCase("")) && priority == -1 && version == -1 && 
					authorID == -1){
				name = "Get All SOPs";
			}else{
				name = "Get SOP with ";
				sql += " where ";
				boolean prevSet = false;
				
				if(sopID != -1){
					name += "id of " + sopID;
					sql += "sop_id = " + sopID;
					prevSet = true;
				}
				
				if(title != null && !title.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "title of " + title;
					sql += "title = '" + title + "'";
					prevSet = true;
				}
				
				// TODO: Likely need to change to search partial descriptions
				if(description != null && !description.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "description of " + description;
					sql += "description = '" + description + "'";
					prevSet = true;
				}
				
				if(priority != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "priority " + priority;
					sql += "priority = " + priority;
					prevSet = true;
				}
				
				if(version != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "version " + version;
					sql += "version = " + version;
					prevSet = true;
				}
				
				if(authorID != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "author_id of" + authorID;
					sql += "author_id = " + authorID;
					prevSet = true;
				}
			}
			ArrayList<SOP> results = executeQuery(name, sql, sopResFormat);
			if(sopID != -1){
				if(results.size() == 0){
					System.out.println("No SOP found with ID " + sopID);
				}else if(results.size() > 1){
					System.out.println("Multiple SOPs found with ID " + sopID + "! Returning null");
					return null;
				}
			}
			return results;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated // TODO: Remove, use searchForSOPss instead
	public ArrayList<SOP> findAllSOPs(){
		return searchForSOPss(-1, null, null, -1, -1, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForSOPss instead
	public SOP findSOPbyID(int sop_id){
		ArrayList<SOP> result = searchForSOPss(sop_id, null, null, -1, -1, -1);
		if(result != null){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	@Deprecated // TODO: Remove, use searchForSOPss instead
	public ArrayList<SOP> findSOPsByTitle(String title){
		return searchForSOPss(-1, title, null, -1, -1, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForSOPss instead
	public ArrayList<SOP> findSOPsByPriority(int priority){
		return searchForSOPss(-1, null, null, priority, -1, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForSOPss instead
	public ArrayList<SOP> findSOPsByVersion(int version){
		return searchForSOPss(-1, null, null, -1, version, -1);
	}
	
	@Deprecated // TODO: Remove, use searchForSOPss instead
	public ArrayList<SOP> findSOPsByAuthorID(int authorID){
		return searchForSOPss(-1, null, null, -1, -1, authorID);
	}
	
	public void archiveSOP(int sop_id){
		executeUpdate("Archive SOP with ID " + sop_id, "update SOP set archive_flag = true where sop_id = " + sop_id);
	}
	
	public void unarchiveSOP(int sop_id){
		executeUpdate("Unarchive SOP with ID " + sop_id, "update SOP set archive_flag = false where sop_id = " + sop_id);
	}
	
	// TODO: I think Ryan wants revert to go back to using an old version of an SOP, as in archiving the newer revision and 
	// unarchiving the old one?
	public SOP revertSOP(final int sop_id, final int revision) {
		return executeTransaction(new Transaction<SOP>() {
			@Override
			public SOP execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"UPDATE SOP SET version = ? WHERE sop_id = ? ");
					
					stmt.setInt(1, revision);
					stmt.setInt(2, sop_id);
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM SOP WHERE sop_id = ?");
					
					stmt2.setInt(1, sop_id);
					
					resultSet = stmt2.executeQuery();

					SOP result = new SOP(); 
					
					Boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						result.setID(resultSet.getInt(1));
						result.setName(resultSet.getString(2));
						result.setDescription(resultSet.getString(3));
						result.setPriority(resultSet.getInt(4));
						result.setRevision(resultSet.getInt(5));
						result.setAuthorID(resultSet.getInt(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
					}


					if (!found) {
						System.out.println("SOP with ID <" + sop_id + "> was not found in the SOP table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2); 
				}
			}
		});
	}
	
	public ArrayList<SOP> findSOPsByPosition(int position_id){
		try{
			return executeQuery("Get SOPs By Position", "select " + sopPieces + " from PositionSOP, SOP " + 
					"where position_id = " + position_id, sopResFormat);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void addSOPtoPosition(int positionID, int sopID){
		executeUpdate("Insert Position " + positionID + " and SOP " + sopID + " connection", 
				"insert into PositionSOP (position_id, sop_id) values (" + positionID + ", " + sopID + ")");
	}
	
	/*
	public void changeSOPPriority(SOP sop, int priority){
		executeUpdate("Change SOP " + sop.getID() + " to Priority " + priority, "update SOP set priority = " + priority +
				"where sop_id = " + sop.getID());
		sop.setPriority(priority);
	}*/
	
	// TODO: Remove this to use about method? Not sure at the moment
	public SOP changeSOPPriority(final int sop_id, final int priority) {
		return executeTransaction(new Transaction<SOP>() {
			@Override
			public SOP execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"UPDATE SOP SET priority = ? WHERE sop_id = ? ");
					
					stmt.setInt(1, priority);
					stmt.setInt(2, sop_id);
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM SOP WHERE sop_id = ?");
					
					stmt2.setInt(1, sop_id);
					
					resultSet = stmt2.executeQuery();

					SOP result = new SOP(); 
					
					Boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						result.setID(resultSet.getInt(1));
						result.setName(resultSet.getString(2));
						result.setDescription(resultSet.getString(3));
						result.setPriority(resultSet.getInt(4));
						result.setRevision(resultSet.getInt(5));
						result.setAuthorID(resultSet.getInt(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
					}


					if (!found) {
						System.out.println("SOP with ID <" + sop_id + "> was not found in the SOP table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2); 
				}
			}
		});
	}
	
	public static void cleanDB(){
		Database db = new Database();
		System.out.println("Recreating Database...");
		db.recreateDB();
		System.out.println("Creating Tables again...");
		db.createTables();
		System.out.println("Loading initial data...");
		db.loadInitialData();
		System.out.println("Database cleaned.");
	}
	
	public void recreateDB(){
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				Statement drop_stmt = null;
				Statement create_stmt = null;
				try {
					
					drop_stmt = conn.createStatement();
					String drop_sql = "drop database CS481db;";
					System.out.println("execute drop DB");
					drop_stmt.executeUpdate(drop_sql);
					System.out.println("Database dropped.");
					
					create_stmt = conn.createStatement();
					String create_sql = "CREATE database CS481db;"; 
					System.out.println("execute create DB");
					create_stmt.executeUpdate(create_sql);
					System.out.println("database recreated");
					
					return true;
				}
				finally {
					DBUtil.closeQuietly(drop_stmt);
					DBUtil.closeQuietly(create_stmt);					
				}
			}
			
		});
	}
	
	public static void testDB(){
		Database db = new Database();
		db.createTestDB();
		db.createTables();
	}
	
	public static void delTestDB() {
		Database db = new Database();
		db.deleteTestDB();
	}
	
	public void createTestDB(){
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				Statement create_stmt = null;
				Statement use_stmt = null;
				try {					
					create_stmt = conn.createStatement();
					String create_sql = "CREATE database CS481dbtest;"; 
					System.out.println("Creating test database..");
					create_stmt.executeUpdate(create_sql);
					System.out.println("Test database created!");
					
					use_stmt = conn.createStatement();
					String use_sql = "use CS481dbtest;";
					System.out.println("Switching to test database..");
					use_stmt.executeUpdate(use_sql);
					System.out.println("Now using test database");
					
					return true;
				}
				finally {
					DBUtil.closeQuietly(create_stmt);	
					DBUtil.closeQuietly(use_stmt);				
				}
			}
			
		});
	}
	
	public void deleteTestDB(){
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				Statement drop_stmt = null;
				Statement use_stmt = null;
				try {
					
					drop_stmt = conn.createStatement();
					String drop_sql = "drop database CS481dbtest;";
					System.out.println("Deleting test database..");
					drop_stmt.executeUpdate(drop_sql);
					System.out.println("Test database deleted");
					
					use_stmt = conn.createStatement();
					String use_sql = "use CS481db;"; 
					System.out.println("Switching back to main database..");
					use_stmt.executeUpdate(use_sql);
					System.out.println("Now using main database");
					
					return true;
				}
				finally {
					DBUtil.closeQuietly(drop_stmt);
					DBUtil.closeQuietly(use_stmt);					
				}
			}
			
		});
	}
	
	//main method to generate the DB
	public static void main(String[] args) throws IOException{
		Database db = new Database();
		db.createDatabase();
		db.createTables();
		db.loadInitialData();
		System.out.println("Database creation finished");
	}
}
