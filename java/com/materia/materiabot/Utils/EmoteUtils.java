package com.materia.materiabot.Utils;
import java.util.Optional;

import com.materia.materiabot.Main;

import net.dv8tion.jda.core.entities.Emote;

public abstract class EmoteUtils {
	public static Emote getEmoteClassByName(String name) {
		String name2 = name.replaceAll(" ", "").replaceAll("'", "").replaceAll("&", "");
		return Main.getClient().getGuilds().stream()
				.filter(s -> s.getOwnerId() == (""+Constants.OWNER_ID))
				.flatMap(g -> g.getEmotes().stream())
				.filter(e -> e.getName().equalsIgnoreCase(name2))
				.findFirst().orElse(null);
	}
	public static final String getEmoteByName(String name) {		
		Optional<Emote> o = Optional.ofNullable(getEmoteClassByName(name));
		return o.isPresent() ? "<:" + o.get().getName() + ":" + o.get().getId() + ">" : MessageUtils.UNKNOWN_EMOTE;
	}
}