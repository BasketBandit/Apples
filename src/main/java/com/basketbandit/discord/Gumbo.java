package com.basketbandit.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Gumbo extends ListenerAdapter {
    public Gumbo() {
        try(BufferedReader token = new BufferedReader(new InputStreamReader(new FileInputStream("./data/discordtoken.txt"), StandardCharsets.UTF_8))) {
            JDA jda = JDABuilder.createDefault(token.readLine())
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                    .build();
            jda.addEventListener(new Gumbo());
            jda.updateCommands().addCommands(
                    Commands.slash("ping", "Calculate ping of the bot")
            ).queue();
        } catch(Exception e) {
            System.out.println("Something went wrong! " + e.getMessage());
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch(event.getName()) {
            case "ping" -> {
                long time = System.currentTimeMillis();
                event.reply("Pong!")
                        .setEphemeral(true)
                        .flatMap(v -> event.getHook().editOriginalFormat("Pong: %dms", System.currentTimeMillis() - time))
                        .queue();
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
        //         event.getChannel().asTextChannel().getName(), event.getMember().getEffectiveName(),
        //         event.getMessage().getContentDisplay());
    }
}