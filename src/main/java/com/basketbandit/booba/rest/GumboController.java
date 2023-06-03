package com.basketbandit.booba.rest;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.Instant;
import java.util.Arrays;

@RestController
public class GumboController extends ListenerAdapter implements Controller  {
    private static JDA jda;
    private static byte[] placeArrayOld = new byte[0];

    public GumboController() {
        try(BufferedReader token = new BufferedReader(new InputStreamReader(new FileInputStream("./data/discordtoken.txt"), StandardCharsets.UTF_8))) {
            jda = JDABuilder.createDefault(token.readLine())
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                    .build();
            jda.addEventListener(this);
            jda.updateCommands().addCommands(
                    Commands.slash("ping", "Calculate ping of the bot")
            ).queue();
        } catch(Exception e) {
            log.error("Something went wrong: {}", e.getMessage());
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

    public static void backupPlace() {
        try {
            PlaceController placeController = new PlaceController();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(placeController.getData(), "png", byteArrayOutputStream);
            byte[] placeArrayNew = byteArrayOutputStream.toByteArray();
            if(!Arrays.equals(placeArrayOld, placeArrayNew)) {
                placeArrayOld = placeArrayNew;
                InputStream inputStream = new ByteArrayInputStream(placeArrayNew);
                jda.getThreadChannelById(1114649660003450910L).sendFiles(FileUpload.fromData(inputStream, Time.from(Instant.now()).toString())).queue();
            }
        } catch(Exception e) {
            log.error("Something went wrong! {}", e.getMessage(), e);
        }
    }
}
