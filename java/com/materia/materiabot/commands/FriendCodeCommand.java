package com.materia.materiabot.commands;
import java.util.Arrays;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.materia.materiabot.Listener;
import com.materia.materiabot.GameElements._Library;
import com.materia.materiabot.IO.SQL.SQLAccess;
import com.materia.materiabot.GameElements.Unit;
import com.materia.materiabot.Utils.BotException;
import com.materia.materiabot.Utils.Constants;
import com.materia.materiabot.Utils.Constants.HELP_TYPE;
import com.materia.materiabot.Utils.EmoteUtils;
import com.materia.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class FriendCodeCommand extends _BaseCommand{
	private static class Friend{
		public String friendCode;
		public String unit;
	}
	
	public FriendCodeCommand() {
		super("fc");
	}
	
	@Override
	protected Function getFunction(Message message) {
		if(message.getContentRaw().contains(" ")) {
			String func = message.getContentRaw().split(" ")[1];
			if(func.equalsIgnoreCase("search") || func.equalsIgnoreCase("find"))
				return _BaseCommand.Function.SEARCH;
		}
		return _BaseCommand.Function.EXECUTE;
	}

	@Override
	public void doStuff(Message message) { //TODO Rework the parsing - Read character name one word at a time and build info from there
		try {
			User user = null;
			String region = "GL";
			//Make defaults here
			String msg = message.getContentRaw();
			String[] msgs = msg.split(" ");
			if(msgs[msgs.length-1].equalsIgnoreCase("light")) {
				msgs[msgs.length-3] = "Warrior of Light";
				msgs = Arrays.copyOf(msgs, msgs.length-2);
			}else if(msgs[msgs.length-1].equalsIgnoreCase("sith")) {
				msgs[msgs.length-2] = "Cait Sith";
				msgs = Arrays.copyOf(msgs, msgs.length-1);
			}else if(msgs[msgs.length-1].equalsIgnoreCase("knight")) {
				msgs[msgs.length-2] = "Onion Knight";
				msgs = Arrays.copyOf(msgs, msgs.length-1);
			}
			if(msgs.length > 1){
				if(msgs[1].equalsIgnoreCase("search") || msgs[1].equalsIgnoreCase("find")){
					//$fc search [gl/jp] <UnitName>
					int idx = 2;
					if(msgs[idx].equalsIgnoreCase("GL"))
						region = "GL";
					else if(msgs[idx].equalsIgnoreCase("JP"))
						region = "JP";
					else
						idx--;
					idx++;
					String unitName = "";
					for(int i = idx; i < msgs.length; i++)
						unitName += " " + msgs[i];
					unitName.trim();
					if(unitName.isEmpty()) {
						MessageUtils.sendStatusMessageWarn(message.getChannel(), MessageUtils.DefaultMessages.CHAR_NOT_FOUND.getMessage());
						return;
					}
					Unit c = _Library.get(region).getUnit(unitName);
					if(c == null) {
						MessageUtils.sendStatusMessageWarn(message.getChannel(), MessageUtils.DefaultMessages.CHAR_NOT_FOUND.getMessage());
						return;
					}
					EmbedBuilder eb = showFriends(message.getGuild(), message.getChannel(), region, c);
					if(eb != null)
						MessageUtils.sendEmbed(message.getChannel(), eb);
					else
						MessageUtils.sendStatusMessageError(message.getChannel(), MessageUtils.DefaultMessages.UNKNOWN_ERROR.getMessage());
					return;
				}
				if(msgs[1].equalsIgnoreCase("clear")){
					if(msgs.length == 3)
						region = msgs[2];
					String userId = "" + message.getAuthor().getId(); 
					deleteCode(userId, message.getChannel(), region.equalsIgnoreCase("GL") ? 0 : 1); //0 = GL | 1 = JP
					return;
				}
				else if(msgs[1].equalsIgnoreCase("set")){
					//fc set gl xxxxxxxxx noctis
					// 0  1   2     3       4
					String code = null, unit = null;
					Friend f = null;
					if(msgs.length == 5) {
						region = msgs[2];
						code = msgs[3];
						unit = msgs[4];
					}
					else if(msgs.length == 4) {
						int i = 2;
						if(msgs[i].equalsIgnoreCase("GL") || msgs[i].equalsIgnoreCase("JP"))
							region = msgs[i++];
						if(Constants.isNumber(msgs[i]))
							code = msgs[i++];
						else {
							f = getFriend(message.getAuthor().getId(), region.equalsIgnoreCase("GL") ? 0 : 1);
							if(f == null) {
								MessageUtils.sendStatusMessageWarn(message.getChannel(), "You can only set your shared unit when/after you set your friend code.");
								return;
							}
							code = f.friendCode;
						}
						if(i < msgs.length)
							unit = msgs[i];
					}
					else if(msgs.length == 3) {
						int i = 2;
						region = "GL"; 
						//Make defaults here
						if(Constants.isNumber(msgs[i]))
							code = msgs[i++];
						else {
							f = getFriend(message.getAuthor().getId(), region.equalsIgnoreCase("GL") ? 0 : 1);
							if(f == null) {
								MessageUtils.sendStatusMessageWarn(message.getChannel(), "You need to set your FC before you update your unit!");
								return;
							}
							code = f.friendCode;
							unit = msgs[i++];
						}
					}
					if(!region.equalsIgnoreCase("GL") && !region.equalsIgnoreCase("JP")) {
						MessageUtils.sendStatusMessageWarn(message.getChannel(), "The version you indicated is wrong, use 'GL' or 'JP'.");
						return;
					}
					if(!Constants.isNumber(code) || code.replace(" ", "").length() != 9) {
						MessageUtils.sendStatusMessageWarn(message.getChannel(), "Your friend code is a number, I can't store something that isn't numeric!");
						return;
					}
					if(unit != null) {
						Unit c = _Library.get(region).getUnit(unit);
						if(c == null){
							MessageUtils.sendStatusMessageWarn(message.getChannel(), "The character you mentioned doesn't exist.");
							return;
						}
						if(f == null) {
							f = new Friend();
							f.friendCode = code;
						}
						f.unit = unit = c.getName();
					}
					if(f == null) {
						f = new Friend();
						f.friendCode = code;
						f.unit = unit;
					}
					setCode(message.getAuthor().getId(), message.getChannel(), f, region.equalsIgnoreCase("GL") ? 0 : 1);
					return;
				}else if(msgs[1].equalsIgnoreCase("GL") || msgs[1].equalsIgnoreCase("JP")) {
					region = msgs[1];
					List<User> users = message.getMentionedUsers();
					if(users.size() < 1) {
						user = message.getAuthor();
					}else if(users.size() > 1){
						MessageUtils.sendStatusMessageInfo(message.getChannel(), "Please only mention one person at a time.");
						return;
					}else
						user = users.get(0);
				}else{
					region = "GL";
					List<User> users = message.getMentionedUsers();
					if(users.size() < 1) {
						user = message.getAuthor();
					}else if(users.size() > 1){
						MessageUtils.sendStatusMessageInfo(message.getChannel(), "Please only mention one person at a time.");
						return;
					}else
						user = users.get(0);
				}
			}
			else 
				user = message.getAuthor();
			showCode(user.getId(), message.getChannel(), region.equalsIgnoreCase("GL") ? 0 : 1, user.getName() + " " + region.toUpperCase() + " Friend Code: ");
		} catch (BotException ex) {
			Logger.getLogger(FriendCodeCommand.class.getName()).log(Level.SEVERE, null, ex);
			MessageUtils.sendStatusMessageError(message.getChannel(), "Error accessing Database");
		}
	}

	@Override
	protected String help(final Message event, Constants.HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Commands related to friend codes";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Friend Code Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += "All commands will use GL as default if no version is used!";
			ret += System.lineSeparator() + System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "fc [gl/jp]" + System.lineSeparator();
			ret += ">    Show own friend code on the specified version." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "fc [gl/jp] <MentionUser>" + System.lineSeparator();
			ret += ">    Show the friend code of the specified user on the specified version." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "fc set [gl/jp] [FriendCode] [CharName]" + System.lineSeparator();
			ret += ">    Set the given Friend Code as your own on the specified version. Setting a character is optional." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "fc search [gl/jp] <CharName>" + System.lineSeparator();
			ret += ">    Search for friends that are sharing a specific unit." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "fc clear [gl/jp]" + System.lineSeparator();
			ret += ">    Delete your friend code on the specified version." + System.lineSeparator();
			ret += ">" + System.lineSeparator();
			ret += ">    **Examples:**" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "fc" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "fc " + event.getAuthor().getAsMention() + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "fc set 123456789 Quistis" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "fc set Sephiroth" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "fc search Quistis" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
	
	private Friend getFriend(String userID, int gl_jp) throws BotException {
		try(ResultSet result = SQLAccess.executeSelect("SELECT friendCode, unitShared FROM users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " WHERE userId = ?", userID)) {
			if (result.next()) {
				Friend f = new Friend();
				f.friendCode = result.getString(1);
				f.unit = result.getString(2);
				return f;
			}
			else
				return null;
		} catch (SQLException e) {
			throw new BotException(e);
		}
	}

	private void showCode(String userID, MessageChannel channel, int gl_jp, String show) throws BotException{
		Friend f = getFriend(userID, gl_jp);
		String message = null;
		if (f != null)
			message = show + f.friendCode + 
				(f.unit != null ? System.lineSeparator() + "This player is sharing a " + _Library.JP.getUnit(f.unit).getEmote() + f.unit + "." : "");
		else
			message = "No friend code registered.";
		MessageUtils.sendStatusMessageInfo(channel, message);
	}
	private void setCode(String userID, MessageChannel channel, Friend f, int gl_jp) throws BotException{
		try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " WHERE userId = ?", userID)) {
			if (result.next()){
				if(f.unit != null)
					SQLAccess.executeInsert("UPDATE users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " SET friendcode = ?, unitShared = ? WHERE userid = ?", f.friendCode, f.unit, userID);
				else
					SQLAccess.executeInsert("UPDATE users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " SET friendcode = ? WHERE userid = ?", f.friendCode, userID);
				MessageUtils.sendStatusMessageInfo(channel, "Friend code successfully updated!");
			}else{
				if(f.unit != null)
					SQLAccess.executeInsert("INSERT into users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " VALUES (?, ?, ?)", userID, f.friendCode, f.unit);
				else
					SQLAccess.executeInsert("INSERT into users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + "(userId, friendCode) VALUES (?, ?)", userID, f.friendCode);
				MessageUtils.sendStatusMessageInfo(channel, "Friend code successfully registered!");
			}
		} catch (SQLException e) {
			throw new BotException(e);
		}
	}
	private void deleteCode(String userID, MessageChannel channel, int gl_jp) throws BotException{
		try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " WHERE userId = ?", userID)) {
			if (result.next()){
				SQLAccess.executeInsert("DELETE FROM users_friendcodes_oo" + (gl_jp == 0 ? "_gl" : "_jp") + " WHERE userid = ?", userID);
				MessageUtils.sendStatusMessageInfo(channel, "Friend code removed.");
			}else{
				MessageUtils.sendStatusMessageInfo(channel, "You don't have any friend code registered.");
			}
		} catch (SQLException e) {
			throw new BotException(e);
		}
	}
	private EmbedBuilder showFriends(Guild server, MessageChannel messageChannel, String region, Unit c) throws BotException {
		EmbedBuilder builder = new EmbedBuilder();
		Emote emoji = EmoteUtils.getEmoteClassByName(c.getName());
		builder.setThumbnail(emoji == null ? EmoteUtils.Emotes.SURPRISED_MOOGLE_URL : emoji.getImageUrl());
		builder.setTitle(c.getEmoteText() + " " + c.getName());
		builder.setFooter("This list depends on the users to be kept updated, please update your unit on the bot whenever you change it!", null);
		if(c.getCrystal() != null)
			builder.setColor(c.getCrystal().getColor());
		String onServer = "", offServer = "";
		int id = 0;
		try(ResultSet result = SQLAccess.executeSelect("SELECT userId, friendCode FROM users_friendcodes_oo" + (region.equalsIgnoreCase("GL") ? "_gl" : "_jp") + " WHERE unitShared = ?", c.getName())) {
			while (result.next()) {
				long userId = Long.parseLong(result.getString(1));
				String fc = result.getString(2);
				User user = server.getMemberById(userId).getUser();
				if(user != null) {
					onServer += System.lineSeparator() + fc + " (" + user.getName() + "#" + user.getDiscriminator() + ")";
					if(onServer.length() > 1000) {
						builder.addField("On Server", onServer.substring(0, onServer.lastIndexOf(System.lineSeparator())), false);
						onServer = onServer.substring(onServer.lastIndexOf(System.lineSeparator()));
					}
				}else {
					offServer += (id++%3 == 0 ? System.lineSeparator() : " | ") + fc;
					if(offServer.length() > 1000) {
						builder.addField("On Server", offServer.substring(0, offServer.lastIndexOf(System.lineSeparator())), false);
						offServer = onServer.substring(offServer.lastIndexOf(System.lineSeparator()));
					}
				}
			}
			if(!onServer.isEmpty())
				builder.addField("On Server", onServer, false);
			else
				builder.addField("On Server", "---", false);
			if(!offServer.isEmpty())
				builder.addField("Off Server", offServer, false);
			else
				builder.addField("Off Server", "---", false);
			return builder;
		} catch (SQLException e) {
			throw new BotException(e);
		}
	}
}
