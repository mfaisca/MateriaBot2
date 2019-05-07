package com.materia.materiabot.commands;
import java.util.List;
import com.materia.materiabot.Listener;
import com.materia.materiabot.Utils.Constants;
import java.util.LinkedList;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;

public abstract class _BaseCommand{
	public static enum Function{
		EXECUTE, INSERT, UPDATE, SEARCH;
	}
	private List<String> triggerWords = new LinkedList<String>();

	public String getCommand() { return triggerWords.get(0); }
	public List<String> getTriggerWords() { return triggerWords; }
	
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
		if(Function.EXECUTE.equals(getFunction(message)))
			return true; //TODO
		return false;
	}
	protected Function getFunction(Message message) {
		return Function.EXECUTE;
	}
	
	public void doReactionStuff(Message messOrig, Message messResp, Emote emote) {}
	public abstract void doStuff(Message message);
	
	protected abstract String help(Message message, Constants.HELP_TYPE helpType);
}