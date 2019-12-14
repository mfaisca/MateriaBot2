package com.materiabot.commands;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.Main;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.AuthorCommand;
import com.materiabot.commands.general.CleverbotCommand;
import com.materiabot.commands.general.EmoteCommand;
import com.materiabot.commands.general.PatreonCommand;
import com.materiabot.commands.general.StatusCommand;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class _Listener extends ListenerAdapter{
	public static final String COMMAND_PREFIX;
	public static final CleverbotCommand CLEVERBOT;
	public static final List<_BaseCommand> COMMANDS = new LinkedList<_BaseCommand>();
	
	static {
		String p = "$";
		try {
			 p = InetAddress.getLocalHost().getHostName().equalsIgnoreCase("HEAVEN") ? "%" : "$";
		} catch (UnknownHostException e) {}
		COMMAND_PREFIX = p;
		COMMANDS.addAll(Arrays.asList(
				new PatreonCommand(),
				new EmoteCommand(),
				new StatusCommand(),
				new AuthorCommand(),
				CLEVERBOT = new CleverbotCommand(),
				new SimpleCommand("invite", "**MateriaBot Server:** <https://discord.gg/XCTC7jY>" + System.lineSeparator() + "**MateriaBot Patreon:** https://www.patreon.com/MateriaBot", "Command to show the invite link to the Support Server")
			));
	}
	
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getMessage().getMentionedUsers().contains(Main.getClient().getSelfUser())) {
			CLEVERBOT.doChatStuff(event.getMessage());
			return;
		}
    	if(!event.getMessage().getContentRaw().startsWith(COMMAND_PREFIX))
    		return;
    	if(event.getAuthor().isBot())
    		return;
		if(event.isFromType(ChannelType.PRIVATE) || event.isFromType(ChannelType.GROUP))
			return;
    	for(_BaseCommand c : COMMANDS)
    		if(c.validateCommand(event.getMessage()))
    			if(c.validatePermission(event.getMessage())) {
    	    		int cd = -1;
    	    		if((cd = CooldownManager.userCooldown(event.getAuthor(), c.getCooldown(event.getMessage()))) == -1)
                		new Thread(() -> c.doStuff(event.getMessage())).start();
    	    		else {
    	    			cd = (cd/1000)+1;
    	    			int cd2 = cd;
    	    			Message m = MessageUtils.sendStatusMessageWarn(event.getChannel(), "Please wait " + cd + " second" + (cd == 1 ? "" : "s") + " to use that command.");
                		new Thread(() -> {
                			Constants.sleep(cd2 * 1000);
                			MessageUtils.deleteMessage(m);
                		}).start();
    	    		}
    				return;
    			}
    }
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
    	if(event.getUser().isBot())
    		return;
		if(event.isFromType(ChannelType.PRIVATE) || event.isFromType(ChannelType.GROUP))
			return;
		Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
		if(!originalMessage.getAuthor().equals(Main.getClient().getSelfUser())) //Confirmar se o equals funciona
			return;
    }
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
    	if(event.getUser().isBot())
    		return;
		if(event.isFromType(ChannelType.PRIVATE) || event.isFromType(ChannelType.GROUP))
			return;
    }
}