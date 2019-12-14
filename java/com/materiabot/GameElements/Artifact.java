package com.materiabot.GameElements;

public class Artifact {
	private int id, rank, cp, effect, type;
	private String nameGL, nameJP, description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getNameGL() {
		return nameGL;
	}
	public void setNameGL(String name) {
		this.nameGL = name;
	}
	public String getNameJP() {
		return nameJP;
	}
	public void setNameJP(String name) {
		this.nameJP = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCp() {
		return cp;
	}
	public void setCp(int cp) {
		this.cp = cp;
	}
	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}