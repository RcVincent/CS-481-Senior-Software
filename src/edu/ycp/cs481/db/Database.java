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

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS481db?user=root&password=password");

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
				  "password VARCHAR(32) NOT NULL, " +
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
	
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<User> userList;
				List<Position> posList;
				List<SOP> SOPList;
				
				userList       = new InitialData().getInitialUsers();
				posList		   = new InitialData().getInitialPositions();
				SOPList		   = new InitialData().getInitialSOPs();
			

				PreparedStatement insertUser       = null;
				PreparedStatement insertPos		   = null;
				PreparedStatement insertSOP	       = null;
				PreparedStatement insertPosSOP	   = null;

				try {
					// Insert Positions
					insertPos = conn.prepareStatement(
							"insert into Position "
							+ "(title, description, priority)"
							+ " values (?, ?, ?)"
					);
					for (Position p : posList) {
						insertPos.setString(1, p.getTitle());
						insertPos.setString(2, p.getDescription());
						insertPos.setInt(3, p.getPriority());
						insertPos.addBatch();
					}
					insertPos.executeBatch();
					
					System.out.println("Position table populated");
					
					// Insert Users
					insertUser = conn.prepareStatement(
							"insert into User "
							+ "(email, password, first_name, last_name, admin_flag, archive_flag, position_id)"
							+ " values (?, ?, ?, ?, ?, ?, ?)"
					);
					for (User u : userList) {
						insertUser.setString(1, u.getEmail());
						insertUser.setString(2, u.getPassword());
						insertUser.setString(3, u.getFirstname());
						insertUser.setString(4, u.getLastname());
						insertUser.setBoolean(5, u.isAdminFlag());
						insertUser.setBoolean(6, u.isArchiveFlag());
						insertUser.setInt(7, u.getPosition().getID());
						insertUser.addBatch();
					}
					insertUser.executeBatch();
					
					System.out.println("User table populated");
					
					// Insert SOPs
					insertSOP = conn.prepareStatement(
							"insert into SOP "
							+ "(title, description, priority, version, author_id, archive_flag)"
							+ " values (?, ?, ?, ?, ?, ?)"
					);
					for (SOP s : SOPList) {
						insertSOP.setString(1, s.getName());
						insertSOP.setString(2, s.getDescription());
						insertSOP.setInt(3, s.getPriority());
						insertSOP.setInt(4, s.getRevision());
						insertSOP.setInt(5, s.getAuthorID());
						insertSOP.setBoolean(6, s.getArchiveFlag());
						insertSOP.addBatch();
					}
					insertSOP.executeBatch();
					
					System.out.println("SOP table populated");

					// TODO
					/*// Insert PositionSOPs
					insertPosSOP = conn.prepareStatement(
							"insert into PositionSOP "
							+ "(position_id, sop_id)"
							+ " values (?, ?)"
					);
					for (Position p: posList) {
						insertPosSOP.setInt(1, p.getID());
						insertPosSOP.setInt(2, p.getRequirements());
					}
					*/
					return true;
				} finally {
					DBUtil.closeQuietly(insertUser);
					DBUtil.closeQuietly(insertPos);
					DBUtil.closeQuietly(insertSOP);
					DBUtil.closeQuietly(insertPosSOP);
				}
			}
		});
	}

	
	/*public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<User> userList;
				List<Position> posList;
				List<SOP> SOPList;
				
				try {
					userList       = InitialData.getUsers();
					posList		   = InitialData.getPositions();
					SOPList		   = InitialData.getSOPs();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertUser       = null;
				PreparedStatement insertPos		   = null;
				PreparedStatement insertSOP	       = null;

				try {
					// Insert Positions
					insertPos = conn.prepareStatement(
							"insert into Position "
							+ "(title, description, priority)"
							+ " values (?, ?, ?)"
					);
					for (Position p : posList) {
						insertPos.setString(1, p.getTitle());
						insertPos.setString(2, p.getDescription());
						insertPos.setInt(3, p.getPriority());
						insertPos.addBatch();
					}
					insertPos.executeBatch();
					
					System.out.println("Position table populated");
					
					// Insert Users
					insertUser = conn.prepareStatement(
							"insert into User "
							+ "(email, password, first_name, last_name, admin_flag, archive_flag, position_id)"
							+ " values (?, ?, ?, ?, ?, ?, ?)"
					);
					for (User u : userList) {
						insertUser.setString(1, u.getEmail());
						insertUser.setString(2, u.getPassword());
						insertUser.setString(3, u.getFirstname());
						insertUser.setString(4, u.getLastname());
						insertUser.setString(5, u.isAdminFlag());
						insertUser.setBoolean(6, u.isArchiveFlag());
						insertUser.setInt(7, u.getPosition().getID());
						insertUser.addBatch();
					}
					insertUser.executeBatch();
					
					System.out.println("User table populated");
					
					// Insert SOPs
					insertSOP = conn.prepareStatement(
							"insert into SOP "
							+ "(title, description, priority, version, author_id, archive_flag)"
							+ " values (?, ?, ?, ?, ?, ?)"
					);
					for (SOP s : SOPList) {
						insertSOP.setString(1, s.getName());
						insertSOP.setString(2, s.getDescription());
						insertSOP.setInt(3, s.getPriority());
						insertSOP.setInt(4, s.getRevision());
						insertSOP.setInt(5, s.getAuthorID());
						insertSOP.setBoolean(6, s.getArchiveFlag());
						insertSOP.addBatch();
					}
					insertSOP.executeBatch();
					
					System.out.println("SOP table populated");
					// TODO:  PositionSOP junction table
					return true;
				} finally {
					DBUtil.closeQuietly(insertUser);
					DBUtil.closeQuietly(insertPos);
					DBUtil.closeQuietly(insertSOP);
				}
			}
		});
	}*/
	
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
							insertSQL += args[i] + ") values (";
						}else{
							insertSQL += args[i] + ", ";
						}
					}
					for(int i = 0; i < values.length; i++){
						if(values[i].equalsIgnoreCase("true") || values[i].equalsIgnoreCase("false")){
							insertSQL += values[i];
						}else{
							insertSQL += "'" + values[i] + "'";
						}
						if(i == values.length - 1){
							insertSQL += ");";
						}else{
							insertSQL += ", ";
						}
					}
					insert.executeUpdate(insertSQL);
					System.out.println("Executed Insert of a " + table + "!");
					
					selectID = conn.createStatement();
					String selectSQL = "select " + id_str + " from " + table + " where ";
					for(int i = 0; i < args.length; i++){
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
	
	public Integer insertUser(User u){
		return insertAndGetID("User", "user_id", 
				new String[]{"email", "password", "first_name", "last_name", "admin_flag", "archive_flag", "position_id"}, 
				new String[]{u.getEmail(), u.getPassword(), u.getFirstname(), u.getLastname(), String.valueOf(u.isAdminFlag()),
						String.valueOf(u.isArchiveFlag()), String.valueOf(u.getPosition().getID())});
	}
	
	public Integer insertSOP(SOP s){
		return insertAndGetID("SOP", "sop_id", 
				new String[]{"title", "description", "priority", "version", "author_id", "archive_flag"}, 
				new String[]{s.getName(), s.getDescription(), String.valueOf(s.getPriority()), String.valueOf(s.getRevision()), 
						String.valueOf(s.getAuthorID()), String.valueOf(s.getArchiveFlag())});
	}
	
	public List<Position> findAllPositions() {
		return executeTransaction(new Transaction<List<Position>>() {
			@Override
			public List<Position> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM Position");
					
					List<Position> result = new ArrayList<Position>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						Position p = new Position();
						p.setID(resultSet.getInt(1));
						p.setTitle(resultSet.getString(2));
						p.setDescription(resultSet.getString(3));
						p.setPriority(resultSet.getInt(4));
						
						result.add(p);
					}
					
					if (!found) {
						System.out.println("No Positions were found in the database");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Position findPositionByID(int position_id) {
		return executeTransaction(new Transaction<Position>() {
			@Override
			public Position execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM Position WHERE position_id = ?");
					stmt.setInt(1, position_id);
					
					Position result = new Position();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						result.setID(resultSet.getInt(1));
						result.setTitle(resultSet.getString(2));
						result.setDescription(resultSet.getString(3));
						result.setPriority(resultSet.getInt(4));
					}
					
					if (!found) {
						System.out.println("No Positions were found with this ID");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Position getPositionByUser(int userID){
		return executeTransaction(new Transaction<Position>(){
			@Override
			public Position execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try{
					stmt = conn.prepareStatement(
							"SELECT Position.position_id, Position.title, Position.description, Position.priority " +
							"from Position, User where user_id = ? and Position.position_id = User.position_id");
					stmt.setInt(1, userID);
					
					resultSet = stmt.executeQuery();
					
					Position result = new Position();
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						result.setID(resultSet.getInt(1));
						result.setTitle(resultSet.getString(2));
						result.setDescription(resultSet.getString(3));
						result.setPriority(resultSet.getInt(4));
					}
					
					if (!found) {
						System.out.println("No Positions were found for this User");
					}
					
					return result;
				}finally{
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}

	public ArrayList<Position> getPositionBySOPID(int SOPID){
		return executeTransaction(new Transaction<ArrayList<Position>>(){
			@Override
			public ArrayList<Position> execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try{
					stmt = conn.prepareStatement(
							"SELECT Position.position_id, Position.title, Position.description, Position.priority " + 
							"from Position, PositionSOP where PositionSOP.sop_id = ? and " +
							"Position.position_id = PositionSOP.position_id");
					stmt.setInt(1, SOPID);
					
					resultSet = stmt.executeQuery();
					
					ArrayList<Position> positions = new ArrayList<Position>();
					
					Position position;
					
					while(resultSet.next()){
						position = new Position();
						position.setID(resultSet.getInt(1));
						position.setTitle(resultSet.getString(2));
						position.setDescription(resultSet.getString(3));
						position.setPriority(resultSet.getInt(4));
						positions.add(position);
					}
					
					return positions;
				}finally{
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}

	public ArrayList<Position> getPositionByName(String title){
		return executeTransaction(new Transaction<ArrayList<Position>>(){
			@Override
			public ArrayList<Position> execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try{
					stmt = conn.prepareStatement(
							"SELECT * from Position where title = ?");
					stmt.setString(1, title);
					
					resultSet = stmt.executeQuery();
					
					ArrayList<Position> positions = new ArrayList<Position>();
					
					Position position;
					
					while(resultSet.next()){
						position = new Position();
						position.setID(resultSet.getInt(1));
						position.setTitle(resultSet.getString(2));
						position.setDescription(resultSet.getString(3));
						position.setPriority(resultSet.getInt(4));
						positions.add(position);
					}
					
					return positions;
				}finally{
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}

	public ArrayList<Position> getPositionByPriority(int priority){
		return executeTransaction(new Transaction<ArrayList<Position>>(){
			@Override
			public ArrayList<Position> execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try{
					stmt = conn.prepareStatement(
							"SELECT * from Position where priority = ?");
					stmt.setInt(1, priority);
					
					resultSet = stmt.executeQuery();
					
					ArrayList<Position> positions = new ArrayList<Position>();
					
					Position position;
					
					while(resultSet.next()){
						position = new Position();
						position.setID(resultSet.getInt(1));
						position.setTitle(resultSet.getString(2));
						position.setDescription(resultSet.getString(3));
						position.setPriority(resultSet.getInt(4));
						positions.add(position);
					}
					
					return positions;
				}finally{
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}

	public Position changePositionPriority(final Position p, final int priority) {
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
					stmt.setInt(2, p.getID());
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM User WHERE position_id = ?");
					
					stmt2.setInt(1, p.getID());
					
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
						System.out.println("Position with ID "+ p.getID() +" was not found in the Position table");
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

	public Boolean deletePosition(int position_id){
		return executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				int removed = 0;
				
				try{
					stmt = conn.prepareStatement(
							"DELETE FROM Position where position_id = ?");
					stmt.setInt(1, position_id);
					
					removed = stmt.executeUpdate();
					
					return removed == 1;
				}finally{
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public List<User> findAllUsers() {
		return executeTransaction(new Transaction<List<User>>() {
			@Override
			public List<User> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM User");
					
					List<User> result = new ArrayList<User>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						User u = new User();
						u.setUserID(resultSet.getInt(1));
						u.setEmail(resultSet.getString(2));
						u.setPassword(resultSet.getString(3));
						u.setFirstname(resultSet.getString(4));
						u.setLastname(resultSet.getString(5));
						u.setAdminFlag(resultSet.getBoolean(6));
						u.setArchiveFlag(resultSet.getBoolean(7));
						u.setPosition(findPositionByID(resultSet.getInt(9)));			
						
						result.add(u);
					}
					
					if (!found) {
						System.out.println("No Users were found in the database");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public User getUserByID(final int ID) {

		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT * FROM User WHERE user_id = ?");
					
					stmt.setInt(1, ID);
					resultSet = stmt.executeQuery();

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

					// check if the title was found
					if (!found) {
						System.out.println("User with ID <" + ID + "> was not found in the Users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public User getUserByEmail(final String email) {

		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT * FROM User WHERE email = ?");
					
					stmt.setString(1, email);
					resultSet = stmt.executeQuery();

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

					// check if the title was found
					if (!found) {
						System.out.println("User with email <" + email + "> was not found in the Users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public User getUserByFirstName(final String firstname) {

		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT * FROM User WHERE first_name = ?");
					
					stmt.setString(1, firstname);
					resultSet = stmt.executeQuery();

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

					// check if the title was found
					if (!found) {
						System.out.println("User with firstname <" + firstname + "> was not found in the Users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public User getUserByLastName(final String lastname) {

		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT * FROM User WHERE last_name = ?");
					
					stmt.setString(1, lastname);
					resultSet = stmt.executeQuery();

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

					// check if the title was found
					if (!found) {
						System.out.println("User with lastname <" + lastname + "> was not found in the Users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public User changeUserPassword(final String email, final String oldPass, final String newPass) {

		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"UPDATE User SET password = ? WHERE email = ? AND password = ? ");
					
					stmt.setString(1, newPass);
					stmt.setString(2, email);
					stmt.setString(3, oldPass);
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM User WHERE email = ? and password = ?");
					
					stmt2.setString(1, email);
					stmt2.setString(2, newPass);
					
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

					// check if the title was found
					if (!found) {
						System.out.println("User with email <" + email + "> was not found in the Users table");
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
	
	public User changeUserEmail(final String oldEmail, final String newEmail, final String Pass) {

		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"UPDATE User SET email = ? WHERE email = ? AND password = ? ");
					
					stmt.setString(1, newEmail);
					stmt.setString(2, oldEmail);
					stmt.setString(3, Pass);
					stmt.executeUpdate();
					
					
					stmt2 = conn.prepareStatement(
							"SELECT * FROM User WHERE email = ? and password = ?");
					
					stmt2.setString(1, newEmail);
					stmt2.setString(2, Pass);
					
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

					// check if the title was found
					if (!found) {
						System.out.println("User with email <" + newEmail + "> was not found in the Users table");
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
	
	public Boolean archiveUser(final int user_id) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT archive_flag FROM User where user_id = ?");
					
					stmt.setInt(1, user_id);
					resultSet = stmt.executeQuery();
					
					boolean result = !resultSet.getBoolean(1);
					
					
					stmt2 = conn.prepareStatement(
							"UPDATE User SET archive_flag = ? WHERE user_id = ? ");
					
					stmt2.setBoolean(1, result);
					stmt2.setInt(2, user_id);
					stmt2.executeUpdate();
					
					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2); 
				}
			}
		});
	}

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
	
	public User findUserByPosition(final int position_id) {
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT * FROM User WHERE position_id = ?");
					
					stmt.setInt(1, position_id);
					resultSet = stmt.executeQuery();

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

					// check if the title was found
					if (!found) {
						System.out.println("User with Position ID <" + position_id + "> was not found in the Users table");
					}

					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
		
	public List<SOP> findAllSOPs() {
		return executeTransaction(new Transaction<List<SOP>>() {
			@Override
			public List<SOP> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM SOP");
					
					List<SOP> result = new ArrayList<SOP>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						SOP s = new SOP();
						s.setID(resultSet.getInt(1));
						s.setName(resultSet.getString(2));
						s.setDescription(resultSet.getString(3));
						s.setPriority(resultSet.getInt(4));
						s.setRevision(resultSet.getInt(5));
						s.setAuthorID(resultSet.getInt(6));
						s.setArchiveFlag(resultSet.getBoolean(7));
						
						result.add(s);
					}
					
					if (!found) {
						System.out.println("No SOPs were found in the database");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public SOP findSOPbyID(final int id) {
		return executeTransaction(new Transaction<SOP>() {
			@Override
			public SOP execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM SOP WHERE sop_id = ?");
					
					stmt.setInt(1, id);
					
					SOP result = new SOP();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
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
						System.out.println("No SOPs were found with that ID");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public List<SOP> findSOPbyName(final String name) {
		return executeTransaction(new Transaction<List<SOP>>() {
			@Override
			public List<SOP> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM SOP WHERE title = ?");
					
					stmt.setString(1, name);
					
					List<SOP> result = new ArrayList<SOP>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						SOP s = new SOP();
						s.setID(resultSet.getInt(1));
						s.setName(resultSet.getString(2));
						s.setDescription(resultSet.getString(3));
						s.setPriority(resultSet.getInt(4));
						s.setRevision(resultSet.getInt(5));
					//	s.setFilepath(resultSet.getString(6);
						s.setAuthorID(resultSet.getInt(7));
						
						result.add(s);
					}
					
					if (!found) {
						System.out.println("No SOPs were found with that name");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public List<SOP> findSOPbyPriority(final int priority) {
		return executeTransaction(new Transaction<List<SOP>>() {
			@Override
			public List<SOP> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM SOP WHERE priority = ?");
					
					stmt.setInt(1, priority);
					
					List<SOP> result = new ArrayList<SOP>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						SOP s = new SOP();
						s.setID(resultSet.getInt(1));
						s.setName(resultSet.getString(2));
						s.setDescription(resultSet.getString(3));
						s.setPriority(resultSet.getInt(4));
						s.setRevision(resultSet.getInt(5));
					//	s.setFilepath(resultSet.getString(6);
						s.setAuthorID(resultSet.getInt(7));
						
						result.add(s);
					}
					
					if (!found) {
						System.out.println("No SOPs were found with that priority");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public List<SOP> findSOPbyVersion(final int version) {
		return executeTransaction(new Transaction<List<SOP>>() {
			@Override
			public List<SOP> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM SOP WHERE version = ?");
					
					stmt.setInt(1, version);
					
					List<SOP> result = new ArrayList<SOP>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						SOP s = new SOP();
						s.setID(resultSet.getInt(1));
						s.setName(resultSet.getString(2));
						s.setDescription(resultSet.getString(3));
						s.setPriority(resultSet.getInt(4));
						s.setRevision(resultSet.getInt(5));
					//	s.setFilepath(resultSet.getString(6);
						s.setAuthorID(resultSet.getInt(7));
						
						result.add(s);
					}
					
					if (!found) {
						System.out.println("No SOPs were found with that version");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public List<SOP> findSOPbyAuthorID(final int id) {
		return executeTransaction(new Transaction<List<SOP>>() {
			@Override
			public List<SOP> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM SOP WHERE author_id = ?");
					
					stmt.setInt(1, id);
					
					List<SOP> result = new ArrayList<SOP>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						SOP s = new SOP();
						s.setID(resultSet.getInt(1));
						s.setName(resultSet.getString(2));
						s.setDescription(resultSet.getString(3));
						s.setPriority(resultSet.getInt(4));
						s.setRevision(resultSet.getInt(5));
					//	s.setFilepath(resultSet.getString(6);
						s.setAuthorID(resultSet.getInt(7));
						
						result.add(s);
					}
					
					if (!found) {
						System.out.println("No SOPs were found with that author");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Boolean archiveSOP(final int sop_id) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;


				try{
					stmt = conn.prepareStatement(
							"SELECT archive_flag FROM SOP where sop_id = ?");
					
					stmt.setInt(1, sop_id);
					resultSet = stmt.executeQuery();
					
					boolean result = !resultSet.getBoolean(1);
					
					
					stmt2 = conn.prepareStatement(
							"UPDATE SOP SET archive_flag = ? WHERE sop_id = ? ");
					
					stmt2.setBoolean(1, result);
					stmt2.setInt(2, sop_id);
					stmt2.executeUpdate();
					
					return result;


				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2); 
				}
			}
		});
	}

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
						result.setAuthorID(resultSet.getInt(7));
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
	
	public SOP findSOPthruPosition(final int user_id) {
		// TODO: User -> Position -> requirements
		return null;
	}
	
	public List<SOP> findSOPbyPosition(final int position_id) {
		return executeTransaction(new Transaction<List<SOP>>() {
			@Override
			public List<SOP> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"SELECT * FROM PositionSOP WHERE position_id = ?");
					
					stmt.setInt(1, position_id);
					
					List<SOP> result = new ArrayList<SOP>();
					
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						SOP s = new SOP();
						s.setID(resultSet.getInt(1));
						s.setName(resultSet.getString(2));
						s.setDescription(resultSet.getString(3));
						s.setPriority(resultSet.getInt(4));
						s.setRevision(resultSet.getInt(5));
						s.setAuthorID(resultSet.getInt(7));
						
						result.add(s);
					}
					
					if (!found) {
						System.out.println("No SOPs were found for that position");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Boolean addSOPtoPosition(final int position_id, final int sop_id) {
		return executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
							"insert into PositionSOP (position_id, sop_id) values (?, ?)");
					stmt.setInt(1, position_id);
					stmt.setInt(2, sop_id);
					
					stmt.executeUpdate();
					
					return true;
				}finally{
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
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
						result.setAuthorID(resultSet.getInt(7));
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
