package io.montage.bot.commands;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.List;

import io.montage.bot.commands.inerf.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SayCommand extends Command{

	public SayCommand() {
		super("say");
	}

	@Override
	public void executeAndHandle (GuildMessageReceivedEvent event, List<String> params) {
		StringBuilder b = new StringBuilder();
		User user = event.getAuthor();

		String col = params.get(0);
		params.remove(0);
		String msg = String.join(" ", params);

		EmbedBuilder embed = new EmbedBuilder()
				.setColor(getColor(col))
				.setTitle(user.getName())
				.setDescription(msg);
		
		
		event.getChannel().sendMessage(embed.build()).queue(o -> event.getMessage().delete().queue());
		
	}
	
	private static Color getColor(String string) {
		try {
		    Field field = Color.class.getField(string);
		    return (Color)field.get(null);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public String getDescription () {
		return "Say something as the bot!";
	}

}
