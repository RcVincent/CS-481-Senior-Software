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
import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.UserController;

public class Database {
	public String positionPieces = "Position.position_id, Position.title, Position.description, Position.priority";
	public String sopPieces = "SOP.sop_id, SOP.title, SOP.description, SOP.priority, SOP.version, SOP.author_id, SOP.archive_flag";
	public String dbName = "";
	
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
				PositionController pc = new PositionController();
				u.setPosition(pc.searchForPositions(resultSet.getInt(9), null, null, -1).get(0));
				
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
	
	public QueryResultFormat<ArrayList<User>> getUserResFormat(){
		return userResFormat;
	}
	
	public QueryResultFormat<ArrayList<Position>> getPosResFormat(){
		return posResFormat;
	}
	
	public String getPositionPieces(){
		return positionPieces;
	}
	
	public QueryResultFormat<ArrayList<SOP>> getSopResFormat(){
		return sopResFormat;
	}
	
	public String getSopPieces(){
		return sopPieces;
	}

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName +"?user=root&password=password");

		conn.setAutoCommit(false);

		return conn;
	}
	
	/*
	 * This method is used in executing a list of updates to the database (e.g. create tables, update data, etc.)
	 */
	public boolean executeUpdates(ArrayList<String> names, ArrayList<String> sqls){
		return executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				ArrayList<Statement> stmts = new ArrayList<Statement>();
				try{
					boolean hasNames = names != null;
					if(hasNames && names.size() != sqls.size()){
						throw new IllegalArgumentException("Must have all sql statements named or pass null names array!");
					}
					for(int i = 0; i < sqls.size(); i++){
						if(hasNames){
							System.out.println("Starting " + names.get(i));
						}
						stmts.add(conn.createStatement());
						stmts.get(i).executeUpdate(sqls.get(i));
						if(hasNames){
							System.out.println("Finished " + names.get(i));
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
		ArrayList<String> names = new ArrayList<String>();
		names.add(name);
		
		ArrayList<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		
		return executeUpdates(names, sqls);
	}
	
	public void createDatabase(){
		//executeUpdate("Dropping old database..", "drop database cs481db");
		executeUpdate("Creating CS481DB database", "create database if not exists cs481db");
		this.dbName = "cs481db";
	}
	
	public void createTables(){
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> sqls = new ArrayList<String>();
		
		names.add("Create Position table");
		sqls.add("CREATE TABLE IF NOT EXISTS Position (" +
				 "position_id INT NOT NULL AUTO_INCREMENT," +
				 "title VARCHAR(80) NOT NULL," +
				 "description VARCHAR(255) NOT NULL," +
				 "priority INT NOT NULL," +
				 "PRIMARY KEY (position_id)," +
				 "UNIQUE INDEX position_id_UNIQUE (position_id ASC) VISIBLE);");
		
		names.add("Create Users table");
		sqls.add("CREATE TABLE IF NOT EXISTS User (" +
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
				  "ON UPDATE NO ACTION);");
		
		names.add("Create SOP table");
		sqls.add("CREATE TABLE IF NOT EXISTS SOP (" +
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
				  "ON UPDATE NO ACTION);");
		
		names.add("Create PositionSOP table");
		sqls.add("CREATE TABLE IF NOT EXISTS PositionSOP (" +
					"position_id INT NOT NULL, " +
					"sop_id INT NOT NULL, " +
					"CONSTRAINT FOREIGN KEY (position_id) REFERENCES Position (position_id), " + 
					"CONSTRAINT FOREIGN KEY (sop_id) REFERENCES SOP (sop_id) " +
					");");
		
		executeUpdates(names, sqls);
	}
	
	public void loadInitialData(){
		InitialData initData = new InitialData();
		
		// They must go in this order
		List<Position> posList = initData.getInitialPositions();
		List<User> userList = initData.getInitialUsers();
		List<SOP> sopList = initData.getInitialSOPs();
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> sqls = new ArrayList<String>();
		
		for(Position p: posList){
			names.add("Insert Position " + p.getTitle());
			sqls.add("insert into Position (title, description, priority) " +
					"values ('" + p.getTitle() + "', '" + p.getDescription() + "', " + p.getPriority() + ")");
		}
		
		for(User u: userList){
			names.add("Insert User " + u.getFirstname() + " " + u.getLastname());
			sqls.add("insert into User (email, password, first_name, last_name, admin_flag, archive_flag, " +
					"position_id)  values ('" + u.getEmail() + "', '" + UserController.hashPassword(u.getPassword()) + "', '" + 
					u.getFirstname() + "', '" + u.getLastname() + "', " + u.isAdminFlag() + ", " + u.isArchiveFlag() + ", " + 
					u.getPosition().getID() + ")");
		}
		
		for(SOP s: sopList){
			names.add("Insert SOP " + s.getName());
			sqls.add("insert into SOP (title, description, priority, version, author_id, archive_flag)" +
					" values ('" + s.getName() + "', '" + s.getDescription() + "', " + s.getPriority() + ", " +
					s.getRevision() + ", " + s.getAuthorID() + ", " + s.getArchiveFlag() + ")");
		}
		
		for(Position p: posList){
			List<SOP> reqs = p.getRequirements();
			
			for(SOP s: reqs){
				names.add("Insert SOP " + s.getName() + " and Position " + p.getTitle() + " connection");
				sqls.add("insert into PositionSOP (position_id, sop_id) " + 
						" values (" + p.getID() + ", " + s.getID() + ")");
			}
		}
		
		executeUpdates(names, sqls);
	}
	
	public String formatInsertStatement(String table, String[] args, String[] values){
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
		return insertSQL;
	}
	
	public void insert(String table, String[] args, String[] values){
		executeUpdate("Insert a " + table, formatInsertStatement(table, args, values));
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
					
					String insertSQL = formatInsertStatement(table, args, values);
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
	
	public Integer insertPosition_SOP(Position p){
		String[] reqs = new String[p.getRequirements().size()];
		
		for(int i = 0; i < p.getRequirements().size(); i++) {
			reqs[i] = String.valueOf(p.getRequirements().get(i).getID());
		}
		
		return insertAndGetID("PositionSOP", "position_id", 
				new String[]{"sop_id"}, reqs);
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
	
	public void addSOPtoPosition(int positionID, int sopID){
		executeUpdate("Insert Position " + positionID + " and SOP " + sopID + " connection", 
				"insert into PositionSOP (position_id, sop_id) values (" + positionID + ", " + sopID + ")");
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
