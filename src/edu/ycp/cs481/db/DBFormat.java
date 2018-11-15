package edu.ycp.cs481.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

public class DBFormat{
	private static String userPieces = "User.user_id, User.employee_id, User.email, User.password, User.first_name, User.last_name,"
			+ " User.locked_out, User.archive_flag, User.position_id";
	private static String positionPieces = "Position.position_id, Position.title, Position.description, Position.priority";
	private static String sopPieces = "SOP.sop_id, SOP.title, SOP.description, SOP.priority, SOP.version, SOP.author_id, "
			+ "SOP.archive_flag";
	
	private static QueryResultFormat<ArrayList<Integer>> intResFormat = new QueryResultFormat<ArrayList<Integer>>(){
		@Override
		public ArrayList<Integer> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Integer> values = new ArrayList<Integer>();
			while(resultSet.next()){
				int value = resultSet.getInt(1);
				values.add(value);
			}
			return values;
		}
	};
	
	private static QueryResultFormat<ArrayList<User>> userResFormat = new QueryResultFormat<ArrayList<User>>(){
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
	
	private static QueryResultFormat<ArrayList<Date>> dateResFormat = new QueryResultFormat<ArrayList<Date>>(){
		@Override
		public ArrayList<Date> convertFromResultSet(ResultSet resultSet) throws SQLException{
			ArrayList<Date> dates = new ArrayList<Date>();
			while(resultSet.next()){
				dates.add(resultSet.getTimestamp(1));
			}
			return dates;
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
	
	public static QueryResultFormat<ArrayList<Integer>> getIntResFormat(){
		return intResFormat;
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
	
	public static QueryResultFormat<ArrayList<Date>> getDateResFormat(){
		return dateResFormat;
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
