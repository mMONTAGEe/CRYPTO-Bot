package io.montage.bot.listeners;

import javax.annotation.Nonnull;

import io.montage.bot.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener interface for receiving message events.
 * The class that is interested in processing a message
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMessageListener<code> method. When
 * the message event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MessageEvent
 */
public class MessageListener extends ListenerAdapter {

    /**
     * On guild message received.
     *
     * @param event the event
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot())
            return;

        if(messageContainsPrefix(event))
            Bot.getInstance().handleCommandEvent(event);

    }

    /**
     * Returns true if the message contains the bots prefix.
     * TODO: Create a per-guild prefix option
     *
     * @param event the message received event
     * @return true, if successful
     */
    private boolean messageContainsPrefix(GuildMessageReceivedEvent event) {
        return event.getMessage().getContentRaw().startsWith(Bot.getInstance().getPrefix());
    }
}
