package com.materiabot.GameElements.OperaOmnia.Datamining;
import java.util.Arrays;

public class Ability {
	public static enum Type{
		BRV(1, "BRV Attack"),
		HP(2, "HP Attack"),
		None(6, "None"),
		Unknown1(7, "Unknown1"),
		Unknown2(11, "Unknown2"),
		Unknown3(14, "Unknown3");

		private int id;
		private String description;
		
		private Type(int id, String desc) {
			this.id = id;
			this.description = desc;
		}
		
		public int getId() {
			return id;
		}
		public String getDescription() {
			return description;
		}
		
		public static Type get(int id) {
			return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
		}
	}
	public static enum Attack_Type{
		Melee(1, "Melee Attack"),
		Ranged(2, "Ranged Attack"),
		Magic(3, "Magic Attack"),
		MixedMeleeRanged(4, "Melee + Ranged Attack"),
		MixedMeleeMagic(5, "Melee + Magic Attack");

		private int id;
		private String description;
		
		private Attack_Type(int id, String desc) {
			this.id = id;
			this.description = desc;
		}
		
		public int getId() {
			return id;
		}
		public String getDescription() {
			return description;
		}
		
		public static Attack_Type get(int id) {
			return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
		}
	}
	
	public static abstract class HitData {
		public static enum Type{
			BRV(1), HP(2), Ailment(6), HPSplash(7), BRVIgnoreDEF(14), SketchSummon(15);
			
			private int id;
			
			private Type(int id) { this.id = id; }
		}
		public static enum Attack_Type{
			
		}
		public static enum Element{
			
		}
		public static enum Target{
			
		}
		public static enum EffectEnum{
			
		}
		public static class Effect{
			private int effectValueType;
		}
		
		private int id;
		private Type type;
		private int[] arguments;
		private Attack_Type attackType;
		private Target target;
		private Element element;
		private Effect effect;
	}
	
	
	public String getName() {
		return null;
	}
}