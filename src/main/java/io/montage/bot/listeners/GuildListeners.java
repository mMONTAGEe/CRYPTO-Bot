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
	Random rand = new Random(); 
	private Member	member;
	private Guild	guild;

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if (isSet() && !isDisabled()) {

			this.member = event.getMember();
			this.guild = event.getGuild();

			FileUtil.handleXPDataFile(new File("users/" + event.getUser().getId()), false);
			int number = rand.nextInt(join_messages.length);
			String msg = formattedMessage(join_messages[number]);
			System.out.println(msg);

			EmbedBuilder join = new EmbedBuilder();
			join.setColor(0x39fc03);
			join.setDescription(msg);

			TextChannel welcomeChannel = event.getGuild().getTextChannelById(Config.JOIN_CHANNEL_ID);
			System.out.println(welcomeChannel.getName());
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
			int number = rand.nextInt(leave_messages.length);
			String msg = formatLeaveMsg(leave_messages[number]);
			System.out.println(msg);

			EmbedBuilder leave = new EmbedBuilder();
			leave.setColor(0xff0000);
			leave.setDescription(msg);

			TextChannel leaveChannel = event.getGuild().getTextChannelById(Config.LEAVE_CHANNEL_ID);
			System.out.println(leaveChannel.getName());
			if ((leaveChannel != null) && leaveChannel.canTalk()) {
				leaveChannel.sendMessage(leave.build()).queue();
			}
		}
	}

	private String formatLeaveMsg(String message) {
		return message.replace("[member]", this.member.getEffectiveName());
	}

	private String formattedMessage(String message) {
		message.replace("[member]", this.member.getAsMention());
		if(message.contains("[server]")) {
			message.replace("[server]", this.guild.getName());
			return message;
		}
		return message;
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
		return list.toArray(new String[list.size()]);
	}
}
