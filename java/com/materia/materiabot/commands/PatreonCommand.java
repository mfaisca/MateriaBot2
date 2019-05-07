package com.materia.materiabot.commands;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import com.corhm.cultbot.Utils.Constants;
import com.patreon.PatreonAPI;
import com.patreon.resources.Campaign;
import com.patreon.resources.Pledge;
import com.corhm.cultbot.Utils.Constants.HELP_TYPE;
import com.corhm.cultbot.Utils.MessageUtils;
import com.corhm.cultbot.Utils.SharedMethods;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class PatreonCommand extends _BaseCommand{
	private static final String PATREON_KEY = "r-lYyoIHMoT9m4Xc_9wxSDwombbzxSGaT9zy3-2IMT0";
	private static final String PATREON_LINK = "https://www.patreon.com/MateriaBot";
	private static final int NUMBER_OF_VISIBLE_PATRONS = 10;
	
	public PatreonCommand() {
		super("patreon","donate");
	}
	
	private static EmbedBuilder build(String bannerURL, String topPatrons1, String topPatrons2, String mostDonated, String oldest, String mostRecent, int totalPatrons) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("MateriaBot Patreon");
		builder.withThumbnail("https://pbs.twimg.com/profile_images/1101648595046940674/V0hZzqiJ_400x400.png");
		builder.withColor(249, 104, 84);
		builder.withImage(bannerURL);
		builder.appendField("Link", PATREON_LINK, false);
		builder.appendField("Special Mentions", 
				//"Most Pledged: " + mostDonated + System.lineSeparator() + 
				"Oldest Patron: " + oldest + System.lineSeparator() + 
				"Newest Patron: " + mostRecent + System.lineSeparator(), true);
		builder.appendField("Patron Count", totalPatrons + " Patreons", true);
		builder.appendField("Top Patrons", topPatrons1, false);
		builder.appendField(MessageUtils.S, topPatrons2, false);
		return builder;
	}

	@Override
	protected void doStuff(final MessageReceivedEvent event) {
		try {
			final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			PatreonAPI apiClient = new PatreonAPI(PATREON_KEY);
			Campaign campaign = apiClient.fetchCampaigns().get().get(0);
			List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
			
			int totalPatrons = pledges.size();
			String mostRecent = pledges.stream().sorted((p1, p2) -> {
				try { return f.parse(p2.getCreatedAt()).compareTo(f.parse(p1.getCreatedAt()));
				} catch (ParseException e) { ; } return 0;
				}).findFirst().map(p -> SharedMethods.getEmoteByName(p.getReward().getTitle()) + " " + p.getPatron().getFullName()).orElse(null);
			String oldest = pledges.stream().sorted((p1, p2) -> {
				try { return f.parse(p1.getCreatedAt()).compareTo(f.parse(p2.getCreatedAt()));
				} catch (ParseException e) { ; } return 0;
				}).findFirst().map(p -> SharedMethods.getEmoteByName(p.getReward().getTitle()) + " " + p.getPatron().getFullName()).orElse(null);
//			String mostPledged = pledges.stream().sorted((p1, p2) -> 
//				p1.getTotalHistoricalAmountCents() - p2.getTotalHistoricalAmountCents()
//				).findFirst().map(p -> SharedMethods.getEmoteByName(p.getReward().getTitle()) + " " + p.getPatron().getFullName()).orElse(null);
			String topPatrons1 = pledges.stream().sorted((p1, p2) -> 
				p2.getAmountCents() - p1.getAmountCents()
				).limit(NUMBER_OF_VISIBLE_PATRONS/2).map(p -> SharedMethods.getEmoteByName(p.getReward().getTitle()) + " " + p.getPatron().getFullName() + " - " + p.getReward().getTitle()).reduce((p1, p2) -> p1 + System.lineSeparator() + p2).orElse(null);
			String topPatrons2 = pledges.stream().sorted((p1, p2) -> 
				p2.getAmountCents() - p1.getAmountCents()
				).skip(NUMBER_OF_VISIBLE_PATRONS/2).limit(NUMBER_OF_VISIBLE_PATRONS/2).map(p -> p.getPatron().getFullName()).reduce((p1, p2) -> p1 + System.lineSeparator() + p2).orElse(MessageUtils.S);
			
			MessageUtils.sendEmbed(event.getChannel(), build(campaign.getImageUrl(), topPatrons1, topPatrons2, /*mostPledged*/null, oldest, mostRecent, totalPatrons));
		} catch (IOException e) {
			e.printStackTrace();
		}
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