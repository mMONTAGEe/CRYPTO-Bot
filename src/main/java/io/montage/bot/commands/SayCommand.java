package io.montage.bot.commands;

import java.util.List;

import io.montage.bot.commands.inerf.Command;
import io.montage.bot.utilities.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SayCommand extends Command{

	public SayCommand() {
		super("say");
	}

	@Override
	public void executeAndHandle (GuildMessageReceivedEvent event, List<String> params, User author, Message inputMessage) {
		User user = event.getAuthor();
		String msg = String .join(" ", params);
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle(user.getName())
				.setDescription(msg);
		
		event.getChannel().sendMessage(embed.build()).queue();
		
	}

	@Override
	public String getDescription () {
		return "Say something as the bot!";
	}

}
