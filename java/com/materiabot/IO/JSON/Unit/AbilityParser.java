package com.materiabot.IO.JSON.Unit;
import java.util.List;
import java.util.LinkedList;
import com.materiabot.GameElements.Datamining.Ability;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class AbilityParser {
	public static class AilmentParser{
		
	}
	
	public static List<Ability> parseAbilities(MyJSONObject obj) {
		List<Ability> ret = new LinkedList<Ability>();
		for(MyJSONObject a : obj.getObjectArray("completeListOfAbilities")) {
			Ability ab = parseAbility(a);
			if(isValidAbility(ab))
				ret.add(ab);
		}
		return ret;
	}
	private static boolean isValidAbility(Ability a) {
		return true;
	}
	private static Ability parseAbility(MyJSONObject obj) {
		
		return null;
	}
}