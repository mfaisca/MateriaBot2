package com.materiabot.IO.SQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import com.materiabot.Utils.BotException;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.Constants.Dual;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.entities.Guild;

public class SQLAccess {
	private static Connection connection;
	private static final String DATABASE = "../_files/Database.db";
	public static final String BOT_TOKEN_KEY = "BOT_TOKEN_KEY";
	public static final String PATREON_TOKEN_KEY = "PATREON_TOKEN_KEY";
	public static final String CLEVERBOT_TOKEN_KEY = "CLEVERBOT_TOKEN_KEY";
	
	static{
		try { connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE); }
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
			else
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

	public static String getGuildPrefix(Guild guild) {
		try {
			ResultSet rs = executeSelect("SELECT prefix FROM Servers WHERE id = ?", guild.getIdLong());
			rs.next();
			return rs.getString("prefix");
		} catch (Exception e) {
			return _Listener.DEFAULT_PREFIX;
		}
	}
	public static void setGuildPrefix(Guild guild, String prefix) throws BotException {
		SQLAccess.executeInsert("INSERT OR REPLACE INTO Servers VALUES(?, ?, ?)", guild.getIdLong(), prefix, 0);
	}
	
	public static String getKeyValue(String key){
		try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM Configs WHERE key = ?", key)) {
			String res = null;
			if(result.next())
				res = result.getString("value");
			return res;
		} catch (SQLException | BotException e) {
			System.out.println("Error communicating with the database!");
			MessageUtils.sendWhisper(Constants.QUETZ, e.getLocalizedMessage());
			return null;
		}
	}
	public static void setKeyValue(String key, String value) throws BotException{
		SQLAccess.executeInsert("INSERT OR REPLACE INTO Configs VALUES(?, ?)", key, value);
	}
	
	public static List<Dual<String, String>> getCommandValue(String command, Guild g) throws BotException{
		try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM bot_commands WHERE commandName = ? ORDER BY guildId ASC", command)) {
			LinkedList<Dual<String, String>> res = new LinkedList<Dual<String, String>>(); //Returns global commands before server-specific ones.
			while(result.next())
				res.add(new Dual<String, String>(result.getString("message"), result.getString("help")));
			return res;
		} catch (SQLException e) {
			throw new BotException("Error communicating with the database!", e);
		}
	}
	public static void setCommandValue(String command, Long guildId, String message, String help) throws BotException{
		SQLAccess.executeInsert("INSERT OR REPLACE INTO Commands VALUES(?, ?, ?, ?)", command, guildId, message, help);
	}
}