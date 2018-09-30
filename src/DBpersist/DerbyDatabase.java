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
									" user_password (varchar(40)," +
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
							")"
					);
					sop_stmt.executeUpdate();
					
					System.out.println("SOPs table created");
					//if the method executed this far it was a success 
					return true;
				}
				finally {
					DBUtil.closeQuietly(user_stmt);
					DBUtil.closeQuietly(pos_stmt);
					
				}
			}
			
		});
}
	// retrieve Position information - if we end up not needing these types of methods, just delete
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
					 
				}
				
				catch(IOException e) {
					throw new SQLException("Couldn't read the initial data");
				}
				
				
				//the create tables lists
				PreparedStatement insertUsers = null;
				
				try {
					//set up the users list to be imported 
					System.out.println("Preparing user insertion");
					insertUsers = conn.prepareStatement("insert into user ()");
					//actually do the insert 
					
					for (User u : userList) {
						insertUsers.setString(1, u.getEmail()); 
						insertUsers.setString(2, u.getPassword());
					}
					
					//verify and execute 
					System.out.println("inserting users");
					insertUsers.executeBatch();
					System.out.println("Users table populated");
					
					
					
					
					
					return true;
				}
				finally {
					DBUtil.closeQuietly(insertUsers);
				}
			}	
		});		
	}
	
	
}
