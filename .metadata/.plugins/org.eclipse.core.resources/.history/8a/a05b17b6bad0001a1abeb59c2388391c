package io.montage.bot.listeners;

import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@SuppressWarnings("deprecation")
public class GuildMemberLeave extends ListenerAdapter {
	String[] messages = {
			"[member] STOLE OUR TARDIS AND RAN AWAY! >:O" };
	

	

	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		Random rand = new Random(); 
		int number = rand.nextInt(messages.length);
		
		EmbedBuilder join = new EmbedBuilder();
		join.setColor(0xff0000);
		join.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));
		
		event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
		
	}
}
