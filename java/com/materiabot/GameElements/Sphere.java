package com.materiabot.GameElements;

import com.materiabot.Utils.EmoteUtils;

public class Sphere{
	public enum SphereType {
		A("Attack"), B("Defense"), C("Recovery"), D("Support"), E("Jamming");	
		
		private SphereType(String name) {
			this.name = name;
		}
		
		private String name;
		
		public String getName() { return name; }
		public String getEmoteLetter() { return EmoteUtils.getEmoteText("letter_" + this.name()); }
		public String getEmoteSphere() { return EmoteUtils.getEmoteText("sphere_" + this.name()); }
		public String getEmoteSphereLetter() { return EmoteUtils.getEmoteText("sphereLetter_" + this.name()); }
		public String getEmoteSlot(int slot) { return EmoteUtils.getEmoteText("slot_" + this.name() + slot); }
	}
	private SphereType type;
	private String name;
	private String description;
	private Unit character;
	
	public Sphere(SphereType type, String charName, String name, String description) {
		this(type, _Library.JP.getUnit(charName), name, description);
	}
	public Sphere(SphereType type, Unit character, String name, String description) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.character = character;
	}

	public SphereType getType() {
		return type;
	}
	public void setType(SphereType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Unit getCharacter() {
		return character;
	}
	public void setCharacter(Unit character) {
		this.character = character;
	}
}