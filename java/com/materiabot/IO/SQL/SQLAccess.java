package com.materiabot.IO.SQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import com.materiabot.Utils.BotException;
import com.materiabot.Utils.Constants;

public class SQLAccess {
	private static Connection connection;
	
	static{
		try { connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE); }
		catch (SQLException e) { e.printStackTrace(); }
	}
	
	public static Connection getConnection(){ return connection; }
	
	private static PreparedStatement prepare(String query, Object...params) throws SQLException {
		PreparedStatement statement = SQLAccess.getConnection().prepareStatement(query);
		for(int i = 0; i < params.length; i++)
			if(params[i] == null)
				statement.setInt(i+1, Types.NULL);
			else if(params[i].getClass().equals(String.class))
				statement.setString(i+1, (String)params[i]);
			else if(params[i].getClass().equals(Long.class))
				statement.setLong(i+1, (Long)params[i]);
			else if(params[i].getClass().equals(Integer.class))
				statement.setInt(i+1, (Integer)params[i]);
			else if(params[i].getClass().equals(Boolean.class))
				statement.setBoolean(i+1, (Boolean)params[i]);
			else if(params[i].getClass().isArray()) {
				statement.setArray(i+1, SQLAccess.getConnection().createArrayOf("TEXT", (Object[])params[i]));
			}else
				throw new RuntimeException("Inserting unknown type into DB: " + params[i].getClass());
		statement.closeOnCompletion();
		return statement;
	}
	
	public static synchronized int executeInsert(String query, Object... params) throws BotException{
		try(PreparedStatement ps = prepare(query, params)) {
			int result = ps.executeUpdate();
			return result;
		} catch(Exception e) { 
			final String textParams = Arrays.stream(params).map(o -> o.toString()).reduce((o1, o2) -> o1 + " || " + o2).orElse("null");
			throw new BotException("Error when running the select query '" + query + "' with parameters: " + textParams, e);
		}
	}
	
	public static ResultSet executeSelect(String query, Object... params) throws BotException{
		try(PreparedStatement ps = prepare(query, params)) {
			try(ResultSet rs = ps.executeQuery()){
				CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
			    crs.populate(rs);
				return crs;
			}
		} catch(Exception e) { 
			final String textParams = Arrays.stream(params).map(o -> o.toString()).reduce((o1, o2) -> o1 + " || " + o2).orElse("null");
			throw new BotException("Error when running the select query '" + query + "' with parameters: " + textParams, e);
		}
	}
}