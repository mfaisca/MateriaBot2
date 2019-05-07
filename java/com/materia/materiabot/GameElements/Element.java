package com.materia.materiabot.GameElements;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public enum Element{
	Fire(221, 47, 41, "412043706515062823"), Ice(4, 223, 238, "412043706535903233"), Lightning(240, 233, 76, "412043706758332416"), Thunder(240, 233, 76, "412043706758332416"), Earth(155, 97, 34, "412043706468794372"), 
	Water(27, 78, 242, "412043706384908311"), Wind(131, 213, 116, "412043706741293056"), Holy(255, 255, 255, "412043706724778003"), Dark(102, 49, 143, "412043706741424138");
	private Color c;
	private String emote;
	
	private Element(int r, int g, int b, String e) { c = new Color(r, g, b); emote = e;}
	public Color getColor() { return c; }
	public String getEmote() { return "<:" + name() + "Element:" + emote + ">"; }
	public String getURL() { return "https://cdn.discordapp.com/emojis/" + emote + ".png"; }
	
	public static List<Element> find(String text) {
		List<Element> list = new LinkedList<Element>();
		for(String word : text.replace("\n", " ").replace("/", " ").split(" "))
			for(Element element : values())
				if(element.name().equalsIgnoreCase(word) && !list.contains(element))
					list.add(element);
		return list;
	}
}