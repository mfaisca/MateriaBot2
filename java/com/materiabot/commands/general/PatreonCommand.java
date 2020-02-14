package com.materiabot.commands.general;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.EmoteUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.Utils.EmoteUtils.Emotes;
import com.materiabot.commands._BaseCommand;
import com.patreon.PatreonAPI;
import com.patreon.resources.Campaign;
import com.patreon.resources.Pledge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class PatreonCommand extends _BaseCommand{
	private static final String PATREON_KEY = "r-lYyoIHMoT9m4Xc_9wxSDwombbzxSGaT9zy3-2IMT0"; //ConfigsDB.getKeyValue(ConfigsDB.PATREON_TOKEN_KEY);
	private static final String PATREON_LINK = "https://www.patreon.com/MateriaBot";
	private static final int NUMBER_OF_VISIBLE_PATRONS = 20;
	
	public PatreonCommand() { super("patreon","donate"); }
	
	public static boolean isUserPatreon(User u) {
		return Constants.MATERIABOT_SERVER.getMembersWithRoles(Constants.MATERIABOT_SERVER.getRoleById(554660182093987869L)).stream().anyMatch(m -> m.getUser().equals(u));
	}
	
	private static EmbedBuilder build(String bannerURL, List<String> patrons, String oldest, String mostRecent, int totalPatrons) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("MateriaBot Patreon");
		builder.setThumbnail(Constants.getClient().getSelfUser().getAvatarUrl());
		builder.setColor(new Color(249,104,84));
		builder.setImage(bannerURL);
		builder.addField("Link", PATREON_LINK, false);
		builder.addField("Special Mentions", 
				"Oldest Patron: " + oldest + System.lineSeparator() + 
				"Newest Patron: " + mostRecent + System.lineSeparator(), true);
		builder.addField("Patron Count", totalPatrons + " Patreons", true);
		int i = 0;
		while((i * NUMBER_OF_VISIBLE_PATRONS) < patrons.size()) {
			builder.addField(i == 0 ? "Patrons" : MessageUtils.S, 
							patrons.subList(
								i * NUMBER_OF_VISIBLE_PATRONS, 
								(i * NUMBER_OF_VISIBLE_PATRONS + NUMBER_OF_VISIBLE_PATRONS) >= 
									patrons.size() ? patrons.size() : 
									i * NUMBER_OF_VISIBLE_PATRONS + NUMBER_OF_VISIBLE_PATRONS)
							.stream().reduce((o1,  o2) -> o1 + System.lineSeparator() + o2).orElse("---"), false);
			i++;
		}
		return builder;
	}

	@Override
	public void doStuff(Message message) {
		try {
			final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			PatreonAPI apiClient = new PatreonAPI(PATREON_KEY);
			Campaign campaign = apiClient.fetchCampaigns().get().get(0);
			List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
			int totalPatrons = pledges.size();
			String mostRecent = pledges.stream().sorted((p1, p2) -> {
				try { return f.parse(p2.getCreatedAt()).compareTo(f.parse(p1.getCreatedAt()));
				} catch (ParseException e) { ; } return 0;
				}).findFirst().map(p -> (p.getReward() == null ? Emotes.UNKNOWN_EMOTE : EmoteUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).orElse(null);
			String oldest = pledges.stream().sorted((p1, p2) -> {
				try { return f.parse(p1.getCreatedAt()).compareTo(f.parse(p2.getCreatedAt()));
				} catch (ParseException e) { ; } return 0;
				}).findFirst().map(p -> (p.getReward() == null ? Emotes.UNKNOWN_EMOTE : EmoteUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).orElse(null);
			List<String> patrons = pledges.stream().sorted((p1, p2) -> 
				p2.getAmountCents() - p1.getAmountCents()
				).map(p -> (p.getReward() == null ? Emotes.UNKNOWN_EMOTE : EmoteUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName() + " - " + (p.getReward() == null ? "No Tier" : p.getReward().getTitle())).collect(Collectors.toList());
			MessageUtils.sendEmbed(message.getChannel(), build(campaign.getImageUrl(), patrons, oldest, mostRecent, totalPatrons));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}