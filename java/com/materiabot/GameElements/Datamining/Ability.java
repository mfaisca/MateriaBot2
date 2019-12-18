package com.materiabot.GameElements.Datamining;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.materiabot.GameElements.Element;

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
	public static enum Command_Type{
		Normal(1, "Normal"),
		EX(2, "EX"),
		Unknown(3, "Unknown");

		private int id;
		private String description;
		
		private Command_Type(int id, String desc) {
			this.id = id;
			this.description = desc;
		}
		
		public int getId() {
			return id;
		}
		public String getDescription() {
			return description;
		}
		
		public static Command_Type get(int id) {
			return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
		}
	}
	public static enum Target_Type{
		Counter(1, "Counter"),
		Enemy(2, "Enemy"),
		Ally(3, "Ally"),
		OtherAlly(4, "Other Ally"),
		Self(5, "Self"),
		Party2(7, "Party?");

		private int id;
		private String description;
		
		private Target_Type(int id, String desc) {
			this.id = id;
			this.description = desc;
		}
		
		public int getId() {
			return id;
		}
		public String getDescription() {
			return description;
		}
		
		public static Target_Type get(int id) {
			return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
		}
	}
	
	public static abstract class Hit_Data {
		public static enum Type{
			BRV(1), HP(2), Ailment(6), HPSplash(7), BRVIgnoreDEF(14), SketchSummon(15);
			
			private int id;
			
			private Type(int id) { this.id = id; }

			public int getId() {
				return id;
			}
		}
		public static enum Attack_Type{
			UnknownN1(-1), Unknown1(1), Unknown2(2), Unknown3(3);
			
			private int id;
			
			private Attack_Type(int id) { this.id = id; }
			
			public int getId() { return id; }
		}
		public static enum Target{
			ST(1, "ST"),
			Self(2, "Self?"),
			Random(3, "Random Targets"),
			AoE(5, "AoE"),
			Party(6, "Party"),
			Party2(7, "Party"),
			Splash(10, "Splash"),
			SplitBRV(11, "Split"),
			Ally(13, "Ally only"),
			SplitHP(18, "Split");
			
			private int id;
			private String desc;
			
			private Target(int id, String desc) {this.id = id; this.desc = desc; }

			public int getId() {
				return id;
			}
			public String getDesc() {
				return desc;
			}
		}
		public static enum Effect{
			
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
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
		public int[] getArguments() {
			return arguments;
		}
		public void setArguments(int[] arguments) {
			this.arguments = arguments;
		}
		public Attack_Type getAttackType() {
			return attackType;
		}
		public void setAttackType(Attack_Type attackType) {
			this.attackType = attackType;
		}
		public Target getTarget() {
			return target;
		}
		public void setTarget(Target target) {
			this.target = target;
		}
		public Element getElement() {
			return element;
		}
		public void setElement(Element element) {
			this.element = element;
		}
		public Effect getEffect() {
			return effect;
		}
		public void setEffect(Effect effect) {
			this.effect = effect;
		}
	}
	
	private int id;
	private String name;
	private String description;
	private int movementCost;
	private int useCount;
	private Type type;
	private Attack_Type attackType;
	private Command_Type commandType;
	private Target_Type targetType;
	private int chaseDmg; //can_initiate_chase * chase_dmg
	private List<Hit_Data> hits = new LinkedList<Hit_Data>();
	
	public String getName() {
		return null;
	}
}