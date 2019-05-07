package com.materia.materiabot.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.corhm.cultbot.IO.SQL.SQLAccess;
import com.corhm.cultbot.Utils.BotException;
import com.corhm.cultbot.Utils.Constants;
import com.corhm.cultbot.Utils.Constants.HELP_TYPE;
import com.corhm.cultbot.Utils.MessageUtils;
import com.corhm.cultbot.Utils.SharedMethods;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class PermissionCommand extends _BaseCommand{
	public PermissionCommand() {
		super("permission");
	}

	/*Permissions: (They are server-specific, but Quetzalma user can always use them)
		Nothing on the DB = Allowed
		ON on the DB = Allowed
		OFF on the DB = Disallowed
		Anything else = Needs that specific role(Configurable by ID or Name)
	*/
	public static boolean hasPermission(String command, String function, MessageReceivedEvent event) throws BotException {
		IGuild guild = event.getGuild();
		IUser author = event.getAuthor();
		boolean isWhisper = guild == null;
		boolean admin = author.getLongID() == Constants.QUETZ_ID || (!isWhisper && author.getLongID() == guild.getOwnerLongID()) || (!isWhisper && author.getRolesForGuild(guild).stream()
				.flatMap(r -> r.getPermissions().stream()).distinct().anyMatch(p -> p.equals(Permissions.ADMINISTRATOR)));
		if(admin)
			return true; //Is Admin
		return isVisible(command, function, event);
	}
	public static boolean isVisible(String command, String function, MessageReceivedEvent event) throws BotException {
		Boolean serverPermission = null, channelPermission = null, rolePermission = null, userPermission = null;
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		boolean isWhisper = guild == null;
		try {
			ResultSet rs = SQLAccess.executeSelect("SELECT * FROM permissions WHERE command = ? AND function = ? AND guildId = ? ORDER BY permission ASC", command, function, isWhisper ? -1 : guild.getLongID());
			if(!rs.next())
				return true; //No Permission defined
			do {
				final Long chanValue = rs.getLong("channelId");
				final Long roleValue = rs.getLong("roleId");
				final Long userValue = rs.getLong("userId");
				final String permValue = rs.getString("permission");
//				if(permValue.equals("BLOCK"))
//					throw new BotException("Command disabled", 201);
				if(roleValue == 0 && chanValue == 0 && userValue == 0) //Server
					serverPermission = permValue.equalsIgnoreCase("ON");
				if(chanValue != 0) { //Channel
					boolean check = channel.getLongID() == chanValue;
					if(check)
						channelPermission = permValue.equalsIgnoreCase("ON");
				}
				if(roleValue != 0) { //Role
					boolean check = author.getRolesForGuild(guild).stream().map(r -> r.getLongID()).anyMatch(n -> n.longValue() == roleValue);
					if(check)
						rolePermission = permValue.equalsIgnoreCase("ON");
				}
				if(userValue != 0) {
					if(author.getLongID() == userValue)
						userPermission = permValue.equalsIgnoreCase("ON");
				}
			} while(rs.next());
		} catch (SQLException e) {
			throw new BotException(e, "Error obtaining permissions", 300);
		}
		if(userPermission != null)
			return userPermission;
		if(rolePermission != null)
			return rolePermission;
		if(channelPermission != null)
			return channelPermission;
		if(serverPermission != null)
			return serverPermission;
		return false;
	}
	
	@Override
	protected void doStuff(final MessageReceivedEvent event) {
		try {
			boolean whisperPermission = false;
			String msg = event.getMessage().getContent();
			if(msg.contains(" ")){
				msg = msg.substring(msg.indexOf(" ") + 1).trim();
				if(msg.contains("whisper ") && event.getAuthor().getLongID() == Constants.QUETZ_ID) {
					whisperPermission = true;
					msg = msg.replace("whisper ", "");
				}
				//Split is done manually because some roles might have spaces in their names
				String[] split = msg.split(" ");
				String command = split[0];
				if(command.equalsIgnoreCase("info") && event.getAuthor().getLongID() == Constants.QUETZ_ID) {
					IGuild newGuild = SharedMethods.client.getGuildByID(Long.parseLong(split[1]));
					MessageUtils.sendMessage(event.getChannel(), "Server: " + newGuild.getName() + ""
							+ "\nOwner: " + newGuild.getOwner().mention()
							+ "\nMembers: " + newGuild.getTotalMemberCount()
							+ "\nInactive Members(30+d): " + newGuild.getUsersToBePruned(30));
					return;
				}
				else if(command.equalsIgnoreCase("list")) {
					String list = getPermissionList(event, whisperPermission);
					if(list.length() > MessageUtils.DISCORD_MESSAGE_LIMIT)
						MessageUtils.sendMessageError(event.getChannel(), "List is above the Discord message limit and Quetz was too lazy to fix it.");
					else
						MessageUtils.sendMessage(event.getChannel(), list);
					return;
				}
				String function = split[1];
				String entityType = split[2];
				String entity = split.length == 5 ? split[3] : null;
				String permission = split.length == 5 ? split[4] : split[3];
				if(!(entityType.equalsIgnoreCase("server") || entityType.equalsIgnoreCase("channel") || entityType.equalsIgnoreCase("role") || entityType.equalsIgnoreCase("user"))) {
					MessageUtils.sendMessage(event.getChannel(), "Please use the correct format. Contact Quetz if you can't make it work.");
					return;
				}
				if(!(entityType.equalsIgnoreCase("server") || (entity != null && entity.startsWith("<") && entity.endsWith(">")))) {
					MessageUtils.sendMessage(event.getChannel(), "Please use the correct format. Contact Quetz if you can't make it work.");
					return;
				}
				if(entityType.equalsIgnoreCase("user"))
					entity = event.getMessage().getMentions().get(0).getStringID();
				else if(entityType.equalsIgnoreCase("channel"))
					entity = event.getMessage().getChannelMentions().get(0).getStringID();//entity.substring(2, entity.length()-1);
				else if(entityType.equalsIgnoreCase("role"))
					entity = event.getMessage().getRoleMentions().get(0).getStringID();//entity.substring(3, entity.length()-1);
				changePermission(command.toLowerCase(), function.toLowerCase(), whisperPermission ? -1 : event.getGuild().getLongID(), entityType, entity, permission.toUpperCase());
				MessageUtils.sendMessage(event.getChannel(), "Permission to command '" + command + "/" + function + "' was updated");
			}
			else{
				MessageUtils.sendMessage(event.getChannel(), "Please use the correct format. Contact Quetz if you can't make it work.");
			}
		} catch (BotException e) {
			e.printStackTrace();
			MessageUtils.sendMessageError(event.getChannel(), e.getMessage());
		}
	}
	
	private String getPermissionList(MessageReceivedEvent event, boolean whisperPermission) throws BotException {
		String finalString = "";
		String command = null, function = null, permission = "";
		long channelId, roleId, userId, guildId = whisperPermission ? -1 : event.getGuild().getLongID();
		long messageState = 0, helper = 0;
		try(ResultSet rs = SQLAccess.executeSelect("SELECT * FROM permissions WHERE guildId = ? ORDER BY userId, roleId, channelId, command, function", guildId)){
			while(rs.next()) {
				command = rs.getString(1);
				function = rs.getString(2);
				channelId = rs.getLong(4);
				roleId = rs.getLong(5);
				userId = rs.getLong(6);
				permission = rs.getString(7);
				
				if(messageState == 0) {
					finalString = "Current Permissions:" + System.lineSeparator() + "===============" + System.lineSeparator() + System.lineSeparator();
					if(whisperPermission)
						finalString += "[*][Whispers][*]" + System.lineSeparator();
					else
						finalString += "[*][Server][*]" + System.lineSeparator();
					messageState++;
				}
				if(channelId == 0 && roleId == 0 && userId == 0) {
					finalString += ">    " + command + "/" + function + " - " + permission + System.lineSeparator();
					continue;
				}
				if(messageState == 1) {
					finalString += "[*][Channels][*]" + System.lineSeparator();
					messageState = 2;
				}
				if(channelId != 0 && roleId == 0 && userId == 0) {
					IChannel channel = event.getGuild().getChannelByID(channelId);
					if(channel == null) continue;
					if(helper != channelId){
						helper = channelId;
						finalString += "* " + channel.getName() + System.lineSeparator();
					}
					finalString += ">    " + command + "/" + function + " - " + permission + System.lineSeparator();
					continue;
				}
				if(messageState == 2) {
					finalString += "[*][Roles][*]" + System.lineSeparator();
					messageState = 3;
				}
				if(channelId == 0 && roleId != 0 && userId == 0) {
					IRole role = event.getGuild().getRoleByID(roleId);
					if(role == null) continue;
					if(helper != roleId){
						helper = roleId;
						finalString += "* " + role.getName() + System.lineSeparator();
					}
					finalString += ">    " + command + "/" + function + " - " + permission + System.lineSeparator();
					continue;
				}
				if(messageState == 3) {
					finalString += "[*][Users][*]" + System.lineSeparator();
					messageState = 4;
				}
				if(channelId == 0 && roleId == 0 && userId != 0) {
					IUser user = null;
					String username = null;
					if(whisperPermission) {
						user = SharedMethods.client.getUserByID(userId);
						if(user == null) continue;
						username = user.getName() + "#" + user.getDiscriminator() + " (" + userId + ")";
					}
					else{
						user = event.getGuild().getUserByID(userId);
						if(user == null) continue;
						username = user.getDisplayName(event.getGuild());
					}
					if(helper != userId){
						helper = userId;
						finalString += "* " + username + System.lineSeparator();
					}
					finalString += ">    " + command + "/" + function + " - " + permission + System.lineSeparator();
					continue;
				}
			}
			if(finalString.length() == 0)
				finalString = "All commands are allowed on this server";
			else
				finalString = "```md" + System.lineSeparator() + finalString + "```";
			return finalString.trim();
		} catch (SQLException e) {
			throw new BotException(e);
		}
	}

	private void changePermission(String command, String function, Long guildId, String entityType, String entity, String permission) throws BotException {
		if(permission.equalsIgnoreCase("clear")) {
			if(entityType.equalsIgnoreCase("server")) {
				String query = "DELETE FROM permissions WHERE command = ? AND function = ? AND guildId = ?";
				SQLAccess.executeInsert(query, command, function, guildId);
			}
			else if(entityType.equalsIgnoreCase("channel")) {
				String query = "DELETE FROM permissions WHERE command = ? AND function = ? AND guildId = ? AND channelId = ?";
				SQLAccess.executeInsert(query, command, function, guildId, entity);
			}
			else if(entityType.equalsIgnoreCase("role")) {
				String query = "DELETE FROM permissions WHERE command = ? AND function = ? AND guildId = ? AND roleId = ?";
				SQLAccess.executeInsert(query, command, function, guildId, entity);
			}
			else if(entityType.equalsIgnoreCase("user")) {
				String query = "DELETE FROM permissions WHERE command = ? AND function = ? AND guildId = ? AND userId = ?";
				SQLAccess.executeInsert(query, command, function, guildId, entity);
			}
			return;
		}
		String query = "INSERT OR REPLACE INTO permissions VALUES(?, ?, ?, ?, ?, ?, ?)";
		SQLAccess.executeInsert(query, command, function, guildId, 
				entityType.equalsIgnoreCase("channel") ? entity : 0, 
				entityType.equalsIgnoreCase("role") 	 ? entity : 0, 
				entityType.equalsIgnoreCase("user") 	 ? entity : 0, 
				permission);
	}

	/* 
	 * $permission emote execute server off
	 * $permission emote execute channel <@&437181417751969802> off
	 * $permission emote execute role <@&437181417751969802> on
	 * $permission emote execute user <@&437181417751969802> off
	 */
	@Override
	protected String help(final MessageReceivedEvent event, Constants.HELP_TYPE helpType) {
		if(helpType.equals(HELP_TYPE.SHOW))	return null;
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret = "Manage Permissions for commands";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Permission Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "permission list" + System.lineSeparator();
			ret += ">    Shows a list of all permissions on the server." + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "permission <command> <function> <Type> {Entity} [ON/OFF/CLEAR]" + System.lineSeparator();
			ret += ">    Update permission to the function of specified command." + System.lineSeparator();
			ret += ">    Types: server/channel/role/user" + System.lineSeparator();
			ret += ">    Entity: #ChannelName / @RoleName / @User - All these must be linked" + System.lineSeparator();
			ret += ">    Examples:" + System.lineSeparator();
			ret += ">        $permission pull execute server off" + System.lineSeparator();
			ret += ">        $permission emote add server off" + System.lineSeparator();
			ret += ">        $permission pull execute channel #botpulls on" + System.lineSeparator();
			ret += ">        $permission emote add role @EmoteMakers on" + System.lineSeparator();
			ret += ">        $permission emote add user @Macilento on" + System.lineSeparator();
			ret += ">    Please ask Quetz for help if you aren't sure how to do it!!!";
			ret += "```";
		}
		return ret;
	}
	
	@Override
	public String getFunction(MessageReceivedEvent event) {
		if(event.getMessage().getContent().contains(" new "))
			return Constants.FUNCTION_NAMES.ADD.name;
		return Constants.FUNCTION_NAMES.EXECUTE.name;
	}
}