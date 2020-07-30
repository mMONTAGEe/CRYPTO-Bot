package io.montage.bot;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_PRESENCES;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import com.romvoid.config.ConfigBuilder;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import io.montage.bot.commands.ActivityCommand;
import io.montage.bot.commands.LatencyCommand;
import io.montage.bot.commands.SayCommand;
import io.montage.bot.commands.SayCommand2;
import io.montage.bot.commands.getXpCommand;
import io.montage.bot.commands.inerf.Command;
import io.montage.bot.commands.inerf.CommandManager;
import io.montage.bot.listeners.GuildMemberJoin;
import io.montage.bot.listeners.GuildMemberLeave;
import io.montage.bot.listeners.MessageListener;
import io.montage.bot.utilities.logging.WebhookAppender;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.JDAImpl;
import net.rom.lib.async.threads.builder.ThreadBuilder;

public class Bot {

	private static Bot                           instance;
	private JDAImpl                              jda;
	private CommandManager                       commandManager;
	public static final Logger                   LOG     = (Logger) LoggerFactory.getLogger(Bot.class);
	private static Set<GatewayIntent>            intents = new HashSet<>();
	private final static File                    file    = new File("application.cfg");
	public static final ScheduledExecutorService EXEC    = Executors
			.newSingleThreadScheduledExecutor(new ThreadBuilder().setName("main-executor").startAfterBuilt());

	public Bot() throws Exception {
		instance = this;
		if (!file.exists()) {
			new ConfigBuilder(Config.class, file).build(true);
			System.out
					.println("Created application.cfg file, please fill in file options as needed and re-run the bot");
			System.exit(0);
		}
		new ConfigBuilder(Config.class, file).build(true);
		setIntents();
		commandManager = new CommandManager();
		File file = new File("users/anchorfile");
		file.getParentFile().mkdirs();        
		file.createNewFile();
		if (Config.WEBOOK_CONSOLE) {
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

			ThresholdFilter filter = new ThresholdFilter();
			filter.setLevel("all");
			filter.setContext(lc);
			filter.start();

			PatternLayoutEncoder encoder = new PatternLayoutEncoder();
			encoder.setPattern("**" + Config.PATTERN + "**");
			encoder.setContext(lc);
			encoder.start();

			WebhookAppender appender = new WebhookAppender();
			appender.setEncoder(encoder);
			appender.addFilter(filter);
			appender.setWebhookUrl(Config.WEBHOOK_URL);
			appender.setName("ERROR_WH");
			appender.setContext(lc);
			appender.start();

			Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
			root.addAppender(appender);
		}
		installCommands();
		initJDA();

		pause(8);

		log("Online -> Regisered [ {} ] Commands", getRegisteredCommands().size());
	}

	public static void main (String[] args) {
		if (instance != null)
			throw new RuntimeException("Bot has already been initialized in this VM.");
		try {
			new Bot();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initJDA () {
		if (instance == null)
			throw new NullPointerException("Bot has not been initialized yet.");

		try {
			instance.jda = (JDAImpl) JDABuilder.create(Config.BOT_TOKEN, intents).setStatus(OnlineStatus.DO_NOT_DISTURB)
					.setActivity(Activity.playing("Galacticraft").asRichPresence())
					.addEventListeners(new MessageListener(), new GuildMemberJoin(), new GuildMemberLeave())
					.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).build();
		}
		catch (LoginException e) {
			e.printStackTrace();
		}
		getJDA().getPresence().setStatus(OnlineStatus.ONLINE);

	}

	private void setIntents () {
		intents.add(GUILD_MESSAGES);
		intents.add(GUILD_MEMBERS);
		intents.add(GUILD_PRESENCES);
	}

	public String getPrefix () {
		return Config.BOT_COMMAND_PREFIX;
	}

	/**
	 * Install commands.
	 */
	private void installCommands () {
		commandManager.register(new LatencyCommand());
		commandManager.register(new SayCommand());
		commandManager.register(new SayCommand2());
		commandManager.register(new ActivityCommand());
		commandManager.register(new getXpCommand());

	}

	/**
	 * Handle command event.
	 *
	 * @param event the event
	 */
	public void handleCommandEvent (GuildMessageReceivedEvent event) {
		// If the event message is, e.g. !cmd testing testing, commandName is set to
		// "cmd"
		String commandName = event.getMessage().getContentRaw().substring(1).split(" ")[0].toLowerCase();
		commandManager.handleCommand(commandName, event);
		log("{} Invoked {} Command", event.getAuthor().getAsTag(), commandName);
	}

	public static Bot getInstance () {
		if (instance == null)
			throw new IllegalStateException("Bot has not been initialised");
		return instance;
	}

	public static JDAImpl getJDA () {
		return instance == null ? null : instance.jda;
	}

	public static void pause (double seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		}
		catch (InterruptedException e) {}
	}

	private Set<Command> getRegisteredCommands () {
		Set<Command> set = new HashSet<Command>();
		for (Iterator<Entry<String, Command>> itr = commandManager.commands.entrySet().iterator(); itr.hasNext();) {
			Map.Entry<String, Command> entrySet = (Entry<String, Command>) itr.next();

			Command value = entrySet.getValue();

			if (!set.add(value)) {
				itr.remove();
			}
		}
		return set;
	}
	
	public static void log(String msg, @Nullable Object... object) {
		LOG.info("'" + msg + "'", object);
	}
}
