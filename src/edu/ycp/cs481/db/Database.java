package edu.ycp.cs481.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ycp.cs481.model.ClockTime;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;
import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.UserController;

public class Database {
	private String userPieces = "User.user_id, User.employee_id, User.email, User.password, User.first_name, User.last_name,"
							+ " User.locked_out, User.archive_flag, User.position_id";
	private String positionPieces = "Position.position_id, Position.title, Position.description, Position.priority";
	private String sopPieces = "SOP.sop_id, SOP.title, SOP.description, SOP.priority, SOP.version, SOP.author_id, SOP.archive_flag";
	private static String dbName = "cs481db";
	
	static{
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e){
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
				u.setEmployeeID(resultSet.getInt(2));
				u.setEmail(resultSet.getString(3));
				u.setPassword(resultSet.getString(4));
				u.setFirstName(resultSet.getString(5));
				u.setLastName(resultSet.getString(6));
				u.setLockedOut(resultSet.getBoolean(7));
				u.setArchived(resultSet.getBoolean(8));
				PositionController pc = new PositionController();
				u.setPosition(pc.searchForPositions(resultSet.getInt(9), false, null, false, null, -1).get(0));
				
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
					s.setArchived(resultSet.getBoolean(7));
					
					sops.add(s);
			}
			
			return sops;
		}
	};
	
	public QueryResultFormat<ArrayList<ClockTime>> timeResFormat = new QueryResultFormat<ArrayList<ClockTime>>(){
		@Override
		public ArrayList<ClockTime> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<ClockTime> times = new ArrayList<ClockTime>();
			while(resultSet.next()){
				Date date = resultSet.getTimestamp(1);
				boolean in = resultSet.getBoolean(2);
				times.add(new ClockTime(date, in));
			}
			return times;
		}
	};
	
	public QueryResultFormat<ArrayList<User>> getUserResFormat(){
		return userResFormat;
	}
	
	public String getUserPieces() {
		return userPieces;
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
	
	public QueryResultFormat<ArrayList<ClockTime>> getTimeResFormat(){
		return timeResFormat;
	}
	
	public void addConditionalAndToQuery(boolean prevSet, StringBuilder name, StringBuilder sql){
		if(prevSet){
			name.append(" and ");
			sql.append(" and ");
		}
	}
	
	public boolean addConditionalIntToQuery(boolean prevSet, StringBuilder name, StringBuilder sql, String arg, int value){
		if(value != -1){
			addConditionalAndToQuery(prevSet, name, sql);
			name.append(arg + " of " + value);
			sql.append(arg + " = " + value);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean addConditionalStringToQuery(boolean prevSet, StringBuilder name, StringBuilder sql, boolean partial, 
			String arg, String value){
		if(value != null && !value.equalsIgnoreCase("")){
			addConditionalAndToQuery(prevSet, name, sql);
			if(partial){
				name.append(arg + " with " + value);
				sql.append(arg + " like '%" + value + "%'");
			}else{
				name.append(arg + " of " + value);
				sql.append(arg + " = '" + value + "'");
			}
			return true;
		}else{
			return false;
		}
	}
	
	// TODO: Use otherTables
	public<ResultType> ResultType doSearch(QueryResultFormat<ResultType> queryResFormat, String mainTable, String[] otherTables, 
			String[] intArgs, int[] intValues,
			boolean[] partialStrings, String[] stringArgs, String[] stringValues) throws SQLException{
		if(intArgs.length != intValues.length){
			throw new IllegalArgumentException("Database.doSearch: intArgs and intValues are different sizes!");
		}
		if(stringArgs.length != stringValues.length || stringArgs.length != partialStrings.length){
			throw new IllegalArgumentException("Database.doSearch: partialStrings, stringArgs, and stringValues must be the same "
					+ "size!");
		}
		String returnPieces;
		switch(mainTable.toLowerCase()){
			case "user":
				returnPieces = userPieces;
				if(queryResFormat != userResFormat){
					throw new IllegalArgumentException("Database.doSearch: Must use userResFormat if pulling Users!");
				}
				break;
			case "position":
				returnPieces = positionPieces;
				if(queryResFormat != posResFormat){
					throw new IllegalArgumentException("Database.doSearch: Must use posResFormat if pulling Positions!");
				}
				break;
			case "sop":
				returnPieces = sopPieces;
				if(queryResFormat != sopResFormat){
					throw new IllegalArgumentException("Database.doSearch: Must use sopResFormat if pulling SOPs!");
				}
				break;
			default:
				throw new IllegalArgumentException("Database.doSearch: Unsupported table: " + mainTable + "!");
		}
		StringBuilder name = new StringBuilder("");
		StringBuilder sql = new StringBuilder("select " + returnPieces + " from " + mainTable);
		boolean searchAll = true;
		for(int i = 0; searchAll && i < intValues.length; i++){
			if(intValues[i] != -1){
				searchAll = false;
			}
		}
		for(int i = 0; searchAll && i < stringValues.length; i++){
			String s = stringValues[i];
			if(s != null && !s.equalsIgnoreCase("")){
				searchAll = false;
			}
		}
		if(searchAll){
			name.append("Get all " + mainTable + "s");
		}else{
			name.append("Get " + mainTable + "s with ");
			sql.append(" where ");
			boolean prevSet = false;
			
			for(int i = 0; i < intValues.length; i++){
				prevSet = (addConditionalIntToQuery(prevSet, name, sql, intArgs[i], intValues[i]))?true:prevSet;
			}
			for(int i = 0; i < stringValues.length; i++){
				prevSet = (addConditionalStringToQuery(prevSet, name, sql, partialStrings[i], stringArgs[i], stringValues[i]))
						?true:prevSet;
			}
		}
		
		System.out.println("Name: " + name.toString());
		System.out.println("SQL: " + sql.toString());
		
		return executeQuery(name.toString(), sql.toString(), queryResFormat);
	}
	
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
	
	public Boolean executeCheck(String name, String sql) throws SQLException{
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException{
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
					
					if(resultSet.next())
						return true;
					else
						return false;
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
		executeUpdate("Creating CS481DB database", "create database if not exists cs481db");
		dbName = "cs481db";
	}
	
	public void dropDatabase() {
		executeUpdate("Dropping old database..", "drop database if exists cs481db");
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
				  "locked_out TINYINT NOT NULL, " +
				  "archive_flag TINYINT NOT NULL, " +
				  "create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
				  "position_id INT NOT NULL, " +
				  "employee_id INT NOT NULL DEFAULT 0, " +
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
		
		names.add("Create Permission table");
		sqls.add("CREATE TABLE IF NOT EXISTS Permission (" +
				  "perm_id INT NOT NULL AUTO_INCREMENT," +
				  "permission VARCHAR(255) NOT NULL," +
				  "PRIMARY KEY (perm_id)," +
				  "UNIQUE INDEX perm_id_UNIQUE (perm_id ASC) VISIBLE);");
		
		names.add("Create PositionPermission table");
		sqls.add("CREATE TABLE IF NOT EXISTS PositionPermission (" +
				   "position_id INT NOT NULL, " +
				   "perm_id INT NOT NULL, " +
					"CONSTRAINT FOREIGN KEY (position_id) REFERENCES Position (position_id), " + 
					"CONSTRAINT FOREIGN KEY (perm_id) REFERENCES Permission (perm_id) " +
					");");
		
		names.add("Create CompletedSOP table");
		sqls.add("CREATE TABLE IF NOT EXISTS CompletedSOP (" +
				  "user_id INT NOT NULL," +
				  "sop_id INT NOT NULL," +
				  "CONSTRAINT FOREIGN KEY (user_id) REFERENCES User (user_id), " + 
				  "CONSTRAINT FOREIGN KEY (sop_id) REFERENCES SOP (sop_id) " +
				  ");");		
		
		names.add("Create Subordinate table");
		sqls.add("CREATE TABLE IF NOT EXISTS Subordinate (" +
				   "manager_id INT NOT NULL, " +
				   "subordinate_id INT NOT NULL," +
				   "CONSTRAINT FOREIGN KEY (manager_id) REFERENCES User (user_id), " +
				   "CONSTRAINT FOREIGN KEY (subordinate_id) REFERENCES User (user_id)" +
				   ");");
		
		executeUpdates(names, sqls);
	}
	
	public void loadInitialData(){
		InitialData initData = new InitialData();
		
		// They must go in this order
		List<Position> posList = initData.getInitialPositions();
		List<User> userList = initData.getInitialUsers();
		List<SOP> sopList = initData.getInitialSOPs();
		String[] perms = initData.getInitialPermissions();
		String[] permNames = initData.getInitialPermissionNames();
		int[] permIds = initData.getInitialPermissionIDs();
		int id = 0;
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> sqls = new ArrayList<String>();
		
		for(Position p: posList){
			names.add("Insert Position " + p.getTitle());
			sqls.add("insert into Position (title, description, priority) " +
					"values ('" + p.getTitle() + "', '" + p.getDescription() + "', " + p.getPriority() + ")");
		}
		
		for(User u: userList){
			names.add("Insert User " + u.getFirstName() + " " + u.getLastName());
			sqls.add("insert into User (email, password, first_name, last_name, locked_out, archive_flag, " +
					"position_id)  values ('" + u.getEmail() + "', '" + UserController.hashPassword(u.getPassword()) + "', '" + 
					u.getFirstName() + "', '" + u.getLastName() + "', " + u.isLockedOut() + ", " + u.isArchived() + ", " + 
					u.getPosition().getID() + ")");
		}
		
		for(SOP s: sopList){
			names.add("Insert SOP " + s.getName());
			sqls.add("insert into SOP (title, description, priority, version, author_id, archive_flag)" +
					" values ('" + s.getName() + "', '" + s.getDescription() + "', " + s.getPriority() + ", " +
					s.getRevision() + ", " + s.getAuthorID() + ", " + s.isArchived() + ")");
		}
		
		for(Position p: posList){
			List<SOP> reqs = p.getRequirements();
			
			for(SOP s: reqs){
				names.add("Insert SOP " + s.getName() + " and Position " + p.getTitle() + " connection");
				sqls.add("insert into PositionSOP (position_id, sop_id) " + 
						" values (" + p.getID() + ", " + s.getID() + ")");
			}
		}
		
		for(int i = 0; i < perms.length; i++) {
			names.add("Insert Permission " + permNames[i]);
			sqls.add("insert into Permission (permission) " +
			" values ('" + perms[i] + "')");
		}
		
		for(Position p: posList) {
			names.add("Insert Position " + p.getTitle() + " and Permission " + permNames[permIds[id] - 1]);
			sqls.add("insert into PositionPermission (position_id, perm_id) " +
			" values (" + p.getID() + ", " + permIds[id] + ")");
			id++;
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
						result.setArchived(resultSet.getBoolean(7));
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
		dbName = "";
		db.dropDatabase();
		db.createDatabase();
		db.createTables();
		db.loadInitialData();
		System.out.println("Database creation finished");
	}
}
