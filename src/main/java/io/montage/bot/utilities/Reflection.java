package io.montage.bot.utilities;

import java.util.Set;

import org.reflections.Reflections;

import io.montage.bot.commands.inerf.Command;

public class Reflection {

	/**
	 * This method will use Reflection to build a Set of all classes<br>
	 * in the {@code 'io.montage.bot.commands'} package that are sub-types<br>
	 * of our {@linkplain Command} class so we can auto register each command<br>
	 * when we crate a new CommandManager in our Main Class.
	 * <br><br>
	 * This allows the removal of all Command register methods and makes our<br>
	 * code cleaner and easier to read
	 * 
	 * @return {@link Set} of our Command class sub-types
	 */
	public static Set<Class<? extends Command>> getCommandClasses() {
		Reflections reflections = new Reflections("io.montage.bot.commands");
		Set<Class<? extends Command>> scannersSet = reflections.getSubTypesOf(Command.class);
		return scannersSet;
	}
}
