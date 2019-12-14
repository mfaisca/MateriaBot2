package com.materiabot.GameElements;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.GameElements.OperaOmnia.Datamining.Ability;
import com.materiabot.GameElements.OperaOmnia.Datamining.Passive;
import com.materiabot.GameElements.Sphere.SphereType;

public class Unit {	
	private String name;
	private List<String> nicknames = new LinkedList<String>();
	private Crystal crystal;
	private EquipmentType equipmentType;
	
	private List<Ability> abilities = new LinkedList<Ability>();
	private List<Passive> passives = new LinkedList<Passive>();
	private List<Equipment> equipment = new LinkedList<Equipment>();
	private HashMap<Integer, List<Artifact>> artifacts = new HashMap<Integer, List<Artifact>>();
	private SphereType[] spheres = new SphereType[3];
	
	public Unit() {} //TODO Remove maybe?	
	public Unit(String name, Crystal crystal, EquipmentType equipmentType, String... nicknames) {
		this.name = name;
		this.crystal = crystal;
		this.equipmentType = equipmentType;
		this.nicknames.addAll(Arrays.asList(nicknames));
	}
	
	public String getName() {
		return name;
	}
	public List<String> getNicknames() {
		return nicknames;
	}
	
	public Crystal getCrystal() {
		return crystal;
	}
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public List<Ability> getAbilities() {
		return abilities;
	}
	public List<Passive> getPassives() {
		return passives;
	}
	public List<Equipment> getEquipment() {
		return equipment;
	}
	public HashMap<Integer, List<Artifact>> getArtifacts() {
		return artifacts;
	}
	public List<Artifact> getArtifacts(int i) {
		return getArtifacts().get(i);
	}
	public SphereType[] getSpheres() {
		return spheres;
	}
}