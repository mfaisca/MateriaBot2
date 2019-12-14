package com.materiabot;
import java.awt.Color;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class Test2 {
	private static class HelloCommand extends Command{
		private final EventWaiter waiter;
		public HelloCommand(EventWaiter waiter){
			this.waiter = waiter;
			this.name = "hello";
			this.aliases = new String[]{"hi"};
			this.help = "says hello and waits for a response";
		}

		@Override
		protected void execute(CommandEvent event) {
			event.reply("Hello. What is your name?");
			waiter.waitForEvent(MessageReceivedEvent.class, 
					e -> e.getAuthor().equals(event.getAuthor()) 
					&& e.getChannel().equals(event.getChannel()) 
					&& !e.getMessage().equals(event.getMessage()), 
					e -> event.reply("Hello, `"+e.getMessage().getContentRaw()+"`! I'm `"+e.getJDA().getSelfUser().getName()+"`!"),
					1, TimeUnit.MINUTES, 
					() -> event.reply("Sorry, you took too long."));
		}
	}

	public static void main(String[] args) throws IOException, LoginException, IllegalArgumentException, RateLimitedException{
		String token = "NTMxNDE2ODc4MDExMzgzODA4.XdVNkw.cFe6MR_P8iOnwTOlc2Dmmkm9LWo";
		String ownerId = "141599746987917312";
		EventWaiter waiter = new EventWaiter();
		CommandClientBuilder client = new CommandClientBuilder();
		client.useDefaultGame();
		client.setOwnerId(ownerId);
		client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
		client.setPrefix("!!");
		client.addCommands(
				new AboutCommand(Color.BLUE, "an example bot",
						new String[]{"Cool commands","Nice examples","Lots of fun!"},
						new Permission[]{Permission.ADMINISTRATOR}),
				new HelloCommand(waiter),
				new PingCommand(),
				new ShutdownCommand());
		new JDABuilder(AccountType.BOT).setToken(token)
				.setStatus(OnlineStatus.DO_NOT_DISTURB)
				.setActivity(Activity.playing("loading..."))
				.addEventListeners(waiter, client.build())
				.build();
	}
}
