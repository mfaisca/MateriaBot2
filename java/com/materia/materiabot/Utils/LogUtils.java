package com.materia.materiabot.Utils;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtils{
	private static Logger log;
	
	static {
		FileHandler fileTxt = null;
		log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        log.setLevel(Level.INFO);
        try {
			fileTxt = new FileHandler("logger.txt");
	        fileTxt.setFormatter(new SimpleFormatter());
	        log.addHandler(fileTxt);
		} catch (SecurityException | IOException e) {
			log = null;
			System.out.println("ERROR OPENING LOG - PLEASE FIX");
		}
	}

	/*public static void error(MessageReceivedEvent event, String msg){
		log(event, Level.SEVERE, msg, null);
	}
	public static void warn(MessageReceivedEvent event, String msg){
		log(event, Level.WARNING, msg, null);
	}
	public static void info(MessageReceivedEvent event, String msg){
		log(event, Level.INFO, msg, null);
	}
	public static void error(MessageReceivedEvent event, String msg, Throwable e){
		log(event, Level.SEVERE, msg, e);
	}
	public static void warn(MessageReceivedEvent event, String msg, Throwable e){
		log(event, Level.WARNING, msg, e);
	}
	public static void info(MessageReceivedEvent event, String msg, Throwable e){
		log(event, Level.INFO, msg, e);
	}
	private static void log(MessageReceivedEvent event, Level level, String msg, Throwable e) {
		if(log == null || event == null) {
			System.out.println("ERROR OPENING LOG - PLEASE FIX");
			return;
		}
		msg = event.getGuild().getName() + "|" + event.getChannel().getName() + "|" + 
				event.getAuthor().getDisplayName(event.getGuild()) + "(" + event.getAuthor().getLongID() + "): " + msg;
		if(log == null) {
			System.out.println(msg);
			if(e != null)
				e.printStackTrace();
		}
		else if(e == null)
			log.log(level, msg);
		else
			log.log(level, msg, e);
	}*/
}