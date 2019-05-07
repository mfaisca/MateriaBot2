package com.materia.materiabot.commands;
import com.corhm.cultbot.IO.SQL.SQLAccess;
import com.corhm.cultbot.Utils.BotException;
import com.corhm.cultbot.Utils.Constants;
import com.corhm.cultbot.Utils.MessageUtils;
import com.corhm.cultbot.Utils.Constants.HELP_TYPE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class EmoteCommand extends _BaseCommand{
	public static final List<String> ALLOWED_FORMATS = Arrays.asList("png", "jpg", "jpeg", "gif");
	public EmoteCommand() {
		super("emo", "emote");
	}
	
	private static void add(final MessageReceivedEvent event, String message) {
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
		try(ResultSet rs = SQLAccess.executeSelect("SELECT * FROM users_emotes WHERE name = ? AND guildId = ?", params[1], event.getGuild().getLongID())){
			if(rs.next()) {
				MessageUtils.sendMessage(event.getChannel(), "Name already registered.");
				return;
			}
		} catch (SQLException|BotException ex) {
			MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
		//Add if doesnt exist
		try(PreparedStatement statement = SQLAccess.getConnection().prepareStatement("INSERT into users_emotes(userid,name,url,guildId) values(?,?,?,?)")) {
			statement.setLong(1, event.getAuthor().getLongID());
			statement.setString(2, params[1]);
			statement.setString(3, params[2]);
			statement.setLong(4, event.getGuild().getLongID());
			statement.executeUpdate();
			MessageUtils.sendMessage(event.getChannel(), "Emote successfully registered!");
			boolean canManageMessages = event.getClient().getOurUser().
					getRolesForGuild(event.getGuild()).stream().flatMap(r -> r.getPermissions().stream()).
					distinct().anyMatch(p -> p.equals(Permissions.MANAGE_MESSAGES));
			if(canManageMessages)
				event.getMessage().delete();
			showEmote(event, params[1]);
		} catch (MissingPermissionsException ex) {
			System.out.println("Error related to the \"Manage Message\" permission");
		} catch (SQLException ex) {
			MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void remove(final MessageReceivedEvent event, String message) {
		boolean admin = event.getAuthor().getRolesForGuild(event.getGuild()).stream()
				.flatMap(r -> r.getPermissions().stream()).distinct().anyMatch(p -> p.equals(Permissions.MANAGE_MESSAGES));
		String query = "DELETE FROM users_emotes WHERE name = ? AND guildId = ?" + (admin ? "" : " AND userid = ?");
		try(PreparedStatement statement = SQLAccess.getConnection().prepareStatement(query)) {
			statement.setString(1, message.substring(7));
			statement.setLong(2, event.getGuild().getLongID());
			if(!admin)
				statement.setLong(3, event.getAuthor().getLongID());
			if(statement.executeUpdate() == 1)
				MessageUtils.sendMessage(event.getChannel(), "Emote removed!");
			else 
				MessageUtils.sendMessage(event.getChannel(), "You don't own an emote with that name.");
		} catch (SQLException ex) {
			MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void list(final MessageReceivedEvent event, String message) {
		try(ResultSet result = SQLAccess.executeSelect("SELECT name FROM users_emotes WHERE guildId = ? ORDER BY name", event.getGuild().getLongID())) {
			listEmotes(event.getChannel(), result);
		} catch (SQLException|BotException ex) {
			MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void listSelf(final MessageReceivedEvent event, String message) {
		try(ResultSet result = SQLAccess.executeSelect("SELECT name from users_emotes WHERE userId = ? AND guildId = ? ORDER BY name", event.getAuthor().getLongID(), event.getGuild().getLongID())) {
			listEmotes(event.getChannel(), result);
		} catch (SQLException|BotException ex) {
			MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static void listEmotes(IChannel channel, ResultSet result) throws SQLException {
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
	private static void showEmote(final MessageReceivedEvent event, String message) {
		try(ResultSet result = SQLAccess.executeSelect("SELECT url FROM users_emotes WHERE name = ? AND guildId = ?", message, event.getGuild().getLongID())){
			if(result.next()){
				EmbedBuilder embed = new EmbedBuilder();
				embed.withImage(result.getString(1));
				embed.withAuthorName("Emote Name: " + message);
				embed.withFooterIcon(event.getMessage().getAuthor().getAvatarURL());
				embed.withFooterText("by " + event.getMessage().getAuthor().getDisplayName(event.getGuild()));
				MessageUtils.sendEmbed(event.getChannel(), embed);
				boolean canManageMessages = event.getClient().getOurUser().
						getRolesForGuild(event.getGuild()).stream().flatMap(r -> r.getPermissions().stream()).
						distinct().anyMatch(p -> p.equals(Permissions.MANAGE_MESSAGES));
				if(canManageMessages)
					event.getMessage().delete();
			}
		} catch(MissingPermissionsException e) {
			;
		} catch (SQLException | BotException ex) {
			MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
			Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected void doStuff(final MessageReceivedEvent event) {
		String message;
		try {
			message = event.getMessage().getContent().substring(event.getMessage().getContent().indexOf(" ")+1);
		} catch(Exception e) {
			MessageUtils.sendMessageError(event.getChannel(), "Wrong Format");
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
	public String getFunction(MessageReceivedEvent event) {
		String message = event.getMessage().getContent().substring(event.getMessage().getContent().indexOf(" ")+1);
		if(message.startsWith("add "))
			return Constants.FUNCTION_NAMES.ADD.name;
		else if(message.startsWith("remove ") || message.startsWith("delete ")) {
			boolean admin = event.getAuthor().getRolesForGuild(event.getGuild()).stream()
					.flatMap(r -> r.getPermissions().stream()).distinct().anyMatch(p -> p.equals(Permissions.MANAGE_MESSAGES));
			String query = "SELECT * FROM users_emotes WHERE name = ? AND guildId = ?" + (admin ? "" : " AND userid = ?");
			try(PreparedStatement statement = SQLAccess.getConnection().prepareStatement(query)) {
				statement.setString(1, message.substring(7));
				statement.setLong(2, event.getGuild().getLongID());
				if(!admin)
					statement.setLong(3, event.getAuthor().getLongID());
				if(statement.executeQuery().next())
					return Constants.FUNCTION_NAMES.EXECUTE.name;
				else 
					return Constants.FUNCTION_NAMES.DELETE.name;
			} catch (SQLException ex) {
				MessageUtils.sendMessageError(event.getChannel(), "Error communicating with the database!");
				Logger.getLogger(EmoteCommand.class.getName()).log(Level.SEVERE, null, ex);
			}
			return Constants.FUNCTION_NAMES.DELETE.name;
		}
		return Constants.FUNCTION_NAMES.EXECUTE.name;
	}

	@Override
	protected String help(MessageReceivedEvent event, HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Use user-added Emotes";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Emote Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "emo <EmoteName>" + System.lineSeparator();
			ret += ">    Shows the emote with said name." + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "emo add <EmoteName> <EmoteURL>" + System.lineSeparator();
			ret += ">    Adds the emote from the URL to the name." + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "emo remove <EmoteName>" + System.lineSeparator();
			ret += ">    Removes the emote with said name. Only person that added it or moderators can delete it." + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "emo list" + System.lineSeparator();
			ret += ">    Shows all registered emotes" + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "emo list self" + System.lineSeparator();
			ret += ">    Shows all registered emotes by you" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}