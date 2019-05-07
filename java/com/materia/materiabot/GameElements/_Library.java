package com.materia.materiabot.GameElements;

public class _Library {
	public static final _Library GL = new _Library();
	public static final _Library JP = new _Library();
	public static _Library get(String r) { return r.equalsIgnoreCase("JP") ? JP : GL; }
	//public static final List<Summon> SUMMON_CACHE = new LinkedList<Summon>();
	//public static final List<Unit> UNIT_CACHE = new LinkedList<Unit>();
	
	
	private _Library() {}

	public Unit getChar(String c) { return getUnit(c); }
	public Unit getUnit(String u) {
		return null;
	}

	public Summon getSummon(String summonName) { return getSummon(summonName, 20); }
	public Summon getSummon(String summonName, int level) {
		return null;
	}


	public static void reset() {
		// TODO Auto-generated method stub
		
	}
}