package io.montage.bot.listeners;

import java.io.*;
import java.net.URL;
import java.util.*;

import io.montage.bot.Config;
import io.montage.bot.utilities.FileUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildListeners extends ListenerAdapter {

	public static String[]	join_messages = getMessagesFromURL(Config.JOIN_MSG_URL);
	public static String[]	leave_messages = getMessagesFromURL(Config.LEAVE_MSG_URL);
	private static boolean	disabled;

	private Member	member;
	private Guild	guild;

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if (isSet() && !isDisabled()) {

			this.member = event.getMember();
			this.guild = event.getGuild();

			FileUtil.handleXPDataFile(new File("users/" + event.getUser().getId()), false);

			EmbedBuilder join = new EmbedBuilder();
			join.setColor(0x39fc03);
			join.setDescription(formattedMessage(join_messages));

			TextChannel welcomeChannel = event.getGuild().getTextChannelById(Config.JOIN_CHANNEL_ID);
			if ((welcomeChannel != null) && welcomeChannel.canTalk()) {
				welcomeChannel.sendMessage(join.build()).queue();
			}
		}
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

		if (isSet() && !isDisabled()) {

			this.member = event.getMember();
			this.guild = event.getGuild();

			FileUtil.handleXPDataFile(new File("users/" + event.getUser().getId()), true);

			EmbedBuilder leave = new EmbedBuilder();
			leave.setColor(0xff0000);
			leave.setDescription(formattedMessage(leave_messages));

			TextChannel leaveChannel = event.getGuild().getTextChannelById(Config.LEAVE_CHANNEL_ID);
			if ((leaveChannel != null) && leaveChannel.canTalk()) {
				leaveChannel.sendMessage(leave.build()).queue();
			}
		}
	}

	private String formattedMessage(String[] messages) {
		String msg = messages[new Random().nextInt(messages.length)];
		msg.replace("%m", this.member.getAsMention());
		msg.replace("%s", this.guild.getName());
		return msg;
	}

	private boolean isSet() {
		return (!Config.JOIN_CHANNEL_ID.equalsIgnoreCase("<CHANNEL-ID>")
				|| !Config.JOIN_CHANNEL_ID.equalsIgnoreCase(""));
	}

	public boolean isDisabled() {
		return disabled;
	}

	public static void setDisabled(boolean disabled) {
		GuildListeners.disabled = disabled;
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
		return Arrays.asList(list).toArray(new String[list.size()]);
	}
}
