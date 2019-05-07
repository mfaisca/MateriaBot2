package com.materia.materiabot.GameElements;

public class Summon {
	private Element element;
	private String name, dbNonCrystalImageCode, dbCrystalImageCode, attackName, blessing, ability, chargeType, genericMaterial;
	private int turns, maxBrvBonus, maxLevel;
	private AttackType attackType;
	
	private Summon(String name) {
		this.name = name;
		switch(getName()) {
			case "Chocobo": case "Ifrit": case "Leviathan": case "Pandemonium":
				genericMaterial = "genericClaws";
				break;
			case "Sylph": case "Ramuh": case "Alexander": case "Diabolos":
				genericMaterial = "genericLiquid";
				break;
			case "Shiva": case "Odin": case "Brothers": case "Bahamut": case "Spirit Moogle":
				genericMaterial = "genericCrystal";
				break;
		}
	}
	
	public String getName() {
		return name;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public String getDbCrystalImageCode() {
		return dbCrystalImageCode;
	}
	public void setDbCrystalImageCode(String dbImageCode) {
		this.dbCrystalImageCode = dbImageCode;
	}
	public String getDbNonCrystalImageCode() {
		return dbNonCrystalImageCode;
	}
	public void setDbNonCrystalImageCode(String dbImageCode) {
		this.dbNonCrystalImageCode = dbImageCode;
	}
	public String getAttackName() {
		return attackName;
	}
	public void setAttackName(String attackName) {
		this.attackName = attackName;
	}
	public String getBlessing() {
		return blessing;
	}
	public void setBlessing(String blessing) {
		this.blessing = blessing;
	}
	public String getAbility() {
		return ability;
	}
	public void setAbility(String ability) {
		this.ability = ability;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
	public int getMaxBrvBonus() {
		return maxBrvBonus;
	}
	public void setMaxBrvBonus(int maxBrvBonus) {
		this.maxBrvBonus = maxBrvBonus;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public AttackType getAttackType() {
		return attackType;
	}
	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}
	public String getGenericMaterial() {
		return genericMaterial;
	}
	public void setGenericMaterial(String genericMaterial) {
		this.genericMaterial = genericMaterial;
	}
}