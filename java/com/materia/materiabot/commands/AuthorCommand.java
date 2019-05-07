package com.materia.materiabot.commands;
import com.corhm.cultbot.Utils.Constants;
import com.corhm.cultbot.Utils.SharedMethods;
import com.corhm.cultbot.Utils.Constants.HELP_TYPE;
import com.corhm.cultbot.Utils.MessageUtils;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class AuthorCommand extends _BaseCommand{	
	public AuthorCommand() {
		super("author");
	}

	@Override
	protected void doStuff(final MessageReceivedEvent event) {
		IUser quetz = SharedMethods.client.fetchUser(Constants.QUETZ_ID);

		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName(quetz.getDisplayName(event.getGuild()));
		builder.withAuthorIcon(quetz.getAvatarURL());
		builder.withAuthorUrl("https://discord.gg/DXJ47Bv");
		builder.withThumbnail(quetz.getAvatarURL());
		builder.appendField("Opera Omnia Friend ID", "983453344", true);
		builder.appendField("Status", quetz.getPresence().getStatus().toString(), true);
		builder.appendField("About me", 
				"I'm a Day 1 player, I've logged in everyday since the game released and have fully completed all the content released so far(except that damned Lenna EX)" + System.lineSeparator() + "FF8 IS UNDERRATED. FITE ME!!", 
				false);
		builder.appendField("Bot History", 
				"I've started working on the bot shortly after the game released, having initially contacted Rem/Phantasmage on the best way to get information from DissidiaDB into a bot. Since then I have evolved the bot and added new functionalities overtime and have been testing it on a small community I admin with alot of success.", 
				false);
		builder.appendField("MateriaBot Server", 
				"If you wanna join MateriaBot server where I can easily interact with all of you directly about whatever, the link is: \nhttps://discord.gg/XCTC7jY", 
				false);
		MessageUtils.sendEmbed(event.getChannel(), builder);
	}
	
	@Override
	protected String help(final MessageReceivedEvent event, Constants.HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Shows info about who created this bot.";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Author Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + _BaseCommand.BOT_PREFIX + "author" + System.lineSeparator();
			ret += ">    Pretty obvious, don't you think?" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}