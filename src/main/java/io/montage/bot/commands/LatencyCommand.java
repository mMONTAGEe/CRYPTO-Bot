package io.montage.bot.commands;

import java.util.List;

import io.montage.bot.Bot;
import io.montage.bot.commands.inerf.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * The LatencyCommand.
 */
public class LatencyCommand extends Command {

    /**
     * Instantiates a new latency command.
     */
    public LatencyCommand() {
        super("ping");
        addAlias("latency");
        addAlias("pong");
    }
    

	@Override
	public void executeAndHandle(GuildMessageReceivedEvent event, List<String> params,
			User author, Message inputMessage) {
        event.getChannel().sendMessage("The current latency between me and the discord servers is " +
                Bot.getJDA().getGatewayPing()).queue();
		
	}

    /**
     * Gets the description.
     *
     * @return the description
     */
    @Override
    public String getDescription() {
        return "Returns latency between the discord bot and the official discord servers";
    }

}
