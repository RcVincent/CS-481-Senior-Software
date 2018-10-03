package DBpersist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import DBpersist.IDatabase;
import DBpersist.DBUtil;
import DBpersist.PersistenceException;
import DBpersist.DerbyDatabase.Transaction;
import model.Position;
import model.SOP;
import model.User;



public class DerbyDatabase implements IDatabase {

	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}

	public interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 100;
	
	//**********************************************************************************************
	//**********************************************************************************************
	//methods/querries 
	
	
	
	
	//**********************************************************************************************
	//**********************************************************************************************
	
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
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
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
		Connection conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");

		// Set autocommit to false to allow multiple the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement user_stmt = null;
				PreparedStatement pos_stmt = null;
				PreparedStatement sop_stmt = null;
				try {
					System.out.println("Prepare to create the user table");
					user_stmt = conn.prepareStatement(
							"create table user (" +
									" user_id integer primary key" +
									" generated always as identity (start with 1, increment by 1), " +
									" user_email varchar(40), " +
									" user_password varchar(40)," +
									" user_adminFlag"+
									" user_firstname varchar(40) " +
									" user_lastname varchar(40)" +
									") "
							);
					
					System.out.println("execute users");
					user_stmt.executeUpdate();
					//Print a success if it executes to this point 
					System.out.println("user table created");
					
					
					pos_stmt = conn.prepareStatement(
							"create table position (" +
							"	positon_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	title varchar(40)," +
							"	requirements integer," + // requirements = sop.id?
							"	priority integer" +
							")"
					);
					
					System.out.println("execute positions");
					pos_stmt.executeUpdate();
					
					System.out.println("Positions table created");
					
					sop_stmt = conn.prepareStatement(
							"create table sop (" +
							"	sop_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	name varchar(80)," +
							"	desc varchar(400)," +
							"	author_id integer," + // author_id = user.id?
							"	priority integer" + 
							"   revision integer" +
							")"
					);
					System.out.println("execute SOPs");
					sop_stmt.executeUpdate();
					
					System.out.println("SOPs table created");
					//if the method executed this far it was a success 
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
		DerbyDatabase db = new DerbyDatabase(); 
		db.createTables();
		System.out.println("Users");
		System.out.println("SOPs");
		System.out.println("Positions");
		
		System.out.println("Sucess!");
	}
}
