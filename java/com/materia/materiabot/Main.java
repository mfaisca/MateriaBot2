package com.materia.materiabot;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static JDA client;
	public static JDA getClient() { return client; }
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		String privateToken = Configs.BOT_TOKEN;
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		}
        client = new JDABuilder(privateToken).build();
        client.addEventListener(new Listener());
	}
}