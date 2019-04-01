package com.materia.materiabot.Utils;
import java.util.Random;
import com.materia.materiabot.Configs;

public abstract class Constants {
	public static final Long OWNER_ID = Configs.OWNER_ID;
	public static final Random RNG = new Random();
		
	public static final void sleep(int sleep){
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {}
	}
}