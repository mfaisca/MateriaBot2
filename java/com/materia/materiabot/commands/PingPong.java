package com.materia.materiabot.commands;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.materia.materiabot.Utils.Constants.HELP_TYPE;
import com.materia.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.core.entities.Message;

public class PingPong extends _BaseCommand {
	public PingPong() {
		super("ping", "pang", "peng", "pong", "pung");
	}
	
	@Override
	public void doStuff(Message message) {
		
		Random r = new Random();
		List<String> msgs = Arrays.asList("abc", "def", "ghi");
		String response = msgs.get(r.nextInt(msgs.size()));
		response += msgs.get(r.nextInt(msgs.size()));
		MessageUtils.sendMessage(message.getChannel(), response);
				
//		String raw = message.getContentRaw();
//		Member member = message.getMember();
//		message.getGuild().getMembers().stream().filter(m -> m.getUser().equals(message.getAuthor())).findFirst().get().getColor();
//		if(raw.startsWith("ping", Listener.COMMAND_PREFIX.length()))
//			MessageUtils.sendMessage(message.getChannel(), "pong\nThis message was sent by " + message.getAuthor().getName());
//		else if(raw.startsWith("pong", Listener.COMMAND_PREFIX.length()))
//			MessageUtils.sendMessage(message.getChannel(), "ping");
//		else
//			MessageUtils.sendEmbed(message.getChannel(), "derp", member.getUser().getEffectiveAvatarUrl());
	}

	@Override
	protected String help(Message message, HELP_TYPE helpType) {
		// TODO Auto-generated method stub
		return null;
	}
}