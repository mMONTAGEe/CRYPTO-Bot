package io.montage.bot.listeners;

import java.io.*;
import java.net.URL;
import java.util.*;

import io.montage.bot.Config;
import io.montage.bot.utilities.FileUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildListeners extends ListenerAdapter {

	public static String[]	join_messages = getMessagesFromURL(Config.JOIN_MSG_URL);
	public static String[]	leave_messages = getMessagesFromURL(Config.LEAVE_MSG_URL);
	Random rand = new Random(); 

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if (isSet()) {

			FileUtil.handleXPDataFile(new File("users/" + event.getUser().getId()), false);
			int number = rand.nextInt(join_messages.length);
			String msg = join_messages[number].replace("[member]", event.getUser().getAsMention());
			if(msg.contains("[server]")) {
				msg.replace("[server]", event.getGuild().getName());
			}

			EmbedBuilder join = new EmbedBuilder();
			join.setColor(0x39fc03);
			join.setDescription(msg);

			TextChannel welcomeChannel = event.getGuild().getTextChannelById(Config.JOIN_CHANNEL_ID);

			if ((welcomeChannel != null) && welcomeChannel.canTalk()) {
				welcomeChannel.sendMessage(join.build()).queue();
			}
		}
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

		if (isSet()) {

			FileUtil.handleXPDataFile(new File("users/" + event.getUser().getId()), true);
			int number = rand.nextInt(leave_messages.length);

			EmbedBuilder leave = new EmbedBuilder();
			leave.setColor(0xff0000);
			leave.setDescription(leave_messages[number].replace("[member]", event.getUser().getAsTag()));

			TextChannel leaveChannel = event.getGuild().getTextChannelById(Config.LEAVE_CHANNEL_ID);

			if ((leaveChannel != null) && leaveChannel.canTalk()) {
				leaveChannel.sendMessage(leave.build()).queue();

			}
		}
	}

	private boolean isSet() {
		return (!Config.JOIN_CHANNEL_ID.equalsIgnoreCase("<CHANNEL-ID>")
				|| !Config.JOIN_CHANNEL_ID.equalsIgnoreCase(""));
	}

	private static String[] getMessagesFromURL(String URL) {
		String strLine = "";
		List<String> list = new ArrayList<>();
		try {
			URL url = new URL(URL);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while (strLine != null) {
				strLine = br.readLine();
				if (strLine == null) {
					break;
				}
				list.add(strLine);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
}
