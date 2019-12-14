package com.materiabot.IO.SQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.materiabot.Utils.BotException;

public class ConfigsDB {
	public static final String OPERAOMNIA_TOKEN_KEY = "OO_BOT_TOKEN_KEY";
	public static final String PATREON_TOKEN_KEY = "PATREON_TOKEN_KEY";
	
	public static final void buildTables() throws SQLException{
		try(Statement statement = SQLAccess.getConnection().createStatement()){
			statement.executeUpdate("CREATE TABLE bot_configs (key string COLLATE nocase, value string COLLATE nocase, PRIMARY KEY(key))");
			statement.executeUpdate("CREATE TABLE users_emotes (name string COLLATE nocase, guildId INTEGER, userid integer NOT NULL, url string NOT NULL, PRIMARY KEY(name, serverID))");
			statement.executeUpdate("CREATE TABLE permissions (command TEXT COLLATE nocase, function TEXT COLLATE nocase DEFAULT 'execute', guildId INTEGER, roleId INTEGER, channelId INTEGER, userId INTEGER, permission TEXT COLLATE nocase NOT NULL, PRIMARY KEY(command,guildId,function,userId,channelId,roleId))");
			setKeyValue(OPERAOMNIA_TOKEN_KEY, null);
			setKeyValue(PATREON_TOKEN_KEY, null);
			statement.executeUpdate("CREATE TABLE current_events (eventId INTEGER, eventName TEXT, startDate INTEGER, endDate INTEGER, PRIMARY KEY(eventId))");
			statement.executeUpdate("CREATE TABLE current_events_links (eventId INTEGER, linkId INTEGER, 'order' INTEGER, name TEXT, url TEXT, PRIMARY KEY(eventId,linkId, order), FOREIGN KEY(eventId) REFERENCES current_events(eventId))");
			statement.executeUpdate("CREATE TABLE current_events_jp (eventId INTEGER, eventName TEXT, startDate INTEGER, endDate INTEGER, PRIMARY KEY(eventId))");
			statement.executeUpdate("CREATE TABLE current_events_links_jp (eventId INTEGER, linkId INTEGER, 'order' INTEGER, name TEXT, url TEXT, PRIMARY KEY(eventId,linkId, name), FOREIGN KEY(eventId) REFERENCES current_events_jp(eventId))");
			statement.executeUpdate("CREATE TABLE users_friendcodes_oo_gl (userId TEXT, "
					+ "friendCode TEXT, unitShared TEXT, PRIMARY KEY(userId))");
			statement.executeUpdate("CREATE TABLE users_friendcodes_oo_jp (userId TEXT, "
					+ "friendCode TEXT, unitShared TEXT, PRIMARY KEY(userId))");
			statement.executeUpdate("CREATE TABLE users_pull_statistics (userId INTEGER, gems INTEGER DEFAULT 0, tickets INTEGER DEFAULT 0, " + 
					"countBT INTEGER DEFAULT 0, countLD INTEGER DEFAULT 0, countEX INTEGER DEFAULT 0, count35 INTEGER DEFAULT 0, count15 INTEGER DEFAULT 0, " + 
					"countOffBanner INTEGER DEFAULT 0, PRIMARY KEY(userId)");
		} catch(Exception e) { }
	}
	
	public static String getKeyValue(String key) throws BotException{
		try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM bot_configs WHERE key = ?", key)) {
			String res = null;
			if(result.next())
				res = result.getString("value");
			return res;
		} catch (SQLException e) {
			throw new BotException("Error communicating with the database!", e);
		}
	}
	public static void setKeyValue(String key, String value) throws BotException{
		SQLAccess.executeInsert("INSERT OR REPLACE INTO bot_configs VALUES(?, ?)", key, value);
	}
}