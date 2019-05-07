package com.materia.materiabot.commands;
import com.materia.materiabot.Listener;
import com.materia.materiabot.GameElements.Crystal;
import com.materia.materiabot.GameElements._Library;
import com.materia.materiabot.GameElements.Unit;
import com.materia.materiabot.Utils.Constants;
import com.materia.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CrystalLevelCommand extends _BaseCommand{
	private static int[][] crystals = new int[71][5];
	static {
		crystals[0] = crystals[1] = new int[]{0,0,0,0,0};
		crystals[2] = new int[]{2,0,0,0,0};
		crystals[3] = new int[]{2,0,0,0,0};
		crystals[4] = new int[]{2,0,0,0,0};
		crystals[5] = new int[]{5,0,0,0,0};
		crystals[6] = new int[]{5,0,0,0,0};
		crystals[7] = new int[]{5,0,0,0,0};
		crystals[8] = new int[]{5,0,0,0,0};
		crystals[9] = new int[]{5,0,0,0,0};
		crystals[10] = new int[]{10,0,0,0,0};
		crystals[11] = new int[]{10,0,0,0,0};
		crystals[12] = new int[]{10,0,0,0,0};
		crystals[13] = new int[]{10,0,0,0,0};
		crystals[14] = new int[]{10,0,0,0,0};
		crystals[15] = new int[]{20,0,0,0,0};
		crystals[16] = new int[]{20,0,0,0,0};
		crystals[17] = new int[]{20,0,0,0,0};
		crystals[18] = new int[]{20,0,0,0,0};
		crystals[19] = new int[]{20,0,0,0,0};
		crystals[20] = new int[]{30,0,0,0,0};
		crystals[21] = new int[]{5,5,0,0,0};
		crystals[22] = new int[]{5,5,0,0,0};
		crystals[23] = new int[]{5,5,0,0,0};
		crystals[24] = new int[]{5,5,0,0,0};
		crystals[25] = new int[]{10,10,0,0,0};
		crystals[26] = new int[]{10,10,0,0,0};
		crystals[27] = new int[]{10,10,0,0,0};
		crystals[28] = new int[]{10,10,0,0,0};
		crystals[29] = new int[]{10,10,0,0,0};
		crystals[30] = new int[]{30,30,0,0,0};
		crystals[31] = new int[]{20,20,0,0,0};
		crystals[32] = new int[]{20,20,0,0,0};
		crystals[33] = new int[]{20,20,0,0,0};
		crystals[34] = new int[]{20,20,0,0,0};
		crystals[35] = new int[]{30,30,0,0,0};
		crystals[36] = new int[]{30,30,0,0,0};
		crystals[37] = new int[]{30,30,0,0,0};
		crystals[38] = new int[]{30,30,0,0,0};
		crystals[39] = new int[]{30,30,0,0,0};
		crystals[40] = new int[]{50,50,0,0,0};
		crystals[41] = new int[]{10,10,10,0,0};
		crystals[42] = new int[]{20,20,20,0,0};
		crystals[43] = new int[]{30,30,30,0,0};
		crystals[44] = new int[]{40,40,40,0,0};
		crystals[45] = new int[]{50,50,50,0,0};
		crystals[46] = new int[]{60,60,60,0,0};
		crystals[47] = new int[]{70,70,70,0,0};
		crystals[48] = new int[]{80,80,80,0,0};
		crystals[49] = new int[]{90,90,90,0,0};
		crystals[50] = new int[]{100,100,100,0,0};
		//First Awakening
		crystals[51] = new int[]{20,15,10,5,0};
		crystals[52] = new int[]{40,30,20,10,0};
		crystals[53] = new int[]{60,45,30,15,0};
		crystals[54] = new int[]{80,60,40,20,0};
		crystals[55] = new int[]{100,75,50,25,0};
		crystals[56] = new int[]{120,90,60,30,0};
		crystals[57] = new int[]{140,105,70,35,0};
		crystals[58] = new int[]{160,120,80,40,0};
		crystals[59] = new int[]{180,135,90,45,0};
		crystals[60] = new int[]{200,150,100,50,0};
		//Second Awakening
		crystals[61] = new int[]{30,25,20,10,5};
		crystals[62] = new int[]{50,40,30,15,10};
		crystals[63] = new int[]{70,55,40,20,15};
		crystals[64] = new int[]{90,70,50,25,20};
		crystals[65] = new int[]{110,85,60,30,25};
		crystals[66] = new int[]{130,100,70,35,30};
		crystals[67] = new int[]{150,115,80,40,35};
		crystals[68] = new int[]{170,130,90,45,40};
		crystals[69] = new int[]{190,145,100,50,45};
		crystals[70] = new int[]{210,160,110,55,50};
	}
	
	public CrystalLevelCommand() {
		super("cl", "clvl");
	}
	
	private EmbedBuilder build(Crystal crystal, int fromLevel, int toLevel) {
		if(toLevel <= fromLevel || fromLevel < 1 || toLevel > 70)
			return null;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Crystals Required");
		builder.addField("From:", ""+fromLevel, true);
		builder.addField("To:", ""+toLevel, true);
		builder.addField(MessageUtils.S, MessageUtils.S, false);
		builder.setColor(crystal.getColor());
		int t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0;
		String desc = "";
		for(int i = fromLevel+1; i <= toLevel; i++) {
			t1 += crystals[i][0];
			t2 += crystals[i][1];
			t3 += crystals[i][2];
			t4 += crystals[i][3];
			t5 += crystals[i][4];
		}
		if(t1 > 0)
			desc = crystal.getEmote(1) + t1;
		if(t2 > 0)
			desc += System.lineSeparator() + crystal.getEmote(2) + t2;
		if(t3 > 0)
			desc += System.lineSeparator() + crystal.getEmote(3) + t3;
		if(t4 > 0)
			desc += System.lineSeparator() + crystal.getEmote(4) + t4;
		if(t5 > 0)
			desc += System.lineSeparator() + crystal.getEmote(5) + t5;
		builder.addField(MessageUtils.S, desc, false);
		return builder;
	}

	@Override
	public void doStuff(Message message) {
		String msg = message.getContentRaw();
		String[] msgs = new String[0];
		String msg1, msg2;
		if(msg.contains(" "))
			msgs = msg.split(" ");
		else {
			msg1 = "1";
			msg2 = "60";
		}
		if(msgs.length >= 2 && msgs[1].contains("-")) {
			msg1 = msgs[1].split("-")[0].trim();
			msg2 = msgs[1].split("-")[1].trim();
			if(!Constants.isNumber(msg1) || !Constants.isNumber(msg2)) {
				MessageUtils.sendStatusMessageWarn(message.getChannel(), "Wrong Format");
				return;
			}
		}else {
			msg1 = "1";
			msg2 = msgs.length == 3 ? msgs[1].trim() : "70";
		}
		Crystal c = Crystal.find(msgs.length >= 2 ? msgs[msgs.length-1] : null);
		if(c == null) {
			Unit ch = _Library.JP.getChar(msgs.length >= 2 ? msgs[msgs.length-1] : null);//OperaOmniaConstants.getCharacter(msgs.length >= 2 ? msgs[msgs.length-1] : null);
			if(ch != null)
				c = ch.getCrystal();
			else
				c = Crystal.values()[Math.abs(message.getAuthor().getId().hashCode()) % 6];
		}
		EmbedBuilder embed = build(c, Integer.parseInt(msg1), Integer.parseInt(msg2));
		if(embed != null)
			MessageUtils.sendEmbed(message.getChannel(), embed);
		else
			MessageUtils.sendStatusMessageError(message.getChannel(), "Wrong Format - Levels must be between 1 and 70, with the first level lower than the second.");
	}

	@Override
	protected String help(Message message, Constants.HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Calculates how many crystals are required to level";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Crystal Level Calculator Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(message, Constants.HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "cl <fromL>-<toL> [color/CharName]" + System.lineSeparator();
			ret += ">    Shows how many crystals are required of each tier to go from the level to the level specified." + System.lineSeparator();
			ret += ">    The color/charName parameter is just fluff." + System.lineSeparator();
			ret += ">" + System.lineSeparator();
			ret += ">    **Examples:**" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "cl 15-55" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "cl 60-70 Black" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "cl 1-70 Ultimecia" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}
