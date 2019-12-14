package com.materiabot.commands.general;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.materiabot.Main;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class EmoteCommand extends _BaseCommand{
	public static final List<String> ALLOWED_FORMATS = Arrays.asList("png", "jpg", "jpeg", "gif");
	public EmoteCommand() {
		super("emo", "emote");
	}
	
	private static void add(final Message event, String message) {
		final String[] params = message.split(" "); 
		if(params.length != 3) {
			MessageUtils.sendMessage(event.getChannel(), "Syntax Error");
			return;
		}
		//Only allows direct image links
		if(ALLOWED_FORMATS.stream().noneMatch(af -> params[2].toLowerCase().endsWith(af))) {
			MessageUtils.sendMessage(event.getChannel(), "Only URLs that end with the following formats are allowed: " + Arrays.toString(ALLOWED_FORMATS.toArray()));
			return;
		}
		//Search if exists
		try(ResultSet rs = SQLAccess.executeSelect("SELECT * FROM users_emotes WHERE name = ? AND guildId = ?", params[1], event.getGuild().getId())){
			if(rs.next()) {
				MessageUtils.sendMessage(event.getChannel(), "Name already registered.");
				return;
			}
		} catch (Exception ex) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
		//Add if doesnt exist
		try(PreparedStatement statement = SQLAccess.getConnection().prepareStatement("INSERT into users_emotes(userid,name,url,guildId) values(?,?,?,?)")) {
			statement.setLong(1, event.getAuthor().getIdLong());
			statement.setString(2, params[1]);
			statement.setString(3, params[2]);
			statement.setLong(4, event.getGuild().getIdLong());
			statement.executeUpdate();
			MessageUtils.sendMessage(event.getChannel(), "Emote successfully registered!");
			if(event.getGuild().getMember(Main.getClient().getSelfUser()).hasPermission(Permission.MESSAGE_MANAGE))
				event.delete();
			showEmote(event, params[1]);
		} catch (InsufficientPermissionException ex) {
			System.out.println("Error related to the \"Manage Message\" permission");
		} catch (SQLException ex) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void remove(final Message event, String message) {
		boolean admin = event.getGuild().getMember(Main.getClient().getSelfUser()).hasPermission(Permission.MESSAGE_MANAGE);
		String query = "DELETE FROM users_emotes WHERE name = ? AND guildId = ?" + (admin ? "" : " AND userid = ?");
		try(PreparedStatement statement = SQLAccess.getConnection().prepareStatement(query)) {
			statement.setString(1, message.substring(7));
			statement.setLong(2, event.getGuild().getIdLong());
			if(!admin)
				statement.setLong(3, event.getAuthor().getIdLong());
			if(statement.executeUpdate() == 1)
				MessageUtils.sendMessage(event.getChannel(), "Emote removed!");
			else 
				MessageUtils.sendMessage(event.getChannel(), "You don't own an emote with that name.");
		} catch (SQLException ex) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void list(final Message event, String message) {
		try(ResultSet result = SQLAccess.executeSelect("SELECT name FROM users_emotes WHERE guildId = ? ORDER BY name", event.getGuild().getIdLong())) {
			listEmotes(event.getChannel(), result);
		} catch (Exception ex) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void listSelf(final Message event, String message) {
		try(ResultSet result = SQLAccess.executeSelect("SELECT name from users_emotes WHERE userId = ? AND guildId = ? ORDER BY name", event.getAuthor().getIdLong(), event.getGuild().getIdLong())) {
			listEmotes(event.getChannel(), result);
		} catch (Exception ex) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void listEmotes(MessageChannel channel, ResultSet result) throws SQLException {
		String string = "Emotes registered: ";
		if (result.next()){
			string = string + result.getString(1);
			while(result.next())
				if(string.length() + result.getString(1).length() >= MessageUtils.DISCORD_MESSAGE_LIMIT) {
					MessageUtils.sendMessage(channel, string);
					string = "Emotes registered: " + result.getString(1);
				}
				else
					string = string + ", " + result.getString(1);
		}
		else
			string = "No emotes";
		MessageUtils.sendMessage(channel, string);
	}
	private static void showEmote(final Message event, String message) {
		try(ResultSet result = SQLAccess.executeSelect("SELECT url FROM users_emotes WHERE name = ? AND guildId = ?", message, event.getGuild().getIdLong())){
			if(result.next()){
				EmbedBuilder embed = new EmbedBuilder();
				embed.setImage(result.getString(1));
				embed.setAuthor("Emote Name: " + message);
				embed.setFooter("by " + event.getGuild().getMember(event.getAuthor()).getEffectiveName(), event.getAuthor().getAvatarUrl());
				MessageUtils.sendEmbed(event.getChannel(), embed);
				boolean canManageMessages = event.getGuild().getMember(Main.getClient().getSelfUser()).hasPermission(Permission.MESSAGE_MANAGE);
				if(canManageMessages)
					event.delete();
			}
		} catch(InsufficientPermissionException e) {
			;
		} catch (Exception ex) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void doStuff(final Message event) {
		String message;
		try {
			message = event.getContentDisplay().substring(event.getContentDisplay().indexOf(" ")+1);
		} catch(Exception e) {
			MessageUtils.sendStatusMessageError(event.getChannel(), "Wrong Format");
			return;
		}
		if(message.startsWith("add "))
			add(event, message);
		else if(message.startsWith("remove ") || message.startsWith("delete "))
			remove(event, message);
		else if(message.equals("list"))
			list(event, message);
		else if(message.equals("list self"))
			listSelf(event, message);
		else
			showEmote(event, message);
	}

	@Override
	public String help(Message event, HelpCommand.HELP_TYPE helpType) {
		String ret = "";
		if(HelpCommand.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Use user-added Emotes";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Emote Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HelpCommand.HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + _Listener.COMMAND_PREFIX + "emo <EmoteName>" + System.lineSeparator();
			ret += ">    Shows the emote with said name." + System.lineSeparator();
			ret += "* " + _Listener.COMMAND_PREFIX + "emo add <EmoteName> <EmoteURL>" + System.lineSeparator();
			ret += ">    Adds the emote from the URL to the name." + System.lineSeparator();
			ret += "* " + _Listener.COMMAND_PREFIX + "emo remove <EmoteName>" + System.lineSeparator();
			ret += ">    Removes the emote with said name. Only person that added it or moderators can delete it." + System.lineSeparator();
			ret += "* " + _Listener.COMMAND_PREFIX + "emo list" + System.lineSeparator();
			ret += ">    Shows all registered emotes" + System.lineSeparator();
			ret += "* " + _Listener.COMMAND_PREFIX + "emo list self" + System.lineSeparator();
			ret += ">    Shows all registered emotes by you" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}