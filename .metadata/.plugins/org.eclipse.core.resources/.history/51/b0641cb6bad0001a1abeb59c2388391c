package io.montage.bot.listeners;

import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoin extends ListenerAdapter {
	
	String[] messages = {
			"[member] joined. You must construct additional TARDIS.",
			"Never gonna give [member] up. Never let [member] down!",
			"Hey! Listen! [member] Is you the best member that we waited?",
			"Ha! [member] Has Joined. Finally new Friend!",
			"We've been waiting you Agent[member].",
			"It's dangerous to go alone, take [member]! with you!",
			"*Metallic scrumbling*. [member] just landed the TARDIS!.",
			"Everyone Calm down! [member] just joined the server.",
			"Here comes! [member] Take a seat..",
			"Time and Relative Dimensions in [member]",
			"This is Guards. You are welcome here [member]",
			"[member] Welcome to the Club buddy!",
			"[member] Have you brought pizza?",
			"[member] BRUH. Finally you are here!",
			"Respond fast! are you [member] A good man?"
		};
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Random rand = new Random(); 
		int number = rand.nextInt(messages.length);
		
		EmbedBuilder join = new EmbedBuilder();
		join.setColor(0x39fc03);
		join.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));
		
		event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
		
		
		
	}

}
