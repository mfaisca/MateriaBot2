package com.materiabot.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class MessageUtils {
	public static enum DefaultMessages{
		CHAR_NOT_FOUND("Character Not Found Error"),
		EQUIP_NOT_FOUND("Equipment Not Found Error"),
		SKILL_NOT_FOUND("Skill Not Found Error"),
		PASSIVE_NOT_FOUND("Passive Not Found Error"),
		SKILL_SETUP_ERROR("Skill Setup Error"),
		SYNTAX_ERROR("Syntax Error"),
		UNKNOWN_ERROR("Unknown Error - Please contact Quetz!"),
		;
		
		private String msg;
		
		private DefaultMessages(String msg) { this.msg = msg; }
		public String getMessage() { return msg; }
	}
	
	public static final String S = " ︀︀";
    public static final int DISCORD_MESSAGE_LIMIT = 1800;
    public static final int DISCORD_EMBED_FIELD_LIMIT = 1024;
    public static final int DISCORD_EMBED_MAX_COLUMN_WIDTH = 30;
	public static final String UNKNOWN_EMOTE = "<:unknownSpr:647380214820765709>";

    public static final String empty(int l) {
    	String s = "";
    	for(int i = 0; i < l; i++)
    		s += S;
    	return s;
    }
    
	public static final Message sendStatusMessageError(MessageChannel channel, String message) {
    	return sendMessage(channel, ":no_entry: | " + message);
	}
	public static final Message sendStatusMessageCrash(MessageChannel channel, String message) {
    	return sendMessage(channel, ":ambulance: | " + message);
	}
	public static final Message sendStatusMessageInfo(MessageChannel channel, String message) {
    	return sendMessage(channel, ":information_source: | " + message);
	}
	public static final Message sendStatusMessageWarn(MessageChannel channel, String message) {
    	return sendMessage(channel, ":warning: | " + message);
	}
    public static final Message sendWhisper(final User user, final String message){
    	return sendMessage(user.getJDA().getPrivateChannelById(user.getIdLong()), message);
    }
    public static final Message sendMessage(final MessageChannel channel, final String message){
    	long time = System.currentTimeMillis();
    	while((System.currentTimeMillis() - time) < 30000)
	    	try{
	    		return channel.sendMessage(message).complete();
	    	} catch(Exception e){ 
	    		Constants.sleep(1000);
	    	}
    	return null;
    }
    
    public static final Message sendImage(final MessageChannel channel, final String imageURL){
    	return sendEmbed(channel, new EmbedBuilder().setImage(imageURL));
    }
	public static final Message sendError(MessageChannel channel, String message) {
    	return sendEmbed(channel, message, null); //TODO Add Moogle Sorry Emote Link
	}
    public static final Message sendEmbed(final MessageChannel channel, String message, String emote) {
		EmbedBuilder builder = new EmbedBuilder();
		if(emote != null)
			builder.setThumbnail(emote);
		builder.setDescription(message);
		return sendEmbed(channel, builder);
    }
    public static final Message sendEmbed(final MessageChannel channel, final EmbedBuilder embed){
    	long time = System.currentTimeMillis();
    	while((System.currentTimeMillis() - time) < 30000)
	    	try{
	        	return channel.sendMessage(embed.build()).complete();
	    	} catch(Exception e){ 
	    		Constants.sleep(1000);
	    	}
    	return null;
    }
    
	public static final Message editMessage(Message message, String msg) {
    	long time = System.currentTimeMillis();
    	while((System.currentTimeMillis() - time) < 30000)
	    	try{
	    		return message.editMessage(msg).complete();
	    	} catch(Exception e){ 
	    		Constants.sleep(1000);
	    	}
    	return null;
	}
	public static final Message editMessage(Message message, EmbedBuilder msg) {
    	while(true)
	    	try{
	    		return message.editMessage(msg.build()).complete();
	    	} catch(Exception e){
	    		Constants.sleep(1000);
	    	}
	}
    
	public static final Message addReactions(Message message, Emote... reactions) {
		long time = System.currentTimeMillis();
		for(Emote emote : reactions) {
			while(true)
				try{
					message.addReaction(emote);
					Constants.sleep(1000);
					break;
				} catch(Exception e){
					if(System.currentTimeMillis() - time > 30000)
						break;
					Constants.sleep(2000);
				}
		}
		return message;
	}

	public static void removeReaction(Message message, User user, Emote reaction) {
		long time = System.currentTimeMillis();
		while(true)
			try{
				message.removeReaction(reaction, user);
				Constants.sleep(1000);
				break;
			} catch(Exception e){
				if(System.currentTimeMillis() - time > 30000)
					break;	
				Constants.sleep(2000);
			}
	}

	public static void deleteMessage(Message m) {
		long time = System.currentTimeMillis();
		while(true)
			try{
				m.delete();
				break;
			} catch(Exception e){
				if(System.currentTimeMillis() - time > 30000)
					break;
				Constants.sleep(2000);
			}
	}
}