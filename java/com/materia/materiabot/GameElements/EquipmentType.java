package com.materia.materiabot.GameElements;
import com.materia.materiabot.Utils.Constants;
import com.materia.materiabot.Utils.EmoteUtils;
import net.dv8tion.jda.core.entities.Emote;

public enum EquipmentType{
	Dagger(), Sword(), Greatsword(), Staff(), Gun(), Fist(), ThrowingWeapon(),
	Spear(), Bow(), Whip(), Other(), 
	Armor(), Artifact();
	
	public String getEmote() { return getEmoteClass().getAsMention(); }
	public Emote getEmoteClass() { return EmoteUtils.getEmoteClassByName(name() + "Equip"); }
	public String getTrashEmote() { return EmoteUtils.getEmoteByName("Trash" + name()); }
	

	public static EquipmentType random3Star() {
		int rng = Constants.RNG.nextInt(8);
		if(rng == 7) rng = 11;
		return values()[rng];
	}
	public static EquipmentType random4Star() {
		int rng = Constants.RNG.nextInt(11);
		if(rng == 10) rng = 11;
		return values()[rng];
	}
	public static EquipmentType random5Star() {
		int rng = Constants.RNG.nextInt(11);
		return values()[rng];
	}
}