package com.materiabot.IO.JSON.Unit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import com.materiabot.GameElements.Artifact;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements._Library;
import com.materiabot.IO.JSON.JSONParser;

public class ArtifactParser {
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
}