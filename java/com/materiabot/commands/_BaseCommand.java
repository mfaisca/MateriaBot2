package com.materiabot.commands;
import java.util.List;

import com.materiabot.Utils.CooldownManager;
import com.materiabot.commands.general.HelpCommand;

import java.util.LinkedList;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

public abstract class _BaseCommand{
	protected List<String> triggerWords = new LinkedList<String>();

	public String getCommand() { return triggerWords.get(0); }
	public List<String> getTriggerWords() { return triggerWords; }
	
	protected _BaseCommand(String keyword, String... keywords) {
		triggerWords.add(keyword);
		for(String k : keywords)
			triggerWords.add(k);
	}
	
	public boolean validateCommand(Message message) {
		for(String t : triggerWords)
			if(message.getContentRaw().toLowerCase().startsWith(t.toLowerCase(), _Listener.COMMAND_PREFIX.length()))
				return true;
		return false;
	}
	public boolean validatePermission(Message message) {
		return true;
	}
	public CooldownManager.Type getCooldown(Message message) {
		return CooldownManager.Type.REGULAR;
	}
	
	public void doReactionStuff(Message messOrig, Message messResp, Emote emote) {}
	public abstract void doStuff(Message message);
	
	public abstract String help(Message message, HelpCommand.HELP_TYPE helpType);
}