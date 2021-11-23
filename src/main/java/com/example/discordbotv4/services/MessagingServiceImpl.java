package com.example.discordbotv4.services;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

@Component
public class MessagingServiceImpl {


    public CompletableFuture<Message> sendMessage(MessageAuthor author, String title, String description, String footer, String thumbnail, TextChannel channel) {

        int red = (int) Math.floor(Math.random() * 255);
        int green = (int) Math.floor(Math.random() * 255);
        int blue = (int) Math.floor(Math.random() * 255);

        return new MessageBuilder().setEmbed(
                new EmbedBuilder()
                        .setAuthor(author)
                        .setTitle(title)
                        .setDescription(description)
                        .setFooter(footer)
                        .setThumbnail(thumbnail)
                        .setColor(new Color(red, green, blue))
        ).send(channel);
    }

}
