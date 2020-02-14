package com.materiabot.commands;
import java.util.List;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.commands.general.HelpCommand;
import java.util.LinkedList;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public abstract class _BaseCommand{
	protected List<String> triggerWords = new LinkedList<String>();

	public String getCommand() { return triggerWords.get(0); }
	public List<String> getTriggerWords() { return triggerWords; }
	
	protected _BaseCommand(String keyword, String... keywords) {
		triggerWords.add(keyword);
		for(String k : keywords)
			triggerWords.add(k);
	}
	
	public boolean validateCommand(final Message message) {
		String prefix = _Listener.getGuildPrefix(message.getGuild());
		for(String t : triggerWords)
			if(message.getContentRaw().toLowerCase().startsWith(t.toLowerCase(), prefix.length()))
				return true;
		return false;
	}
	public boolean validatePermission(Message message) {
		return true;
	}
	public CooldownManager.Type getCooldown(Message message) {
		return CooldownManager.Type.REGULAR;
	}

	public void doAddReactionStuff(MessageReactionAddEvent event) {}
	public void doRemoveReactionStuff(MessageReactionRemoveEvent event) {}
	public abstract void doStuff(Message message);
	
	public String help(HelpCommand.HELP_TYPE helpType) { return null; }
}