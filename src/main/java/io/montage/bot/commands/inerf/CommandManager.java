package io.montage.bot.commands.inerf;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

/**
 * The Class CommandManager.
 */
public class CommandManager {

    /** The commands. */
    private Map<String, Command> commands;

    /**
     * Instantiates a new command manager.
     */
    public CommandManager() {
        commands = new HashMap<>();
    }

    /**
     * Handle command.
     *
     * @param commandName the command name
     * @param event the event
     */
    public void handleCommand(String commandName, GuildMessageReceivedEvent event) {
        Optional<Command> commandOptional = commandFromName(commandName);

        // Adds any space separated strings to the parameter list
        commandOptional.ifPresent(command -> {
            String[] tokens = event.getMessage().getContentRaw().substring(1).toLowerCase().split(" ", 2);
            List<String> paramList = new ArrayList<>();
            if(hasParams(tokens)) {
                final String params = tokens[1].trim();
                paramList = new ArrayList<>(Arrays.asList(params.split(" ")));
            }
            command.executeAndHandle(event, paramList, null, null);
        });
    }

    /**
     * Register.
     *
     * @param command the command
     */
    public void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);

        for(String alias : command.getAliases())
            commands.put(alias.toLowerCase(), command);
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
}
