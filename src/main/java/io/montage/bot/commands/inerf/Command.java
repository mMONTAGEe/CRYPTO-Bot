package io.montage.bot.commands.inerf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import io.montage.bot.utilities.EmbedUtil;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * The Class Command.
 */
public abstract class Command {

	/** The command name. */
	protected String name;

	/** The commands parameters. */
	protected List<String> parameters;

	/** The commands aliases. */
	protected Set<String> aliases;

	/**
	 * Instantiates a new command.
	 *
	 * @param name the name
	 */
	public Command(String name) {
		this.name       = name;
		this.parameters = new ArrayList<>(3);
		this.aliases    = new HashSet<>();
	}

	/**
	 * Adds the alias.
	 *
	 * @param alias the alias
	 * @return the command
	 */
	public Command addAlias (String alias) {
		aliases.add(alias);
		return this;
	}

	public Command params (String... params) {
		for (String string : params)
			parameters.add(string);
		return this;
	}

	/**
	 * @param args         arguments for the command
	 * @param channel      channel where the command is executed
	 * @param author       who invoked the command
	 * @param inputMessage the incoming message object
	 * @return the message to output or an empty string for nothing
	 */
	public abstract void executeAndHandle (GuildMessageReceivedEvent event, List<String> params, User author, Message inputMessage);

	/**
	 * Gets the name.
	 *
	 * @return {@link #name}
	 */
	public String getName () {
		return name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public abstract String getDescription ();

	/**
	 * Gets the parameters.
	 *
	 * @return {@link #parameters}
	 */
	public List<String> getParameters () {
		return Collections.unmodifiableList(parameters);
	}

	/**
	 * Gets the aliases.
	 *
	 * @return {@link #aliases}
	 */
	public Set<String> getAliases () {
		return Collections.unmodifiableSet(aliases);
	}

	public void getHelpMessage (GuildMessageReceivedEvent event) {
		EmbedUtil.sendAndDeleteOnGuilds(event.getChannel(), new MessageBuilder("Must Supply Correct Parameters \n\n "
				+ "`" + this.name + " " + StringUtils.join(getParameters(), " ") + "`").build(), 15, TimeUnit.SECONDS);
		event.getMessage().delete().queue();
	}
}
