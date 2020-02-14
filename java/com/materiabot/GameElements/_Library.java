package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.GameElements.Datamining.Ability;
import com.materiabot.GameElements.Datamining.Ailment;
import com.materiabot.GameElements.Datamining.Passive;

public class _Library {
	public static final _Library GL = new _Library();
	public static final _Library JP = new _Library();
	public static _Library get(String r) { return r.equalsIgnoreCase("JP") ? JP : GL; }	
	
	private _Library() {}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Unit> UNIT_LIST = new LinkedList<Unit>();
	public static List<Summon> SUMMON_LIST = new LinkedList<Summon>();
	
	public Unit getUnit(String u) {
		return null;
	}

	public Ability getSkillById(int id) {
		return null;
	}
	public Passive getPassiveById(int id) {
		return null;
	}
	public Ailment getAilmentById(int id) {
		return null;
	}
	
	public Summon getSummon(String summonName) { return getSummon(summonName, 30); }
	public Summon getSummon(String summonName, int level) {
		return SUMMON_LIST.stream().filter(s -> s.getMaxLevel() == level).filter(s -> s.getNicknames().contains(summonName.toLowerCase())).findFirst().orElse(null);
	}

	public static void reset() {
		SUMMON_LIST.clear();
		SUMMON_LIST = new LinkedList<Summon>();
	}
}