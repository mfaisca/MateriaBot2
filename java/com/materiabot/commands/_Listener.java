package com.materiabot.commands;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class _Listener extends ListenerAdapter{
	private static JDA client; //Small cheat to have Dynamic Constants
	public static JDA getClient() { return client; }
	public static void setClient(JDA c) { client = c; }
	
	public static final String DEFAULT_PREFIX;
    private static HashMap<Long, String> PREFIX = new HashMap<Long, String>();
	private static final List<_BaseCommand> BASE_COMMANDS = new LinkedList<_BaseCommand>();
	public static final List<_BaseCommand> COMMANDS = new LinkedList<_BaseCommand>();
	
	static {
		String p = "$";
		try {
			 p = InetAddress.getLocalHost().getHostName().equalsIgnoreCase("HEAVEN") ? "%" : "$";
		} catch (UnknownHostException e) {}
		DEFAULT_PREFIX = p;
		BASE_COMMANDS.addAll(Arrays.asList(
				new StatusCommand(),
				new AuthorCommand(),
				new PatreonCommand()
			));
		COMMANDS.addAll(BASE_COMMANDS);
	}
	
	public static void unloadPluginCommands() {
		COMMANDS.clear();
		COMMANDS.addAll(BASE_COMMANDS);
	}
	
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
    	new Thread(() -> {
	    	if(event.getAuthor().isBot())
	    		return;
	    	for(_BaseCommand c : COMMANDS)
	    		if(c.validateCommand(event.getMessage()))
	    			if(c.validatePermission(event.getMessage())) {
	    	    		int cd = -1;
	    	    		if((cd = CooldownManager.userCooldown(event.getAuthor(), c.getCooldown(event.getMessage()))) == -1)
	                		c.doStuff(event.getMessage());
	    	    		else {
	    	    			cd = (cd / 1000) + 1;
	    	    			Message m = MessageUtils.sendStatusMessageWarn(event.getChannel(), "Please wait " + cd + " second" + (cd == 1 ? "" : "s") + " to use that command.");
    	    				Constants.sleep(cd * 1000);
                			MessageUtils.deleteMessage(m);
	    	    		}
	    				return;
	    			}
    	}).start();
    }
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
    	new Thread(() -> {
		    	if(event.getUser().isBot())
		    		return;
//				if(event.isFromType(ChannelType.PRIVATE) || event.isFromType(ChannelType.GROUP))
//					return;
				Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				if(!originalMessage.getAuthor().equals(Constants.getClient().getSelfUser()))
					return;
		    	for(_BaseCommand c : COMMANDS)
		    		if(c.validateCommand(originalMessage))
		        		new Thread(() -> c.doAddReactionStuff(event)).start();
    	}).start();
    }
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
    	new Thread(() -> {
	    	if(event.getUser().isBot())
	    		return;
//			if(event.isFromType(ChannelType.PRIVATE) || event.isFromType(ChannelType.GROUP))
//				return;
			Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
			if(!originalMessage.getAuthor().equals(Constants.getClient().getSelfUser()))
				return;
	    	for(_BaseCommand c : COMMANDS)
	    		if(c.validateCommand(originalMessage))
	        		new Thread(() -> c.doRemoveReactionStuff(event)).start();
    	}).start();
    }
    
    public static String getGuildPrefix(Guild g) {
    	if(!PREFIX.containsKey(g.getIdLong())) {
        	String p = SQLAccess.getGuildPrefix(g);
        	PREFIX.put(g.getIdLong(), p);
    	}
    	return PREFIX.get(g.getIdLong());
    }
}