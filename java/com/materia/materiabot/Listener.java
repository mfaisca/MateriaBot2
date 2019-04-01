package com.materia.materiabot;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.materia.materiabot.IO.SQL.ConfigsDB;
import com.materia.materiabot.Utils.Constants.Dual;
import com.materia.materiabot.commands.PingPong;
import com.materia.materiabot.commands._BaseCommand;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class Listener extends ListenerAdapter{
	public static String COMMAND_PREFIX = "!!";
	private static List<_BaseCommand> commands = new LinkedList<_BaseCommand>();
	
	static {
		commands.add(new PingPong());
	}
	
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
    	for(_BaseCommand c : commands)
    		if(c.validateCommand(event.getMessage()))
    			if(c.validatePermission(event.getMessage())) {
            		new Thread(() -> c.doStuff(event.getMessage())).start();
            		ConfigsDB.addStatistic(event.getGuild().getIdLong(), event.getAuthor().getIdLong(), c.getCommand());
    				return;
    			}
    }
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
    	if(!messageAssociation.containsKey(event.getMessageIdLong()))
    		return;
    	if(event.getUser().getIdLong() == event.getJDA().getSelfUser().getIdLong())
    		return;
    	Message mOrig = messageAssociation.get(event.getMessageIdLong()).getValue1();
    	Message mResp = messageAssociation.get(event.getMessageIdLong()).getValue2();
    	for(_BaseCommand c : commands)
    		if(c.validateCommand(mOrig))
    			if(c.validatePermission(mOrig)) {
            		new Thread(() -> c.doReactionStuff(mOrig, mResp, event.getReactionEmote().getEmote())).start();
            		ConfigsDB.addStatistic(event.getGuild().getIdLong(), event.getMember().getUser().getIdLong(), c.getCommand());
    				return;
    			}
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    
	public static final class ReactionInfo{
		//TODO
		//original & response messages
		//some status id
	}
	private static Map<Long, Dual<Message, Message>> messageAssociation = new HashMap<Long, Dual<Message, Message>>();
	//Add Messages to messageAssociation IF its a message the bot has to look for related to reactions
	
    public void addReactionListener(Message original, Message response) {
    	messageAssociation.put(response.getIdLong(), new Dual<Message, Message>(original, response));
    }
}