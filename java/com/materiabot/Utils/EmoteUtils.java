package com.materiabot.Utils;
import java.util.Optional;

import com.materiabot.Main;

import net.dv8tion.jda.api.entities.Emote;

public abstract class EmoteUtils {
	public static abstract class Emotes{
		public static final String UNKNOWN_EMOTE = getEmoteText("unknownEmote");
		public static final String SORRY_MOOGLE_URL = "https://dissidiadb.com/static/img/0002.0260da5.png";
		public static final String SURPRISED_MOOGLE_URL = "https://dissidiadb.com/static/img/0014.6ec7022.png";

		//2s
		public static final String BLUE_POP_LINK = "https://media.discordapp.net/attachments/412042912894025730/487345309681188878/blue.gif";
		public static final String SILVER_POP_LINK = "https://media.discordapp.net/attachments/412042912894025730/487345317230936064/silverfast.gif";
		public static final String GOLD_POP_LINK = "https://media.discordapp.net/attachments/412042912894025730/487345312890093583/goldfast.gif";
		public static final String BURST_POP_LINK = "https://media.discordapp.net/attachments/570156853296758794/640990621187964948/burst.gif";
		
		//6s
		public static final String SILVER_PROGRESS_POP_LINK = "https://media.discordapp.net/attachments/412042912894025730/487345316522360842/silver.gif";
		public static final String GOLD_S_PROGRESS_POP_LINK = "https://media.discordapp.net/attachments/412042912894025730/487345312227262464/gold.gif";
		public static final String GOLD_PROGRESS_POP_LINK = "https://media.discordapp.net/attachments/412042912894025730/487345312227262464/gold.gif";

		public static final String ORB_3 = "<:blueorb:486857801356935188>";
		public static final String ORB_4 = "<:silverorb:486857801562324992>";
		public static final String ORB_5 = "<:goldorb:486857802292396032>";
		public static final String ORB_B = "<:burstorb:640996359641759754>";
		public static final String ORB_S_3 = "<:bluepop:486857801537421315>";
		public static final String ORB_S_4 = "<:silverpop:486857809015865344>";
		public static final String ORB_S_5 = "<:goldpop:486857801281437697>";
		public static final String ORB_S_B = "<:burstpop:640996359012614167>";
		public static final String ORB_ANIM_3 = "<:blueanim:487346265546555409>";
		public static final String ORB_ANIM_4 = "<:silveranim:487346647790256138>";
		public static final String ORB_ANIM_5 = "<:goldanim:487347689340796931>";
		public static final String ORB_ANIM_B = "<:burstanim:640916306266292224>";
		
		public static final String RARITY_5 = "<:rarity3:486857801428369408>";
		public static final String RARITY_10 = "<:rarity4:486857801495347220>";
		public static final String RARITY_20 = "<:rarity4:486857801495347220>";
		
		public static final String RARITY_15 = "<:15cp:486857801449209866>";
		public static final String RARITY_35 = "<:35cp:486857801361129472>";
		public static final String RARITY_70 = "<:70cp:516738393556647936>";
		public static final String RARITY_90 = "<:90cp:640335935086526490>";
		public static final String RARITY_140 = "<:140cp:640335935057428510>";

		public static final String getOrb(int cp, Boolean shatter) {
			if(shatter == null) return UNKNOWN_EMOTE;
			switch(cp) {
				case 5: return shatter ? ORB_ANIM_3 : ORB_3;
				case 10: 
				case 20: return shatter ? ORB_ANIM_4 : ORB_4;
				case 140: return shatter ? ORB_ANIM_B : ORB_B;
				default: return shatter ? ORB_ANIM_5 : ORB_5;
			}
		}
		public static final String getRarityByCp(int cp) {
			switch(cp) {
				case 5: return RARITY_5;
				case 10: return RARITY_10;
				case 15: return RARITY_15;
				case 20: return RARITY_20;
				case 35: return RARITY_35;
				case 70: return RARITY_70;
				case 90: return RARITY_90;
				case 140: return RARITY_140;
			}
			return UNKNOWN_EMOTE;
		}
	}
	
	public static Emote getEmoteClassByName(String name) {
		String name2 = name.replaceAll(" ", "").replaceAll("'", "").replaceAll("&", "");
		return Main.getClient().getGuilds().stream()
				.filter(s -> s.getOwner().getIdLong() == Constants.QUETZ.getIdLong())
				.flatMap(g -> g.getEmotes().stream())
				.filter(e -> e.getName().equalsIgnoreCase(name2))
				.findFirst().orElse(null);
	}
	public static final String getEmoteText(String name) {		
		Optional<Emote> o = Optional.ofNullable(getEmoteClassByName(name));
		return o.isPresent() ? "<:" + o.get().getName() + ":" + o.get().getId() + ">" : null;
	}
}