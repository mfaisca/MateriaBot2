package com.materiabot;
import org.plugface.core.PluginManager;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.factory.PluginSources;

import com.materiabot.GameElements.UnitOverride;
import com.materiabot.IO.SQL.ConfigsDB;
import com.materiabot.commands._Listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {
	private static JDA client;
	public static JDA getClient() { return client; }
	
	public static void main(String[] args) throws Exception {
		ConfigsDB.buildTables();
		String privateToken = ConfigsDB.getKeyValue(ConfigsDB.OPERAOMNIA_TOKEN_KEY); //"Mzg3NTQ5NDkxNDAzODE2OTYx.XdVXiQ.HhsfPR34EdVKPC25CHfhzyxrQ-g";  //
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		}
        client = new JDABuilder(privateToken).setAutoReconnect(true)
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.playing("Opera Omnia")).addEventListeners(new _Listener()).build();
		client.awaitReady();
		System.out.println("Bot is ready!!");
		
		PluginManager manager = PluginManagers.defaultPluginManager();
		manager.loadPlugins(PluginSources.jarSource("file:/C:/Test"));

		UnitOverride greeterPlugin = manager.getPlugin(UnitOverride.class); // Here is why having a shared interface between plugins and applications is convenient

		System.out.println(greeterPlugin.getName()); // It works!
	}
}