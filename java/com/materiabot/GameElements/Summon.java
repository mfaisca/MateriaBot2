package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Summon{	
	public static class SummonPassive{
		private String name, description, shortDesc;
		private boolean recommended = false;

		public SummonPassive(String n, String d){
			this(n, d, d, false);
		}
		public SummonPassive(String n, String d, boolean r){
			this(n, d, d, r);
		}
		public SummonPassive(String n, String d, String sd){
			this(n, d, sd, false);
		}
		public SummonPassive(String n, String d, String sd, boolean r) {
			name = n; description = d; shortDesc = sd; recommended = r;
		}
		
		public String getName() { return name; }
		public String getDescription() { return description; }
		public String getShortDesc() { return shortDesc; }
		public boolean isRecommended() { return recommended; }
		public void setRecommended(boolean recommended) { this.recommended = recommended; }
	}
	
	private Element element;
	private String attackName, blessing, ability, chargeType, genericMaterial, nodeCount, boardImage;
	private List<String> nicknames = new LinkedList<String>();
	private int turns, maxBrvBonus, maxLevel;
	private SummonPassive[] boardPassives;
	private Unit[] chars = new Unit[6];
	
	private Summon(List<String> names, int level, Element element, String attackName, String blessing, String ability, String materials, int turns, int maxbrvBonus, String chargeSpeed) {
		this(names, level, element, attackName, blessing, null, ability, null, null, materials, turns, maxbrvBonus, chargeSpeed);
	}
	private Summon(List<String> names, int level, Element element, String attackName, String blessing, String boardImage, String ability, 
					String nodeCount, String specialBoostedChars, String materials, int turns, int maxbrvBonus, String chargeSpeed, 
					SummonPassive... boardPassives) {
		nicknames = names.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
		this.element = element;
		this.attackName = attackName;
		this.blessing = blessing;
		this.ability = ability;
		this.nodeCount = nodeCount;
		this.boardImage = boardImage;
		this.boardPassives = boardPassives;
		this.genericMaterial = materials;
		this.turns = turns;
		this.maxBrvBonus = maxbrvBonus;
		this.chargeType = chargeSpeed;
		int i = 0;
		if(specialBoostedChars != null)
			for(String ch : StringUtils.split(specialBoostedChars, "|"))
				chars[i++] = null; //TODO OperaOmniaConstants.getCharacter(ch);
	}
	
	public String getName() {
		return nicknames.get(0);
	}
	public Element getElement() {
		return element;
	}
	public String getAttackName() {
		return attackName;
	}
	public String getBlessing() {
		return blessing;
	}
	public String getAbility() {
		return ability;
	}
	public String getChargeType() {
		return chargeType;
	}
	public String getGenericMaterial() {
		return genericMaterial;
	}
	public String getNodeCount() {
		return nodeCount;
	}
	public String getBoardImage() {
		return boardImage;
	}
	public List<String> getNicknames() {
		return nicknames;
	}
	public int getTurns() {
		return turns;
	}
	public int getMaxBrvBonus() {
		return maxBrvBonus;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public SummonPassive[] getBoardPassives() {
		return boardPassives;
	}
	public Unit[] getSpecialBoosteds() {
		return chars;
	}
}