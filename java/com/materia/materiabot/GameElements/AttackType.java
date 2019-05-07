package com.materia.materiabot.GameElements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AttackType{
	Melee_BRVHP("465819474663178251", "melee", "brv", "hp"), 
	Ranged_BRVHP("465819473883168769", "ranged", "brv", "hp"), 
	Magical_BRVHP("465819474646269952", "magic", "brv", "hp"), 
	Melee_HP("465819474663178251", "melee", "brv", "hp"), 
	Ranged_HP("465819473883168769", "ranged", "brv", "hp"), 
	Magical_HP("465819474646269952", "magic", "brv", "hp"), 
	Melee_BRV("465819390840012810", "melee", "brv"), 
	Ranged_BRV("465819390668046356", "ranged", "brv"), 
	Magical_BRV("465819390215192576", "magic", "brv"), 
	Heal("465819474541674536", "restore"),
	Buff("465819399597588480", "grants", "raises"), 
	Debuff("465819474117787648", "debuff"), 
	Additional("531636803883761675", "[A]"), 
	Other("465837588377370625");
	
	private String emote;
	private List<String> catchWords;
	
	private AttackType(String emote, String... catchWords) {
		this.emote = emote;
		this.catchWords = Arrays.asList(catchWords);
	}
	public String getEmote() { return "<:" + name() + "AttackType:" + emote + ">"; }
	public String getURL() { return "https://cdn.discordapp.com/emojis/" + emote + ".png"; }
	public List<String> getCatchWords(){ return catchWords; }
	
	public static AttackType find(String text) {
		for(AttackType type : values()) {
			ArrayList<String> catchWords = new ArrayList<String>(type.getCatchWords());
			for(String word : text.replace("\n", " ").split(" ")) {
				if(type.getCatchWords().contains(word.toLowerCase()))
					catchWords.remove(word.toLowerCase());
				if(catchWords.size() == 0)
					return type;
			}
		}
		return Other;
	}
}