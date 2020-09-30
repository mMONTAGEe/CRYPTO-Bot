package io.montage.bot.commands.inerf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import io.montage.bot.utilities.FileUtil;
import io.montage.bot.utilities.Reflection;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager {

	public Map<String, Command> commands;

	public CommandManager() {
		commands = new HashMap<>();
		this.registerCommands();
	}

	/**
	 * Handle command.
	 *
	 * @param commandName the command name
	 * @param event       the event
	 */
	public void handleCommand(String commandName, GuildMessageReceivedEvent event) {
		Optional<Command> commandOptional = commandFromName(commandName);
		// Adds any space separated strings to the parameter list
		commandOptional.ifPresent(command -> {
			String[] tokens = event.getMessage().getContentRaw().substring(1).split(" ", 2);
			List<String> paramList = new ArrayList<>();
			if (hasParams(tokens)) {
				final String params = tokens[1].trim();
				paramList = new ArrayList<>(Arrays.asList(params.split(" ")));
			}
			command.executeAndHandle(event, paramList);
			if(!command.getName().equalsIgnoreCase("xp")) {
				increaseXp(event);
			}
		});
	}

	/**
	 * 
	 *
	 * @param command the command
	 */
	private void registerCommands() {
		Command command;
		for (Class<? extends Command> clazz : Reflection.getCommandClasses()) {
			try {
				command = clazz.newInstance();

				commands.put(command.getName().toLowerCase(), command);

				for (String alias : command.getAliases()) {
					commands.put(alias.toLowerCase(), command);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}


	public Set<Command> getRegisteredCommands() {
		Set<Command> set = new HashSet<>();
		for (Iterator<Entry<String, Command>> itr = this.commands.entrySet().iterator(); itr.hasNext();) {
			Map.Entry<String, Command> entrySet = itr.next();
			Command value = entrySet.getValue();
			if (!set.add(value)) {
				itr.remove();
			}
		}
		return set;
	}

	/**
	 * Checks for params.
	 *
	 * @param tokens the tokens
	 * @return true, if successful
	 */
	private boolean hasParams(String[] tokens) {
		return tokens.length > 1;
	}

	/**
	 * Command from name.
	 *
	 * @param name the name
	 * @return the optional
	 */
	public Optional<Command> commandFromName(String name) {
		return Optional.ofNullable(commands.get(name));
	}

	private void increaseXp(GuildMessageReceivedEvent event) {
		FileUtil.createFileIfNotExist(new File("users/" + event.getAuthor().getId()));
		int x = FileUtil.readIntFromFile(FileUtils.getFile("users/" + event.getAuthor().getId()));
		if(x != -1) {
			Integer a = new Integer(++x);
			File file = FileUtils.getFile("users/" + event.getMember().getId());
			try {
				FileUtils.write(file, String.valueOf(a), StandardCharsets.UTF_8, false);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
