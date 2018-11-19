package edu.ycp.cs481.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.Shift;
import edu.ycp.cs481.model.User;

public class DBFormat{
	private static String userPieces = "User.user_id, User.employee_id, User.email, User.password, User.first_name, User.last_name,"
			+ " User.locked_out, User.archive_flag, User.position_id";
	private static String positionPieces = "Position.position_id, Position.title, Position.description, Position.priority";
	private static String sopPieces = "SOP.sop_id, SOP.title, SOP.description, SOP.priority, SOP.version, SOP.author_id, "
			+ "SOP.archive_flag";
	private static String quarantinePieces = "Quarantine.email, Quarantine.password, Quarantine.first_name, Quarantine.last_name";
	
	
	private static QueryResultFormat<ArrayList<Boolean>> boolResFormat = new QueryResultFormat<ArrayList<Boolean>>(){
		@Override
		public ArrayList<Boolean> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Boolean> values = new ArrayList<Boolean>();
			while(resultSet.next()){
				values.add(resultSet.getBoolean(1));
			}
			return values;
		}
	};
	
	private static QueryResultFormat<ArrayList<Integer>> intResFormat = new QueryResultFormat<ArrayList<Integer>>(){
		@Override
		public ArrayList<Integer> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Integer> values = new ArrayList<Integer>();
			while(resultSet.next()){
				values.add(resultSet.getInt(1));
			}
			return values;
		}
	};
	
	private static QueryResultFormat<ArrayList<String>> stringResFormat = new QueryResultFormat<ArrayList<String>>(){
		@Override
		public ArrayList<String> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<String> values = new ArrayList<String>();
			while(resultSet.next()){
				values.add(resultSet.getString(1));
			}
			return values;
		}
	};
	
	private static QueryResultFormat<ArrayList<String>> quarantineResFormat = new QueryResultFormat<ArrayList<String>>(){
		@Override
		public ArrayList<String> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<String> values = new ArrayList<String>();
			if(resultSet.next()) {
				values.add(resultSet.getString(1));
				values.add(resultSet.getString(2));
				values.add(resultSet.getString(3));
				values.add(resultSet.getString(4));
			}
			return values;
		}
	};
	
	private static QueryResultFormat<Boolean> checkResFormat = new QueryResultFormat<Boolean>(){
		@Override
		public Boolean convertFromResultSet(ResultSet resultSet) throws SQLException{
			return resultSet.next();
		}
	};
	
	private static QueryResultFormat<ArrayList<User>> userResFormat = new QueryResultFormat<ArrayList<User>>(){
		@Override
		public ArrayList<User> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<User> users = new ArrayList<User>();
			
			while(resultSet.next()){
				User u = new User();
				
				u.setID(resultSet.getInt(1));
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
	
	private static QueryResultFormat<ArrayList<Position>> posResFormat = new QueryResultFormat<ArrayList<Position>>(){
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
	
	private static QueryResultFormat<ArrayList<SOP>> sopResFormat = new QueryResultFormat<ArrayList<SOP>>(){
		@Override
		public ArrayList<SOP> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<SOP> sops = new ArrayList<SOP>();
			
			while(resultSet.next()){
					SOP s = new SOP();
					
					s.setID(resultSet.getInt(1));
					s.setTitle(resultSet.getString(2));
					s.setDescription(resultSet.getString(3));
					s.setPriority(resultSet.getInt(4));
					s.setVersion(resultSet.getInt(5));
					s.setAuthorID(resultSet.getInt(6));
					s.setArchived(resultSet.getBoolean(7));
					
					sops.add(s);
			}
			
			return sops;
		}
	};
	
	private static QueryResultFormat<ArrayList<Timestamp>> timeResFormat = new QueryResultFormat<ArrayList<Timestamp>>(){
		@Override
		public ArrayList<Timestamp> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Timestamp> times = new ArrayList<Timestamp>();
			while(resultSet.next()){
				times.add(resultSet.getTimestamp(1));
			}
			return times;
		}
	};
	
	private static QueryResultFormat<ArrayList<Shift>> shiftResFormat = new QueryResultFormat<ArrayList<Shift>>(){
		@Override
		public ArrayList<Shift> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Shift> shifts = new ArrayList<Shift>();
			while(resultSet.next()){
				Timestamp timeIn = resultSet.getTimestamp(1);
				Timestamp timeOut = resultSet.getTimestamp(2);
				int hours = resultSet.getInt(3);
				shifts.add(new Shift(timeIn, timeOut, hours));
			}
			return shifts;
		}
	};
	
	public static String getUserPieces(){
		return userPieces;
	}
	
	public static String getPositionPieces(){
		return positionPieces;
	}
	
	public static String getSopPieces(){
		return sopPieces;
	}
	
	public static String getQuarantinePieces() {
		return quarantinePieces;
	}
	
	public static QueryResultFormat<ArrayList<Boolean>> getBoolResFormat(){
		return boolResFormat;
	}
	
	public static QueryResultFormat<ArrayList<Integer>> getIntResFormat(){
		return intResFormat;
	}
	
	public static QueryResultFormat<ArrayList<String>> getStringResFormat(){
		return stringResFormat;
	}
	
	public static QueryResultFormat<ArrayList<String>> getQuarantineResFormat(){
		return quarantineResFormat;
	}
	
	public static QueryResultFormat<Boolean> getCheckResFormat(){
		return checkResFormat;
	}
	
	public static QueryResultFormat<ArrayList<User>> getUserResFormat(){
		return userResFormat;
	}
	
	public static QueryResultFormat<ArrayList<Position>> getPosResFormat(){
		return posResFormat;
	}
	
	public static QueryResultFormat<ArrayList<SOP>> getSopResFormat(){
		return sopResFormat;
	}
	
	public static QueryResultFormat<ArrayList<Timestamp>> getTimeResFormat(){
		return timeResFormat;
	}
	
	public static QueryResultFormat<ArrayList<Shift>> getShiftResFormat(){
		return shiftResFormat;
	}
	
	public static void addConditionalAndToQuery(boolean prevSet, StringBuilder name, StringBuilder sql){
		if(prevSet){
			name.append(" and ");
			sql.append(" and ");
		}
	}
	
	public static boolean addConditionalIntToQuery(boolean prevSet, StringBuilder name, StringBuilder sql, String arg, int value){
		if(value != -1){
			addConditionalAndToQuery(prevSet, name, sql);
			name.append(arg + " of " + value);
			sql.append(arg + " = " + value);
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean addConditionalStringToQuery(boolean prevSet, StringBuilder name, StringBuilder sql, boolean partial, 
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
	
	public static void addJunctionStringToQuery(boolean prevSet, StringBuilder name, StringBuilder sql, String junction){
		addConditionalAndToQuery(prevSet, name, sql);
		name.append(junction);
		sql.append(junction);
	}
	
	public static String formatInsertStatement(String table, String[] args, String[] values){
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
}
