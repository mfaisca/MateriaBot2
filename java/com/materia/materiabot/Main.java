package com.materia.materiabot;
import javax.security.auth.login.LoginException;
import com.materia.materiabot.IO.SQL.ConfigsDB;
import com.materia.materiabot.Utils.BotException;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static JDA client;
	public static JDA getClient() { return client; }
	
	/*Self-hosting this bot (running a copy yourself) is not supported, and no help will be provided for editing nor compiling the code in this repository. 
	 * The source code is provided here for transparency about how the bot's primary features work.
	 * If you decide to edit, compile, or use this code in any way, please respect the license*/
	
	public static void main(String[] args) throws LoginException, InterruptedException, BotException {
		String privateToken = "Mzg3NTQ5NDkxNDAzODE2OTYx.XJ9joA.ugokEaW5MHTyHOfqa3ZhtkSsa0w";//ConfigsDB.getKeyValue(ConfigsDB.DEBUG_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator()
			 + "Please use any SQLite Browser to manually add your bot's token in the 'bot_configs' table.");
			return;
		}
        client = new JDABuilder(privateToken).build();
        client.addEventListener(new Listener());
//		jda.awaitReady();
//		for(Guild server : jda.getGuilds())
//			System.out.println(server.getName() + " (" + server.getId() + ") - " + server.getMembers().size() + " Members");
//		System.out.println("Total Users: " + jda.getGuilds().stream().flatMap(g -> g.getMembers().stream()).distinct().count());
	}
}