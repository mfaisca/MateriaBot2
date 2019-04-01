package com.materia.materiabot.IO.SQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.materia.materiabot.Utils.BotException;

public class ConfigsDB {
	public static final String OPERAOMNIA_TOKEN_KEY = "OO_BOT_TOKEN_KEY";
	public static final String CLEVERBOT_TOKEN_KEY = "CLEVERBOT_TOKEN_KEY";
	public static final String DEBUG_TOKEN_KEY = "DEBUG_TOKEN_KEY";
	
	public static final void buildTables() throws SQLException{
		try(Statement statement = SQLAccess.getConnection().createStatement()){
			statement.executeUpdate("CREATE TABLE bot_configs (key string COLLATE nocase, value string COLLATE nocase, PRIMARY KEY(key))");
			statement.executeUpdate("CREATE TABLE users_emotes (name string COLLATE nocase, guildId INTEGER, userid integer NOT NULL, url string NOT NULL, PRIMARY KEY(name, serverID))");
			statement.executeUpdate("CREATE TABLE permissions (command TEXT COLLATE nocase, function TEXT COLLATE nocase DEFAULT 'execute', guildId INTEGER, roleId INTEGER, channelId INTEGER, userId INTEGER, permission TEXT COLLATE nocase NOT NULL, PRIMARY KEY(command,guildId,function,userId,channelId,roleId))");
			setKeyValue(OPERAOMNIA_TOKEN_KEY, null);
			setKeyValue(DEBUG_TOKEN_KEY, "Mzg3NTQ5NDkxNDAzODE2OTYx.XJ9joA.ugokEaW5MHTyHOfqa3ZhtkSsa0w");
		} catch(Exception e) { }
	}

	public static void buildOperaOmniaTables() {
		try(Statement statement = SQLAccess.getConnection().createStatement()){
			statement.executeUpdate("CREATE TABLE users_friendcodes_oo_gl (userId INTEGER, "
					+ "friendCode TEXT, unitShared TEXT, PRIMARY KEY(userId))");
			statement.executeUpdate("CREATE TABLE users_friendcodes_oo_jp (userId INTEGER, "
					+ "friendCode TEXT, unitShared TEXT, PRIMARY KEY(userId))");
			statement.executeUpdate("CREATE TABLE users_pull_statistics (userId INTEGER, gems INTEGER DEFAULT 0, tickets INTEGER DEFAULT 0, "
					+ "countEX INTEGER DEFAULT 0, count35 INTEGER DEFAULT 0, count15 INTEGER DEFAULT 0, countOffBanner INTEGER DEFAULT 0, PRIMARY KEY(userId)");
			statement.executeUpdate("CREATE TABLE command_statistics (guildId INTEGER, userId INTEGER, command TEXT, "
					+ "numUses INTEGER DEFAULT 0, PRIMARY KEY(guildId, userId, command))");
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
	public static void getStatistics(Long guildId, Long userId, String command) throws BotException{ //TODO
		/*try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM command_statistics WHERE key = ?", key)) {
			String res = null;
			if(result.next())
				res = result.getString("value");
			return res;
		} catch (SQLException e) {
			throw new BotException("Error communicating with the database!", e);
		}*/
	}
	public static void addStatistic(Long guildId, Long userId, String command){
		new Thread(() -> { //TODO
			/*SQLAccess.executeInsert("INSERT OR REPLACE INTO bot_configs VALUES(?, ?)", key, value);*/
		}).start();
	}
}