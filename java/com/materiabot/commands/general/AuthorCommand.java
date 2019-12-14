package com.materiabot.commands.general;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class AuthorCommand extends _BaseCommand{	
	public AuthorCommand() {
		super("author");
	}

	@Override
	public void doStuff(final Message event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(Constants.QUETZ.getEffectiveName(), Constants.QUETZ.getUser().getAvatarUrl());
		builder.setThumbnail(Constants.QUETZ.getUser().getAvatarUrl());
		builder.addField("Opera Omnia Friend ID", "983453344", true);
		builder.addField("Status", Constants.QUETZ.getOnlineStatus().toString(), true);
		builder.addField("About me", 
				"I'm a Day 1 player, I've actively played this game since it was released for GL and have completed almost all the content released so far. I'm 28 and I'm a computer engineer, I use my professional experience to bring the best experience I can to Discord users. I'm a carefree person and I love talking with people, so feel free to DM me whenever you want!", 
				false);
		builder.addField("Bot History", 
				"I've been working on Discord bots for a couple years already, having done a Final Fantasy Brave Exvius before for my own server, when most of us finally got fed up with the game, Opera Omnia released." + System.lineSeparator() + "I've started working on the bot shortly after the game released, having initially contacted Rem/Phantasmage on the best way to get information from DissidiaDB into a bot. Since then I have evolved the bot and added new functionalities overtime and in January 2019, I made the bot public, and here you are using it now <3.", 
				false);
		builder.addField("MateriaBot Server", 
				"If you wanna join MateriaBot server where I can easily interact with all of you directly about whatever, the link is: \nhttps://discord.gg/XCTC7jY", 
				false);
		MessageUtils.sendEmbed(event.getChannel(), builder);
	}
	
	@Override
	public String help(final Message event, HelpCommand.HELP_TYPE helpType) {
		String ret = "";
		if(HelpCommand.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Shows info about who created this bot.";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Author Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(event, HelpCommand.HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + _Listener.COMMAND_PREFIX + "author" + System.lineSeparator();
			ret += ">    Pretty obvious, don't you think?" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}