package com.materia.materiabot.commands;
import org.apache.commons.lang3.text.WordUtils;
import com.corhm.cultbot.Utils.BotException;
import com.corhm.cultbot.Utils.Constants;
import com.corhm.cultbot.Utils.MessageUtils;
import com.corhm.cultbot.Utils.Constants.HELP_TYPE;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class HelpCommand extends _BaseCommand{	
	public HelpCommand() {
		super("help", "halp", "plz");
	}

	@Override
	protected void doStuff(final MessageReceivedEvent event) {
		try {
			String msg = event.getMessage().getContent();
			if(!msg.contains(" "))
				showAvailableCommands(event);
			else {
				msg = msg.substring(msg.indexOf(" ") + 1).trim();
				if(msg.equalsIgnoreCase("all"))
					showAllCommands(event);
				else
					showSpecificHelp(event, msg);
			}
		} catch (BotException e) {
			e.printStackTrace();
		}
	}
	
	private void showAllCommands(final MessageReceivedEvent event) throws BotException {
		String str = "";
		str += "```md" + System.lineSeparator();
		str += "List of Command" + System.lineSeparator();
		str += "===============" + System.lineSeparator();
		str += System.lineSeparator();
		for(_BaseCommand command : _Listener.COMMANDS) {
			str += "* " + WordUtils.capitalize(command.getCommand()) + " Command" + System.lineSeparator();
			str += ">    " + command.help(event, HELP_TYPE.SHORT) + System.lineSeparator();
		}
		str += "```";
		MessageUtils.sendMessage(event.getChannel(), str);
	}
	
	private void showAvailableCommands(final MessageReceivedEvent event) throws BotException {
		String str = "";
		str += "```md" + System.lineSeparator();
		str += "List of Command" + System.lineSeparator();
		str += "===============" + System.lineSeparator();
		str += System.lineSeparator();
		for(_BaseCommand command : _Listener.COMMANDS) {
			if(command.help(event, HELP_TYPE.SHOW) == null) continue;
			if(command.help(event, HELP_TYPE.SHORT) == null || command.help(event, HELP_TYPE.SHORT).equals("null"))
				continue;
			str += "* " + WordUtils.capitalize(command.getCommand()) + " Command" + System.lineSeparator();
			str += ">    " + command.help(event, HELP_TYPE.SHORT) + System.lineSeparator();
		}
		str += "```";
		MessageUtils.sendMessage(event.getChannel(), str);
	}
	
	private void showSpecificHelp(final MessageReceivedEvent event, String command) throws BotException {
		for(_BaseCommand command2 : _Listener.COMMANDS) 
			if(command2.getCommands().contains(command)) {
				if(command2.help(event, HELP_TYPE.LONG) != null && !command2.help(event, HELP_TYPE.LONG).equals("null"))
					MessageUtils.sendMessage(event.getChannel(), command2.help(event, HELP_TYPE.LONG));
				return;
			}
	}
	
	@Override
	protected String help(final MessageReceivedEvent event, Constants.HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Shows the help information for commands";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Help Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += ">    Did you really ask for the help for the help command?" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}