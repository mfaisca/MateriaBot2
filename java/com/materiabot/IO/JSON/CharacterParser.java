package com.materiabot.IO.JSON;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

import com.materiabot.GameElements.Artifact;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements._Library;
import com.materiabot.GameElements.OperaOmnia.Datamining.Ability;
import com.materiabot.GameElements.OperaOmnia.Datamining.Passive;
import com.materiabot.GameElements.OperaOmnia.Datamining.Passive.*;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.Utils.Constants.Dual;

public class CharacterParser {
	@SuppressWarnings("unchecked")
	public static void parseAllCharacterArtifacts() throws MalformedURLException, IOException {
		JSONObject json = JSONParser.loadContent(JSONParser.JSON_PATH.ARTIFACT_PATH, true).obtainJSON();
		HashMap<Integer, Artifact> passiveEffects = new HashMap<Integer, Artifact>();
		JSONObject passiveList = ((JSONObject)json.get("passivesList"));
		for(String passiveId : ((Set<String>)passiveList.keySet())){
			JSONObject eff = ((JSONObject)passiveList.get(passiveId));
			Artifact a = new Artifact();
			a.setNameGL(((JSONObject)eff.opt("name")).optString("en"));
			a.setNameJP(((JSONObject)eff.opt("name")).optString("jp"));
			a.setDescription(((JSONObject)eff.opt("desc")).optString("en"));
			a.setRank(eff.optInt("rank"));
			a.setId(eff.optInt("id"));
			a.setType(eff.optInt("type"));
			a.setEffect(eff.optJSONArray("effects").getInt(0));
			passiveEffects.put(a.getId(), a);
		}
		JSONObject charPassives = ((JSONObject)json.get("characterPassives"));
		for(String charname : ((Set<String>)charPassives.keySet())){
			JSONObject arts = ((JSONObject)charPassives.get(charname));
			Unit c = _Library.JP.getUnit(charname);
			for(int r = 1; r <= 5; r++) {
				JSONArray artsRarityList = arts.getJSONArray("" + r);
				for(int i = 0; i < artsRarityList.length(); i++)
					c.getArtifacts(r).add(passiveEffects.get(artsRarityList.getInt(i)));
			}
		}
	}

	private static final HashMap<String, Character> CHARACTER_CACHE = new HashMap<String, Character>();
	
	public static Unit parseCharacter(String name) {
		Unit c = new Unit();
		//MyJSONObject obj = JSONParser.loadContent(OperaOmniaConstants.JSON_PATH.CHARACTERS_PATH.replace("{1}", name), false);
		MyJSONObject obj = JSONParser.loadContent("D:\\Workspace\\_files\\gl\\raw_data_{1}.json".replace("{1}", name), false);
		parseAbilities(obj);
		c.getPassives().addAll(parsePassives(obj));
		return c;
	}
	private static Ability[] parseAbilities(MyJSONObject obj) {
		return null;
	}
	private static List<Passive> parsePassives(MyJSONObject obj) {
		List<Passive> passives = new LinkedList<Passive>();
		for(MyJSONObject s : obj.getObjectArray("passives")) {
			Passive p = new Passive();
			p.setId(s.getInt("id"));
			p.setName(s.getString("name"));
			p.setDesc(s.getString("desc"));
			p.setShortDesc(s.getString("short_desc"));
			p.setCp(s.getObject("meta_data").getInt("cp"));
			p.setLevel(s.getObject("meta_data").getInt("level"));
			p.setTarget(s.getObject("meta_data").getInt("target") == 3 ? Target.T3 : Target.T2);
			for(MyJSONObject e : s.getObjectArray("effects")) {
				Dual<ValueGrouping<Effect>, ValueGrouping<Required>> v = passiveExceptions(p, e);
				if(v != null){
					p.getEffects().add(v);
					continue;
				}
				Effect eff = Effect.get(e.getInt("effect_id"));
				Required req = Required.get(e.getInt("required_id"));
				Integer[] ev = e.getIntArray("effect_values");
				Integer[] rv = e.getIntArray("required_values");
				ValueGrouping<Effect> vge = eff == null ? 
												new ValueGrouping<Effect>(e.getInt("effect_id"), ev) : 
												new ValueGrouping<Effect>(eff, ev);
				ValueGrouping<Required> vgr = req == null ? 
												new ValueGrouping<Required>(e.getInt("required_id"), rv) : 
												new ValueGrouping<Required>(req, rv);
				v = new Dual<ValueGrouping<Effect>, ValueGrouping<Required>>(vge, vgr);
				p.getEffects().add(v);
			}
			passives.add(p);
		}
		return passives;
	}
	private static Dual<ValueGrouping<Effect>, ValueGrouping<Required>> passiveExceptions(Passive p, MyJSONObject e) {
		Effect eff = null;
		Required req = null;
		if(p.getId() == 0 && e.getInt("effect_id") == -0)
			eff = Effect.E102; //Filler for Example
		if(p.getId() == 1742 && e.getInt("required_id") == 52)
			req = Required.R52_2;
		else if(p.getId() == 296 && e.getInt("required_id") == 59)
			req = Required.R59_2;
		else if(p.getId() == 1000 && e.getInt("required_id") == 30)
			req = Required.R1;
		///////////////////////////////////////////////////////
		if(eff == null && req == null)
			return null;
		if(eff == null)
			eff = Effect.get(e.getInt("effect_id"));
		else if(req == null)
			req = Required.get(e.getInt("required_id"));
		return new Dual<ValueGrouping<Effect>, ValueGrouping<Required>>(
					new ValueGrouping<Effect>(eff, e.getIntArray("effect_values")), 
					new ValueGrouping<Required>(req, e.getIntArray("required_values")));
	}
	
	public static void clearCache() {
		CHARACTER_CACHE.clear();
	}
}