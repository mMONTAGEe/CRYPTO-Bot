package io.montage.bot.commands;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.List;

import io.montage.bot.commands.inerf.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SayCommand2 extends Command{

	public SayCommand2() {
		super("botsay");
	}

	@Override
	public void executeAndHandle (GuildMessageReceivedEvent event, List<String> params, User author, Message inputMessage) {

		String msg = String.join(" ", params);

		event.getChannel().sendMessage(msg).queue(o -> event.getMessage().delete().queue());
		
	}
	
	@Override
	public String getDescription () {
		return "Say something as the bot!";
	}

}
