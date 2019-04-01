package com.materia.materiabot.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class MessageUtils {
    public static final String UNKNOWN_EMOTE = "<:UnknownEmote:562403473438670863>";
    
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
}