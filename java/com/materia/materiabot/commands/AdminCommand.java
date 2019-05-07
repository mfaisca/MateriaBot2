package com.materia.materiabot.commands;
import com.materia.materiabot.Listener;
import com.materia.materiabot.GameElements._Library;
import com.materia.materiabot.Utils.Constants;
import com.materia.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.core.entities.Message;

public class AdminCommand extends _BaseCommand{	
	public AdminCommand() {
		super("admin");
	}

	@Override
	public void doStuff(Message event) {
		if(event.getAuthor().getId().equalsIgnoreCase(""+Constants.QUETZ_ID))
			return;
		String msg = event.getContentRaw();
		if(msg.contains("reset") || msg.contains("reload")) {
			MessageUtils.sendMessage(event.getChannel(), "Reloading skills & passives...");
			_Library.reset();
			MessageUtils.sendMessage(event.getChannel(), "Skills & passives loaded successfully.");
		}
		else if(msg.contains("shutdown") || msg.contains("stop")) {
			Listener.SHUTDOWN = true;
			MessageUtils.sendMessage(event.getChannel(), "Shutdown Initiated.\nThe bot will shutdown in 30 seconds.");
			Constants.sleep(30 * 1000);
			MessageUtils.sendMessage(event.getChannel(), "Shutting down...");
			System.exit(0);
		}
	}
	
	@Override
	protected String help(Message event, Constants.HELP_TYPE helpType) {
		return null;
	}
}