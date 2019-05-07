package com.materia.materiabot.GameElements;

public enum CommandAttributes{
	brvhp("BRV + HP"), brv("BRV"), hp("HP"),
	melee("Melee"), ranged("Ranged"), magic("Magic"), poison("Poison"), sap("Sap"), 
	buff("Buff"), recovery("Recovery"), support("Support"), self("Self"), hp_regen("HP Regen"), self_recovery("Self Recovery"), 
	debuff("Debuff"), overflow("Overflow"), chase("Chase"), delay("Delay"), paralysis("Paralysis"), esuna("Esuna"), dispel("Dispel"), 
	party_brv("Party BRV"), party_recovery("Party Recovery"), party_raise("Party Raise"), party_buff("Party Buff"), 
	free_ability("Free Ability"), abilities_p1("Abilities +1"), aoe100("100% AoE"), ignore_def("Ignore DEF"), machine("Machine"),
	provoke("Provoke"), shield("Shield");
	private String description;
	
	private CommandAttributes(String desc){ description = desc; }
	
	public String getDescription() { return description; }
	
	public static CommandAttributes getAttribute(String attr) { 
		for(CommandAttributes att : CommandAttributes.values()) 
			if(att.name().equalsIgnoreCase(attr) || att.getDescription().equalsIgnoreCase(attr))
				return att;
		return null;
	}
}