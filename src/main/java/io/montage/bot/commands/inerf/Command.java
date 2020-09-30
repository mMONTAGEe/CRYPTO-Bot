package io.montage.bot.commands.inerf;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import io.montage.bot.utilities.EmbedUtil;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {

	protected String		name;
	protected List<String>	parameters;
	protected List<String>	aliases;

	/**
	 * Instantiates a new command.
	 *
	 * @param name the name
	 */
	public Command(String name) {
		this.name = name;
		this.parameters = new ArrayList<>();
		this.aliases = new ArrayList<>();
	}

	/**
	 * Adds the alias.
	 *
	 * @param alias the alias
	 * @return the command
	 */
	public Command addAlias(String... alias) {
		for (String string : alias) {
			aliases.add(string);
		}
		return this;
	}

	public Command params(String... params) {
		for (String string : params) {
			parameters.add(string);
		}
		return this;
	}

	/**
	 * @param args         arguments for the command
	 * @param channel      channel where the command is executed
	 * @param author       who invoked the command
	 * @param inputMessage the incoming message object
	 * @return the message to output or an empty string for nothing
	 */
	public abstract void executeAndHandle(GuildMessageReceivedEvent event, List<String> params);

	/**
	 * Gets the name.
	 *
	 * @return {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public abstract String getDescription();

	/**
	 * Gets the parameters.
	 *
	 * @return {@link #parameters}
	 */
	public List<String> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

	/**
	 * Gets the aliases.
	 *
	 * @return {@link #aliases}
	 */
	public List<String> getAliases() {
		return Collections.unmodifiableList(aliases);
	}

	public void getHelpMessage(GuildMessageReceivedEvent event) {
		EmbedUtil.sendAndDeleteOnGuilds(event.getChannel(), new MessageBuilder("Must Supply Correct Parameters \n\n " + "`" + this.name + " " + StringUtils.join(getParameters(), " ") + "`").build(), 15, TimeUnit.SECONDS);
		event.getMessage().delete().queue();
	}
}
