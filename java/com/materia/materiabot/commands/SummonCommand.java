package com.materia.materiabot.commands;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.materia.materiabot.Listener;
import com.materia.materiabot.GameElements.Summon;
import com.materia.materiabot.GameElements._Library;
import com.materia.materiabot.Utils.BotException;
import com.materia.materiabot.Utils.Constants;
import com.materia.materiabot.Utils.Constants.HELP_TYPE;
import com.materia.materiabot.Utils.EmoteUtils;
import com.materia.materiabot.Utils.MessageUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class SummonCommand extends _BaseCommand{
	private static int[][] normalMaterials = new int[30][8];
	private static int[][] specialMaterials = new int[20][8];
	static {
		normalMaterials[0] = new int[]{0,0,1,0,0,0,0,1000};
		normalMaterials[1] = new int[]{10,0,5,0,0,0,0,1500};
		normalMaterials[2] = new int[]{20,0,10,0,0,0,0,2000};
		normalMaterials[3] = new int[]{30,10,15,0,0,0,0,2500};
		normalMaterials[4] = new int[]{40,20,20,0,0,0,0,3000};
		normalMaterials[5] = new int[]{50,30,25,5,0,0,0,3500};
		normalMaterials[6] = new int[]{60,40,50,10,0,0,0,4000};
		normalMaterials[7] = new int[]{70,50,75,20,0,0,0,4500};
		normalMaterials[8] = new int[]{80,60,100,30,0,0,0,5000};
		normalMaterials[9] = new int[]{90,70,150,40,0,0,0,6000};
		normalMaterials[10] =new int[]{100,80,175,50,5,0,0,7000};
		normalMaterials[11] =new int[]{0,90,200,60,10,0,0,8000};
		normalMaterials[12] =new int[]{0,100,225,70,15,0,0,9000};
		normalMaterials[13] =new int[]{0,0,250,80,20,5,0,10000};
		normalMaterials[14] =new int[]{0,0,275,90,25,10,0,12500};
		normalMaterials[15] =new int[]{0,0,300,100,30,15,0,15000};
		normalMaterials[16] =new int[]{0,0,350,125,40,20,0,17500};
		normalMaterials[17] =new int[]{0,0,400,150,50,25,0,20000};
		normalMaterials[18] =new int[]{0,0,500,175,75,30,0,22500};
		normalMaterials[19] =new int[]{0,0,750,200,100,50,0,25000};
		normalMaterials[20] =new int[]{0,0,0,225,150,60,5,30000};
		normalMaterials[21] =new int[]{0,0,0,250,175,70,10,35000};
		normalMaterials[22] =new int[]{0,0,0,275,200,80,15,40000};
		normalMaterials[23] =new int[]{0,0,0,300,225,90,20,45000};
		normalMaterials[24] =new int[]{0,0,0,350,250,100,25,50000};
		normalMaterials[25] =new int[]{0,0,0,400,275,125,30,60000};
		normalMaterials[26] =new int[]{0,0,0,450,300,150,40,70000};
		normalMaterials[27] =new int[]{0,0,0,500,350,175,50,80000};
		normalMaterials[28] =new int[]{0,0,0,550,400,200,75,90000};
		normalMaterials[29] =new int[]{0,0,0,600,500,250,100,100000};
		specialMaterials[0] = new int[]{0,0,1,0,0,0,0,500};
		specialMaterials[1] = new int[]{10,0,5,0,0,0,0,750};
		specialMaterials[2] = new int[]{20,0,10,0,0,0,0,1000};
		specialMaterials[3] = new int[]{30,10,15,0,0,0,0,1250};
		specialMaterials[4] = new int[]{40,20,20,0,0,0,0,1500};
		specialMaterials[5] = new int[]{50,30,25,1,0,0,0,1750};
		specialMaterials[6] = new int[]{60,40,30,5,0,0,0,2000};
		specialMaterials[7] = new int[]{70,50,35,10,0,0,0,2250};
		specialMaterials[8] = new int[]{80,60,40,15,0,0,0,2500};
		specialMaterials[9] = new int[]{90,70,50,20,0,0,0,3000};
		specialMaterials[10] = new int[]{100,80,60,25,1,0,0,3500};
		specialMaterials[11] = new int[]{150,90,70,30,5,0,0,4000};
		specialMaterials[12] = new int[]{200,100,80,40,10,0,0,4500};
		specialMaterials[13] = new int[]{0,150,90,50,20,0,0,5000};
		specialMaterials[14] = new int[]{0,200,100,75,30,0,0,6250};
		specialMaterials[15] = new int[]{0,0,120,100,40,0,0,7500};
		specialMaterials[16] = new int[]{0,0,150,125,50,0,0,8750};
		specialMaterials[17] = new int[]{0,0,200,150,100,0,0,10000};
		specialMaterials[18] = new int[]{0,0,250,175,150,0,0,11250};
		specialMaterials[19] = new int[]{0,0,300,200,200,0,0,12500};
	}
	public SummonCommand() {
		super("summon");
	}

	private EmbedBuilder showSummonBoard(Summon summon) throws BotException {
		throw new BotException("Not yet implemented", 201);
	}
	private EmbedBuilder showSummary() throws BotException {
		throw new BotException("Not yet implemented", 201);
	}
	private EmbedBuilder showMaterials(Summon summon, String levels) throws BotException {
		int fromLevel = Integer.parseInt(levels.split("-")[0].trim());
		int toLevel = Integer.parseInt(levels.split("-")[1].trim());
		if(toLevel <= fromLevel || fromLevel < 1 || toLevel > summon.getMaxLevel())
			throw new BotException(MessageUtils.DefaultMessages.SYNTAX_ERROR.getMessage(), 201);
		int[][] materials;
//		if(summon.equals(Summon.Chocobo_20) || summon.equals(Summon.Sylph_20) || summon.equals(Summon.Spirit_Moogle_20))
//			materials = specialMaterials;
//		else
			materials = normalMaterials;
		
		EmbedBuilder builder = new EmbedBuilder(); 

		builder.setAuthor(summon.getName(), 
			"https://dissidiadb.com/static/img/" + summon.getName().toLowerCase() + "." + summon.getDbCrystalImageCode() + ".png", 
			"https://dissidiadb.com/summons/" + summon.getName().toLowerCase());
		builder.setThumbnail("https://dissidiadb.com/static/img/" + summon.getName().toLowerCase() + "." + summon.getDbNonCrystalImageCode() + ".png");
		builder.setTitle("Materials Required");
		builder.addField("From:", ""+fromLevel--, true);
		builder.addField("To:", ""+toLevel--, true);
		if(summon.getElement() != null)
			builder.setColor(summon.getElement().getColor());
		
		int t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0, gil = 0;
		String desc = "";
		for(int i = fromLevel; i < toLevel; i++) {
			t1 += materials[i][0];
			t2 += materials[i][1];
			t3 += materials[i][2];
			t4 += materials[i][3];
			t5 += materials[i][4];
			t6 += materials[i][5];
			t7 += materials[i][6];
			gil += materials[i][7];
		}
		if(t1 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getGenericMaterial() + "T1") + t1;
		if(t2 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getGenericMaterial() + "T2") + t2;
		if(!desc.equals(""))
			builder.addField(MessageUtils.S, desc.trim(), true);
		desc = "";
		if(t3 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getName().toLowerCase() + "T3") + t3;
		if(t4 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getName().toLowerCase() + "T4") + t4;
		if(t5 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getName().toLowerCase() + "T5") + t5;
		if(t6 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getName().toLowerCase() + "T6") + t6;
		if(t7 > 0)
			desc += System.lineSeparator() + EmoteUtils.getEmoteText(summon.getName().toLowerCase() + "T7") + t7;
		if(!desc.equals(""))
			builder.addField(MessageUtils.S, desc.trim(), true);
		if(gil > 0) {
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setGroupingSeparator('.');
			formatter.setDecimalFormatSymbols(symbols);
			builder.addField(MessageUtils.S, EmoteUtils.getEmoteText("gil") + formatter.format(gil), false);
		}
		return builder;
	}
	private EmbedBuilder showSummon(Summon summon) throws BotException {
		EmbedBuilder builder = new EmbedBuilder(); 

		builder.setAuthor(summon.getName(), 
				"https://dissidiadb.com/summons/" + summon.getName().toLowerCase(), 
				"https://dissidiadb.com/static/img/" + summon.getName().toLowerCase() + "." + summon.getDbCrystalImageCode() + ".png");
		builder.setThumbnail("https://dissidiadb.com/static/img/" + summon.getName().toLowerCase() + "." + summon.getDbNonCrystalImageCode() + ".png");
		if(summon.getElement() != null)
			builder.setColor(summon.getElement().getColor());
		builder.addField("Max Level", ""+summon.getMaxLevel(), true);
		builder.addField("Summon Speed", summon.getChargeType(), true);
		builder.addField("Max BRV Bonus", ""+summon.getMaxBrvBonus(), true);
		builder.addField("Turns", ""+summon.getTurns(), true);
		builder.addField("Blessing", summon.getBlessing(), false);
		builder.addField(
				(summon.getElement() != null ? summon.getElement().getEmote() : "") + 
				summon.getAttackType().getEmote() + summon.getAttackName(), summon.getAbility(), false);
		return builder;
	}
	
	@Override
	public void doStuff(Message message) {
		String[] text = message.getContentRaw().split(" ");
		int level = 20;
		String summonName = null;
		boolean showSummary = false;
		boolean showBoard = false;
		boolean showMaterials = false;
		if(text.length > 1) {
			if(text[1].equalsIgnoreCase("boards")) 
				showSummary = true;
			else
				summonName = text[1];
		}
		if(text.length > 2) {
			if(Constants.isNumber(text[2]))
				level = Integer.parseInt(text[2]);
			else if(text[2].equalsIgnoreCase("board"))
				showBoard = true;
			else if(text[2].contains("-"))
				showMaterials = true;
		}
		Summon summon = _Library.JP.getSummon(summonName, level);
		if(summon == null)
			MessageUtils.sendEmbed(message.getChannel(), "There's no summon with that name.", EmoteUtils.Emotes.SORRY_MOOGLE_URL);
		EmbedBuilder embed = null;
		try {
			if(showBoard)
				embed = showSummonBoard(summon);
			else if(showSummary)
				embed = showSummary();
			else if(showMaterials)
				embed = showMaterials(summon, text[2]);
			else
				embed = showSummon(summon);
			MessageUtils.sendEmbed(message.getChannel(), embed);
		} catch (BotException e) {
			if(e.getErrorCode() == 201)
				MessageUtils.sendStatusMessageWarn(message.getChannel(), e.getMessage());
			else
				MessageUtils.sendStatusMessageError(message.getChannel(), MessageUtils.DefaultMessages.UNKNOWN_ERROR.getMessage());
		}
	}
	
	@Override
	protected String help(Message message, HELP_TYPE helpType) {
		String ret = "";
		if(Constants.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Show summon information";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Summon Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(message, HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "summon <SummonName> [20/30]" + System.lineSeparator();
			ret += ">    Shows info about the summon." + System.lineSeparator();
			ret += ">    The last parameter defaults to Level 20." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "summon <SummonName> <fromL>-<toL>" + System.lineSeparator();
			ret += ">    Shows how many materials are required of each to go from the level to the level specified." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "summon <SummonName> board" + System.lineSeparator();
			ret += ">    Shows info about the board of the specified summon." + System.lineSeparator();
			ret += "* " + Listener.COMMAND_PREFIX + "summon {boards/summary}" + System.lineSeparator();
			ret += ">    Shows summary information about Summon Boards." + System.lineSeparator();
			ret += ">" + System.lineSeparator();
			ret += ">    **Examples:**" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "summon Ifrit" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "summon Shiva 30" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "summon Ramuh board" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "summon Brothers 20-30" + System.lineSeparator();
			ret += ">    " + Listener.COMMAND_PREFIX + "summon boards" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}