package io.montage.bot.commands;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import io.montage.bot.commands.inerf.Command;
import io.montage.bot.utilities.FileUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class getXpCommand extends Command {
	String xp;

	public getXpCommand() {
		super("xp");
		this.params("[username]");
	}

	@Override
	public void executeAndHandle (GuildMessageReceivedEvent event, List<String> params, User author, Message inputMessage) {
		if (params.size() == 0) {
			File file2 = FileUtils.getFile("user/" + event.getAuthor().getId());
			xp = FileUtil.readFromFile(file2);

		}
		if (params.size() > 0) {
			String user  = params.get(0);
			File   file2 = FileUtils.getFile("user/" + user);
			xp = FileUtil.readFromFile(file2);
		}
		event.getChannel().sendMessage("User has " + xp).queue(o -> event.getMessage().delete().queue());

	}

	@Override
	public String getDescription () {
		return "See your or someones XP";
	}

}
