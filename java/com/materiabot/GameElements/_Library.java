package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.GameElements.OperaOmnia.Datamining.Ability;
import com.materiabot.GameElements.OperaOmnia.Datamining.Ailment;

public class _Library {
	public static final _Library GL = new _Library();
	public static final _Library JP = new _Library();
	public static _Library get(String r) { return r.equalsIgnoreCase("JP") ? JP : GL; }	
	
	private _Library() {}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public static List<Summon> SUMMON_LIST = new LinkedList<Summon>();
	
	public Unit getChar(String c) { return getUnit(c); }
	public Unit getUnit(String u) {
		return null;
	}

	public Ability getSkillById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	public Ailment getAilmentById(int id) {
		// TODO Auto-generated method stub
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