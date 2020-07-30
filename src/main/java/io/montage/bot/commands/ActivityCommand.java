package io.montage.bot.commands;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.HashedMap;

import io.montage.bot.Bot;
import io.montage.bot.commands.inerf.Command;
import io.montage.bot.utilities.EmbedUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ActivityCommand extends Command {

	public ActivityCommand() {
		super("play");
		this.params("<tpye>", "<name>", "[url]");
	}

	@Override
	public void executeAndHandle (GuildMessageReceivedEvent event, List<String> params, User author, Message inputMessage) {
		if (params.isEmpty()) {
			this.getHelpMessage(event);
			return;
		}
		JDA                  bot  = Bot.getJDA();
		String               type = params.get(0);
		Map<String, Integer> m    = new HashedMap<>();
		m.put("default", 0);
		m.put("streaming", 1);
		m.put("listening", 2);
		m.put("watching", 3);

		params.remove(0);
		String msg = String.join(" ", params);
		try {
			if (type == "streaming") {
				EmbedUtil.sendAndDeleteOnGuilds(event
						.getChannel(), new MessageBuilder("Streaming is unsupported at this time")
								.build(), 15, TimeUnit.SECONDS);
				event.getMessage().delete();
			}
			bot.getPresence().setActivity(Activity.of(ActivityType.fromKey(m.get(type)), msg));
			EmbedUtil.sendAndDeleteOnGuilds(event.getChannel(), new MessageBuilder("Bot Status updated sucessfully")
					.build(), 15, TimeUnit.SECONDS);
			event.getMessage().delete();
		}
		catch (Exception e) {
			StringBuffer b = new StringBuffer();
			b.append(e.getLocalizedMessage());
			EmbedUtil.sendAndDeleteOnGuilds(event
					.getChannel(), new MessageBuilder("Error While Setting Bot Status \n\n " + b.toString())
							.build(), 15, TimeUnit.SECONDS);
			event.getMessage().delete();
		}

	}

	@Override
	public String getDescription () {
		return "Say something as the bot!";
	}

}
