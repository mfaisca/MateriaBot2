package com.materiabot.Utils;
import java.util.LinkedList;
import java.util.Random;

import com.materiabot.Main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public abstract class Constants {
	public static final Guild MATERIABOT_SERVER = Main.getClient().getGuildById(544340710862618624L);
	public static final long QUETZ_ID = 141599746987917312L;
	public static final Member QUETZ = Constants.MATERIABOT_SERVER.getMemberById(QUETZ_ID);
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
	
	public static final boolean isNumber(String str) {
		for(char c : str.toCharArray())
			if(!('0' <= c && c <= '9')) return false;
		return true;
	}
	public static final LinkedList<String> splitString(String s, int size) {
		LinkedList<String> split = new LinkedList<String>();
		int spaceIndex = s.indexOf(" ");
		if(spaceIndex != -1)
			while(spaceIndex < s.length()) {
				int nextLineIndex = s.indexOf("\n", 0);
				if(nextLineIndex != -1 && nextLineIndex < size) {
					split.add(s.substring(0, nextLineIndex));
					split.addAll(splitString(s.substring(nextLineIndex + 1), size));
					return split;
				}
				int nextSpaceIndex = s.indexOf(" ", spaceIndex + 1);
				if(nextSpaceIndex == -1){
					if(s.length() < size)
						split.addFirst(s);
					else {
						split.add(s.substring(0, spaceIndex));
						split.add(s.substring(spaceIndex+1));
					}
					return split;
				}
				if(nextSpaceIndex < size)
					spaceIndex = nextSpaceIndex;
				else {
					split.add(s.substring(0, spaceIndex));
					split.addAll(splitString(s.substring(spaceIndex + 1), size));
					return split;
				}
			}
		split.add(s);
		return split;
	}

	public static String replaceLast(String string, String toReplace, String replacement) {
	    int pos = string.lastIndexOf(toReplace);
	    if (pos > -1) {
	        return string.substring(0, pos)
	             + replacement
	             + string.substring(pos + toReplace.length(), string.length());
	    } else {
	        return string;
	    }
	}
}