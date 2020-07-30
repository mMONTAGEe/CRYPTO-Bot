package io.montage.bot.commands.inerf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import io.montage.bot.utilities.FileUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * The Class CommandManager.
 */
public class CommandManager {

    /** The commands. */
    public Map<String, Command> commands;
    
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
            increaseXp(event);
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
    
    private void increaseXp(GuildMessageReceivedEvent event) {
        int x = FileUtil.readIntFromFile(FileUtils.getFile("user/" + event.getMember().getId()));
        if(x != -1) {
        Integer a = new Integer(++x);
        File file = FileUtils.getFile("user/" + event.getMember().getId());
        try {
			FileUtils.write(file, String.valueOf(a), Charset.defaultCharset(), false);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
        }
    }
}
