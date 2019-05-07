package com.materia.materiabot.GameElements;
import com.materia.materiabot.Utils.EmoteUtils;
import net.dv8tion.jda.core.entities.Emote;

public class Unit {

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEmoteText() {
		return EmoteUtils.getEmoteClassByName(getName()).getAsMention();
	}
	public Emote getEmote() {
		return EmoteUtils.getEmoteClassByName(getName());
	}

	public Crystal getCrystal() {
		// TODO Auto-generated method stub
		return null;
	}
}