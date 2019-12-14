package com.materiabot.commands.general;
import java.util.HashMap;
import java.util.Map;

import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.michaelwflaherty.cleverbotapi.CleverBotQuery;
import net.dv8tion.jda.api.entities.Message;

public class CleverbotCommand extends _BaseCommand {
	private static final CleverBotQuery BOT = new CleverBotQuery("CC5weO-5tdJfovsUHgeMK8jUHPA", null);
	private static final Map<String, String> serverConversationID = new HashMap<String, String>();
	
	public CleverbotCommand() {
		super("cleverbot");
	}

	@Override
	public void doStuff(Message message) {
		serverConversationID.put(message.getGuild().getId(), null);
		MessageUtils.sendMessage(message.getChannel(), "Chat History has been cleared. RIP");
		
	}
	public synchronized void doChatStuff(Message message) {
		if(!(message.getAuthor().equals(Constants.QUETZ.getUser()) || PatreonCommand.isUserPatreon(message.getAuthor()) || 
			 message.getGuild().equals(Constants.MATERIABOT_SERVER) || message.getGuild().getId().equals("304968876582633474"))) {
			MessageUtils.sendStatusMessageInfo(message.getChannel(), "Only Patreons can use this feature outside the MateriaBot Server.");
			return;
		}
		int cd = -1;
		if((cd = CooldownManager.userCooldown(message.getAuthor(), CooldownManager.Type.CLEVERBOT)) != -1){
			cd = (cd/1000)+1;
			int cd2 = cd;
			Message m = MessageUtils.sendStatusMessageWarn(message.getChannel(), "Please wait " + cd + " second" + (cd == 1 ? "" : "s") + " to use that command.");
    		new Thread(() -> {
    			Constants.sleep(cd2 * 1000);
    			MessageUtils.deleteMessage(m);
    		}).start();
    		return;
		}
		String msg = message.getContentDisplay().replace("@MateriaBot", "").replace("@", "").replace("#", "").trim();
		try {
			String conversationID = serverConversationID.get(message.getGuild().getId());
			BOT.setConversationID(conversationID == null ? "" : conversationID);
			BOT.setPhrase(msg);
			BOT.sendRequest();
			String response = BOT.getResponse();
			MessageUtils.sendMessage(message.getChannel(), response);
			if(conversationID == null)
				serverConversationID.put(message.getGuild().getId(), BOT.getConversationID());
		} catch (Exception e) {
			MessageUtils.sendStatusMessageError(message.getChannel(), "Error accessing CleverBot API. Please try again later.");
			MessageUtils.sendWhisper(Constants.QUETZ.getUser(), "Error using CleverBot. Error: " + e.getMessage());
		}
	}

	@Override
	public boolean validatePermission(Message message) {
		return message.getAuthor().getIdLong() == Constants.QUETZ.getIdLong();
	}

	@Override
	public String help(Message message, HelpCommand.HELP_TYPE helpType) {
		return null;
	}
}