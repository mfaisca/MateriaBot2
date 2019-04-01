package com.materia.materiabot.commands;
import java.util.List;
import com.materia.materiabot.Listener;
import java.util.LinkedList;
import net.dv8tion.jda.core.entities.Message;

public abstract class _BaseCommand {
	private List<String> triggerWords = new LinkedList<String>();
	
	protected _BaseCommand(String keyword, String... keywords) {
		triggerWords.add(keyword);
		for(String k : keywords)
			triggerWords.add(k);
	}
	
	public boolean validateCommand(Message message) {
		for(String t : triggerWords)
			if(message.getContentRaw().toLowerCase().startsWith(t.toLowerCase(), Listener.COMMAND_PREFIX.length()))
				return true;
		return false;
	}
	public boolean validatePermission(Message message) {
		return true;
	}
	
	public abstract void doStuff(Message message);
}