package com.materia.materiabot.Utils;
import java.util.Random;

public abstract class Constants {
	public static final Long OWNER_ID = 141599746987917312L;
	public static final String DATABASE = "../_files/Database.db";
	public static final Random RNG = new Random();
	
	public static final class Dual<O1, O2>{
		private O1 value1;
		private O2 value2;
		
		public Dual(O1 v1, O2 v2){
			value1 = v1; value2 = v2;
		}

		public O1 getValue1() { return value1; }
		public O2 getValue2() { return value2; }
		public void setValue1(O1 v) { value1 = v; }
		public void setValue2(O2 v) { value2 = v; }
	}
	
	public static final void sleep(int sleep){
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {}
	}
}