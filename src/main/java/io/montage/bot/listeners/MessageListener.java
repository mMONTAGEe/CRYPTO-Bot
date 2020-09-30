package io.montage.bot.listeners;

import javax.annotation.Nonnull;

import io.montage.bot.Crypto;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	private final String PREFIX = Crypto.getInstance().getPrefix();

	/**
	 * On guild message received.
	 *
	 * @param event the event
	 */
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}

		if (hasPrefix(event)) {
			String trim = event.getMessage().getContentRaw().replace(PREFIX, "");
			String commandName = trim.split(" ")[0];
			Crypto.getCommandManager().handleCommand(commandName, event);
			Crypto.log("{} Invoked {} Command", event.getAuthor().getAsTag(), commandName);
		}
	}

	/**
	 * Returns true if the message contains the bots prefix. TODO: Create a per-guild prefix option
	 *
	 * @param event the message received event
	 * @return true, if successful
	 */
	private boolean hasPrefix(GuildMessageReceivedEvent event) {
		return event.getMessage().getContentRaw().startsWith(PREFIX);
	}
}
