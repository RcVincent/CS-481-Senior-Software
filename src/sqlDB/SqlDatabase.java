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

	public Integer insertPosition(final Position p) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement insertPos = null;		
				
				ResultSet resultSet1 = null;
					
				try {
				insertPos = conn.prepareStatement(
						"insert into Position values (default, ?, ?, ?)"
				);
				insertPos.setString(1, p.getTitle());
				insertPos.setString(2, p.getDescription());
				insertPos.setInt(3, p.getPriority());
							
				// Execute the update
				insertPos.executeUpdate();
							
				resultSet1 = insertPos.executeQuery();
							
				System.out.println("Position successfully inserted");							

				return 1;
				
				} finally {
					DBUtil.closeQuietly(resultSet1);
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
				
				ResultSet resultSet1 = null;
					
				try {
				insertUser = conn.prepareStatement(
						"insert into User values (default, ?, ?, ?, ?, ?, ?, default, ?)"
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
							
				resultSet1 = insertUser.executeQuery();
							
				System.out.println("User successfully registered!");							
					
				return 1;
				
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(insertUser);			
				}
			} 
		});
	}
	
	public Integer insertSOP(final SOP s, final String filepath) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement insertSOP = null;		
				 
				ResultSet resultSet1 = null;
					
				try {
				insertSOP = conn.prepareStatement(
						"insert into SOP values (default, ?, ?, ?, ?, ?, ?, ?)"
				);
				insertSOP.setString(1, s.getName());
				insertSOP.setString(2, s.getDescription());
				insertSOP.setInt(3, s.getPriority());
				insertSOP.setInt(4, s.getRevision());
				insertSOP.setString(5, filepath);
				insertSOP.setInt(6, s.getAuthorID());
				insertSOP.setBoolean(7, s.getArchiveFlag());
							
				// Execute the update
				insertSOP.executeUpdate();
							
				resultSet1 = insertSOP.executeQuery();
							
				System.out.println("SOP successfully inserted");							
					
				return 1;
				
				} finally {
					DBUtil.closeQuietly(resultSet1);
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
