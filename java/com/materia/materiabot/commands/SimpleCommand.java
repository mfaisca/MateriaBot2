package com.materia.materiabot.commands;
import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;
import com.corhm.cultbot.Utils.Constants;
import com.corhm.cultbot.Utils.MessageUtils;
import com.corhm.cultbot.Utils.Constants.HELP_TYPE;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class SimpleCommand extends _BaseCommand{
	private final String message;
	private final String helpShort;
	private final String helpLong;
	private final boolean removeMessage;

	public SimpleCommand(final String command, final String message, final String help) {
		super(command);
		this.message = message;
		helpShort = helpLong = help;
		removeMessage = false;
	}
	public SimpleCommand(final String[] commands, final String message, final String help) {
		super(commands[0]);
		this.commands.addAll(Arrays.asList(commands).subList(1, commands.length));
		this.message = message;
		helpShort = helpLong = help;
		removeMessage = false;
	}
	
	public SimpleCommand(final String command, final String message, final String help, final boolean remove) {
		super(command);
		this.message = message;
		helpShort = helpLong = help;
		removeMessage = remove;
	}

	@Override
	protected void doStuff(final MessageReceivedEvent event) {
        if(removeMessage)
        	event.getMessage().delete();
        MessageUtils.sendMessage(event.getChannel(), message);
	}
	
	@Override
	protected String help(final MessageReceivedEvent event, Constants.HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += helpShort;
		}else{
			ret += "```md" + System.lineSeparator();
			ret += WordUtils.capitalize(command) + " Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* $" + command + System.lineSeparator();
			ret += ">    " + helpLong + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}
