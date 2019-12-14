package com.materiabot.GameElements;

public enum ChargeRate{
	VerySlow("Very Slow"), Slow("Slow"), SlightlySlow("Slightly Slow"), 
	Normal("Normal"), 
	SlightlyFast("Slightly Fast"), Fast("Fast"), VeryFast("Very Fast");
	
	private String description;
	
	private ChargeRate(String desc) { description = desc; }
	
	public String getDescription() { return description; }
}