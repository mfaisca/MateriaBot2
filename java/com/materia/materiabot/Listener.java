package com.materia.materiabot;
import java.util.LinkedList;
import java.util.List;
import com.materia.materiabot.commands.PingPong;
import com.materia.materiabot.commands._BaseCommand;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Listener extends ListenerAdapter{
	public static String COMMAND_PREFIX = Configs.COMMAND_PREFIX;
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
    				return;
    			}
    }
}