package com.materiabot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.materiabot.IO.JSON.JSONParser;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class JSON_Analyzer {
	public static void main(String[] args) throws IOException {
		File main = new File(JSONParser.JSON_PATH.CHARACTERS_PATH).getParentFile();
		HashMap<Integer, List<String>> abilityMap = new HashMap<Integer, List<String>>();
		for(File f : main.listFiles()){
			String charName = getNameFromFileName(f.getName());
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);
			MyJSONObject[] abilities = obj.getObjectArray("completeListOfAbilities");
			MyJSONObject[] passives = obj.getObjectArray("passives");
			boolean parseAbilities = true, parsePassives = false, single = false;
			if(parseAbilities)
				for(MyJSONObject ability : abilities) {
					try {
						boolean brea = false;
						for(char c : ability.getString("name").toCharArray())
							if(!(('A' <= c && c <= 'z') || Arrays.asList('\'', '+', '&', '-', ':', '(', ')', '5').contains(c) || Character.isWhitespace(c)))
								brea = true;
						if(brea)
							continue;
						int i = 0;
						if(!single)
							for(MyJSONObject oo : ability.getObjectArray("hit_data")) {
								Integer key = oo.getInt("effect")*100 + oo.getInt("effect_value_type");
								if(!abilityMap.containsKey(key))
									abilityMap.put(key, new LinkedList<String>());
								abilityMap.get(key).add(charName + "@" + ability.getString("name") + "/" + i++ + "(" + ability.getInt("id") + "/" + oo.getInt("id") + ")");
							}
						else {
							Integer key = ability.getObject("chase_data").getInt("chase_dmg");
							//Integer key = ability.getInt("movement_cost");
							if(!abilityMap.containsKey(key))
								abilityMap.put(key, new LinkedList<String>());
							abilityMap.get(key).add(charName + "@" + ability.getString("name") + "/" + i++ + "(" + ability.getInt("id") + ")");
						}
					} catch(Exception e) {
						//System.out.println();
					}
				}
			if(parsePassives)
				for(MyJSONObject passive : passives) {
					try {
						boolean brea = false;
						for(char c : passive.getString("name").toCharArray())
							if(!(('A' <= c && c <= 'z') || Arrays.asList('\'', '+', '&', '-', ':', '(', ')', '5').contains(c) || Character.isWhitespace(c)))
								brea = true;
						if(brea)
							continue;
						int i = 0;
						if(!single)
							for(MyJSONObject oo : passive.getObjectArray("effects")) {
								Integer key = oo.getInt("effect_id");
//								if(Passive.Required.get(key) != null)    //CHANGE_THIS
//									continue;
								if(!abilityMap.containsKey(key))
									abilityMap.put(key, new LinkedList<String>());
								abilityMap.get(key).add(charName + "@" + passive.getString("name") + "/" + i++ + "(" + passive.getInt("id") + ")");
							}
						else {
							Integer key = passive.getObject("type_data").getInt("condition_type");
							if(!abilityMap.containsKey(key))
								abilityMap.put(key, new LinkedList<String>());
							abilityMap.get(key).add(charName + "@" + passive.getString("name") + "/" + i++ + "(" + passive.getInt("id") + ")");
						}
					} catch(Exception e) {
						
					}
				}
		}
		//FileWriter fw = new FileWriter(new File("D:\\output.txt"));
		for(Entry<Integer, List<String>> s : abilityMap.entrySet().stream().sorted((s1, s2) -> s1.getKey().compareTo(s2.getKey())).collect(Collectors.toList())) {
			System.out.println(s.getKey() + ": {");
			//fw.write(s.getKey() + ": {" + "\n");
			int cur = 0;
			for(String ss : s.getValue()) {
				if(++cur == 100) break;
				System.out.println("\t" + ss);
				//fw.write("\t" + ss + "\n");
			}
			System.out.println("}");
			//fw.write("}\n");
		}
		//fw.close();
	}
	
//	public static final int getNumberFromFileName(String name){
//		name = name.substring(9);
//		int i = 0;
//		for(char c : name.toCharArray())
//			if('0' <= c && c <= '9')
//				i++;
//			else
//				break;
//		name = name.substring(0, i);
//		return Integer.parseInt(name);
//	}
	
	public static final String getNameFromFileName(String name){
		name = name.substring(9);
		int i = 0;
		for(char c : name.toCharArray())
			if('0' <= c && c <= '9')
				i++;
			else
				break;
		return name.substring(i, name.indexOf("."));
	}
}