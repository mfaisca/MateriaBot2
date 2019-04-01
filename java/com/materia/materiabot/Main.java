package com.materia.materiabot;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static JDA client;
	public static JDA getClient() { return client; }
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		String privateToken = Configs.BOT_TOKEN; //ConfigsDB.getKeyValue(ConfigsDB.OPERAOMNIA_TOKEN_KEY)
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator()
			 + "Please use any SQLite Browser to manually add your bot's token in the 'bot_configs' table.");
			return;
		}
        client = new JDABuilder(privateToken).build();
        client.addEventListener(new Listener());
	}
}