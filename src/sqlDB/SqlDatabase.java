package sqlDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import DBpersist.IDatabase;
import DBpersist.InitialData;
import DBpersist.DBUtil;
import DBpersist.DerbyDatabase;
import DBpersist.PersistenceException;
import DBpersist.DerbyDatabase.Transaction;
import model.Position;
import model.SOP;
import model.User;

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
				try {
					
					pos_stmt = conn.createStatement();
					String pos_sql = "CREATE TABLE IF NOT EXISTS Position (" +
									 "position_id INT NOT NULL," +
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
									  "user_id INT NOT NULL," +
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
					System.out.println("execute users");
					user_stmt.executeUpdate(user_sql);
					System.out.println("user table created");

					sop_stmt = conn.createStatement();
					String sop_sql = "CREATE TABLE IF NOT EXISTS SOP (" +
							"sop_id INT NOT NULL, " +
							"title VARCHAR(80) NOT NULL, " +
							"description VARCHAR(255) NOT NULL, " +
							"priority INT NOT NULL, " +
							"version INT NOT NULL, " +
							"filepath VARCHAR(255) NOT NULL, " +
							"author_id INT NOT NULL, " +
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
					return true;
				}
				finally {
					DBUtil.closeQuietly(user_stmt);
					DBUtil.closeQuietly(pos_stmt);
					DBUtil.closeQuietly(sop_stmt);
					
				}
			}
			
		});
}
	
	private void loadUser(User user, ResultSet resultSet, int index) throws SQLException {
		user.setUserID(resultSet.getInt(index++));
		user.setEmail(resultSet.getString(index++)); 
		user.setPassword(resultSet.getString(index++));
		user.setFirstname(resultSet.getString(index++));
		user.setLastname(resultSet.getString(index++));
		user.setAdminFlag(resultSet.getString(index++));
		user.setArchiveFlag(resultSet.getBoolean(index++));
	}
	
	private void loadSOP(SOP sop, ResultSet resultSet, int index) throws SQLException {
		sop.setID(resultSet.getInt(index++));
		sop.setName(resultSet.getString(index++));
		sop.setDescription(resultSet.getString(index++));
		sop.setPriority(resultSet.getInt(index++));
		sop.setRevision(resultSet.getInt(index++));
	}
	
	private void loadPosition(Position position, ResultSet resultSet, int index) throws SQLException {
		position.setID(resultSet.getInt(index++));
		position.setTitle(resultSet.getString(index++));
		position.setPriority(resultSet.getInt(index++));
	}
	
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				
				//
				List<User> userList;
				List<SOP> sopList;
				List<Position> positionList;
				
				try {
					System.out.println("Init userlist");
					userList = InitialData.getUsers();
					positionList = InitialData.getPositions();
					sopList = InitialData.getSOPs();
				}
				
				catch(IOException e) {
					throw new SQLException("Couldn't read the initial data");
				}
				
				
				//the create tables lists
				PreparedStatement insertUsers = null;
				PreparedStatement insertPositions = null;
				PreparedStatement insertSOPs = null;
				
				try {
					//set up the users list to be imported 
					System.out.println("Preparing user insertion");
					insertUsers = conn.prepareStatement("insert into user ()");
					//actually do the insert 
					
					for (User u : userList) {
						insertUsers.setInt(1, u.getUserID());
						insertUsers.setString(2, u.getEmail()); 
						insertUsers.setString(3, u.getPassword());
						insertUsers.setString(4, u.getFirstname());
						insertUsers.setString(5, u.getLastname());
						insertUsers.setString(6, u.isAdminFlag());
						insertUsers.setBoolean(7, u.isArchiveFlag());
					}
					
					//verify and execute 
					System.out.println("inserting users");
					insertUsers.executeBatch();
					System.out.println("Users table populated");
				
					// Set up the Positions list to be imported 
					System.out.println("Preparing position insertion");
					insertPositions = conn.prepareStatement("insert into position ()");
					
					for (Position p : positionList) {
						insertPositions.setInt(1, p.getID());
						insertPositions.setString(2, p.getTitle());
						insertPositions.setInt(3, p.getPriority());
					}
					
					// Insert Positions
					System.out.println("Inserting positions");
					insertPositions.executeBatch();
					System.out.println("Positions table populated");
					
					// Set up the SOPs list to be imported 
					System.out.println("Preparing sop insertion");
					insertSOPs = conn.prepareStatement("insert into sop ()");
					
					for (SOP s : sopList) {
						insertSOPs.setInt(1, s.getID());
						insertSOPs.setString(2, s.getName());
						insertSOPs.setString(3, s.getDescription());
						insertSOPs.setInt(4, s.getPriority());
						insertSOPs.setInt(5, s.getRevision());
					}
					
					// Insert SOPs
					System.out.println("Inserting sops");
					insertSOPs.executeBatch();
					System.out.println("SOPs table populated");

					return true;
				}
				finally {
					DBUtil.closeQuietly(insertUsers);
				}
			}	
		});		
	}
	
	//main method to generate the DB
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables ");
		SqlDatabase db = new SqlDatabase(); 
		db.createTables();
		System.out.println("Users");
		System.out.println("SOPs");
		System.out.println("Positions");
	}
}