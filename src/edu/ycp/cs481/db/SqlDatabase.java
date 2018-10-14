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

public class SqlDatabase {
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
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				Statement user_stmt = null;
				Statement pos_stmt = null;
				Statement sop_stmt = null;
				Statement pos_sop_stmt = null;
				try {
					
					pos_stmt = conn.createStatement();
					String pos_sql = "CREATE TABLE IF NOT EXISTS Position (" +
									 "position_id INT NOT NULL AUTO_INCREMENT," +
									 "title VARCHAR(80) NOT NULL," +
									 "description VARCHAR(255) NOT NULL," +
									 "priority INT NOT NULL," +
									 "PRIMARY KEY (position_id)," +
									 "UNIQUE INDEX position_id_UNIQUE (position_id ASC) VISIBLE);";
					System.out.println("execute positions");
					pos_stmt.executeUpdate(pos_sql);
					System.out.println("Positions table created");
					
					
					
					System.out.println("Prepare to create the user table");
					user_stmt = conn.createStatement();
					String user_sql = "CREATE TABLE IF NOT EXISTS User (" +
									  "user_id INT NOT NULL AUTO_INCREMENT," +
									  "email VARCHAR(255) NOT NULL," +
									  "password VARCHAR(32) NOT NULL, " +
									  "first_name VARCHAR(80) NOT NULL, " +
									  "last_name VARCHAR(80) NOT NULL, " +
									  "admin_flag VARCHAR(32) NOT NULL, " +
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
					System.out.println("execute users");
					user_stmt.executeUpdate(user_sql);
					System.out.println("user table created");

					sop_stmt = conn.createStatement();
					String sop_sql = "CREATE TABLE IF NOT EXISTS SOP (" +
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
					System.out.println("execute SOPs");
					sop_stmt.executeUpdate(sop_sql);
					System.out.println("SOPs table created");
					
					pos_sop_stmt = conn.createStatement();
					String pos_sop_sql = "CREATE TABLE IF NOT EXISTS PositionSOP (" +
										"position_id INT NOT NULL, " +
										"sop_id INT NOT NULL, " +
										"CONSTRAINT FOREIGN KEY (position_id) REFERENCES Position (position_id), " + 
										"CONSTRAINT FOREIGN KEY (sop_id) REFERENCES SOP (sop_id) " +
										");";
					System.out.println("Execute create PositionSOP table");
					pos_sop_stmt.executeUpdate(pos_sop_sql);
					System.out.println("PositionSOP table created");
					return true;
				}
				finally {
					DBUtil.closeQuietly(user_stmt);
					DBUtil.closeQuietly(pos_stmt);
					DBUtil.closeQuietly(sop_stmt);
					DBUtil.closeQuietly(pos_sop_stmt);
					
				}
			}
			
		});
	}

	public void createDatabase() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				Statement db_stmt = null;
				try {
					
					db_stmt = conn.createStatement();
					String db_sql = "create database if not exists cs481db;";
					System.out.println("Creating database");
					db_stmt.executeUpdate(db_sql);
					System.out.println("Database created with name CS481db");
					
					return true;
				}
				finally {
					DBUtil.closeQuietly(db_stmt);
					
				}
			}
			
		});
	}
	
	public void loadInitialData() {
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
						insertUser.setString(1, s.getName());
						insertUser.setString(2, s.getDescription());
						insertUser.setInt(3, s.getPriority());
						insertUser.setInt(4, s.getRevision());
						insertUser.setInt(5, s.getAuthorID());
						insertUser.setBoolean(6, s.getArchiveFlag());
						insertUser.addBatch();
					}
					
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
}

	public Integer insertPosition(final Position p) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement insertPos = null;		
				
					
				try {
				insertPos = conn.prepareStatement(
						"insert into Position (title, description, priority)"
						+ " values (?, ?, ?)"
				);
				insertPos.setString(1, p.getTitle());
				insertPos.setString(2, p.getDescription());
				insertPos.setInt(3, p.getPriority());
							
				// Execute the update
				insertPos.executeUpdate();
							
				System.out.println("Position successfully inserted");							

				return 1;
				
				} finally {
					DBUtil.closeQuietly(insertPos);			
				}
			} 
		});
	}
	
	public Integer insertUser(final User u) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement insertUser = null;		

				try {
				insertUser = conn.prepareStatement(
						"insert into User "
						+ "(email, password, first_name, last_name, admin_flag, archive_flag, position_id)"
						+ " values (?, ?, ?, ?, ?, ?, ?)"
				);
				insertUser.setString(1, u.getEmail());
				insertUser.setString(2, u.getPassword());
				insertUser.setString(3, u.getFirstname());
				insertUser.setString(4, u.getLastname());
				insertUser.setString(5, u.isAdminFlag());
				insertUser.setBoolean(6, u.isArchiveFlag());
				insertUser.setInt(7, u.getPosition().getID());
							
				// Execute the update
				insertUser.executeUpdate();
							
				System.out.println("User successfully registered!");							
					
				return 1;
				
				} finally {
					DBUtil.closeQuietly(insertUser);			
				}
			} 
		});
	}
	
	public Integer insertSOP(final SOP s) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement insertSOP = null;		
					/*"sop_id INT NOT NULL AUTO_INCREMENT, " +
									  "title VARCHAR(80) NOT NULL, " +
									  "description VARCHAR(255) NOT NULL, " +
									  "priority INT NOT NULL, " +
									  "version INT NOT NULL, " +
									  "author_id INT NOT NULL, " +
									  "archive_flag TINYINT NOT NULL, " +*/
				try {
				insertSOP = conn.prepareStatement(
						"insert into SOP "
						+ "(title, description, priority, version, author_id, archive_flag)"
						+ " values (?, ?, ?, ?, ?, ?)"
				);
				insertSOP.setString(1, s.getName());
				insertSOP.setString(2, s.getDescription());
				insertSOP.setInt(3, s.getPriority());
				insertSOP.setInt(4, s.getRevision());
				//insertSOP.setString(5, filepath);
				insertSOP.setInt(5, s.getAuthorID());
				insertSOP.setBoolean(6, s.getArchiveFlag());
							
				// Execute the update
				insertSOP.executeUpdate();
							
				System.out.println("SOP successfully inserted");							
					
				return 1;
				
				} finally {
					DBUtil.closeQuietly(insertSOP);			
				}
			} 
		});
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
					
					Position position = new Position();
					
					position.setID(resultSet.getInt(1));
					position.setTitle(resultSet.getString(2));
					position.setDescription(resultSet.getString(3));
					position.setPriority(resultSet.getInt(4));
					
					return position;
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
						u.setAdminFlag(resultSet.getString(6));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
						result.setAdminFlag(resultSet.getString(6));
						result.setArchiveFlag(resultSet.getBoolean(7));
						result.getPosition().setID(resultSet.getInt(9));
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
					//	s.setFilepath(resultSet.getString(6);
						s.setAuthorID(resultSet.getInt(7));
						
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
					//	s.setFilepath(resultSet.getString(6);
						result.setAuthorID(resultSet.getInt(7));
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
		SqlDatabase db = new SqlDatabase();
		System.out.println("Recreating Database...");
		db.recreateDB();
		System.out.println("Creating Tables again...");
		db.createTables();
		// TODO: Load Initial Data (Currently method isn't setup)
		/*System.out.println("Loading initial data...");
		db.loadInitialData();
		*/
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
		SqlDatabase db = new SqlDatabase();
		db.createTestDB();
		db.createTables();
	}
	
	public static void delTestDB() {
		SqlDatabase db = new SqlDatabase();
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
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables ");
		SqlDatabase db = new SqlDatabase(); 
		db.createTables();
		db.createDatabase();
		System.out.println("Users");
		System.out.println("SOPs");
		System.out.println("Positions");
	}
}
