package io.montage.bot;

import java.io.File;
import java.util.EnumSet;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import io.montage.bot.commands.inerf.CommandManager;
import io.montage.bot.listeners.GuildListeners;
import io.montage.bot.listeners.MessageListener;
import io.montage.bot.utilities.logging.WebhookAppender;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.JDAImpl;
import net.rom.BuildCfg;

public class Crypto {

	private static Crypto							instance;
	private JDAImpl									jda;
	private CommandManager							commandManager;
	public static final Logger						LOG		= (Logger) LoggerFactory.getLogger(Crypto.class);
	private final static File						file	= new File("application.cfg");

	private Crypto() throws Exception {
		instance = this;
		if (!file.exists()) {
			new BuildCfg(Config.class, file).build(true);
			System.out.println("Created application.cfg file, please fill in file options as needed and re-run the bot");
			System.exit(0);
		}
		new BuildCfg(Config.class, file).build(true);
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
		initJDA();

		Thread.sleep(8 * 1000);

		log("Online -> Regisered [ {} ] Commands", commandManager.getRegisteredCommands().size());
	}

	public static void main(String[] args) {
		if (instance != null) {
			throw new RuntimeException("Bot has already been initialized in this VM.");
		}
		try {
			new Crypto();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initJDA() {
		try {
			instance.jda = (JDAImpl) JDABuilder
					.createLight(Config.BOT_TOKEN)
					.enableIntents(EnumSet.allOf(GatewayIntent.class))
					.setStatus(OnlineStatus.DO_NOT_DISTURB)
					.addEventListeners(
							new MessageListener(), 
							new GuildListeners())
					.enableCache(EnumSet.allOf(CacheFlag.class))
					.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	public String getPrefix() {
		return Config.BOT_COMMAND_PREFIX;
	}

	public static Crypto getInstance() {
		return instance;
	}

	public static CommandManager getCommandManager() {
		return instance == null ? null : instance.commandManager;
	}

	public static JDAImpl getJDA() {
		return instance == null ? null : instance.jda;
	}

	public static void log(String msg, @Nullable Object... object) {
		LOG.info("'" + msg + "'", object);
	}

}
