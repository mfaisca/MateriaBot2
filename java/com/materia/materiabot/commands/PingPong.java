package com.materia.materiabot.commands;
import com.materia.materiabot.Listener;
import com.materia.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.core.entities.Message;

public class PingPong extends _BaseCommand {
	public PingPong() {
		super("ping", "pang", "peng", "pong", "pung");
	}
	
	@Override
	public void doStuff(Message message) {
		String raw = message.getContentRaw();
		if(raw.startsWith("ping", Listener.COMMAND_PREFIX.length()))
			MessageUtils.sendMessage(message.getChannel(), "pong");
		else if(raw.startsWith("pong", Listener.COMMAND_PREFIX.length()))
			MessageUtils.sendMessage(message.getChannel(), "ping");
		else
			MessageUtils.sendMessage(message.getChannel(), "derp");
	}
}