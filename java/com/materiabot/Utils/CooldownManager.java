package com.materiabot.Utils;
import java.util.HashMap;
import com.materiabot.commands.general.PatreonCommand;
import net.dv8tion.jda.api.entities.User;

public class CooldownManager {
	public static enum Type{
		REGULAR(5, 2), PULL(30, 20), GOLDPULL(600, 60), CLEVERBOT(30, 2);
		private int regularCD, patreonCD;
		
		private Type(int rCD, int pCD) { regularCD = rCD; patreonCD = pCD; }
		
		public int getRegularCooldown() { return regularCD; }
		public int getPatreonCooldown() { return patreonCD; }
	}
	
	private static final CooldownManager SINGLETON = new CooldownManager();
    private static final HashMap<User, HashMap<Type, Long>> USER_COOLDOWNS = new HashMap<User, HashMap<Type, Long>>();	
	
	private CooldownManager() {}
	public CooldownManager get() { return SINGLETON; }

	public static int userCooldown(User user) {
		return userCooldown(user, Type.REGULAR);
	}
	public static int userCooldown(User user, Type type) {
		if(!USER_COOLDOWNS.containsKey(user))
			USER_COOLDOWNS.put(user, new HashMap<Type, Long>());
		HashMap<Type, Long> userCDs = USER_COOLDOWNS.get(user);
		if(userCDs.containsKey(type)) {
    		long cooldown = userCDs.get(type);
    		if(cooldown > System.currentTimeMillis())
    			return (int) (cooldown - System.currentTimeMillis());
		}
		userCDs.put(type, System.currentTimeMillis() + ((PatreonCommand.isUserPatreon(user) ? 
															type.getPatreonCooldown() : 
															type.getRegularCooldown()
														) * 1000));
		return -1;
	}
}