package com.materiabot.IO.JSON;
import com.materiabot.GameElements.Unit;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.IO.JSON.Unit.PassiveParser;

public abstract class UnitParser {	
	public static Unit parseCharacter(String name) {
		Unit c = new Unit();
		MyJSONObject obj = JSONParser.loadContent(JSONParser.JSON_PATH.CHARACTERS_PATH.replace("{1}", name), false);
		//c.getAbilities().addAll(AbilityParser.parseAbilities(obj));
		c.getPassives().addAll(PassiveParser.parsePassives(obj));
		return c;
	}
}