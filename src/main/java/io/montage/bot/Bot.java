package io.montage.bot;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_PRESENCES;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.LoginException;

import com.romvoid.config.ConfigBuilder;

import io.montage.bot.commands.LatencyCommand;
import io.montage.bot.commands.SayCommand;
import io.montage.bot.commands.inerf.CommandManager;
import io.montage.bot.listeners.GuildMemberJoin;
import io.montage.bot.listeners.GuildMemberLeave;
import io.montage.bot.listeners.MessageListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.JDAImpl;

public class Bot {
	
	private static Bot instance;
	private JDAImpl jda;
	private CommandManager commandManager;
	private static Set<GatewayIntent> intents = new HashSet<>();

	public Bot() throws Exception {
		instance =  this;
		new ConfigBuilder(Config.class, new File("application.cfg")).build(true);
		setIntents();
		commandManager = new CommandManager();
		installCommands();
		initJDA();
		
	}
	
	public static void main(String[] args) {
		if (instance != null)
			throw new RuntimeException("Bot has already been initialized in this VM.");
		try {
			new Bot();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initJDA() {
		if (instance == null)
			throw new NullPointerException("Bot has not been initialized yet.");

		try {
			instance.jda = (JDAImpl) JDABuilder.create(Config.BOT_TOKEN, intents)
					.setStatus(OnlineStatus.DO_NOT_DISTURB)
					.setActivity(Activity.playing("Galacticraft").asRichPresence())
					.addEventListeners(new MessageListener(), new GuildMemberJoin(), new GuildMemberLeave())
					.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	private void setIntents() {
		intents.add(GUILD_MESSAGES);
		intents.add(GUILD_MEMBERS);
		intents.add(GUILD_PRESENCES);
	}
	
	public String getPrefix() {
		return Config.BOT_COMMAND_PREFIX;
	}
	
	/**
	 * Install commands.
	 */
	private void installCommands() {
		commandManager.register(new LatencyCommand());
		commandManager.register(new SayCommand());
	}

	/**
	 * Handle command event.
	 *
	 * @param event the event
	 */
	public void handleCommandEvent(GuildMessageReceivedEvent event) {
		// If the event message is, e.g. !cmd testing testing, commandName is set to
		// "cmd"
		String commandName = event.getMessage().getContentRaw().substring(1).split(" ")[0].toLowerCase();
		commandManager.handleCommand(commandName, event);
	}
	
	public static Bot getInstance() {
		if (instance == null)
			throw new IllegalStateException("Bot has not been initialised");
		return instance;
	}
	
	public static JDAImpl getJDA() {
		return instance == null ? null : instance.jda;
	}

}
