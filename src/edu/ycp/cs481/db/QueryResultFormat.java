package edu.ycp.cs481.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryResultFormat<ResultType>{
	public ResultType convertFromResultSet(ResultSet resultSet) throws SQLException;
}
